package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.CreationSettings;
import org.mockito.mock.MockCreationSettings;

public class ByteBuddyMockMakerTest {
    
    private ByteBuddyMockMaker byteBuddyMockMaker;
    private CreationSettings mockSettings;
    private MockHandler mockHandler;
    
    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockSettings = new MockCreationSettings();
        mockHandler = new InternalMockHandler();
    }
    
    @Test
    public void testCreateMock() {
        mockSettings.setTypeToMock(Object.class);
        
        Object mock = byteBuddyMockMaker.createMock(mockSettings, mockHandler);
        
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
        assertSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        mockSettings.setTypeToMock(Object.class);
        mockSettings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        
        byteBuddyMockMaker.createMock(mockSettings, mockHandler);
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        mockSettings.setTypeToMock(Object.class);
        Object mock = new Object();
        
        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockSettings, mock);
        
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testDescribeClassWithValidType() {
        String result = byteBuddyMockMaker.describeClass(Object.class);
        
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }
    
    @Test
    public void testDescribeClassWithNullType() {
        String result = byteBuddyMockMaker.describeClass(null);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClassWithValidInstance() {
        String result = byteBuddyMockMaker.describeClass(new Object());
        
        assertEquals("'java.lang.Object', loaded by classloader : 'java.net.URLClassLoader'", result);
    }
    
    @Test
    public void testDescribeClassWithNullInstance() {
        String result = byteBuddyMockMaker.describeClass(null);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testGetHandlerWithValidMock() {
        InternalMockHandler handler = new InternalMockHandler();
        Object mock = new Object();
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(new MockMethodInterceptor(handler, mockSettings));
        
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        assertNotNull(result);
        assertSame(handler, result);
    }
    
    @Test
    public void testGetHandlerWithInvalidMock() {
        Object mock = new Object();
        
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        assertNull(result);
    }
    
    @Test
    public void testResetMock() {
        InternalMockHandler newHandler = new InternalMockHandler();
        Object mock = new Object();
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(new MockMethodInterceptor(mockHandler, mockSettings));
        
        byteBuddyMockMaker.resetMock(mock, newHandler, mockSettings);
        
        assertSame(newHandler, byteBuddyMockMaker.getHandler(mock));
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMockWithInvalidHandler() {
        Object mock = new Object();
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(new MockMethodInterceptor(mockHandler, mockSettings));
        
        byteBuddyMockMaker.resetMock(mock, mockHandler, mockSettings);
    }
    
    @Test
    public void testInitializeClassInstantiatorWithObjenesisClassLoader() {
        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator();
        
        assertNotNull(result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithoutObjenesisClassLoader() {
        try {
            Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
            ClassInstantiator classInstantiator = (ClassInstantiator) objenesisClassLoader.newInstance();
            GlobalConfiguration globalConfiguration = new GlobalConfiguration();
            globalConfiguration.disableClassCache();
            
            ByteBuddyMockMaker.initializeClassInstantiator();
            
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
    
    @Test
    public void testAsInternalMockHandlerWithValidHandler() {
        InternalMockHandler handler = new InternalMockHandler();
        
        InternalMockHandler result = ByteBuddyMockMaker.asInternalMockHandler(handler);
        
        assertNotNull(result);
        assertSame(handler, result);
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithInvalidHandler() {
        MockHandler handler = new MockHandler();
        
        ByteBuddyMockMaker.asInternalMockHandler(handler);
    }
}