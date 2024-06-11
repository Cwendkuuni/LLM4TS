package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.junit.Test;

public class ByteBuddyMockMakerTest {
    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        MockHandler handler = new InternalMockHandler();
        
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        Class clazz = String.class;
        Object mock = new Object();
        
        try {
            mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            String expected = "Cannot cast java.lang.Object to java.lang.String";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void testDescribeClass() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class clazz = String.class;
        assertEquals("'java.lang.String', loaded by classloader : 'null'", mockMaker.describeClass(clazz));
        
        Object instance = new Object();
        assertEquals("null", mockMaker.describeClass(instance));
    }
    
    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();
        
        assertNull(mockMaker.getHandler(mock));
    }
    
    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();
        MockHandler newHandler = new InternalMockHandler();
        MockCreationSettings settings = new MockCreationSettings();
        
        try {
            mockMaker.resetMock(mock, newHandler, settings);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            String expected = "At the moment you cannot provide own implementations of MockHandler."
                    + "Please see the javadocs for the MockMaker interface.";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        try {
            ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
            assertNotNull(classInstantiator);
        } catch (IllegalStateException e) {
            String expected = "Mockito could not create mock: Objenesis is missing on the classpath."
                    + "Please add Objenesis on the classpath.";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void testAsInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler();
        
        InternalMockHandler internalHandler = mockMaker.asInternalMockHandler(handler);
        assertNotNull(internalHandler);
    }
}