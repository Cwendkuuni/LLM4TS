
package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

@SuppressWarnings("unchecked")
public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock() {
        String mock = null;
        
        mockUtil.resetMock(mock);
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        
        mockUtil.resetMock(mock);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        String mock = null;
        
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock() {
        String mock = "not a mock";
        
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);
        
        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock_NullMock() {
        String mock = null;
        
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_NotAMock() {
        String mock = "not a mock";
        
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock() {
        String mock = mock(String.class);
        
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        
        MockUtil.MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
    }

}
