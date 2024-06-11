package org.mockito.internal.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.mockito.cglib.proxy.Enhancer;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockName;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;

public class MockUtilTest {
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        // Setup
        Class<MockSettingsImpl> classToMock = MockSettingsImpl.class;
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        
        // Test
        MockSettingsImpl result = mockUtil.createMock(classToMock, mockSettings);
        
        // Verify
        assertNotNull(result);
    }
    
    @Test
    public void testCreateMock_InvalidClass() {
        // Setup
        Class<Object> classToMock = Object.class;
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        
        try {
            // Test
            mockUtil.createMock(classToMock, mockSettings);
            
            // Fail if no exception is thrown
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verify
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }
    
    @Test
    public void testCreateMock_NullMockSettings() {
        // Setup
        Class<MockSettingsImpl> classToMock = MockSettingsImpl.class;
        MockSettingsImpl mockSettings = null;
        
        try {
            // Test
            mockUtil.createMock(classToMock, mockSettings);
            
            // Fail if no exception is thrown
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verify
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }
    
    @Test
    public void testResetMock() {
        // Setup
        LenientCopyTool leniendCopyToolMock = mock(LenientCopyTool.class);
        MockHandlerInterface<Object> oldMockHandlerMock = mock(MockHandlerInterface.class);
        MockHandler<Object> newMockHandlerMock = mock(MockHandler.class);
        MethodInterceptorFilter newFilterMock = mock(MethodInterceptorFilter.class);
        Object mock = mock(Object.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandlerMock);
        when(leniendCopyToolMock.copyToMock(any(), eq(mock))).thenReturn(mock);
        
        // Test
        mockUtil.resetMock(mock);
        
        // Verify
        verify(leniendCopyToolMock).copyToMock(any(), eq(mock));
    }
    
    @Test
    public void testResetMock_NullMock() {
        try {
            // Test
            mockUtil.resetMock(null);
            
            // Fail if no exception is thrown
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verify
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }
    
    @Test
    public void testGetMockHandler() {
        // Setup
        MockHandlerInterface<Object> mockHandlerMock = mock(MockHandlerInterface.class);
        Object mock = mock(Object.class);
        
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(mockHandlerMock);
        
        // Test
        MockHandlerInterface<Object> result = mockUtil.getMockHandler(mock);
        
        // Verify
        assertEquals(mockHandlerMock, result);
    }
    
    @Test
    public void testGetMockHandler_NullMock() {
        try {
            // Test
            mockUtil.getMockHandler(null);
            
            // Fail if no exception is thrown
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verify
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }
    
    @Test
    public void testGetMockHandler_NotAMock() {
        // Setup
        Object mock = new Object();
        
        when(mockUtil.isMockitoMock(mock)).thenReturn(false);
        
        try {
            // Test
            mockUtil.getMockHandler(mock);
            
            // Fail if no exception is thrown
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Verify
            assertEquals("Argument should be a mock, but is: " + mock.getClass(), e.getMessage());
        }
    }
    
    @Test
    public void testIsMock() {
        // Setup
        Object mock = mock(Object.class);
        
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        
        // Test
        boolean result = mockUtil.isMock(mock);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testIsMock_NotAMock() {
        // Setup
        Object mock = new Object();
        
        when(mockUtil.isMockitoMock(mock)).thenReturn(false);
        
        // Test
        boolean result = mockUtil.isMock(mock);
        
        // Verify
        assertFalse(result);
    }
    
    @Test
    public void testGetInterceptor() {
        // Setup
        MethodInterceptorFilter interceptorMock = mock(MethodInterceptorFilter.class);
        Object mock = mock(Object.class);
        Factory factoryMock = mock(Factory.class);
        Callback callbackMock = mock(MethodInterceptorFilter.class);
        
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(factoryMock.getCallback(0)).thenReturn(callbackMock);
        when(callbackMock instanceof MethodInterceptorFilter).thenReturn(true);
        when((MethodInterceptorFilter) callbackMock).thenReturn(interceptorMock);
        when(mockUtil.getInterceptor(mock)).thenCallRealMethod();
        
        // Test
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);
        
        // Verify
        assertEquals(interceptorMock, result);
    }
    
    @Test
    public void testGetInterceptor_NullMock() {
        // Setup
        Object mock = null;
        
        when(mockUtil.isMockitoMock(mock)).thenReturn(false);
        
        // Test
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void testGetInterceptor_NotAMock() {
        // Setup
        Object mock = new Object();
        Factory factoryMock = mock(Factory.class);
        Callback callbackMock = mock(MethodInterceptorFilter.class);
        
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(factoryMock.getCallback(0)).thenReturn(callbackMock);
        when(callbackMock instanceof MethodInterceptorFilter).thenReturn(false);
        
        // Test
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void testGetMockName() {
        // Setup
        MockName mockNameMock = mock(MockName.class);
        MockHandlerInterface<Object> mockHandlerMock = mock(MockHandlerInterface.class);
        Object mock = mock(Object.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandlerMock);
        when(mockHandlerMock.getMockSettings().getMockName()).thenReturn(mockNameMock);
        
        // Test
        MockName result = mockUtil.getMockName(mock);
        
        // Verify
        assertEquals(mockNameMock, result);
    }
}