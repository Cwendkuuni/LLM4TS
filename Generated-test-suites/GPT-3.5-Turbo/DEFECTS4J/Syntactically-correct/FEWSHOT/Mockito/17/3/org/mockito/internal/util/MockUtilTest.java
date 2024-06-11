
package org.mockito.internal.util;


import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<T> classToMock = T.class;
        T mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertEquals(classToMock, mock.getClass());
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        T mock = createMock();
        mockUtil.resetMock(mock);
        
        // add assertions here
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        T mock = createMock();
        MockHandlerInterface<T> mockHandler = mockUtil.getMockHandler(mock);
        
        assertNotNull(mockHandler);
        // add more assertions here
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        T mock = createMock();
        boolean isMock = mockUtil.isMock(mock);
        
        assertTrue(isMock);
        // add more assertions here
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        T mock = createMock();
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
        // add more assertions here
    }
   
}
