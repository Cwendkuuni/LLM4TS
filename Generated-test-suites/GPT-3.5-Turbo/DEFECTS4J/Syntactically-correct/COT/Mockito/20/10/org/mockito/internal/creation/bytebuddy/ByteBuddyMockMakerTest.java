package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        // MockCreationSettings
        MockCreationSettings<?> mockCreationSettings = mock(MockCreationSettings.class);
        when(mockCreationSettings.getTypeToMock()).thenReturn(Object.class);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class[]{});
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        
        // MockHandler
        MockHandler mockHandler = mock(MockHandler.class);
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // Create mock
        Object mock = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
        
        // Assertions
        assertNotNull(mock);
        
        // Verify interactions
        verify(mockCreationSettings).getTypeToMock();
        verify(mockCreationSettings).getExtraInterfaces();
        verify(mockCreationSettings).getSerializableMode();
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        // MockCreationSettings
        MockCreationSettings<?> mockCreationSettings = mock(MockCreationSettings.class);
        when(mockCreationSettings.getTypeToMock()).thenReturn(Object.class);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class[]{});
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        // MockHandler
        MockHandler mockHandler = mock(MockHandler.class);
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // Create mock
        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // MockCreationSettings
        MockCreationSettings<?> mockCreationSettings = mock(MockCreationSettings.class);
        when(mockCreationSettings.getTypeToMock()).thenReturn(Object.class);
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // Mock
        Object mock = mock(Object.class);
        
        // Ensure mock is assignable
        Object assignedMock = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);
        
        // Assertions
        assertNotNull(assignedMock);
        assertEquals(Object.class, assignedMock.getClass());
        
        // Verify interactions
        verify(mockCreationSettings).getTypeToMock();
    }
    
    @Test(expected = MockitoException.class)
    public void testEnsureMockIsAssignableToMockedTypeWithClassCastException() {
        // MockCreationSettings
        MockCreationSettings<?> mockCreationSettings = mock(MockCreationSettings.class);
        when(mockCreationSettings.getTypeToMock()).thenReturn(String.class);
        
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // Mock
        Object mock = mock(Object.class);
        
        // Ensure mock is assignable
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);
    }
    
    @Test
    public void testDescribeClassWithType() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // Describe class
        String description = byteBuddyMockMaker.describeClass(Object.class);
        
        // Assertions
        assertNotNull(description);
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", description);
    }
    
    @Test
    public void testDescribeClassWithInstance() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // Describe class
        String description = byteBuddyMockMaker.describeClass(new Object());
        
        // Assertions
        assertNotNull(description);
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", description);
    }
    
    @Test
    public void testGetHandler() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // Mock
        Object mock = mock(Object.class);
        
        // Get handler
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        
        // Assertions
        assertNull(handler);
    }
    
    @Test
    public void testGetHandlerWithMockMethodInterceptorMockAccess() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // MockMethodInterceptor.MockAccess
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        
        // MockHandler
        MockHandler mockHandler = mock(MockHandler.class);
        
        // Set mockito interceptor
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);
        when(mockitoInterceptor.getMockHandler()).thenReturn(mockHandler);
        
        // Get handler
        MockHandler handler = byteBuddyMockMaker.getHandler(mockAccess);
        
        // Assertions
        assertNotNull(handler);
        assertEquals(mockHandler, handler);
        
        // Verify interactions
        verify(mockAccess).getMockitoInterceptor();
        verify(mockitoInterceptor).getMockHandler();
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMockWithMockMethodInterceptorMockAccess() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // MockMethodInterceptor.MockAccess
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        
        // MockHandler
        MockHandler mockHandler = mock(MockHandler.class);
        
        // MockCreationSettings
        MockCreationSettings<?> mockCreationSettings = mock(MockCreationSettings.class);
        
        // Set mockito interceptor
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);
        when(mockitoInterceptor.getMockHandler()).thenReturn(mockHandler);
        
        // Reset mock
        byteBuddyMockMaker.resetMock(mockAccess, mockHandler, mockCreationSettings);
        
        // Verify interactions
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }
    
    @Test(expected = MockitoException.class)
    public void testResetMockWithNonInternalMockHandler() {
        // Create ByteBuddyMockMaker instance
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        
        // MockMethodInterceptor.MockAccess
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        
        // MockHandler
        MockHandler mockHandler = mock(MockHandler.class);
        
        // MockCreationSettings
        MockCreationSettings<?> mockCreationSettings = mock(MockCreationSettings.class);
        
        // Reset mock
        byteBuddyMockMaker.resetMock(mockAccess, mockHandler, mockCreationSettings);
    }
    
}