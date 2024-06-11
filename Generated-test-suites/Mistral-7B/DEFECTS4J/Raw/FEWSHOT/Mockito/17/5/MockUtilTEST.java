package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mockito3.MockitoCore;
import org.mockito.plugin.MockPlugin;
import org.mockito.plugins.MockPluginConfiguration;
import org.mockito.plugins.MockPluginProvider;
import org.powermock.api.mockito.internal.Mockito3MockType;
import org.powermock.core.classloader.MockClassLoader;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

    @Test
    public void testCreateMockWithSettings() {
        MockPluginConfiguration config = new MockPluginConfiguration();
        config.setPluginClass(MockitoCore.class);
        MockPluginPlugin mockPlugin = new MockPluginPlugin();
        MockPluginProvider mockitoMockitoPlugin = new MockPluginProvider(config, Arrays.asList(mockPlugin));
        MockitoCore mockitoCore = new MockitoCore(mockitoMockitoPlugin);

        MockSettingsImpl settings = new MockSettingsImpl("TestClass", new MockHandler<Object>(mockitoCore));
        settings.setExtraInterfaces(new Class[]{MyClass.class});

        MockUtil mockUtil = new MockUtil(new CreationValidator());
        MyClass mock = (MyClass) mockUtil.createMock(MyClass.class, settings);

        assertNotNull(mock);
        verify(mock, mockitoCore.verifyNoMoreInteractions()).myMethod();
    }

    @Test
    public void testCreateMockWithoutSettings() {
        MockUtil mockUtil = new MockUtil();
        MyClass mock = (MyClass) mockUtil.createMock(MyClass.class);

        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(int.class);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        MyClass spiedInstance = spy(new MyClass());
        MockSettingsImpl settings = new MockSettingsImpl("TestClass", new MockHandler<Object>(null));
        settings.setSpiedInstance(spiedInstance);

        MockUtil mockUtil = new MockUtil(new CreationValidator());
        MyClass mock = (MyClass) mockUtil.createMock(MyClass.class, settings);

        LenientCopyTool.copyToMock(spiedInstance, mock);
        verify(spiedInstance).myMethod();
    }

    @Test
    public void testResetMock() {
        MyClass mock = mock(MyClass.class);
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        verify(mock, MockitoCore.verifyNoMoreInteractions()).myMethod();
    }

    @Test(expected = NullPointerException.class)
    public void testResetMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        MyClass mock = mock(MyClass.class);
        MockUtil mockUtil = new MockUtil();

        assertNotNull(mockUtil.getMockHandler(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testIsMockitoMock() {
        MyClass mock = mock(MyClass.class, withSettings().defaultAnswer(MockitoCore.VERIFY_NO_MORE_INTERACTIONS));
        MockUtil mockUtil = new MockUtil();

        assertTrue(mockUtil.isMockitoMock(mock));
    }

    @Test
    public void testIsMockitoMockWithNonMockitoMock() {
        Object nonMockitoMock = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[0], (proxy, method, args) -> null);
        MockUtil mockUtil = new MockUtil();

        assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
    }

    @Test(expected = NotAMockException.class)
    public void testIsMockitoMockWithNullObject() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.isMockitoMock(null);
    }

    @Test
    public void testGetInterceptor() {
        MyClass mock = mock(MyClass.class, withSettings().defaultAnswer(MockitoCore.VERIFY_NO_MORE_INTERACTIONS));
        MockUtil mockUtil = new MockUtil();

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertNotNull(interceptor);
    }

    @Test
    public void testGetMockName() {
        MyClass mock = mock(MyClass.class, withSettings().defaultAnswer(MockitoCore.VERIFY_NO_MORE_INTERACTIONS));
        MockUtil mockUtil = new MockUtil();

        String mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

    @Test
    public void testResetMockWithMockito3MockType() {
        MyClass mock = mock(MyClass.class, withSettings().defaultAnswer(RETURNS_DEFAULTS).mockType(Mockito3MockType.JUNIT_JUnit4_13_Mockito3));
        MockUtil mockUtil = new MockUtil();

        mockUtil.resetMock(mock);
        verify(mock, MockitoCore.verifyNoMoreInteractions()).myMethod();
    }

    @Test
    public void testCreateMockWithClassLoader() throws Exception {
        ClassLoader classLoader = new MockClassLoader();
        MyClass mockClass = classLoader.loadClass(MyClass.class.getName());
        MockSettingsImpl settings = new MockSettingsImpl("TestClass", new MockHandler<Object>(null));

        MockUtil mockUtil = new MockUtil(new CreationValidator());
        MyClass mock = (MyClass) mockUtil.createMock(mockClass, settings);

        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithClassLoaderAndSpiedInstance() throws Exception {
        ClassLoader classLoader = new MockClassLoader();
        MyClass spiedInstance = spy(new MyClass());
        MockSettingsImpl settings = new MockSettingsImpl("TestClass", new MockHandler<Object>(null));
        settings.setSpiedInstance(spiedInstance);

        MockUtil mockUtil = new MockUtil(new CreationValidator());
        MyClass mock = (MyClass) mockUtil.createMock(MyClass.class, settings);

        LenientCopyTool.copyToMock(spiedInstance, mock);
        verify(spiedInstance).myMethod();
    }

    private static class MyClass {
        public void myMethod() {}
    }

    private static class MockPluginPlugin implements MockPlugin {
        @Override
        public void init(MockPluginConfiguration config) {}
    }
}