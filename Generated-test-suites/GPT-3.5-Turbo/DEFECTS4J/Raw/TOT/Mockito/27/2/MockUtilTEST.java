import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_WithClassToMock_ReturnsMockObject() {
        // Arrange
        ClassToMock classToMock = ClassToMock.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        ClassToMock mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
    }

    @Test
    public void testCreateMock_WithSerializableClassToMock_ReturnsSerializableMockObject() {
        // Arrange
        ClassToMockSerializable classToMock = ClassToMockSerializable.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(true);

        // Act
        ClassToMockSerializable mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMockSerializable);
        assertTrue(mock instanceof Serializable);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_WithNullMockObject_ThrowsNotAMockException() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test
    public void testResetMock_WithValidMockObject_ResetsMock() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // No exception should be thrown
    }

    @Test
    public void testGetMockHandler_WithValidMockObject_ReturnsMockHandlerInterface() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        MockHandlerInterface<ClassToMock> handler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WithNullMockObject_ThrowsNotAMockException() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WithNonMockObject_ThrowsNotAMockException() {
        // Arrange
        ClassToMockNonMock mock = new ClassToMockNonMock();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test
    public void testIsMock_WithNullObject_ReturnsFalse() {
        // Arrange
        Object mock = null;

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testIsMock_WithValidMockObject_ReturnsTrue() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_WithNonMockObject_ReturnsFalse() {
        // Arrange
        ClassToMockNonMock mock = new ClassToMockNonMock();

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName_WithValidMockObject_ReturnsMockName() {
        // Arrange
        ClassToMock mock = Mockito.mock(ClassToMock.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_WithNullMockObject_ThrowsNotAMockException() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_WithNonMockObject_ThrowsNotAMockException() {
        // Arrange
        ClassToMockNonMock mock = new ClassToMockNonMock();

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Expecting NotAMockException
    }
}

class ClassToMock {
    // Dummy class for testing purposes
}

class ClassToMockSerializable implements Serializable {
    // Dummy class for testing purposes
}

class ClassToMockNonMock {
    // Dummy class for testing purposes
}