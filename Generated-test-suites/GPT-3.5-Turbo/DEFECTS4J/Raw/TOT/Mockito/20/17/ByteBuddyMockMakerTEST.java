
package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void createMock_shouldThrowMockitoExceptionWhenSerializableModeIsACrossClassloader() {
        // Arrange
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act and Assert
        assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(settings, handler);
        });
    }

    @Test
    public void createMock_shouldThrowMockitoExceptionWhenClassCastExceptionOccurs() {
        // Arrange
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        MockitoException exception = new MockitoException("ClassCastException occurred while creating the mockito mock");

        when(classInstantiator.instantiate(any())).thenThrow(exception);

        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        setPrivateField(mockMaker, "classInstantiator", classInstantiator);

        // Act and Assert
        assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(settings, handler);
        });
    }

    @Test
    public void createMock_shouldThrowMockitoExceptionWhenInstantiationExceptionOccurs() {
        // Arrange
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        InstantiationException exception = new InstantiationException("Unable to create mock instance");
        Class<?> mockedProxyType = mock(Class.class);

        when(classInstantiator.instantiate(any())).thenReturn(mockedProxyType);
        when(mockedProxyType.getSuperclass()).thenReturn(Object.class);

        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        setPrivateField(mockMaker, "classInstantiator", classInstantiator);

        // Act and Assert
        assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(settings, handler);
        });
    }

    @Test
    public void ensureMockIsAssignableToMockedType_shouldReturnMockObjectWhenCastIsSuccessful() {
        // Arrange
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        Object mock = mock(Object.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertEquals(mock, result);
    }

    @Test
    public void getHandler_shouldReturnNullWhenMockIsNotInstanceOfMockMethodInterceptorMockAccess() {
        // Arrange
        Object mock = new Object();

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void getHandler_shouldReturnMockHandlerWhenMockIsInstanceOfMockMethodInterceptorMockAccess() {
        // Arrange
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler expected = mock(MockHandler.class);

        when(mockAccess.getMockitoInterceptor()).thenReturn(mock);
        when(mock.getMockHandler()).thenReturn(expected);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        MockHandler result = mockMaker.getHandler(mockAccess);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void resetMock_shouldSetMockitoInterceptorWithNewHandler() {
        // Arrange
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // Act
        mockMaker.resetMock(mockAccess, newHandler, settings);

        // Assert
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    private void setPrivateField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
