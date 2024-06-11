import static org.junit.Assert.*;
import org.junit.Test;

public class MockUtilTest {
    
    @Test
    public void testCreateMock_basic() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        assertNotNull(mock);
    }
    
    @Test
    public void testCreateMock_withSpiedInstance() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock spiedInstance = new ClassToMock();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        
        assertNotNull(mock);
        assertEquals(spiedInstance, mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMockException() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockException() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock nonMock = new ClassToMock();
        mockUtil.getMockHandler(nonMock);
    }
    
    @Test
    public void testIsMock_true() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testIsMock_false() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock nonMock = new ClassToMock();
        
        assertFalse(mockUtil.isMock(nonMock));
    }
}