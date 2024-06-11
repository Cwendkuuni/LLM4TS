package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;

public class MockUtilTest {
    @Test
    public void testCreateMockWithValidArguments() {
        MockCreationValidator mockCreationValidator = new MockCreationValidator();
        MockUtil mockUtil = new MockUtil(mockCreationValidator);

        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();

        Class<Object> objectClass = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        try {
            mockUtil.createMock(objectClass, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        MockCreationValidator mockCreationValidator = new MockCreationValidator();
        MockUtil mockUtil = new MockUtil(mockCreationValidator);

        Class<String> stringClass = String.class;
        Class<Serializable> serializableClass = Serializable.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[] { serializableClass });

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        MockCreationValidator mockCreationValidator = new MockCreationValidator();
        MockUtil mockUtil = new MockUtil(mockCreationValidator);

        Class<String> stringClass = String.class;
        Object spiedInstance = "hello";
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(spiedInstance);

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
        assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
    }

    @Test
    public void testCreateMockWithNullSpiedInstance() {
        MockUtil mockUtil = new MockUtil();

        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(null);

        try {
            mockUtil.createMock(stringClass, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);

        mockUtil.resetMock(mock);

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        InvocationNotifierHandler<String> invocationNotifierHandler = mockHandler.getInvocationNotifierHandler();
        assertNotNull(invocationNotifierHandler);
    }

    @Test
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.getMockHandler(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testGetMockHandlerWithMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testGetMockHandlerWithNonMockitoMock() {
        String spiedInstance = spy(String.class);

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.getMockHandler(spiedInstance);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testIsMockWithMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        String spiedInstance = spy(String.class);

        MockUtil mockUtil = new MockUtil();

        assertFalse(mockUtil.isMock(spiedInstance));
    }

    @Test
    public void testIsMockWithNullObject() {
        MockUtil mockUtil = new MockUtil();

        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetInterceptorWithMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        Factory factory = (Factory) mock;

        MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mock);
        assertNotNull(interceptorFilter);
        assertEquals(factory.getCallback(0), interceptorFilter);
    }

    @Test
    public void testGetInterceptorWithNonMockitoMock() {
        String spiedInstance = spy(String.class);
        Factory factory = (Factory) Proxy.getProxyClass(spiedInstance.getClass().getClassLoader(), String[].class).getInterfaces()[0].newInstance(new Object[]{spiedInstance});

        MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(spiedInstance);
        assertNull(interceptorFilter);
    }

    @Test
    public void testGetMockNameWithMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);

        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

    @Test
    public void testGetMockNameWithNullMock() {
        MockUtil mockUtil = new MockitoUtil();

        try {
            mockUtil.getMockName(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }
}