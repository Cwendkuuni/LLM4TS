import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private MockCreationValidator mockCreationValidator;
    private LenientCopyTool lenientCopyTool;

    @Before
    public void setup() {
        mockCreationValidator = mock(MockCreationValidator.class);
        lenientCopyTool = mock(LenientCopyTool.class);
        mockUtil = new MockUtil(mockCreationValidator);
    }

    @Test
    public void createMock_shouldCreateMock() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.isSerializable()).thenReturn(false);
        when(settings.getSpiedInstance()).thenReturn(null);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, null);
        verify(mockCreationValidator).validateMockedType(classToMock, null);
        verify(settings).initiateMockName(classToMock);
        verify(lenientCopyTool, never()).copyToMock(any(), any());
        verifyNoMoreInteractions(mockCreationValidator, settings, lenientCopyTool);
    }

    @Test
    public void createMock_shouldCopySpiedInstance() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.isSerializable()).thenReturn(false);
        Object spiedInstance = new Object();
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, null);
        verify(mockCreationValidator).validateMockedType(classToMock, spiedInstance);
        verify(settings).initiateMockName(classToMock);
        verify(lenientCopyTool).copyToMock(spiedInstance, result);
        verifyNoMoreInteractions(mockCreationValidator, settings, lenientCopyTool);
    }

    @Test
    public void createMock_shouldCreateSerializableMock() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.isSerializable()).thenReturn(true);
        when(settings.getSpiedInstance()).thenReturn(null);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, null);
        verify(mockCreationValidator).validateMockedType(classToMock, null);
        verify(settings).initiateMockName(classToMock);
        verify(lenientCopyTool, never()).copyToMock(any(), any());
        verifyNoMoreInteractions(mockCreationValidator, settings, lenientCopyTool);
    }

    @Test
    public void createMock_shouldCreateMockWithExtraInterfaces() {
        // Arrange
        Class<Object> classToMock = Object.class;
        Class<?>[] extraInterfaces = {Serializable.class};
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);
        when(settings.isSerializable()).thenReturn(false);
        when(settings.getSpiedInstance()).thenReturn(null);

        // Act
        Object result = mockUtil.createMock(classToMock, settings);

        // Assert
        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, extraInterfaces);
        verify(mockCreationValidator).validateMockedType(classToMock, null);
        verify(settings).initiateMockName(classToMock);
        verify(lenientCopyTool, never()).copyToMock(any(), any());
        verifyNoMoreInteractions(mockCreationValidator, settings, lenientCopyTool);
    }

    @Test
    public void resetMock_shouldResetMock() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        MockHandlerInterface newMockHandler = mock(MockHandlerInterface.class);
        when(mockHandler.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        when(mockUtil.newMethodInterceptorFilter(any(MockSettingsImpl.class))).thenReturn(mock(MethodInterceptorFilter.class));

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler, never()).getMockSettings();
        verify(mockHandler, never()).getHandler();
        verify(mockUtil).newMethodInterceptorFilter(any(MockSettingsImpl.class));
        verify(mockHandler, never()).getInterceptors();
        verify(mockHandler, atLeastOnce()).getMockInterceptor();
        verify(mockHandler, atLeastOnce()).getInvocationContainer();
        verify(mockHandler, never()).setMethodInterceptor(any());
        verify(mockHandler, never()).setVoidMethodInterceptor(any());
        verify(mockHandler, never()).setMockSettings(any());
        verifyNoMoreInteractions(mockUtil, mockHandler);
    }

    @Test
    public void resetMock_shouldThrowNotAMockExceptionWhenMockIsNull() {
        // Arrange
        Object mock = null;

        // Act & Assert
        try {
            mockUtil.resetMock(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Expected exception
        }

        // Verify
        verify(mockUtil, never()).getMockHandler(mock);
        verifyNoMoreInteractions(mockUtil);
    }

    @Test
    public void resetMock_shouldThrowNotAMockExceptionWhenMockIsNotAMockitoMock() {
        // Arrange
        Object mock = new Object();

        // Act & Assert
        try {
            mockUtil.resetMock(mock);
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Expected exception
        }

        // Verify
        verify(mockUtil).getMockHandler(mock);
        verifyNoMoreInteractions(mockUtil);
    }

    @Test
    public void isMock_shouldReturnFalseWhenMockIsNull() {
        // Arrange
        Object mock = null;

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void isMock_shouldReturnFalseWhenMockIsNotAMockitoMock() {
        // Arrange
        Object mock = new Object();

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertFalse(result);
    }

    @Test
    public void isMock_shouldReturnTrueWhenMockIsAMockitoMock() {
        // Arrange
        Object mock = mock(Object.class);
        when(mockUtil.getInterceptor(mock)).thenReturn(mock(MethodInterceptorFilter.class));

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertTrue(result);
        verify(mockUtil).getInterceptor(mock);
    }

    @Test
    public void getMockName_shouldReturnMockName() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        when(mockHandler.getMockSettings().getMockName()).thenReturn(mock(MockName.class));

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(result);
        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockHandler).getMockSettings().getMockName();
        verifyNoMoreInteractions(mockUtil, mockHandler);
    }
}