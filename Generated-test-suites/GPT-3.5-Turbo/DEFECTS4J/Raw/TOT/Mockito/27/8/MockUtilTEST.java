import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_shouldCreateMockWithExtraInterfaces() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        Class<?>[] extraInterfaces = {Serializable.class};

        // Act
        mockUtil.createMock(classToMock, extraInterfaces);

        // Assert
        verify(classToMock, atLeastOnce()).getClass();
        verifyNoMoreInteractions(classToMock);
    }

    @Test
    public void testCreateMock_shouldCreateMockWithSerializableInterfaceIfNoExtraInterfacesProvided() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);

        // Act
        mockUtil.createMock(classToMock, null);

        // Assert
        verify(classToMock, atLeastOnce()).getClass();
        verifyNoMoreInteractions(classToMock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_shouldThrowNotAMockExceptionIfArgumentIsNull() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.resetMock(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test
    public void testResetMock_shouldResetMockWithNewMockHandler() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock, atLeastOnce()).getClass();
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_shouldThrowNotAMockExceptionIfArgumentIsNull() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_shouldThrowNotAMockExceptionIfArgumentIsNotAMock() {
        // Arrange
        ClassToMock mock = new ClassToMock();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test
    public void testGetMockHandler_shouldReturnMockHandlerIfArgumentIsAMock() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        verify(mock, atLeastOnce()).getClass();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testIsMock_shouldReturnFalseIfArgumentIsNull() {
        // Arrange
        ClassToMock mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_shouldReturnFalseIfArgumentIsNotAMock() {
        // Arrange
        ClassToMock mock = new ClassToMock();

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsMock_shouldReturnTrueIfArgumentIsAMock() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_shouldThrowNotAMockExceptionIfArgumentIsNull() {
        // Arrange
        ClassToMock mock = null;

        // Act
        mockUtil.getMockName(mock);

        // Assert
        // Expecting NotAMockException
    }

    @Test
    public void testGetMockName_shouldReturnMockNameIfArgumentIsAMock() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        mockUtil.getMockName(mock);

        // Assert
        verify(mock, atLeastOnce()).getClass();
        verifyNoMoreInteractions(mock);
    }
}

class ClassToMock {
    // Mock Class
}