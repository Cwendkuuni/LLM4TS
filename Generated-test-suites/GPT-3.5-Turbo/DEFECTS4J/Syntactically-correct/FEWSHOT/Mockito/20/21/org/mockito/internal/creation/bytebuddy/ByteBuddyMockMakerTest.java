package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_SerializableModeCrossClassloaders() {
        // Create mock settings
        MockCreationSettings<T> settings = new MockCreationSettings<>();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        // Create mock handler
        MockHandler handler = new MockHandler();
        
        // Call createMock method and catch the expected exception
        try {
            ByteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Create mock settings
        MockCreationSettings<T> settings = new MockCreationSettings<>();
        Class<T> typeToMock = MyMockedClass.class;
        settings.setTypeToMock(typeToMock);
        // Create mock instance
        T mock = new T();
        
        // Call ensureMockIsAssignableToMockedType method and check the return value
        T result = ByteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        assertNotNull(result);
        assertTrue(typeToMock.isInstance(result));
    }
    
    @Test
    public void testDescribeClass_WithNullType() {
        String result = ByteBuddyMockMaker.describeClass(null);
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_WithNonNullType() {
        String result = ByteBuddyMockMaker.describeClass(String.class);
        assertEquals("'java.lang.String', loaded by classloader : 'java.lang.ClassLoader'", result);
    }
    
    @Test
    public void testDescribeClass_WithNullInstance() {
        String result = ByteBuddyMockMaker.describeClass(null);
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_WithNonNullInstance() {
        String result = ByteBuddyMockMaker.describeClass(new Object());
        assertEquals("'java.lang.Object', loaded by classloader : 'java.lang.ClassLoader'", result);
    }
    
    @Test
    public void testResetMock() {
        // Create mock
        Object mock = new Object();
        // Create new mock handler
        MockHandler newHandler = new MockHandler();
        // Create mock settings
        MockCreationSettings settings = new MockCreationSettings();
        
        // Call resetMock method
        ByteBuddyMockMaker.resetMock(mock, newHandler, settings);
        
        // Verify that the mockito interceptor is set with the new handler
        InternalMockHandler handler = ByteBuddyMockMaker.getHandler(mock);
        assertEquals(newHandler, handler.getMockHandler());
    }

    @Test
    public void testAsInternalMockHandler_WithInternalMockHandler() {
        // Create mock handler
        InternalMockHandler handler = new InternalMockHandler();
        
        // Call asInternalMockHandler method and check the return value
        InternalMockHandler result = ByteBuddyMockMaker.asInternalMockHandler(handler);
        assertEquals(handler, result);
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_WithNonInternalMockHandler() {
        // Create mock handler
        MockHandler handler = new MockHandler();
        
        // Call asInternalMockHandler method, should throw a MockitoException
        ByteBuddyMockMaker.asInternalMockHandler(handler);
    }

}