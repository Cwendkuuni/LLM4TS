
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        
        assertTrue(mock instanceof Object);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        
        mockUtil.resetMock(mock);
        
        verify(mock).resetMock(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_notAMock() {
        Object notAMock = new Object();
        
        mockUtil.resetMock(notAMock);
    }
    
    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);
        
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        
        assertNotNull(mockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_notAMock() {
        Object notAMock = new Object();
        
        mockUtil.getMockHandler(notAMock);
    }
    
    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        
        boolean result = mockUtil.isMock(mock);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsMock_notAMock() {
        Object notAMock = new Object();
        
        boolean result = mockUtil.isMock(notAMock);
        
        assertFalse(result);
    }
    
    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
    }
}
