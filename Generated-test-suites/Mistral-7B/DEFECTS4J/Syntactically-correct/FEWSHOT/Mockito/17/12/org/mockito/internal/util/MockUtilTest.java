package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.ClassImposterizer;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {
    private CreationValidator creationValidator = mock(CreationValidator.class);
    private MockUtil mockUtil = new MockUtil(creationValidator);

    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl("mockName", null, null, null);

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        Class<?>[] ancillaryTypes = settings.getExtraInterfaces() == null ? new Class[0] : settings.getExtraInterfaces();
        assertEquals(ancillaryTypes.length, 0);

        verify(creationValidator, times(3)).validateType(eq(stringClass));
        verify(creationValidator, times(2)).validateExtraInterfaces(eq(stringClass), anyArray());
        verify(creationValidator, times(1)).validateMockedType(eq(stringClass), any());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        Class<?> nullClass = null;
        MockSettingsImpl settings = new MockSettingsImpl("mockName", null, null, null);
        mockUtil.createMock(nullClass, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullSettings() {
        Class<String> stringClass = String.class;
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(stringClass, null);
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<MyMockClass> myMockClass = MyMockClass.class;
        Class<MyExtraInterface> myExtraInterface = MyExtraInterface.class;
        MockSettingsImpl settings = new MockSettingsImpl("mockName", myExtraInterface, null, null);

        MyMockClass mock = mockUtil.createMock(myMockClass, settings);
        assertNotNull(mock);
        MockHandler<MyMockClass> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        Class<?>[] ancillaryTypes = settings.getExtraInterfaces();
        assertEquals(ancillaryTypes.length, 1);
        assertEquals(ancillaryTypes[0], myExtraInterface);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        Class<MyMockClass> myMockClass = MyMockClass.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl("mockName", null, null, spiedInstance);

        MyMockClass mock = mockUtil.createMock(myMockClass, settings);
        assertNotNull(mock);
        MockHandler<MyMockClass> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        new LenientCopyTool().copyToMock(spiedInstance, mock);
    }

    @Test
    public void testResetMock() {
        Class<MyMockClass> myMockClass = MyMockClass.class;
        Object mock = createMock(myMockClass, new MockSettingsImpl("mockName", null, null, null));
        mockUtil.resetMock(mock);
        MockHandler<MyMockClass> newMockHandler = new MockHandler<MyMockClass>(null);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, withSettings().defaultAnswer(RETURNS_DEFAULTS));
        Factory factory = (Factory) mock;
        factory.setCallback(0, newFilter);
    }

    @Test
    public void testIsMock() {
        MyMockClass mock = createMock(MyMockClass.class, new MockSettingsImpl("mockName", null, null, null));
        assertTrue(mockUtil.isMock(mock));

        Object notAMock = new Object();
        assertFalse(mockUtil.isMock(notAMock));
    }

    @Test
    public void testGetMockName() {
        Class<MyMockClass> myMockClass = MyMockClass.class;
        Object mock = createMock(myMockClass, new MockSettingsImpl("mockName", null, null, null));
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals("mockName", mockName.getName());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        Object nullMock = null;
        mockUtil.getMockHandler(nullMock);
    }

    private <T> T createMock(Class<T> clazz, MockSettingsImpl settings) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, (InvocationHandler) mockUtil);
    }

    @SuppressWarnings("unchecked")
    private MethodInterceptor<Object> getInterceptor(Object mock) {
        return (MethodInterceptor<Object>) Proxy.getInvocationHandler(mock);
    }
}

class MyMockClass {
}

interface MyExtraInterface {
}