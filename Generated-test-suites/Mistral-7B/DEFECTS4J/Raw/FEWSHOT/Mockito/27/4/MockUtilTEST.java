package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(Integer.class, new MockSettingsImpl());
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{Serializable.class});
        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));
        assertInstanceOf(Serializable.class, mock);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        String spiedInstance = "spied";
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(spiedInstance);
        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));
        assertEquals(spiedInstance, mock.toString());
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();

        mockUtil.resetMock(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();

        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        assertSame(mock, mockHandler.getMock());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();

        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertTrue(mockName.getName().startsWith("MockitoMock:"));
    }

    @Test
    public void testGetMockNameWithCustomName() {
        String name = "CustomName";
        String mock = mock(String.class, withName(name));
        MockUtil mockUtil = new MockUtil();

        MockName mockName = mockUtil.getMockName(mock);
        assertEquals(name, mockName.getName());
    }

    private boolean isMockitoMock(Object mock) {
        return Proxy.isProxyClass(mock.getClass());
    }
}