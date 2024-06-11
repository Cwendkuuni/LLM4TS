package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        CreationValidator creationValidatorMock = mock(CreationValidator.class);
        MockSettingsImpl settingsMock = mock(MockSettingsImpl.class);
        MockHandler mockHandlerMock = mock(MockHandler.class);
        MethodInterceptorFilter filterMock = mock(MethodInterceptorFilter.class);
        Class<?>[] interfacesMock = new Class<?>[0];
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        LenientCopyTool copyToolMock = mock(LenientCopyTool.class);
        
        MockUtil mockUtil = new MockUtil(creationValidatorMock);
        
        when(settingsMock.getExtraInterfaces()).thenReturn(interfacesMock);
        when(settingsMock.getSpiedInstance()).thenReturn(null);
        when(settingsMock.initiateMockName(any())).thenReturn(settingsMock);
        when(creationValidatorMock.validateType(any())).thenReturn(true);
        when(creationValidatorMock.validateExtraInterfaces(any(), any())).thenReturn(true);
        when(creationValidatorMock.validateMockedType(any(), any())).thenReturn(true);
        
        when(mockHandlerMock.getMockSettings()).thenReturn(settingsMock);
        
        when(filterMock.getHandler()).thenReturn(mockHandlerMock);
        
        when(ClassImposterizer.INSTANCE.imposterise(any(), any(Class.class), any(Class[].class))).thenReturn(mock(MockImposter.class));
        
        mockUtil.createMock(Class.class, settingsMock);
        
        verify(creationValidatorMock).validateType(any());
        verify(creationValidatorMock).validateExtraInterfaces(any(), any());
        verify(creationValidatorMock).validateMockedType(any(), any());
        
        verify(settingsMock).initiateMockName(any());
        
        verify(settingsMock).getExtraInterfaces();
        verify(settingsMock).getSpiedInstance();
        
        verify(mockHandlerMock).getMockSettings();
        
        verify(filterMock).getHandler();
        
        verify(ClassImposterizer.INSTANCE).imposterise(any(), any(Class.class), any(Class[].class));
        verify(copyToolMock, never()).copyToMock(any(), any());
    }
    
    @Test
    public void testCreateMockWithSpiedInstance() {
        CreationValidator creationValidatorMock = mock(CreationValidator.class);
        MockSettingsImpl settingsMock = mock(MockSettingsImpl.class);
        MockHandler mockHandlerMock = mock(MockHandler.class);
        MethodInterceptorFilter filterMock = mock(MethodInterceptorFilter.class);
        Class<?>[] interfacesMock = new Class<?>[0];
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        LenientCopyTool copyToolMock = mock(LenientCopyTool.class);
        Object spiedInstanceMock = mock(Object.class);
        
        MockUtil mockUtil = new MockUtil(creationValidatorMock);
        
        when(settingsMock.getExtraInterfaces()).thenReturn(interfacesMock);
        when(settingsMock.getSpiedInstance()).thenReturn(spiedInstanceMock);
        when(settingsMock.initiateMockName(any())).thenReturn(settingsMock);
        when(creationValidatorMock.validateType(any())).thenReturn(true);
        when(creationValidatorMock.validateExtraInterfaces(any(), any())).thenReturn(true);
        when(creationValidatorMock.validateMockedType(any(), any())).thenReturn(true);
        
        when(mockHandlerMock.getMockSettings()).thenReturn(settingsMock);
        
        when(filterMock.getHandler()).thenReturn(mockHandlerMock);
        
        when(ClassImposterizer.INSTANCE.imposterise(any(), any(Class.class), any(Class[].class))).thenReturn(mock(MockImposter.class));
        
        mockUtil.createMock(Class.class, settingsMock);
        
        verify(creationValidatorMock).validateType(any());
        verify(creationValidatorMock).validateExtraInterfaces(any(), any());
        verify(creationValidatorMock).validateMockedType(any(), any());
        
        verify(settingsMock).initiateMockName(any());
        
        verify(settingsMock).getExtraInterfaces();
        verify(settingsMock).getSpiedInstance();
        
        verify(mockHandlerMock).getMockSettings();
        
        verify(filterMock).getHandler();
        
        verify(ClassImposterizer.INSTANCE).imposterise(any(), any(Class.class), any(Class[].class));
        verify(copyToolMock).copyToMock(any(), any());
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        MockHandlerInterface mockHandlerMock = mock(MockHandlerInterface.class);
        MethodInterceptorFilter filterMock = mock(MethodInterceptorFilter.class);
        
        MockUtil mockUtil = new MockUtil();
        
        when(mockHandlerMock.getHandler()).thenReturn(mockHandlerMock);
        when(filterMock.getHandler()).thenReturn(mockHandlerMock);
        
        when(Enhancer.isEnhanced(any(Class.class))).thenReturn(true);
        
        mockUtil.resetMock(mock);
        
        verify(mockHandlerMock).getHandler();
        verify(filterMock).getHandler();
        
        verify(ClassImposterizer.INSTANCE).imposterise(any(), any(Class.class), any(Class[].class));
        verify(filterMock).setCallback(0, filterMock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMockWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMockWithInvalidMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);
    }
    
    @Test
    public void testGetMockHandler() {
        Object mock = mock(Class.class);
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        MockHandlerInterface mockHandlerMock = mock(MockHandlerInterface.class);
        MethodInterceptorFilter filterMock = mock(MethodInterceptorFilter.class);
        
        MockUtil mockUtil = new MockUtil();
        
        when(mockHandlerMock.getHandler()).thenReturn(mockHandlerMock);
        when(filterMock.getHandler()).thenReturn(mockHandlerMock);
        
        when(Enhancer.isEnhanced(any(Class.class))).thenReturn(true);
        
        MockHandlerInterface result = mockUtil.getMockHandler(mock);
        
        assertEquals(mockHandlerMock, result);
        
        verify(mockHandlerMock).getHandler();
        verify(filterMock, never()).getHandler();
        
        verify(ClassImposterizer.INSTANCE).imposterise(any(), any(Class.class), any(Class[].class));
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithInvalidMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }
    
    @Test
    public void testIsMock() {
        Object mock = mock(Class.class);
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        
        MockUtil mockUtil = new MockUtil();
        
        when(Enhancer.isEnhanced(any(Class.class))).thenReturn(true);
        
        boolean result = mockUtil.isMock(mock);
        
        assertEquals(true, result);
        
        verify(ClassImposterizer.INSTANCE).imposterise(any(), any(Class.class), any(Class[].class));
    }
    
    @Test
    public void testIsMockWithNull() {
        MockUtil mockUtil = new MockUtil();
        
        boolean result = mockUtil.isMock(null);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testIsMockWithInvalidMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        
        boolean result = mockUtil.isMock(mock);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testGetMockName() {
        Object mock = mock(Class.class);
        ClassImposterizer.INSTANCE = mock(ClassImposterizer.class);
        MockHandlerInterface mockHandlerMock = mock(MockHandlerInterface.class);
        
        MockUtil mockUtil = new MockUtil();
        
        when(mockHandlerMock.getMockSettings()).thenReturn(mockSettingsMock);
        when(mockSettingsMock.getMockName()).thenReturn(mock(MockName.class));
        
        when(Enhancer.isEnhanced(any(Class.class))).thenReturn(true);
        
        MockName result = mockUtil.getMockName(mock);
        
        assertEquals(mock(MockName.class), result);
        
        verify(mockHandlerMock).getMockSettings();
        
        verify(ClassImposterizer.INSTANCE).imposterise(any(), any(Class.class), any(Class[].class));
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithInvalidMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(mock);
    }
}