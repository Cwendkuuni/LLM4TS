import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        MyInterface mock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl());
        assertNotNull(mock);
        
        // Edge case
        assertEquals(MyInterface.class, mock.getClass());
        
        // Error scenario
        try {
            mockUtil.createMock(null, new MockSettingsImpl());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testResetMock() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        MyInterface mock = mock(MyInterface.class);
        mockUtil.resetMock(mock);
        
        // Edge case
        assertNotNull(mock);
        
        // Error scenario
        try {
            mockUtil.resetMock(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetMockHandler() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        MyInterface mock = mock(MyInterface.class);
        MockHandlerInterface<MyInterface> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
        
        // Edge case
        assertEquals(mock, handler.getMock());
        
        // Error scenario
        try {
            mockUtil.getMockHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Error scenario
        MyInterface notAMock = new MyInterfaceImpl();
        try {
            mockUtil.getMockHandler(notAMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsMock() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        MyInterface mock = mock(MyInterface.class);
        assertTrue(mockUtil.isMock(mock));
        
        // Edge case
        assertFalse(mockUtil.isMock(null));
        
        // Error scenario
        MyInterface notAMock = new MyInterfaceImpl();
        assertFalse(mockUtil.isMock(notAMock));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetMockName() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        MyInterface mock = mock(MyInterface.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        
        // Edge case
        assertEquals("MyInterface", mockName.toString());
        
        // Error scenario
        try {
            mockUtil.getMockName(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Error scenario
        MyInterface notAMock = new MyInterfaceImpl();
        try {
            mockUtil.getMockName(notAMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected exception
        }
    }
}