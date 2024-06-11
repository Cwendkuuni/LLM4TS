import static org.mockito.Mockito.*;
import org.junit.Test;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mock);
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        mockUtil.resetMock(mock);
        // Verify if the mock was reset
    }
    
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        MockHandlerInterface<?> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
        // Expect an exception to be thrown
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        Object obj = new Object();
        mockUtil.getMockHandler(obj);
        // Expect an exception to be thrown
    }
    
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        boolean isMock = mockUtil.isMock(mock);
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMockWithNullObject() {
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(null);
        assertFalse(isMock);
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}