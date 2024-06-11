package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ExactInvocationMatcher;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock_WithValidSettingsAndHandler_ShouldReturnMockedInstance() {
        // Given
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class, mockHandler);

        // When
        Object mockedInstance = mockMaker.createMock(settings, mockHandler);

        // Then
        assertNotNull(mockedInstance);
        verify(mockHandler).handle(ArgumentMatchers.isInstance(mockedInstance));
    }

    @Test
    public void testCreateMock_WithInvalidSerializableMode_ShouldThrowException() {
        // Given
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class, mockHandler);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        // When & Then
        assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testCreateMock_WithInvalidMockType_ShouldThrowClassCastException() {
        // Given
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(String.class, mockHandler);

        // When
        Object mockedInstance = mockMaker.createMock(settings, mockHandler);

        // Then
        assertThrows(ClassCastException.class, () -> settings.getTypeToMock().cast(mockedInstance));
    }

    @Test
    public void testGetHandler() {
        // Given
        Object mock = mock(Object.class, new MockCreationSettings<>(Object.class, mock -> {}));

        // When
        MockHandler handler = mockMaker.getHandler(mock);

        // Then
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        // Given
        Object mock = mock(Object.class, new MockCreationSettings<>(Object.class, mock -> {}));
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class, handler);

        // When
        mockMaker.createMock(settings, handler);
        mockMaker.resetMock(mock, handler, settings);

        // Then
        MockHandler newHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, settings);
        verifyNoMoreInteractions(handler);
        verify(newHandler).handle(eq(mock));
    }

    @Test
    public void testInitializeClassInstantiator() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Given
        Class<?> classLoaderClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> constructor = classLoaderClass.getDeclaredConstructor(boolean.class);

        // When
        Object classInstantiator = constructor.newInstance(true);

        // Then
        assertEquals(ClassInstantiator.class, classInstantiator.getClass());
    }

    @Test
    public void testAsInternalMockHandler() {
        // Given
        MockHandler mockHandler = mock(MockHandler.class);

        // When
        InternalMockHandler internalMockHandler = (InternalMockHandler) mockMaker.asInternalMockHandler(mockHandler);

        // Then
        assertNotNull(internalMockHandler);
        assertEquals(mockHandler, internalMockHandler.getMockHandler());
    }

    @Test
    public void testCreateMock_WithInvalidInstance_ShouldThrowException() {
        // Given
        Object mock = new Object() {
            @Override
            public Class<?> getClass() {
                return Object.class;
            }
        };
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class, mockHandler);

        // When & Then
        assertThrows(ClassCastException.class, () -> mockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testDescribeClass() {
        // Given
        Class<?> mockedProxyType = Object.class;

        // When
        String description = ByteBuddyMockMaker.describeClass(mockedProxyType);

        // Then
        assertEquals("'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", description);
    }
}