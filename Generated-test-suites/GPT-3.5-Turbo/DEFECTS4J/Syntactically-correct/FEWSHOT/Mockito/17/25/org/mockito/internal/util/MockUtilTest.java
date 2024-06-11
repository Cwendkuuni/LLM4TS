package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = mock(MockUtil.class);
        Class<MockUtil> classToMock = MockUtil.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // Test mock creation
        assertNotNull(mockUtil.createMock(classToMock, settings));
        
        // Test mock validation
        verify(mockUtil, times(1)).createMock(classToMock, settings);
        
        // Test mock return value
        assertEquals(classToMock, mockUtil.createMock(classToMock, settings).getClass());
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = mock(MockUtil.class);
        Object mock = new Object();
        
        // Test mock reset
        mockUtil.resetMock(mock);
        
        // Test mock validation
        verify(mockUtil, times(1)).resetMock(mock);
    }
    
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = mock(MockUtil.class);
        Object mock = new Object();
        
        // Test getting mock handler
        assertNotNull(mockUtil.getMockHandler(mock));
        
        // Test mock validation
        verify(mockUtil, times(1)).getMockHandler(mock);
    }
    
    @Test
    public void testIsMock() {
        MockUtil mockUtil = mock(MockUtil.class);
        Object mock = new Object();
        
        // Test isMock function
        assertFalse(mockUtil.isMock(mock));
        
        // Test mock validation
        verify(mockUtil, times(1)).isMock(mock);
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = mock(MockUtil.class);
        Object mock = new Object();
        
        // Test getting mock name
        assertNotNull(mockUtil.getMockName(mock));
        
        // Test mock validation
        verify(mockUtil, times(1)).getMockName(mock);
    }
}