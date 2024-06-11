package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mock.MockCreationValidator;
import org.mockito.quality.Strictness;

import java.io.Serializable;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    private MockCreationValidator creationValidator = mock(MockCreationValidator.class);
    private MockUtil mockUtil = new MockUtil(creationValidator);

    @Test
    public void testCreateMock() {
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);

        when(creationValidator.validateType(classToMock)).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces())).thenReturn(true);
        when(creationValidator.validateMockedType(classToMock, settings.getSpiedInstance())).thenReturn(true);

        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
        Class<?>[] interfaces = settings.getExtraInterfaces();
        Class<?>[] ancillaryTypes = {Serializable.class};

        Object spiedInstance = settings.getSpiedInstance();

        Factory mock = mock(Factory.class);
        when(ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes)).thenReturn(mock);

        LenientCopyTool copyTool = mock(LenientCopyTool.class);
        doNothing().when(copyTool).copyToMock(spiedInstance, mock);

        MockUtil mockUtil = new MockUtil();
        T result = mockUtil.createMock(classToMock, settings);

        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());

        verify(mockUtil).newMethodInterceptorFilter(settings);
        verify(mockUtil).getInterceptor(mock);

        verify(copyTool).copyToMock(spiedInstance, mock);

        assertEquals(mock, result);
    }

    @Test
    public void testResetMock() {
        T mock = mock(T.class);
        MockHandlerInterface<T> oldMockHandler = mock(MockHandlerInterface.class);
        MockHandler<T> newMockHandler = mock(MockHandler.class);
        MethodInterceptorFilter newFilter = mock(MethodInterceptorFilter.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);
        when(newMethodInterceptorFilter(settings)).thenReturn(newFilter);
        when(org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS)).thenReturn(settings);

        doNothing().when(((Factory) mock)).setCallback(0, newFilter);

        mockUtil.resetMock(mock);

        verify(mockUtil).getMockHandler(mock);
        verify(mockUtil).newMethodInterceptorFilter(settings);
        verify(mockUtil).getInterceptor(mock);

        verify(((Factory) mock)).setCallback(0, newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        T mock = null;

        mockUtil.getMockHandler(mock);

        verify(mockUtil).getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithInvalidMock() {
        T mock = mock(T.class);

        when(isMockitoMock(mock)).thenReturn(false);
        when(mock.getClass()).thenReturn(mock.getClass().getName());

        mockUtil.getMockHandler(mock);

        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testGetMockHandlerWithValidMock() {
        T mock = mock(T.class);
        MockHandlerInterface<T> mockHandler = mock(MockHandlerInterface.class);

        when(isMockitoMock(mock)).thenReturn(true);
        when(getInterceptor(mock)).thenReturn(mockHandler);

        MockHandlerInterface<T> result = mockUtil.getMockHandler(mock);

        verify(mockUtil).getMockHandler(mock);
        verify(isMockitoMock(mock));
        verify(getInterceptor(mock));

        assertEquals(mockHandler, result);
    }

    @Test
    public void testIsMock() {
        T mock = mock(T.class);

        when(isMockitoMock(mock)).thenReturn(true);

        boolean result = mockUtil.isMock(mock);

        verify(mockUtil).isMock(mock);
        verify(isMockitoMock(mock));

        assertTrue(result);
    }

    @Test
    public void testIsMockWithNull() {
        T mock = null;

        boolean result = mockUtil.isMock(mock);

        verify(mockUtil).isMock(mock);
        verify(isMockitoMock(mock));

        assertFalse(result);
    }

    @Test
    public void testIsMockWithInvalidMock() {
        T mock = mock(T.class);

        when(isMockitoMock(mock)).thenReturn(false);

        boolean result = mockUtil.isMock(mock);

        verify(mockUtil).isMock(mock);
        verify(isMockitoMock(mock));

        assertFalse(result);
    }

    @Test
    public void testGetInterceptorWithFactory() {
        T mock = mock(T.class);
        Factory factory = mock(Factory.class);
        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);

        when(mock instanceof Factory).thenReturn(true);
        when(factory.getCallback(0)).thenReturn(filter);

        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        verify(mockUtil).getInterceptor(mock);
        verify(mock instanceof Factory);
        verify(factory).getCallback(0);

        assertEquals(filter, result);
    }

    @Test
    public void testGetInterceptorWithoutFactory() {
        T mock = mock(T.class);

        when(mock instanceof Factory).thenReturn(false);

        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        verify(mockUtil).getInterceptor(mock);
        verify(mock instanceof Factory);

        assertNull(result);
    }

    @Test
    public void testGetMockName() {
        T mock = mock(T.class);
        MockHandlerInterface<T> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);

        MockName result = mockUtil.getMockName(mock);

        verify(mockUtil).getMockName(mock);
        verify(mockUtil).getMockHandler(mock);
        verify(settings).getMockName();

        assertEquals(mockName, result);
    }
}