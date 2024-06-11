package org.mockito.internal.util;

import static org.mockito.Mockito.*;

import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.reflection.ArrayUtils;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import org.mockito.cglib.proxy.Factory;

import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.creation.MethodInterceptorFilter;

public class MockUtilTest {
    
    MockUtil util;
    
    @Before
    public void setUp() {
        util = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        MockUtil util = new MockUtil(mockCreationValidator);
        
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        
        String[] extraInterfaces = {"TestInterface"};
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);
        
        String spiedInstance = "TestInstance";
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);
        
        util.createMock(classToMock, settings);
        
        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, extraInterfaces);
        verify(mockCreationValidator).validateMockedType(classToMock, spiedInstance);
        
        verify(settings).initiateMockName(classToMock);
        
        verify(settings).isSerializable();
        verify(settings).getExtraInterfaces();
        
        verify(settings).getSpiedInstance();
        
        verify(ClassImposterizer.INSTANCE).imposterise(any(MethodInterceptorFilter.class), eq(classToMock), any(Class[].class));
        
        verify(new LenientCopyTool()).copyToMock(spiedInstance, mock(classToMock));
    }
    
    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MethodInterceptorFilter mockInterceptor = mock(MethodInterceptorFilter.class);
        
        Factory factoryMock = mock(Factory.class);
        when(factoryMock.getCallback(0)).thenReturn(mockInterceptor);
        
        when(mock instanceof Factory).thenReturn(true);
        when((Factory) mock).thenReturn(factoryMock);
        
        util.resetMock(mock);
        
        verify(factoryMock).setCallback(0, mockInterceptor);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNull() {
        util.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNotAMock() {
        Object mock = new Object();
        
        util.getMockHandler(mock);
    }
    
    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);
        
        Factory factoryMock = mock(Factory.class);
        Callback callbackMock = mock(MethodInterceptorFilter.class);
        
        when(mock instanceof Factory).thenReturn(true);
        when((Factory) mock).thenReturn(factoryMock);
        when(factoryMock.getCallback(0)).thenReturn(callbackMock);
        
        util.getMockHandler(mock);
        
        verify(factoryMock).getCallback(0);
    }
    
    @Test
    public void testIsMockNull() {
        assertFalse(util.isMock(null));
    }
    
    @Test
    public void testIsMockitoMock() {
        Object mock = mock(Object.class);
        
        Factory factoryMock = mock(Factory.class);
        when(mock instanceof Factory).thenReturn(true);
        when((Factory) mock).thenReturn(factoryMock);
        
        assertTrue(util.isMock(mock));
    }
    
    @Test
    public void testIsMockitoMockFalse() {
        Object notMock = new Object();
        
        assertFalse(util.isMock(notMock));
    }
    
    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        
        Factory factoryMock = mock(Factory.class);
        when(mock instanceof Factory).thenReturn(true);
        when((Factory) mock).thenReturn(factoryMock);
        
        assertTrue(util.isMock(mock));
    }
    
    @Test
    public void testGetInterceptor() {
        Object mock = mock(Object.class);
        MethodInterceptorFilter mockInterceptor = mock(MethodInterceptorFilter.class);
        
        Factory factoryMock = mock(Factory.class);        
        when(mock instanceof Factory).thenReturn(true);
        when((Factory) mock).thenReturn(factoryMock);
        when(factoryMock.getCallback(0)).thenReturn(mockInterceptor);
        
        util.getInterceptor(mock);
        
        verify(factoryMock).getCallback(0);
    }
    
    @Test
    public void testGetInterceptorNotFactory() {
        Object mock = mock(Object.class);
        
        assertNull(util.getInterceptor(mock));
    }
    
    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        
        when(util.getMockHandler(mock)).thenReturn(mockHandler);
        
        util.getMockName(mock);
        
        verify(mockHandler).getMockSettings();
    }
}