package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
  
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        
        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }
  
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
  
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        String mock = mockMaker.ensureMockIsAssignableToMockedType(settings, "mock");
  
        assertNotNull(mock);
        assertTrue(mock instanceof String);
    }
  
    @Test
    public void testDescribeClass() {
        Class type = null;
        assertEquals("null", ByteBuddyMockMaker.describeClass(type));
  
        type = String.class;
        assertEquals("'java.lang.String', loaded by classloader : 'null'", ByteBuddyMockMaker.describeClass(type));
    }
  
    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        Object mock = mock(Object.class);
        assertNull(mockMaker.getHandler(mock));
        
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        mock = mockAccess;
        
        MockHandler handler = mock(MockHandler.class);
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(mockMaker.asInternalMockHandler(handler), null));
  
        assertEquals(handler, mockMaker.getHandler(mock));
    }
  
    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(mockMaker.asInternalMockHandler(newHandler), settings));
        mock = mockAccess;
  
        mockMaker.resetMock(mock, newHandler, settings);
        
        verify(mockAccess).setMockitoInterceptor(new MockMethodInterceptor(mockMaker.asInternalMockHandler(newHandler), settings));
    }
  
    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
  
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
  
        assertNotNull(classInstantiator);
    }
  
    @Test
    public void testAsInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
  
        MockHandler handler = mock(MockHandler.class);
        InternalMockHandler internalHandler = mockMaker.asInternalMockHandler(handler);
  
        assertNotNull(internalHandler);
        assertTrue(internalHandler instanceof InternalMockHandler);
    }
}