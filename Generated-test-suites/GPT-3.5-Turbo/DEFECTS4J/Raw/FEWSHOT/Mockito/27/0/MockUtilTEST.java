package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;

import java.io.Serializable;

public class MockUtilTest {
    private MockCreationValidator mockCreationValidator = new MockCreationValidator();
    private MockUtil mockUtil = new MockUtil(mockCreationValidator);

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        String mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertEquals(classToMock, mock.getClass());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_NotAMockException() {
        Class<String> classToMock = String.class;
        MockHandlerInterface<String> mockHandler = mock(MockHandlerInterface.class);
        Factory factory = mock(Factory.class);
        MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class);

        when(factory.getCallback(0)).thenReturn(mock(MethodInterceptorFilter.class));
        mockUtil.getMockHandler(factory);
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        MockHandlerInterface<String> oldMockHandler = mock(MockHandlerInterface.class);
        MockHandler<String> newMockHandler = mock(MockHandler.class);
        MethodInterceptorFilter newFilter = mock(MethodInterceptorFilter.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);
        when(oldMockHandler.getHandler()).thenReturn(newMockHandler);
        when(newMockHandler.settings()).thenReturn(new MockSettingsImpl());
        when(mockSettings.defaultAnswer(Mockito.RETURNS_DEFAULTS)).thenReturn(mock(MethodInterceptorFilter.class));
        mockUtil.resetMock(mock);

        verify(mock).setCallback(0, newFilter);
    }

    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        MockHandler<String> mockHandler = mock(MockHandler.class);
        InvocationNotifierHandler<String> invocationNotifierHandler = mock(InvocationNotifierHandler.class);

        when(mockHandler.getHandler()).thenReturn(invocationNotifierHandler);
        mockUtil.newMethodInterceptorFilter(settings);

        verify(mockHandler).getHandler();
        verify(invocationNotifierHandler).getSettings();
    }

    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);
        MockHandlerInterface<String> mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(interceptor.getHandler()).thenReturn(mockHandler);
        when(mockUtil.getInterceptor(mock)).thenReturn(interceptor);

        MockHandlerInterface<String> result = mockUtil.getMockHandler(mock);

        assertEquals(mockHandler, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMockException() {
        String mock = mock(String.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        String mock = mock(String.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_NotAMock() {
        String mock = mock(String.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_Null() {
        String mock = null;

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testGetInterceptor() {
        String mock = mock(String.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
        Factory factory = mock(Factory.class);

        when(factory.getCallback(0)).thenReturn(interceptor);
        when(mockUtil.getInterceptor(mock)).thenCallRealMethod();

        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        assertEquals(interceptor, result);
        verify(factory).getCallback(0);
    }

    @Test
    public void testGetInterceptor_NotMethodInterceptorFilter() {
        String mock = mock(String.class);
        Callback callback = mock(Callback.class);
        Factory factory = mock(Factory.class);

        when(factory.getCallback(0)).thenReturn(callback);
        when(mockUtil.getInterceptor(mock)).thenCallRealMethod();

        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        assertNull(result);
        verify(factory).getCallback(0);
    }

    @Test
    public void testGetInterceptor_NotFactory() {
        String mock = mock(String.class);

        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        assertNull(result);
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        MockHandlerInterface<String> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);

        when(mockHandler.getSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        MockName result = mockUtil.getMockName(mock);

        assertEquals(mockName, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NotAMockException() {
        String mock = mock(String.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        mockUtil.getMockName(mock);
    }
}