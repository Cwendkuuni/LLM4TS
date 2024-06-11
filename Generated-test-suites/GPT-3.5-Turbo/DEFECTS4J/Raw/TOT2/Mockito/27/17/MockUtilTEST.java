import static org.junit.Assert.*;
import org.junit.Test;

public class MockUtilTest {

    @Test
    public void testCreateMock_basic() {
        // Test creating a basic mock with no extra settings
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mock);
    }

    @Test
    public void testResetMock_basic() {
        // Test resetting a mock
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // Assuming no exceptions are thrown, the reset should succeed
    }

    @Test
    public void testGetMockHandler_nullArgument() {
        // Test getting mock handler with null argument
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.getMockHandler(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetMockHandler_invalidArgument() {
        // Test getting mock handler with invalid argument
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.getMockHandler(new Object());
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMock_nullMock() {
        // Test checking if null is a mock
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName() {
        // Test getting mock name
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}