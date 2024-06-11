package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MockCreationValidator mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);

        MockUtil mockUtilDefault = new MockUtil();

        String mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);

        String mockDefault = mockUtilDefault.createMock(classToMock, settings);
        Assert.assertNotNull(mockDefault);

        Class<?>[] extraInterfaces = new Class[] {Serializable.class};
        String mockWithExtraInterfaces = mockUtil.createMock(classToMock, settings.withExtraInterfaces(extraInterfaces));
        Assert.assertNotNull(mockWithExtraInterfaces);

        String spiedInstance = new String("Hello World");
        String mockWithSpiedInstance = mockUtil.createMock(classToMock, settings.withSpiedInstance(spiedInstance));
        Assert.assertNotNull(mockWithSpiedInstance);
        Assert.assertEquals("Hello World", mockWithSpiedInstance);

        MockSettingsImpl customSettings = new MockSettingsImpl();
        customSettings.initiateMockName(classToMock);
        MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(customSettings);
        Class<?>[] ancillaryTypes = customSettings.isSerializable() ? new Class[] {Serializable.class} : new Class[0];
        String customMock = (String) mockUtil.createMock(classToMock, settings, filter, ancillaryTypes);
        Assert.assertNotNull(customMock);
    }

    @Test
    public void testResetMock() {
        String mock = Mockito.mock(String.class);
        MockUtil mockUtil = new MockUtil();

        MockHandler<String> oldMockHandler = Mockito.mock(MockHandler.class);
        MethodInterceptorFilter newFilter = mockUtil.newMethodInterceptorFilter(new MockSettingsImpl());

        mockUtil.resetMock(mock);

        Assert.assertSame(oldMockHandler, mockUtil.getMockHandler(mock));
        Assert.assertNull(mockUtil.getInterceptor(mock));
    }

    @Test
    public void testIsMock() {
        String mock = Mockito.mock(String.class);
        Object notAMock = new Object();

        Assert.assertTrue(mockUtil.isMock(mock));
        Assert.assertFalse(mockUtil.isMock(notAMock));
    }

    @Test
    public void testGetInterceptor() {
        String mock = Mockito.mock(String.class);

        Assert.assertNull(mockUtil.getInterceptor(mock));
    }

    @Test
    public void testGetMockHandler() {
        String mock = Mockito.mock(String.class);

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(mockHandler);
    }

    @Test
    public void testGetMockName() {
        String mock = Mockito.mock(String.class);

        MockName mockName = mockUtil.getMockName(mock);
        Assert.assertNotNull(mockName);
        Assert.assertEquals("MockitoMock_" + System.identityHashCode(mock), mockName.getName());
    }

    @Test
    public void testIsMockitoMock() {
        String mock = Mockito.mock(String.class);

        Assert.assertTrue(mockUtil.isMockitoMock(mock));
    }
}