import static org.junit.Assert.*;
import org.junit.Test;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mock);

        // Edge case: Class to mock is null
        try {
            mockUtil.createMock(null, new MockSettingsImpl());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
        
        // Additional edge cases and exception handling scenarios can be added here
    }

    @Test
    public void testResetMock() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);

        // Edge case: Mock is null
        try {
            mockUtil.resetMock(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Success
        }
        
        // Additional edge cases and exception handling scenarios can be added here
    }

    @Test
    public void testGetMockHandler() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        MockHandlerInterface handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);

        // Edge case: Mock is null
        try {
            mockUtil.getMockHandler(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Success
        }
        
        // Additional edge cases and exception handling scenarios can be added here
    }

    @Test
    public void testIsMock() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));

        // Edge case: Mock is null
        assertFalse(mockUtil.isMock(null));
        
        // Additional edge cases and exception handling scenarios can be added here
    }

    @Test
    public void testGetMockName() {
        // Typical use case
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);

        // Edge case: Mock is null
        try {
            mockUtil.getMockName(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Success
        }
        
        // Additional edge cases and exception handling scenarios can be added here
    }
}