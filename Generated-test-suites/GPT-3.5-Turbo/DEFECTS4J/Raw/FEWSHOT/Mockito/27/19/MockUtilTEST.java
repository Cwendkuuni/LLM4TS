package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

public class MockUtilTest {
    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_DefaultSettings() {
        MockSettingsImpl defaultSettings = new MockSettingsImpl();
        defaultSettings.setSerializable(false);
        defaultSettings.setSpiedInstance(null);
        defaultSettings.setExtraInterfaces(null);

        Object mock = mockUtil.createMock(Object.class, defaultSettings);

        assertNotNull(mock);
        assertEquals(Object.class, mock.getClass());
    }

    @Test
    public void testCreateMock_Serializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(true);
        settings.setSpiedInstance(null);
        settings.setExtraInterfaces(null);

        Object mock = mockUtil.createMock(Object.class, settings);

        assertNotNull(mock);
        assertEquals(Object.class, mock.getClass());

        assertTrue(mock instanceof Serializable);
    }

    @Test
    public void testCreateMock_SpiedInstance() {
        Object spiedInstance = new Object();

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(false);
        settings.setSpiedInstance(spiedInstance);
        settings.setExtraInterfaces(null);

        Object mock = mockUtil.createMock(Object.class, settings);

        assertNotNull(mock);
        assertEquals(Object.class, mock.getClass());

        LenientCopyTool copyTool = new LenientCopyTool();
        assertSame(spiedInstance, copyTool.copyToMock(spiedInstance, mock));
    }

    @Test
    public void testCreateMock_ExtraInterfaces() {
        Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(false);
        settings.setSpiedInstance(null);
        settings.setExtraInterfaces(extraInterfaces);

        Object mock = mockUtil.createMock(Object.class, settings);

        assertNotNull(mock);
        assertEquals(Object.class, mock.getClass());

        for (Class<?> extraInterface : extraInterfaces) {
            assertTrue(extraInterface.isInstance(mock));
        }
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock() {
        mockUtil.resetMock(null);
    }

    @Test
    public void testResetMock() {
        // TODO: Write test case for resetting a mock
    }

    @Test
    public void testGetMockHandler_ValidMock() {
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());

        MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);

        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock() {
        Object notAMock = new Object();

        mockUtil.getMockHandler(notAMock);
    }

    @Test
    public void testIsMock_ValidMock() {
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_NullMock() {
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMock_NotAMock() {
        Object notAMock = new Object();

        assertFalse(mockUtil.isMock(notAMock));
    }

    @Test
    public void testGetMockName() {
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }
}