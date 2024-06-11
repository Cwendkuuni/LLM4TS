import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Test typical case
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = new ClassToMock();
        MockSettingsImpl settings = new MockSettingsImpl();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, settings);
        assertNotNull(mock);

        // Test edge case with spied instance
        ClassToMock spiedInstance = new ClassToMock();
        settings.setSpiedInstance(spiedInstance);
        ClassToMock spyMock = mockUtil.createMock(ClassToMock.class, settings);
        assertNotNull(spyMock);
        assertEquals(spiedInstance, spyMock);
    }

    @Test
    public void testResetMock() {
        // Test resetting mock
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        mockUtil.resetMock(mock);
        assertNotNull(mock);
    }

    @Test
    public void testGetMockHandler() {
        // Test typical case
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        // Test error scenario with null mock
        try {
            mockUtil.getMockHandler(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        // Test error scenario with non-mock object
        ClassToMockObject nonMock = new ClassToMockObject();
        try {
            mockUtil.getMockHandler(nonMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testIsMock() {
        // Test typical case
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        assertTrue(mockUtil.isMock(mock));

        // Test edge case with non-mock object
        ClassToMockObject nonMock = new ClassToMockObject();
        assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void testGetMockName() {
        // Test typical case
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}