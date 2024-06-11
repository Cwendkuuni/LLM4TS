package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = new MockCreationValidator();
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<?> mockClass = MockitoMockitoTestClass.class;
        Object spiedInstance = spy(mockClass);
        MockSettingsImpl settings = new MockSettingsImpl();

        MockHandlerInterface<Object> mockHandler = mockUtil.createMock(mockClass, settings);
        assertNotNull(mockHandler);

        Object createdMock = mockUtil.createMock(mockClass, settings);
        assertNotNull(createdMock);
        assertNotNull(Proxy.getInvocationHandler(createdMock));

        settings.setSpiedInstance(spiedInstance);
        Object createdMockWithSpiedInstance = mockUtil.createMock(mockClass, settings);
        assertNotNull(createdMockWithSpiedInstance);
        LenientCopyTool.copyToMock(spiedInstance, createdMockWithSpiedInstance);
        verify(spiedInstance).toString();
    }

    @Test
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        assertNull(mockUtil.createMock(null, new MockSettingsImpl()));
    }

    @Test
    public void testCreateMockWithNullSettings() {
        MockUtil mockUtil = new MockUtil();
        assertNull(mockUtil.createMock(MockitoMockitoTestClass.class, null));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNotMockObject() {
        MockUtil mockUtil = new MockUtil();
        Object notMockObject = new Object();
        mockUtil.createMock(notMockObject.getClass(), new MockSettingsImpl());
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(MockitoMockitoTestClass.class);
        mockUtil.resetMock(mock);
        verify(mock).toString();
    }

    @Test
    public void testResetMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(MockitoMockitoTestClass.class);
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNotMockObject() {
        MockUtil mockUtil = new MockUtil();
        Object notMockObject = new Object();
        mockUtil.getMockHandler(notMockObject);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(MockitoMockitoTestClass.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNullObject() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetInterceptor() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(MockitoMockitoTestClass.class);
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertNotNull(interceptor);
    }

    @Test
    public void testGetInterceptorWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        assertNull(mockUtil.getInterceptor(null));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(MockitoMockitoTestClass.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

    @Test
    public void testGetMockNameWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        assertNull(mockUtil.getMockName(null));
    }
}

class MockitoMockitoTestClass {
}