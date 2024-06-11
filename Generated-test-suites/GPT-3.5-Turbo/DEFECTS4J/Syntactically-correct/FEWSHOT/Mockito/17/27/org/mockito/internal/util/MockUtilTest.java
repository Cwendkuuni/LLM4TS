
package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidType() {
        Class<Object> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        mockUtil.resetMock(mock);

        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        Object mock = null;
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);

        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        Object mock = null;
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        Object nonMock = new Object();
        mockUtil.getMockHandler(nonMock);
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        boolean isMock = mockUtil.isMock(mock);

        assertTrue(isMock);
    }

    @Test
    public void testIsMockWithNonMockObject() {
        Object nonMock = new Object();
        boolean isMock = mockUtil.isMock(nonMock);

        assertTrue(!isMock);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }
}
