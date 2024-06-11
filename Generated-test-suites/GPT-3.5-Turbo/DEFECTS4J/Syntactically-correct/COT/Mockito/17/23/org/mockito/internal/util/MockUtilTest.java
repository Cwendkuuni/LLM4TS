
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, null);
        
        assertNotNull(mock);
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        
        ClassToMock mock = mock(ClassToMock.class);
        
        mockUtil.resetMock(mock);
        
        // Verify that the mock has been reset
        verify(mockUtil).resetMock(mock);
    }
    
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        
        ClassToMock mock = mock(ClassToMock.class);
        
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);
        
        assertNotNull(mockHandler);
    }
    
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        
        ClassToMock mock = mock(ClassToMock.class);
        
        boolean isMock = mockUtil.isMock(mock);
        
        assertTrue(isMock);
    }
    
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        
        ClassToMock mock = mock(ClassToMock.class);
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
    }
}
