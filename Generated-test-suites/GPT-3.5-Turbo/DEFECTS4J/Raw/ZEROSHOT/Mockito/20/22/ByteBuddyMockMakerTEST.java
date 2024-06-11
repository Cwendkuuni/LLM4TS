package org.mockito.internal.creation.bytebuddy;

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

    @Test
    public void testCreateMock_SerializableMode_ACROSS_CLASSLOADERS_ThrowsMockitoException() {
        // Create mock settings
        MockCreationSettings settings = new MockCreationSettings<>();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        
        // Create mock handler
        InternalMockHandler handler = new InternalMockHandler();
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Verify that MockitoException is thrown
        assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(settings, handler);
        });
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Create mock settings
        MockCreationSettings settings = new MockCreationSettings<>();
        ClassMock mock = new ClassMock();
        settings.setTypeToMock(ClassMock.class);
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Ensure mock is assignable to mocked type
        ClassMock result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        // Verify result
        assertNotNull(result);
        assertTrue(result instanceof ClassMock);
    }
    
    @Test
    public void testGetHandler() {
        // Create mock handler
        InternalMockHandler handler = new InternalMockHandler();
        
        // Create mock
        ClassMock mock = new ClassMock();
        mock.setMockitoInterceptor(new MockMethodInterceptor(handler, null));
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Get handler from mock
        MockHandler result = mockMaker.getHandler(mock);
        
        // Verify result
        assertNotNull(result);
        assertTrue(result instanceof InternalMockHandler);
    }
    
    @Test
    public void testResetMock() {
        // Create mock handler
        InternalMockHandler handler = new InternalMockHandler();
        
        // Create mock settings
        MockCreationSettings settings = new MockCreationSettings<>();
        
        // Create mock
        ClassMock mock = new ClassMock();
        mock.setMockitoInterceptor(new MockMethodInterceptor(handler, null));
        
        // Create new handler
        InternalMockHandler newHandler = new InternalMockHandler();
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Reset mock with new handler
        mockMaker.resetMock(mock, newHandler, settings);
        
        // Verify that new handler is set to mock
        assertNotNull(mock.getMockitoInterceptor());
        assertEquals(newHandler, mock.getMockitoInterceptor().getMockHandler());
    }
    
    @Test
    public void testDescribeClass_TypeIsNull_ReturnsNull() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Describe null type
        String result = mockMaker.describeClass(null);
        
        // Verify result
        assertNull(result);
    }
    
    @Test
    public void testDescribeClass_TypeIsNotNull_ReturnsCanonicalNameAndClassLoader() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Create test class
        ClassMock mock = new ClassMock();
        
        // Describe test class
        String result = mockMaker.describeClass(mock);
        
        // Verify result
        assertNotNull(result);
        assertTrue(result.contains(mock.getClass().getCanonicalName()));
        assertTrue(result.contains(mock.getClass().getClassLoader().toString()));
    }
    
    @Test
    public void testDescribeClass_InstanceIsNull_ReturnsNull() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Describe null instance
        String result = mockMaker.describeClass((Object)null);
        
        // Verify result
        assertNull(result);
    }
    
    @Test
    public void testDescribeClass_InstanceIsNotNull_ReturnsCanonicalNameAndClassLoader() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Create test instance
        ClassMock mock = new ClassMock();
        
        // Describe test instance
        String result = mockMaker.describeClass(mock);
        
        // Verify result
        assertNotNull(result);
        assertTrue(result.contains(mock.getClass().getCanonicalName()));
        assertTrue(result.contains(mock.getClass().getClassLoader().toString()));
    }
    
    @Test
    public void testInitializeClassInstantiator_WithObjenesisClassLoader_ReturnsClassInstantiatorUsingObjenesis() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Initialize class instantiator
        ClassInstantiator result = mockMaker.initializeClassInstantiator();
        
        // Verify result
        assertNotNull(result);
        assertTrue(result instanceof ByteBuddyMockMaker.ClassInstantiator.UsingObjenesis);
    }
    
    @Test
    public void testAsInternalMockHandler_HandlerIsNotInternalMockHandler_ThrowsMockitoException() {
        // Create mock handler
        MockHandler handler = new MockHandler() {};
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        // Verify that MockitoException is thrown
        assertThrows(MockitoException.class, () -> {
            mockMaker.asInternalMockHandler(handler);
        });
    }
    
    private static class ClassMock {
        private MockMethodInterceptor.MockAccess mockitoInterceptor;
        
        public void setMockitoInterceptor(MockMethodInterceptor.MockAccess mockitoInterceptor) {
            this.mockitoInterceptor = mockitoInterceptor;
        }
        
        public MockMethodInterceptor.MockAccess getMockitoInterceptor() {
            return mockitoInterceptor;
        }
    }
}