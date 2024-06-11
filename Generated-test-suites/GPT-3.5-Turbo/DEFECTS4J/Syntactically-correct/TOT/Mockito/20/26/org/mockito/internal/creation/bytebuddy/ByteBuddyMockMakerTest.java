import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassloadersNotSupported() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Act
        byteBuddyMockMaker.createMock(settings, mockHandler);

        // Assert (exception expected)
    }

    @Test
    public void testCreateMock_NormalCase() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NOT_SERIALIZABLE);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{TestInterface.class});

        // Act
        TestClass mock = byteBuddyMockMaker.createMock(settings, mockHandler);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof TestClass);
    }

    @Test
    public void testCreateMock_InstantiationException() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NOT_SERIALIZABLE);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});

        // Act
        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            fail("Exception not thrown");
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'Object'", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        TestClass mock = mock(TestClass.class);

        // Act
        TestClass result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }

    @Test
    public void testGetHandler_NoMockAccess() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();

        // Act
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetHandler_MockAccess() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));
        Object mock = mockAccess;

        // Act
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testResetMock() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mockAccess;

        // Act
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Assert
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_NonInternalMockHandler() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = mock(TestClass.class);

        // Act
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Assert (exception expected)
    }

    @Test
    public void testInitializeClassInstantiator_Success() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        ClassInstantiator expectedClassInstantiator = new ClassInstantiator();

        // Act
        ClassInstantiator actualClassInstantiator = byteBuddyMockMaker.initializeClassInstantiator();

        // Assert
        assertEquals(expectedClassInstantiator.getClass(), actualClassInstantiator.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_MissingObjenesis() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration.disableClassCache();

        // Act
        byteBuddyMockMaker.initializeClassInstantiator();

        // Assert (exception expected)
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_NonInternalMockHandler() {
        // Arrange
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);

        // Act
        byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        // Assert (exception expected)
    }

    private static class TestClass {}
    private interface TestInterface {}

}