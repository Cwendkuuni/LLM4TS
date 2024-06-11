import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void createMock_ValidClassAndSettings_CreatesMock() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        ClassToMock mock = new MockUtil().createMock(ClassToMock.class, settings);

        // Assert
        assertSame(ClassToMock.class, mock.getClass());
        assertTrue(mock instanceof Serializable);
        verify(classToMock, times(1)).getClass();
        verify(classToMock, times(1)).getExtraInterfaces();
        verify(classToMock, times(1)).getSpiedInstance();
    }

    @Test(expected = NotAMockException.class)
    public void createMock_InvalidClassToMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMockImpl classToMock = new ClassToMockImpl();
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        new MockUtil().createMock(ClassToMockImpl.class, settings);

        // Assert

        // Expecting NotAMockException
    }

    @Test
    public void resetMock_ValidMock_ResetsMock() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        new MockUtil().resetMock(mock);

        // Assert
        verify(mock, times(1)).getHandler();
        verify(mock, times(1)).getDefaultAnswer();
    }

    @Test(expected = NotAMockException.class)
    public void resetMock_InvalidMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMockImpl mock = new ClassToMockImpl();

        // Act
        new MockUtil().resetMock(mock);

        // Assert

        // Expecting NotAMockException
    }

    @Test
    public void getMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        MockHandlerInterface<ClassToMock> handler = new MockUtil().getMockHandler(mock);

        // Assert
        verify(mock, times(1)).getHandler();
        assertSame(MockHandler.class, handler.getClass());
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMock mock = null;

        // Act
        new MockUtil().getMockHandler(mock);

        // Assert

        // Expecting NotAMockException
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_NonMockitoMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMockImpl mock = new ClassToMockImpl();

        // Act
        new MockUtil().getMockHandler(mock);

        // Assert

        // Expecting NotAMockException
    }

    @Test
    public void isMock_ValidMock_ReturnsTrue() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        boolean result = new MockUtil().isMock(mock);

        // Assert
        assertTrue(result);
    }

    @Test
    public void isMock_NullMock_ReturnsFalse() {
        // Arrange
        ClassToMock mock = null;

        // Act
        boolean result = new MockUtil().isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void isMock_NonMockitoMock_ReturnsFalse() {
        // Arrange
        ClassToMockImpl mock = new ClassToMockImpl();

        // Act
        boolean result = new MockUtil().isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getMockName_ValidMock_ReturnsMockName() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        MockName name = new MockUtil().getMockName(mock);

        // Assert
        assertNotNull(name);
    }

    @Test(expected = NotAMockException.class)
    public void getMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMock mock = null;

        // Act
        new MockUtil().getMockName(mock);

        // Assert

        // Expecting NotAMockException
    }

    @Test(expected = NotAMockException.class)
    public void getMockName_NonMockitoMock_ThrowsNotAMockException() {
        // Arrange
        ClassToMockImpl mock = new ClassToMockImpl();

        // Act
        new MockUtil().getMockName(mock);

        // Assert

        // Expecting NotAMockException
    }
}

class ClassToMock implements Serializable {

    public Class<?>[] getExtraInterfaces() {
        return new Class<?>[]{};
    }

    public Object getSpiedInstance() {
        return null;
    }

    public void setCallback(int index, MethodInterceptorFilter filter) {

    }

    public MockHandlerInterface getHandler() {
        return null;
    }

    public String getDefaultAnswer() {
        return null;
    }
}

class ClassToMockImpl extends ClassToMock {
}