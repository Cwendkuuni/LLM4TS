package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);
        assertTrue(mock instanceof String);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);

        mockUtil.resetMock(mock);

        assertFalse(mock.isEmpty());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(new Object());
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);

        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        assertTrue(mockHandler instanceof MockHandlerInterface);
    }

    @Test
    public void testIsMockWithNull() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMockWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetInterceptorWithNull() {
        MockUtil mockUtil = new MockUtil();
        assertNull(mockUtil.getInterceptor(null));
    }

    @Test
    public void testGetInterceptorWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        assertNull(mockUtil.getInterceptor(new Object()));
    }

    @Test
    public void testGetInterceptor() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS);
        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertNotNull(interceptor);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl().defaultAnswer(RETURNS_DEFAULTS).name("mockName");
        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);

        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertEquals("mockName", mockName.toString());
    }
}