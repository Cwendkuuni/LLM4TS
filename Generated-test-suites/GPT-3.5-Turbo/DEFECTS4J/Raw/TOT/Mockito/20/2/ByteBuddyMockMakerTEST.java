import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        // Edge case: SerializableMode ACROSS_CLASSLOADERS
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        settings.getTypeToMock();
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(settings, mock(MockHandler.class));
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_ClassCastException() {
        // Error scenario: ClassCastException
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        settings.getTypeToMock();
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(null);

        try {
            mockMaker.createMock(settings, mock(MockHandler.class));
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("ClassCastException occurred while creating the mockito mock"));
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        // Error scenario: InstantiationException
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        settings.getTypeToMock();
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(String.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        Class<String> mockedProxyType = mock(Class.class);
        when(mockedProxyType.getSuperclass()).thenReturn(String.class);

        when(mockedProxyType.cast(any())).thenThrow(ClassCastException.class);
        when(settings.getTypeToMock()).thenReturn(mockedProxyType);

        try {
            mockMaker.createMock(settings, mock(MockHandler.class));
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("Unable to create mock instance of type"));
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Typical use case: Ensure mock is assignable to mocked type
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        settings.getTypeToMock();
        when(settings.getTypeToMock()).thenReturn(String.class);

        String mock = mockMaker.ensureMockIsAssignableToMockedType(settings, "mock");

        assertEquals("mock", mock);
    }

    @Test
    public void testDescribeClass_TypeNull() {
        // Typical use case: Describe class with null type
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String description = mockMaker.describeClass(null);
        assertEquals("null", description);
    }

    @Test
    public void testDescribeClass_TypeNotNull() {
        // Typical use case: Describe class with non-null type
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<String> type = String.class;
        String description = mockMaker.describeClass(type);
        assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
    }

    @Test
    public void testDescribeClass_InstanceNull() {
        // Typical use case: Describe class with null instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String description = mockMaker.describeClass((Object) null);
        assertEquals("null", description);
    }

    @Test
    public void testDescribeClass_InstanceNotNull() {
        // Typical use case: Describe class with non-null instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = "instance";
        String description = mockMaker.describeClass(instance);
        assertEquals("'java.lang.String', loaded by classloader : 'null'", description);
    }

    @Test
    public void testGetHandler_NotMockMethodInterceptor_MockAccess() {
        // Typical use case: Get handler with non-MockMethodInterceptor.MockAccess mock
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = "mock";
        MockHandler handler = mockMaker.getHandler(mock);
        assertNull(handler);
    }

    @Test
    public void testGetHandler_MockMethodInterceptor_MockAccess() {
        // Typical use case: Get handler with MockMethodInterceptor.MockAccess mock
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        // Typical use case: Reset mock
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        
        try {
            mockMaker.resetMock(mock, newHandler, settings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testResetMock_InvalidMockHandler() {
        // Error scenario: Invalid MockHandler provided
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(newHandler instanceof InternalMockHandler).thenReturn(false);

        try {
            mockMaker.resetMock(mock, newHandler, settings);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("At the moment you cannot provide own implementations of MockHandler."));
        }
    }

    @Test
    public void testInitializeClassInstantiator_ObjenesisMissing() {
        // Error scenario: Objenesis missing
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        try {
            mockMaker.initializeClassInstantiator();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Mockito could not create mock: Objenesis is missing on the classpath."));
        }
    }

}