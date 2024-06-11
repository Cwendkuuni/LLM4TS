package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.MockHandlerInterface;
import org.mockito.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockUtil util = new MockUtil();

        Class<?> clazzToMock = String.class;
        Object mock = util.createMock(clazzToMock, settings);
        assertNotNull(mock);

        Class<?>[] ancillaryTypes = {Integer.class};
        mock = util.createMock(clazzToMock, settings.withExtraInterfaces(ancillaryTypes));
        assertNull(ancillaryTypes[0].isInstance(mock));
        assertNotNull(ancillaryTypes[1].isInstance(mock));

        Object spiedInstance = "foo";
        mock = util.createMock(clazzToMock, settings.withSpiedInstance(spiedInstance));
        assertEquals(spiedInstance, mock.toString());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil util = new MockUtil();
        util.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil util = new MockUtil();
        util.createMock(int.class, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullSettings() {
        MockUtil util = new MockUtil();
        util.createMock(String.class, null);
    }

    @Test
    public void testResetMock() {
        MockHandler<String> oldMockHandler = mock(MockHandler.class);
        String mock = mock(String.class, withSettings().defaultAnswer(Mockito.RETURNS_DEFAULTS));
        MockUtil util = new MockUtil();

        util.resetMock(mock);
        MockHandlerInterface<String> newMockHandler = util.getMockHandler(mock);
        assertNotNull(newMockHandler);
        assertNotNull(newMockHandler.getMockSettings().getLenientMode());
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        MockUtil util = new MockUtil();
        util.resetMock(null);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNonMock() {
        String nonMock = new String("foo");
        MockUtil util = new MockUtil();
        util.resetMock(nonMock);
    }

    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);
        MockUtil util = new MockUtil();

        MockHandlerInterface<String> mockHandler = util.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil util = new MockUtil();
        util.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMock() {
        String nonMock = new String("foo");
        MockUtil util = new MockUtil();
        util.getMockHandler(nonMock);
    }

    @Test
    public void testIsMockitoMock() {
        String mock = mock(String.class);
        Factory factory = (Factory) mock;
        MethodInterceptor interceptor = factory.getCallback(0);
        MockUtil util = new MockUtil();

        assertTrue(util.isMockitoMock(mock));
    }

    @Test
    public void testIsMock() {
        String mock = mock(String.class);
        MockUtil util = new MockUtil();

        assertTrue(util.isMock(mock));
    }

    @Test
    public void testIsMockWithNullMock() {
        MockUtil util = new MockUtil();
        assertFalse(util.isMock(null));
    }

    @Test
    public void testGetInterceptor() {
        String mock = mock(String.class);
        Factory factory = (Factory) mock;
        MockUtil util = new MockUtil();

        MethodInterceptor interceptor = util.getInterceptor(mock);
        assertNotNull(interceptor);
    }

    @Test
    public void testGetInterceptorWithNullMock() {
        MockUtil util = new MockUtil();
        assertNull(util.getInterceptor(null));
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        MockUtil util = new MockUtil();

        MockName mockName = util.getMockName(mock);
        assertEquals(MockName.MOCKITO_1, mockName);
    }

    @Test
    public void testGetMockNameWithNullMock() {
        MockUtil util = new MockUtil();
        assertNull(util.getMockName(null));
    }
}