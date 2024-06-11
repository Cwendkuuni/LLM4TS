import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1
        Class<?> classToMock1 = String.class;
        MockSettingsImpl settings1 = new MockSettingsImpl();
        String mock1 = mockUtil.createMock(classToMock1, settings1);
        assertNotNull(mock1);
        
        // Test case 2
        Class<?> classToMock2 = Integer.class;
        MockSettingsImpl settings2 = new MockSettingsImpl();
        Integer mock2 = mockUtil.createMock(classToMock2, settings2);
        assertNotNull(mock2);
        
        // Test case 3
        Class<?> classToMock3 = Object.class;
        MockSettingsImpl settings3 = new MockSettingsImpl();
        Object mock3 = mockUtil.createMock(classToMock3, settings3);
        assertNotNull(mock3);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1
        String mock1 = mock(String.class);
        mockUtil.resetMock(mock1);
        assertNotNull(mock1);
        
        // Test case 2
        Integer mock2 = mock(Integer.class);
        mockUtil.resetMock(mock2);
        assertNotNull(mock2);
        
        // Test case 3
        Object mock3 = mock(Object.class);
        mockUtil.resetMock(mock3);
        assertNotNull(mock3);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1
        String mock1 = mock(String.class);
        MockHandlerInterface<String> handler1 = mockUtil.getMockHandler(mock1);
        assertNotNull(handler1);
        
        // Test case 2
        Integer mock2 = mock(Integer.class);
        MockHandlerInterface<Integer> handler2 = mockUtil.getMockHandler(mock2);
        assertNotNull(handler2);
        
        // Test case 3
        Object mock3 = mock(Object.class);
        MockHandlerInterface<Object> handler3 = mockUtil.getMockHandler(mock3);
        assertNotNull(handler3);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1
        String mock1 = mock(String.class);
        boolean isMock1 = mockUtil.isMock(mock1);
        assertTrue(isMock1);
        
        // Test case 2
        Integer mock2 = mock(Integer.class);
        boolean isMock2 = mockUtil.isMock(mock2);
        assertTrue(isMock2);
        
        // Test case 3
        Object mock3 = mock(Object.class);
        boolean isMock3 = mockUtil.isMock(mock3);
        assertTrue(isMock3);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1
        String mock1 = mock(String.class);
        MockName name1 = mockUtil.getMockName(mock1);
        assertNotNull(name1);
        
        // Test case 2
        Integer mock2 = mock(Integer.class);
        MockName name2 = mockUtil.getMockName(mock2);
        assertNotNull(name2);
        
        // Test case 3
        Object mock3 = mock(Object.class);
        MockName name3 = mockUtil.getMockName(mock3);
        assertNotNull(name3);
    }
}