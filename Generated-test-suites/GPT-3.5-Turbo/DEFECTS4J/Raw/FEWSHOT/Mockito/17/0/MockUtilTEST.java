package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Class<String> classToMock = String.class;
        String spiedInstance = "MockUtil";
        
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[0]);
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);
        
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertEquals(classToMock, mock.getClass());
        assertEquals(spiedInstance, mock);
        
        verify(settings).initiateMockName(classToMock);
        verify(mockUtil).getMockHandler(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClassToMock() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        
        mockUtil.createMock(null, settings);
    }
    
    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        MockHandlerInterface<String> mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(filter.getHandler()).thenReturn(mockHandler);
        when(mockUtil.getInterceptor(mock)).thenReturn(filter);
        
        mockUtil.resetMock(mock);
        
        verify(filter).defaultAnswer(Mockito.RETURNS_DEFAULTS);
        verify(mockUtil).getMockHandler(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        mockUtil.resetMock(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMockWithNonMockObject() {
        Object obj = new Object();
        
        mockUtil.resetMock(obj);
    }
    
    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);
        MockHandlerInterface<String> mockHandler = mock(MockHandlerInterface.class);
        
        when(mockUtil.getInterceptor(mock)).thenReturn(mockHandler);
        
        MockHandlerInterface<String> result = mockUtil.getMockHandler(mock);
        
        assertEquals(mockHandler, result);
        verify(mockUtil).getInterceptor(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        Object obj = new Object();
        
        mockUtil.getMockHandler(obj);
    }
    
    @Test
    public void testIsMock() {
        String mock = mock(String.class);
        
        boolean result = mockUtil.isMock(mock);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testIsMockWithNonMockObject() {
        Object obj = new Object();
        
        boolean result = mockUtil.isMock(obj);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        MockHandlerInterface<String> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        
        MockName result = mockUtil.getMockName(mock);
        
        assertEquals(mockName, result);
        verify(mockUtil).getMockHandler(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNullMock() {
        mockUtil.getMockName(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNonMockObject() {
        Object obj = new Object();
        
        mockUtil.getMockName(obj);
    }
    
    @Test
    public void testCreateMockWithExtraInterfaces() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Class<String> classToMock = String.class;
        Class<?>[] extraInterfaces = {Comparable.class};
        
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);
        when(settings.getSpiedInstance()).thenReturn(null);
        
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertEquals(classToMock, mock.getClass());
        verify(settings).initiateMockName(classToMock);
        verify(mockUtil).getMockHandler(mock);
    }
    
    @Test
    public void testCreateMockWithSpiedInstance() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Class<String> classToMock = String.class;
        String spiedInstance = "MockUtil";
        
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[0]);
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);
        
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertEquals(classToMock, mock.getClass());
        assertEquals(spiedInstance, mock);
        
        verify(settings).initiateMockName(classToMock);
        verify(mockUtil).getMockHandler(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClassToMock() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Class<Object> classToMock = Object.class;
        
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[0]);
        when(settings.getSpiedInstance()).thenReturn(null);
        
        mockUtil.createMock(classToMock, settings);
    }
}