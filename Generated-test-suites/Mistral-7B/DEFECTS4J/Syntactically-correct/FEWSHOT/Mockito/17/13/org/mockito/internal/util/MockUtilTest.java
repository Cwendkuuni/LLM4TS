package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.mock.MockHandler;
import org.mockito.internal.mock.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;

import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodInterceptorFilter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockName;

import java.lang.reflect.Method;

public class MockUtilTest {

    private CreationValidator creationValidator = new CreationValidator();
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testCreateMock() {
        Class<?> mockClass = MockitoMockitoTestClass.class;

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{String.class});
        settings.setSpiedInstance(new Object());

        MockHandler<MockitoMockitoTestClass> mockHandler = mockUtil.createMock(mockClass, settings);

        assertTrue(mockUtil.isMock(mockHandler.getMock()));
        assertEquals(settings.getMockName(), mockUtil.getMockName(mockHandler.getMock()));
        assertSame(settings.getSpiedInstance(), settings.getMock().getInvocationHandler());

        MockName mockName = mockUtil.getMockName(mockHandler.getMock());
        assertSame(mockName, mockHandler.getMockSettings().getMockName());

        MethodInterceptorFilter filter = Whitebox.getInternalState(mockHandler, "filter");
        assertSame(mockHandler, filter.getMockHandler());
        assertSame(settings, filter.getMockSettings());

        assertSame(settings, Whitebox.getInternalState(mockHandler, "settings"));

        ClassImposterizer classImposterizer = Whitebox.getInternalState(mockUtil, "ClassImposterizer");
        Method intercept = null;
        try {
            intercept = ClassImposterizer.class.getDeclaredMethod("imposterise", MethodInterceptorFilter.class, Class.class, Class[].class);
            intercept.setAccessible(true);
        } catch (NoSuchMethodException e) {
            // ignore, as this test does not depend on the implementation details of ClassImposterizer
        }

        if (intercept != null) {
            Object[] args = new Object[]{filter, mockClass, ancillaryTypes()};
            MockitoMockitoTestClass mock = (MockitoMockitoTestClass) intercept.invoke(classImposterizer, filter, mockClass, ancillaryTypes());
            assertSame(mockHandler.getMock(), mock);
            assertSame(settings.getSpiedInstance(), mock.getInvocationHandler());
        }

        verify(mockHandler).handleInvocation(MockitoMockitoTestClass.class, "doSomething", new Object[]{});
    }

    @Test
    public void testCreateMockWithNullClassToMock() {
        assertNull(mockUtil.createMock(null, new MockSettingsImpl()));
    }

    @Test
    public void testCreateMockWithNullSettings() {
        Class<?> mockClass = MockitoMockitoTestClass.class;
        MockHandler<MockitoMockitoTestClass> mockHandler = mockUtil.createMock(mockClass, null);
        assertFalse(mockUtil.isMock(mockHandler.getMock()));
        assertSame(mockHandler, mockHandler.getMock());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClassToMock() {
        mockUtil.createMock(Object.class, new MockSettingsImpl());
    }

    @Test
    public void testResetMock() {
        MockitoMockitoTestClass mock = mock(MockitoMockitoTestClass.class);
        MockHandler<MockitoMockitoTestClass> mockHandler = mockUtil.getMockHandler(mock);

        mockUtil.resetMock(mock);

        MockHandler<MockitoMockitoTestClass> newMockHandler = mockUtil.getMockHandler(mock);
        assertEquals(mockHandler, newMockHandler);

        verify(mock).doNothing();
        verify(mock).doSomething();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testIsMock() {
        MockitoMockitoTestClass mock = mock(MockitoMockitoTestClass.class);
        assertTrue(mockUtil.isMock(mock));

        Object plainObject = new Object();
        assertFalse(mockUtil.isMock(plainObject));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object plainObject = new Object();
        mockUtil.getMockHandler(plainObject);
    }

    @Test
    public void testGetInterceptorWithMock() {
        MockitoMockitoTestClass mock = mock(MockitoMockitoTestClass.class);
        MethodInterceptor interceptor = mockUtil.getInterceptor(mock);
        assertNotNull(interceptor);
        assertTrue(interceptor instanceof MethodInterceptorFilter);
    }

    @Test
    public void testGetInterceptorWithPlainObject() {
        Object plainObject = new Object();
        assertNull(mockUtil.getInterceptor(plainObject));
    }

    private Class<?>[] ancillaryTypes() {
        return new Class[0];
    }

    private static class MockitoMockitoTestClass {
        public void doSomething() {
        }

        public void doNothing() {
        }
    }
}