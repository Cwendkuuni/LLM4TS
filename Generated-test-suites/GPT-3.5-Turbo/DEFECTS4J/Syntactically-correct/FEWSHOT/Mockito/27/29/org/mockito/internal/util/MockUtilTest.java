package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil util = new MockUtil(creationValidator);
        
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        Object mock = util.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
    }
    
    @Test
    public void testCreateMockWithSerializableInterface() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil util = new MockUtil(creationValidator);
        
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl().serializable(true);
        
        Object mock = util.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        assertTrue(mock instanceof Serializable);
        
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
    }
    
    @Test
    public void testCreateMockWithSpiedInstance() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil util = new MockUtil(creationValidator);
        
        Class<?> classToMock = Object.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl().spiedInstance(spiedInstance);
        
        Object mock = util.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
    }
    
    @Test
    public void testResetMock() throws Exception {
        MockUtil util = new MockUtil();

        ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
        doReturn(classImposterizer).when(ClassImposterizer.class, "INSTANCE");

        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter newFilter = mock(MethodInterceptorFilter.class);

        Object mock = mock(Object.class);
        Field callbackField = mock.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        callbackField.setAccessible(true);
        callbackField.set(mock, oldMockHandler);

        when(util.getMockHandler(mock)).thenReturn(oldMockHandler);
        when(util.newMethodInterceptorFilter(any(MockSettingsImpl.class))).thenReturn(newFilter);

        util.resetMock(mock);

        verify(util).getMockHandler(mock);
        verify(util).newMethodInterceptorFilter(any(MockSettingsImpl.class));
        verify(classImposterizer).imposterise(newFilter, Object.class, new Class<?>[0]);
        verify(util).getMockHandler(mock);
        verify(util).getInterceptor(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil util = new MockUtil();
        
        util.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockUtil util = new MockUtil();
        
        Object obj = new Object();
        
        util.getMockHandler(obj);
    }
    
    @Test
    public void testGetMockHandlerWithMockitoMock() {
        MockUtil util = new MockUtil();
        
        MockHandlerInterface<Object> mockHandler = mock(MockHandlerInterface.class);
        Object mock = mock(Object.class);
        
        when(util.isMockitoMock(mock)).thenReturn(true);
        when(util.getInterceptor(mock)).thenReturn(null);
        when(util.getInterceptor(mock)).thenCallRealMethod();
        when(util.getMockHandler(mock)).thenReturn(mockHandler);
        
        MockHandlerInterface<Object> result = util.getMockHandler(mock);
        
        assertNotNull(result);
        assertEquals(mockHandler, result);
        
        verify(util).getMockHandler(mock);
        verify(util).isMockitoMock(mock);
        verify(util).getInterceptor(mock);
    }
    
    @Test
    public void testIsMockWithNull() {
        MockUtil util = new MockUtil();
        
        boolean result = util.isMock(null);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsMockWithNonMockObject() {
        MockUtil util = new MockUtil();
        
        Object obj = new Object();
        
        boolean result = util.isMock(obj);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsMockWithMockitoMock() {
        MockUtil util = new MockUtil();
        
        Object mock = mock(Object.class);
        
        when(util.isMockitoMock(mock)).thenReturn(true);
        when(util.isMockitoMock(mock)).thenCallRealMethod();
        
        boolean result = util.isMock(mock);
        
        assertTrue(result);
        
        verify(util).isMockitoMock(mock);
    }
    
    @Test
    public void testGetMockName() {
        MockUtil util = new MockUtil();
        
        MockHandlerInterface<Object> mockHandler = mock(MockHandlerInterface.class);
        
        Object mock = mock(Object.class);
        
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        
        when(util.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);
        
        MockName result = util.getMockName(mock);
        
        assertNotNull(result);
        assertEquals(mockName, result);
        
        verify(util).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getMockName();
    }
    
}