import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {
    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock() {
        // Typical use case: create a mock with valid settings
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{Comparable.class});
        String mock = mockMaker.createMock(settings, handler);
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        assertTrue(mock instanceof Comparable);

        // Edge case: check that the created mock is of the correct type
        settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(Thread.class);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{Runnable.class});
        mock = mockMaker.createMock(settings, handler);
        assertNotNull(mock);
        assertTrue(mock instanceof Thread);
        assertTrue(mock instanceof Runnable);

        // Error scenario: check that an exception is thrown for unsupported SerializableMode
        settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(Integer.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Typical use case: verify that a mock is assignable to the mocked type
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(String.class);
        String mock = mock(String.class);
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        assertNotNull(result);
        assertSame(mock, result);

        // Error scenario: verify that a ClassCastException is thrown when the mock is not assignable to the mocked type
        settings = mock(MockCreationSettings.class);
        when(settings.getTypeToMock()).thenReturn(Integer.class);
        mock = mock(String.class);
        try {
            mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Test passes
        }
    }

    @Test
    public void testDescribeClass() {
        // Typical use case: check that the description for a class is returned correctly
        Class<String> stringClass = String.class;
        String description = mockMaker.describeClass(stringClass);
        assertNotNull(description);
        assertEquals("'java.lang.String', loaded by classloader : 'null'", description);

        // Edge case: check that null is handled correctly
        Class<?> nullClass = null;
        description = mockMaker.describeClass(nullClass);
        assertNotNull(description);
        assertEquals("null", description);

        // Error scenario: check that an instance description returns the correct class name and class loader
        Object instance = new Object();
        description = mockMaker.describeClass(instance);
        assertNotNull(description);
        assertEquals("'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", description);
    }

    @Test
    public void testGetHandler() {
        // Typical use case: return the mock handler if the mock is an instance of MockMethodInterceptor.MockAccess
        Object mock = mock(Object.class);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(mock(MockHandler.class));
        when(mock).thenReturn(mockAccess);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);

        // Edge case: return null if the mock is not an instance of MockMethodInterceptor.MockAccess
        mock = mock(Object.class);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(false);
        handler = mockMaker.getHandler(mock);
        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        // Typical use case: reset the mock and set the new handler
        Object mock = mock(Object.class);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mock).thenReturn(mockAccess);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        mockMaker.resetMock(mock, newHandler, settings);
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));

        // Error scenario: throw a MockitoException if the new handler is not an instance of InternalMockHandler
        mock = mock(Object.class);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(mock(MockHandler.class));
        when(mock).thenReturn(mockAccess);
        newHandler = mock(MockHandler.class);
        settings = mock(MockCreationSettings.class);
        try {
            mockMaker.resetMock(mock, newHandler, settings);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("At the moment you cannot provide own implementations of MockHandler.\nPlease see the javadocs for the MockMaker interface.\n", e.getMessage());
        }
    }

    @Test
    public void testInitializeClassInstantiator() {
        // Typical use case: return a valid ClassInstantiator
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        assertNotNull(classInstantiator);

        // Error scenario: throw an IllegalStateException if org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis is missing on the classpath
        MockMaker mockMaker = new ByteBuddyMockMaker() {
            @Override
            protected Class<?> classForName(String name) throws ClassNotFoundException {
                throw new ClassNotFoundException();
            }
        };
        try {
            mockMaker.initializeClassInstantiator();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Mockito could not create mock: Objenesis is missing on the classpath.\nPlease add Objenesis on the classpath.\n", e.getMessage());
        }
    }

    @Test
    public void testAsInternalMockHandler() {
        // Typical use case: return the InternalMockHandler if the handler is an instance of InternalMockHandler
        MockHandler handler = mock(InternalMockHandler.class);
        InternalMockHandler internalMockHandler = mockMaker.asInternalMockHandler(handler);
        assertNotNull(internalMockHandler);

        // Error scenario: throw a MockitoException if the handler is not an instance of InternalMockHandler
        handler = mock(MockHandler.class);
        try {
            internalMockHandler = mockMaker.asInternalMockHandler(handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("At the moment you cannot provide own implementations of MockHandler.\nPlease see the javadocs for the MockMaker interface.\n", e.getMessage());
        }
    }
}