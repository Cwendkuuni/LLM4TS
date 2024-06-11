package org.mockito.internal.util;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(true);

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);
        assertTrue(mock instanceof String);

        settings.setSpiedInstance(mock);
        String spiedMock = mockUtil.createMock(String.class, settings);
        assertNotNull(spiedMock);
        assertTrue(spiedMock instanceof String);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullArgument() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler("test");
    }

    @Test
    public void testGetMockHandler_ValidMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        assertNotNull(mockUtil.getMockHandler(mock));
    }

    @Test
    public void testIsMock_Null() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMock_ValidMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        String mock = mock(String.class);
        assertNotNull(mockUtil.getMockName(mock));
    }
}