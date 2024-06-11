import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_ValidClass_ReturnsMockObject() {
        // Arrange
        ClassToMock classToMock = Mockito.mock(ClassToMock.class);

        // Act
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());

        // Assert
        Assert.assertNotNull(mock);
        Assert.assertTrue(mock instanceof ClassToMock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        // Act
        mockUtil.resetMock(null);

        // Assert - Expects exception to be thrown
    }

    @Test
    public void testResetMock_ValidMock_ResetsMockHandler() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        MockHandlerInterface<ClassToMock> handler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(handler);
        Assert.assertTrue(handler instanceof MockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Act
        mockUtil.getMockHandler(null);

        // Assert - Expects exception to be thrown
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMock nonMock = new ClassToMock();

        // Act
        mockUtil.getMockHandler(nonMock);

        // Assert - Expects exception to be thrown
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        MockHandlerInterface<ClassToMock> handler = mockUtil.getMockHandler(mock);

        // Assert
        Assert.assertNotNull(handler);
        Assert.assertTrue(handler instanceof MockHandler);
    }

    @Test
    public void testIsMock_NullObject_ReturnsFalse() {
        // Act
        boolean isMock = mockUtil.isMock(null);

        // Assert
        Assert.assertFalse(isMock);
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        Assert.assertTrue(isMock);
    }

    @Test
    public void testIsMock_NotAMock_ReturnsFalse() {
        // Arrange
        ClassToMock nonMock = new ClassToMock();

        // Act
        boolean isMock = mockUtil.isMock(nonMock);

        // Assert
        Assert.assertFalse(isMock);
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        Assert.assertNotNull(mockName);
        // Additional assertions can be added to validate mockName properties
    }

    // Additional test cases for other methods in the MockUtil class can be added here

}