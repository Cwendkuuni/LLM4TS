package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.mockito.exceptions.base.*;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.util.StringJoiner;

public class ByteBuddyMockMakerTest {
    
    @Test
    public void testCreateMock_SerializableModeAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>();
        MockHandler handler = new InternalMockHandler();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            String expectedMessage = "Serialization across classloaders not yet supported with ByteBuddyMockMaker";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
    @Test
    public void testCreateMock_NullClassCastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>();
        MockHandler handler = new InternalMockHandler();
        settings.setTypeToMock(null);
        
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            StringJoiner joiner = new StringJoiner("\n");
            joiner.add("ClassCastException occurred while creating the mockito mock :");
            joiner.add("  class to mock : null");
            joiner.add("  created class : null");
            joiner.add("  proxy instance class : " + describeClass(null));
            joiner.add("  instance creation by : ClassInstantiator");
            joiner.add("");
            joiner.add("You might experience classloading issues, please ask the mockito mailing-list.");
            joiner.add("");
            
            String expectedMessage = joiner.toString();
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
    @Test
    public void testCreateMock_ClassCastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>();
        MockHandler handler = new InternalMockHandler();
        settings.setTypeToMock(String.class);
        
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            StringJoiner joiner = new StringJoiner("\n");
            joiner.add("ClassCastException occurred while creating the mockito mock :");
            joiner.add("  class to mock : java.lang.String");
            joiner.add("  created class : " + describeClass(String.class));
            joiner.add("  proxy instance class : " + describeClass(null));
            joiner.add("  instance creation by : ClassInstantiator");
            joiner.add("");
            joiner.add("You might experience classloading issues, please ask the mockito mailing-list.");
            joiner.add("");
            
            String expectedMessage = joiner.toString();
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
    @Test
    public void testCreateMock_InstantiationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings<>();
        MockHandler handler = new InternalMockHandler();
        settings.setTypeToMock(Object.class);
        
        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            String expectedMessage = "Unable to create mock instance of type 'Object'";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>();
        String mock = "mock";
        
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        assertEquals(mock, result);
    }
    
    @Test
    public void testGetHandler_NotInstanceOfMockMethodInterceptorMockAccess() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = "mock";
        
        MockHandler result = mockMaker.getHandler(mock);
        
        assertNull(result);
    }
    
    @Test
    public void testGetHandler_InstanceOfMockMethodInterceptorMockAccess() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();
        
        MockHandler result = mockMaker.getHandler(mock);
        
        assertNotNull(result);
    }
    
    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();
        MockHandler newHandler = new InternalMockHandler();
        MockCreationSettings settings = new MockCreationSettings();
        
        mockMaker.resetMock(mock, newHandler, settings);
        
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        MockMethodInterceptor interceptor = mockAccess.getMockitoInterceptor();
        
        assertEquals(newHandler, interceptor.getMockHandler());
    }
    
    @Test
    public void testInitializeClassInstantiator_ObjenesisClassLoaderNotFound() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        GlobalConfiguration globalConfig = new GlobalConfiguration();
        globalConfig.enableClassCache();
        ClassInstantiator expectedClassInstantiator = new GlobalConfiguration().enableClassCache();
        
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        
        assertEquals(expectedClassInstantiator, classInstantiator);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_Exception() {
        // This test case cannot be properly tested using JUnit as it requires classes not available in the current classpath
        fail("This test case requires Objenesis to be missing in classpath");
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_NotInstanceOfInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new MockHandler() {};
        
        mockMaker.asInternalMockHandler(handler);
    }
    
    @Test
    public void testAsInternalMockHandler_InstanceOfInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler handler = new InternalMockHandler();
        
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);
        
        assertEquals(handler, result);
    }
    
    private static String describeClass(Class type) {
        return type == null ? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }
    
    private static String describeClass(Object instance) {
        return instance == null ? "null" : describeClass(instance.getClass());
    }
}