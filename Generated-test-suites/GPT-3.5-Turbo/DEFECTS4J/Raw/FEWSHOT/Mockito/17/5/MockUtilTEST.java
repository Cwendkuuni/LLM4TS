package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.cglib.proxy.Factory;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.junit.Test;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        TestInterface mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(mock instanceof TestInterface);
        assertTrue(mock instanceof Factory);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        MockUtil mockUtil = new MockUtil();
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        TestInterface spiedInstance = new TestInterfaceImpl();

        settings.setSpiedInstance(spiedInstance);

        TestInterface mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(mock instanceof TestInterface);
        assertTrue(mock instanceof Factory);

        LenientCopyTool lenientCopyTool = new LenientCopyTool();
        lenientCopyTool.copyToMock(spiedInstance, mock);

        assertEquals(spiedInstance.getValue(), mock.getValue());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = null;
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = new TestInterfaceImpl();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = mock(TestInterface.class);
        MockHandlerInterface<TestInterface> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = mock(TestInterface.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMock() {
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = new TestInterfaceImpl();
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = mock(TestInterface.class);

        reset(mock);

        verify(mock).reset();
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = mock(TestInterface.class);

        when(mock.getMockName()).thenReturn("MockName");

        assertEquals("MockName", mockUtil.getMockName(mock));
    }
}