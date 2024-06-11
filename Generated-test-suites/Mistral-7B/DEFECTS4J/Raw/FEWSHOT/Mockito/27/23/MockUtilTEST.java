package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MockCreationValidator mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);
        String mock = mockUtil.createMock(stringClass, settings);
        Assert.assertNotNull(mock);
        Assert.assertSame(stringClass, mock.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidClass() {
        Class<Object> objectClass = Object.class;
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(objectClass, new MockSettingsImpl());
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String spiedInstance = "mock";
        MockCreationValidator mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);
        String mock = mockUtil.createMock(stringClass, settings, spiedInstance);
        Assert.assertNotNull(mock);
        Assert.assertSame(stringClass, mock.getClass());
        Assert.assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithNullSettings() {
        Class<String> stringClass = String.class;
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(stringClass, null);
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<String> stringClass = String.class;
        Class<Serializable> serializableClass = Serializable.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{serializableClass});
        MockCreationValidator mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);
        String mock = mockUtil.createMock(stringClass, settings);
        Assert.assertNotNull(mock);
        Assert.assertSame(stringClass, mock.getClass());
        Assert.assertArrayEquals(new Class[]{serializableClass}, mockUtil.getMockHandler(mock).getMockInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithNullExtraInterfaces() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(null);
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(stringClass, settings);
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);
        verifyNoInteractions(mock);
    }

    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(mockHandler);
        Assert.assertSame(mock.getClass(), mockHandler.getMockType());
        Assert.assertNotNull(mockHandler.getMockSettings());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithMockThatIsNotCreatedWithMockito() {
        String mock = spy(String.class);
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();
        MockName mockName = mockUtil.getMockName(mock);
        Assert.assertNotNull(mockName);
        Assert.assertNotNull(mockName.getName());
        Assert.assertNotNull(mockName.getMockType());
        Assert.assertEquals(getMockHandler(mock).getMockSettings().getMockName(), mockName.getName());
        Assert.assertEquals(mock.getClass(), mockName.getMockType());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(null);
    }

    private MockHandlerInterface<String> getMockHandler(Object mock) {
        return ((MockUtil) Proxy.getInvocationHandler(mock)).getMockHandler(mock.getClass().cast(mock));
    }
}