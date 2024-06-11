import org.junit.Test;
import org.mockito.internal.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    // createMock method test cases
    
    @Test
    public void createMock_ValidClass_ReturnsMockObject() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        
        assertNotNull(mock);
        assertTrue(mock instanceof MyClass);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createMock_NullClass_ThrowsException() {
        MockUtil mockUtil = new MockUtil();
        
        mockUtil.createMock(null, new MockSettingsImpl());
    }
    
    // resetMock method test cases
    
    @Test
    public void resetMock_ValidMock_ResetsMockObject() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = mock(MyClass.class);
        mockUtil.resetMock(mock);
        
        MyClass otherMock = mock(MyClass.class);
        
        assertNotEquals(mock.hashCode(), otherMock.hashCode());
    }
    
    @Test(expected = NotAMockException.class)
    public void resetMock_NullMock_ThrowsException() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = null;
        mockUtil.resetMock(mock);
    }
    
    // getMockHandler method test cases
    
    @Test
    public void getMockHandler_ValidMock_ReturnsMockHandler() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = mock(MyClass.class);
        
        MockHandlerInterface<MyClass> handler = mockUtil.getMockHandler(mock);
        
        assertNotNull(handler);
        assertEquals(mock, handler.getMock());
    }
    
    @Test(expected = NotAMockException.class)
    public void getMockHandler_NullMock_ThrowsException() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = null;
        mockUtil.getMockHandler(mock);
    }
    
    // isMock method test cases
    
    @Test
    public void isMock_ValidMock_ReturnsTrue() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = mock(MyClass.class);
        
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void isMock_NullObject_ReturnsFalse() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = null;
        
        assertFalse(mockUtil.isMock(mock));
    }
    
    // getMockName method test cases
    
    @Test
    public void getMockName_ValidMock_ReturnsMockName() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = mock(MyClass.class);
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
    }
    
    @Test(expected = NotAMockException.class)
    public void getMockName_NullMock_ThrowsException() {
        MockUtil mockUtil = new MockUtil();
        
        MyClass mock = null;
        mockUtil.getMockName(mock);
    }
}