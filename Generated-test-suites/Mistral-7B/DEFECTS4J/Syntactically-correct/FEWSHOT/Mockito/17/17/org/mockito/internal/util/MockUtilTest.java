package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.proxy.InvocationHandler;
import org.mockito.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<MockUtilTest> classToMock = MockUtilTest.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));
        assertNull(settings.getSpiedInstance());

        settings.setSpiedInstance(new MockUtilTest());
        mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));
        assertEquals(new MockName("MockUtilTest"), getMockName(mock));
        assertSame(settings.getSpiedInstance(), getMockHandler(mock).getMockSettings().getSpiedInstance());
    }

    @Test
    public void testResetMock() {
        Class<MockUtilTest> classToMock = MockUtilTest.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = createMock(classToMock, settings);

        resetMock(mock);
        InvocationHandler handler = getInterceptor(mock);

        assertNotNull(handler);
        assertSame(RETURNS_DEFAULTS, handler.getHandler().handle(any(), eq(0)));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object nonMockitoMock = new Object();
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(nonMockitoMock);
    }

    private Object createMock(Class<?> classToMock, MockSettingsImpl settings) {
        ProxyFactory factory = mockitoProxy();
        return factory.getProxy(classToMock.getInterfaces());
    }

    private MockUtil mockitoProxy() {
        return mock(MockUtil.class);
    }

    private <T> MockHandlerInterface<T> getMockHandler(T mock) {
        return new MockUtil().getMockHandler(mock);
    }

    private <T> MethodInterceptorFilter getInterceptor(T mock) {
        return new MockUtil().getInterceptor(mock);
    }

    private <T> MockName getMockName(T mock) {
        return new MockUtil().getMockName(mock);
    }

    private boolean isMockitoMock(Object mock) {
        return new MockUtil().isMockitoMock(mock);
    }
}