package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

@SuppressWarnings("unchecked")
public class MockUtilTest {
    @Test
    public void testCreateMock() {
        // Test 1
        MockSettingsImpl settings1 = new MockSettingsImpl();
        settings1.setExtraInterfaces(null);
        settings1.setSpiedInstance(null);
        Object result1 = new MockUtil().createMock(Object.class, settings1);
        assertNotNull(result1);
        
        // Test 2
        MockSettingsImpl settings2 = new MockSettingsImpl();
        settings2.setExtraInterfaces(new Class<?>[0]);
        settings2.setSpiedInstance(null);
        Object result2 = new MockUtil().createMock(Object.class, settings2);
        assertNotNull(result2);
        
        // Test 3
        MockSettingsImpl settings3 = new MockSettingsImpl();
        settings3.setExtraInterfaces(new Class<?>[]{Runnable.class});
        settings3.setSpiedInstance(null);
        Object result3 = new MockUtil().createMock(Object.class, settings3);
        assertNotNull(result3);
        
        // Test 4
        MockSettingsImpl settings4 = new MockSettingsImpl();
        settings4.setExtraInterfaces(null);
        settings4.setSpiedInstance(new Object());
        Object result4 = new MockUtil().createMock(Object.class, settings4);
        assertNotNull(result4);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }
    
    @Test
    public void testGetMockHandler() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.getMockHandler(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.Object", e.getMessage());
        }
    }
    
    @Test
    public void testIsMock() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        boolean result = mockUtil.isMock(mock);
        assertFalse(result);
    }
    
    @Test
    public void testGetMockName() {
        Object mock = new Object();
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.getMockName(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.Object", e.getMessage());
        }
    }
}