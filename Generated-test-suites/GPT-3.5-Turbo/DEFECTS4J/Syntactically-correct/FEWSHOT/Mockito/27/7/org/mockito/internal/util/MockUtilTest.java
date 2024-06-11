package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.LenientCopyToolTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = Mockito.mock(MockCreationValidator.class);
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);

        MockUtil mockUtil = new MockUtil(creationValidator);
        Object mock = mockUtil.createMock(Object.class, settings);

        assertNotNull(mock);
        verify(creationValidator).validateType(Object.class);
        verify(creationValidator).validateExtraInterfaces(Object.class, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(Object.class, settings.getSpiedInstance());
        verify(settings).initiateMockName(Object.class);
    }

    @Test
    public void testResetMock() {
        MockHandlerInterface mockHandler = Mockito.mock(MockHandlerInterface.class);
        MethodInterceptorFilter methodInterceptorFilter = Mockito.mock(MethodInterceptorFilter.class);
        Factory mock = Mockito.mock(Factory.class);

        when(mockHandler.getHandler()).thenReturn(mock);
        when(methodInterceptorFilter.getHandler()).thenReturn(mockHandler);
        when(mock.getCallback(0)).thenReturn(methodInterceptorFilter);

        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        verify(mock).setCallback(0, methodInterceptorFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockHandlerInterface mockHandler = Mockito.mock(MockHandlerInterface.class);
        Factory mock = Mockito.mock(Factory.class);

        when(mock.getCallback(0)).thenReturn(mockHandler);

        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        MockHandlerInterface mockHandler = Mockito.mock(MockHandlerInterface.class);
        Factory mock = Mockito.mock(Factory.class);

        when(mock.getCallback(0)).thenReturn(mockHandler);

        MockUtil mockUtil = new MockUtil();
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetInterceptor() throws NoSuchFieldException, IllegalAccessException {
        MethodInterceptorFilter methodInterceptorFilter = Mockito.mock(MethodInterceptorFilter.class);
        Field field = Factory.class.getDeclaredField("callback");
        field.setAccessible(true);
        field.set(null, methodInterceptorFilter);

        Object mock = new Object();

        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        assertEquals(methodInterceptorFilter, interceptor);
    }

    @Test
    public void testGetInterceptorWithNonMockObject() throws NoSuchFieldException, IllegalAccessException {
        MethodInterceptorFilter methodInterceptorFilter = Mockito.mock(MethodInterceptorFilter.class);
        Field field = Factory.class.getDeclaredField("callback");
        field.setAccessible(true);
        field.set(null, methodInterceptorFilter);

        Object nonMockObject = new Object();

        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(nonMockObject);

        assertNull(interceptor);
    }

    @Test
    public void testGetMockName() {
        MockHandlerInterface mockHandler = Mockito.mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = Mockito.mock(MockSettingsImpl.class);
        MockName mockName = Mockito.mock(MockName.class);
        Factory mock = Mockito.mock(Factory.class);

        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);

        MockUtil mockUtil = new MockUtil();
        assertEquals(mockName, mockUtil.getMockName(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNonMockObject() {
        MockHandlerInterface mockHandler = Mockito.mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = Mockito.mock(MockSettingsImpl.class);
        Factory notAMock = Mockito.mock(Factory.class);

        when(mockHandler.getMockSettings()).thenReturn(mockSettings);

        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(notAMock);
    }
}