package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockUtil util = new MockUtil();
        
        // Test cases for settings.isSerializable() = false and settings.getExtraInterfaces() = null
        assertEquals(String.class, util.createMock(String.class, new MockSettingsImpl()));
        assertEquals(Integer.class, util.createMock(Integer.class, new MockSettingsImpl()));
        
        // Test cases for settings.isSerializable() = true and settings.getExtraInterfaces() = null
        assertEquals(Serializable.class, util.createMock(Serializable.class, new MockSettingsImpl().serializable()));
        assertEquals(Serializable.class, util.createMock(String.class, new MockSettingsImpl().serializable()));
        
        // Test cases for settings.isSerializable() = false and settings.getExtraInterfaces() = extraInterfaces
        Class<?>[] extraInterfaces = {Comparable.class};
        assertEquals(Object.class, util.createMock(Object.class, new MockSettingsImpl().extraInterfaces(extraInterfaces)));
        assertEquals(String.class, util.createMock(String.class, new MockSettingsImpl().extraInterfaces(extraInterfaces)));
        
        // Test cases for settings.isSerializable() = true and settings.getExtraInterfaces() = extraInterfaces
        assertEquals(Serializable.class, util.createMock(Serializable.class, new MockSettingsImpl().extraInterfaces(extraInterfaces).serializable()));
        assertEquals(Serializable.class, util.createMock(String.class, new MockSettingsImpl().extraInterfaces(extraInterfaces).serializable()));
    }
    
    @Test
    public void testResetMock() {
        MockUtil util = new MockUtil();
        
        // Test cases for valid mocks
        Object mock1 = util.createMock(String.class, new MockSettingsImpl());
        util.resetMock(mock1);
        
        Object mock2 = util.createMock(Integer.class, new MockSettingsImpl());
        util.resetMock(mock2);
    }
    
    @Test
    public void testGetMethodInterceptorFilter() {
        MockUtil util = new MockUtil();
        
        // Test case for valid mock
        Object mock = util.createMock(String.class, new MockSettingsImpl());
        assertEquals(MethodInterceptorFilter.class, util.getMethodInterceptorFilter(mock).getClass());
        
        // Test case for non-mock object
        Object nonMock = new Object();
        assertNull(util.getMethodInterceptorFilter(nonMock));
    }

    @Test
    public void testGetMockHandler() {
        MockUtil util = new MockUtil();

        // Test case for valid mock
        Object mock = util.createMock(String.class, new MockSettingsImpl());
        assertEquals(MockHandlerInterface.class, util.getMockHandler(mock).getClass());

        // Test case for non-mock object
        Object nonMock = new Object();
        try {
            util.getMockHandler(nonMock);
        } catch(NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + nonMock.getClass(), e.getMessage());
        }
    }
}