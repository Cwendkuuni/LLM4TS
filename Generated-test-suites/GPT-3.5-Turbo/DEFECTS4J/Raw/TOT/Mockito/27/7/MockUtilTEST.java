import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

public class MockUtilTest {

    private MockCreationValidator mockCreationValidatorMock = mock(MockCreationValidator.class);
    private MockCreationValidator mockCreationValidatorSpy = spy(new MockCreationValidator());
    
    private MockUtil mockUtil = new MockUtil(mockCreationValidatorMock);
    private MockUtil mockUtilSpy = new MockUtil(mockCreationValidatorSpy);

    @Test
    public void testCreateMock_WithValidInputs_ReturnsMock() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        assert result != null;
        assert result instanceof String;
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_WithNullClassToMock_ThrowsException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testCreateMock_WithSerializableSetting_AddsSerializableInterfaceToAncillaryTypes() {
        // Arrange
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSerializable(true);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);
        
        // Assert
        assert result != null;
        assert result instanceof String;
        assert result instanceof Serializable;
    }

    @Test
    public void testResetMock_WithValidMock_ResetsMock() {
        // Arrange
        String mock = mock(String.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock).setCallback(0, any());
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_WithNonMock_ThrowsException() {
        // Arrange
        String nonMock = "not a mock";

        // Act
        mockUtil.resetMock(nonMock);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testGetMockHandler_WithValidMock_ReturnsMockHandler() {
        // Arrange
        String mock = mock(String.class);

        // Act
        MockHandlerInterface result = mockUtil.getMockHandler(mock);

        // Assert
        assert result != null;
        assert result instanceof MockHandlerInterface;
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WithNullMock_ThrowsException() {
        // Arrange
        String nullMock = null;

        // Act
        MockHandlerInterface result = mockUtil.getMockHandler(nullMock);

        // Assert
        // Exception is thrown
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WithNonMock_ThrowsException() {
        // Arrange
        String nonMock = "not a mock";

        // Act
        MockHandlerInterface result = mockUtil.getMockHandler(nonMock);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testIsMock_WithMock_ReturnsTrue() {
        // Arrange
        String mock = mock(String.class);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assert result;
    }

    @Test
    public void testIsMock_WithNonMock_ReturnsFalse() {
        // Arrange
        String nonMock = "not a mock";

        // Act
        boolean result = mockUtil.isMock(nonMock);

        // Assert
        assert !result;
    }

    @Test
    public void testGetInterceptor_WithMock_ReturnsMethodInterceptorFilter() {
        // Arrange
        String mock = mock(String.class);

        // Act
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        // Assert
        assert result != null;
        assert result instanceof MethodInterceptorFilter;
    }

    @Test
    public void testGetInterceptor_WithNonMock_ReturnsNull() {
        // Arrange
        String nonMock = "not a mock";

        // Act
        MethodInterceptorFilter result = mockUtil.getInterceptor(nonMock);

        // Assert
        assert result == null;
    }

    @Test
    public void testGetMockName_WithValidMock_ReturnsMockName() {
        // Arrange
        String mock = mock(String.class);
        
        // Act
        MockName result = mockUtil.getMockName(mock);
        
        // Assert
        assert result != null;
        assert result instanceof MockName;
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_WithNullMock_ThrowsException() {
        // Arrange
        String nullMock = null;

        // Act
        MockName result = mockUtil.getMockName(nullMock);

        // Assert
        // Exception is thrown
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_WithNonMock_ThrowsException() {
        // Arrange
        String nonMock = "not a mock";

        // Act
        MockName result = mockUtil.getMockName(nonMock);

        // Assert
        // Exception is thrown
    }

}