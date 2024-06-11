import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.LenientCopyTool;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.MockUtil;
import org.mockito.MockName;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private CreationValidator creationValidator;

    @Before
    public void setUp() {
        creationValidator = mock(CreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock_validClassToMock_mockCreatedSuccessfully() {
        // Arrange
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        MyClass mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(Enhancer.isEnhanced(mock.getClass()));
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock_exceptionThrown() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockitoMock_exceptionThrown() {
        // Arrange
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // NotAMockException is expected to be thrown
    }

    @Test
    public void testGetMockHandler_validMock_mockHandlerReturned() {
        // Arrange
        MyClass mock = Mockito.mock(MyClass.class);

        // Act
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(mock);

        // Assert
        assertNotNull(mockHandler);
        verify(creationValidator, never()).validateType(any());
    }

    @Test
    public void testResetMock_validMock_mockResetSuccessfully() {
        // Arrange
        MyClass mock = Mockito.mock(MyClass.class);
        MockHandlerInterface<MyClass> oldMockHandler = Mockito.mock(MockHandlerInterface.class);
        MethodInterceptorFilter filter = Mockito.mock(MethodInterceptorFilter.class);
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
        when(creationValidator.validateType(MyClass.class)).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(MyClass.class, settings.getExtraInterfaces())).thenReturn(true);
        when(creationValidator.validateMockedType(MyClass.class, settings.getSpiedInstance())).thenReturn(true);
        when(oldMockHandler.getHandler()).thenReturn(filter);
        when(settings.defaultAnswer(RETURNS_DEFAULTS)).thenReturn(settings);
        when(filter.getHandler()).thenReturn(null);
        when(mock.getCallback(0)).thenReturn(filter);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(filter).getHandler();
        verify(mock).setCallback(0, filter);
    }

    @Test
    public void testIsMock_validMock_trueReturned() {
        // Arrange
        MyClass mock = Mockito.mock(MyClass.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_nonNullObject_notMocked_falseReturned() {
        // Arrange
        Object nonMock = new Object();

        // Act
        boolean isMock = mockUtil.isMock(nonMock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testIsMock_nullObject_falseReturned() {
        // Arrange
        Object mock = null;

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetMockName_validMock_mockNameReturned() {
        // Arrange
        MyClass mock = Mockito.mock(MyClass.class);
        MockName mockName = Mockito.mock(MockName.class);
        MockHandlerInterface<MyClass> mockHandler = Mockito.mock(MockHandlerInterface.class);
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        // Act
        MockName retrievedMockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(retrievedMockName);
        assertEquals(mockName, retrievedMockName);
    }

    private static class MyClass {
        // Test class
    }
}