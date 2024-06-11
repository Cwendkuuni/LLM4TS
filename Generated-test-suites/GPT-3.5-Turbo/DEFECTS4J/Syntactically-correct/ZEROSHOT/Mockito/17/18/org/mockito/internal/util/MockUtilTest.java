import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = new ClassToMock();

        // Act
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());

        // Assert
        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // No assertion needed since this method only modifies the mock
    }

    @Test
    public void testGetMockHandler() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testGetMockName() {
        // Arrange
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    private class ClassToMock {
        // Class to be mocked for testing purposes
    }
}