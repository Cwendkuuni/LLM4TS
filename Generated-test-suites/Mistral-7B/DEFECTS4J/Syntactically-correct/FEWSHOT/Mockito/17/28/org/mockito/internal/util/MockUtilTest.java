package org.mockito.internal.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doAnswer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        Object mock = mockUtil.createMock(classToMock, settings);

        MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals(classToMock, mockHandler.getMockType());
        assertEquals(settings, mockHandler.getMockSettings());
        assertEquals(RETURNS_DEFAULTS, mockHandler.getMockSettings().getDefaultAnswer());
        verifyZeroInteractions(mock);
    }

    @Test
    public void testCreateMock_withExtraInterfaces() {
        Class<Object> classToMock = Object.class;
        Class<?> extraInterface = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[]{extraInterface});

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals(classToMock, mockHandler.getMockType());
        assertEquals(Arrays.asList(extraInterface), mockHandler.getMockSettings().getExtraInterfaces());
        assertEquals(RETURNS_DEFAULTS, mockHandler.getMockSettings().getDefaultAnswer());
        verifyZeroInteractions(mock);
    }

    @Test
    public void testCreateMock_withSpiedInstance() {
        Class<Object> classToMock = Object.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals(classToMock, mockHandler.getMockType());
        assertEquals(settings, mockHandler.getMockSettings());
        assertEquals(RETURNS_DEFAULTS, mockHandler.getMockSettings().getDefaultAnswer());
        verifyZeroInteractions(mock);

        new LenientCopyTool().copyToMock(spiedInstance, mock);

        Method method = null;
        try {
            method = Object.class.getDeclaredMethod("toString");
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Object result = mock.toString();
        assertEquals(spiedInstance.toString(), result);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);

        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockUtil.getMockHandler(mock), mockUtil.getMockSettings(mock));
        MethodInterceptor interceptor = mock(MethodInterceptor.class);
        doAnswer(invocation -> {
            Method method = invocation.getMethod();
            System.out.println(method.getName());
            return null;
        }).when(mock).answer(RETURNS_DEFAULTS);

        mockUtil.resetMock(mock);

        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);

        MockUtil mockUtil = new MockUtil();
        MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals(mock, mockHandler.getMock());
        assertEquals(mockUtil.getMockSettings(mock), mockHandler.getMockSettings());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_withNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testIsMockitoMock() {
        Object mock = mock(Object.class);
        assertTrue(new MockUtil().isMockitoMock(mock));

        Object notAMock = new Object();
        assertFalse(new MockUtil().isMockitoMock(notAMock));
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        assertTrue(new MockUtil().isMock(mock));

        Object notAMock = new Object();
        assertFalse(new MockUtil().isMock(notAMock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(Object.class);

        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockUtil.getMockHandler(mock), mockUtil.getMockSettings(mock));
        MethodInterceptor interceptor = mockUtil.getInterceptor(mock);
        assertSame(interceptor, filter.getInterceptor());
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);

        MockUtil mockUtil = new MockitoBDDMockitoTestUtils.MockUtil();
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals("mock", mockName.getName());
    }
}