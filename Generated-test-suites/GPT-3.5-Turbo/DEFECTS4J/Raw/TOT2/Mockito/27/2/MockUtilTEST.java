import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Typical use case
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        
        // Edge case: Serializable interface
        settings.setSerializable(true);
        mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        
        // Error scenario: Invalid class type
        Class<Integer> invalidClass = Integer.class;
        try {
            mockUtil.createMock(invalidClass, settings);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testResetMock() {
        // Typical use case
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // Verify the mock was reset successfully
        
        // Error scenario: Null mock
        try {
            mockUtil.resetMock(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
    }

    @Test
    public void testGetMockHandler() {
        // Typical use case
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
        
        // Error scenario: Null mock
        try {
            mockUtil.getMockHandler(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
        
        // Error scenario: Invalid mock
        Object invalidMock = new Object();
        try {
            mockUtil.getMockHandler(invalidMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
    }

    @Test
    public void testIsMock() {
        // Typical use case
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
        
        // Error scenario: Null mock
        assertFalse(mockUtil.isMock(null));
        
        // Error scenario: Invalid mock
        Object invalidMock = new Object();
        assertFalse(mockUtil.isMock(invalidMock));
    }

    @Test
    public void testGetMockName() {
        // Typical use case
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}