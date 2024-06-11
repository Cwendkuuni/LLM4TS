package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock_basic() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(mock);
    }
    
    @Test
    public void testResetMock_basic() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // No exception thrown means resetMock works fine
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        mockUtil.getMockHandler(null);
    }
    
    @Test
    public void testGetMockHandler_validMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(mockUtil.getMockHandler(mock));
    }
    
    @Test
    public void testIsMock_notMockObject() {
        Object obj = new Object();
        assertFalse(mockUtil.isMock(obj));
    }
    
    @Test
    public void testIsMock_mockObject() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testGetMockName() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(mockUtil.getMockName(mock));
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_invalidMock() {
        Object obj = new Object();
        mockUtil.getMockHandler(obj);
    }
    
    private interface ClassToMock {}
}