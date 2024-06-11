package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Class<?>[] interfaces = {Serializable.class};
        Object spiedInstance = new Object();
        
        MockUtil mockUtil = new MockUtil();
        
        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), any(Class.class), any(Class[].class)))
                .thenReturn(mock(Factory.class));
        mockStatic(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE).thenReturn(classImposterizer);
        
        LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);
        when(lenientCopyTool.copyToMock(eq(spiedInstance), any())).thenReturn(true);
        mockStatic(LenientCopyTool.class);
        whenNew(LenientCopyTool.class).withNoArguments().thenReturn(lenientCopyTool);
        
        Object mock = mockUtil.createMock(Object.class, settings);
        
        assertNotNull(mock);
        verify(settings, times(2)).getExtraInterfaces();
        verify(classImposterizer).imposterise(any(MethodInterceptorFilter.class), any(Class.class), any(Class[].class));
        verify(lenientCopyTool).copyToMock(eq(spiedInstance), any());
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullArgument() {
        MockUtil mockUtil = new MockUtil();
        
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockitoMockArgument() {
        MockUtil mockUtil = new MockUtil();
        
        Object mock = new Object();
        
        mockUtil.getMockHandler(mock);
    }
    
    @Test
    public void testGetMockHandler_ValidMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        
        Factory factory = mock(Factory.class);
        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
        when(factory.getCallback(0)).thenReturn(filter);
        
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        when(filter.getHandler()).thenReturn(handler);
        
        Object mock = factory;
        
        MockHandlerInterface actualHandler = mockUtil.getMockHandler(mock);
        
        assertEquals(handler, actualHandler);
        verify(factory).getCallback(0);
        verify(filter).getHandler();
    }
    
    @Test
    public void testIsMock_MockitoMock() {
        MockUtil mockUtil = new MockUtil();
        
        Factory factory = mock(Factory.class);
        
        Object mock = factory;
        
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testIsMock_NotAMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        
        Object mock = new Object();
        
        assertFalse(mockUtil.isMock(mock));
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        
        Factory factory = mock(Factory.class);
        Callback callback = mock(Callback.class);
        when(factory.getCallback(0)).thenReturn(callback);
        
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.defaultAnswer(any())).thenReturn(settings);
        
        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
        whenNew(MethodInterceptorFilter.class).withArguments(any(), eq(settings)).thenReturn(filter);
        
        Object mock = factory;
        
        mockUtil.resetMock(mock);
        
        verify(factory).getCallback(0);
        verify(settings).defaultAnswer(any());
        verifyNew(MethodInterceptorFilter.class).withArguments(any(), eq(settings));
        verify(factory).setCallback(0, filter);
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(handler.getMockSettings()).thenReturn(settings);
        
        MockName mockName = mock(MockName.class);
        when(settings.getMockName()).thenReturn(mockName);
        
        Object mock = mock(Factory.class);
        
        MockName actualMockName = mockUtil.getMockName(mock);
        
        assertEquals(mockName, actualMockName);
        verify(handler).getMockSettings();
        verify(settings).getMockName();
    }
}