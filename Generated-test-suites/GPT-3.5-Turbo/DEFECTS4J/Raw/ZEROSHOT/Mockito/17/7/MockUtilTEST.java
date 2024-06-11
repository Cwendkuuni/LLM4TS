import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_validClass_mockObjectCreated() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getExtraInterfaces()).thenReturn(null);
        when(settings.getSpiedInstance()).thenReturn(null);

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_invalidClass_exceptionThrown() {
        // Arrange
        Class<Object> classToMock = null;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        // Act
        mockUtil.createMock(classToMock, settings);

        // Assert
        // Exception expected
    }

    @Test
    public void testResetMock_mockObject_mockReset() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<Object> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler,
                (MockSettingsImpl) withSettings().defaultAnswer(RETURNS_DEFAULTS));

        // Act
        mockUtil.resetMock(mock);

        // Assert
        assertEquals(newFilter, mockUtil.getInterceptor(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_withNullArgument_exceptionThrown() {
        // Arrange
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_withNonMockObject_exceptionThrown() {
        // Arrange
        String mock = "not a mock";

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // Exception expected
    }

    @Test
    public void testIsMock_withMockObject_trueReturned() {
        // Arrange
        Object mock = mock(Object.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_withNonMockObject_falseReturned() {
        // Arrange
        String mock = "not a mock";

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertFalse(isMock);
    }

    @Test
    public void testGetInterceptor_withValidMockObject_interceptorReturned() {
        // Arrange
        Object mock = mock(Object.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
        Factory factory = mock(Factory.class);
        when(factory.getCallback(0)).thenReturn(interceptor);
        when(mockUtil.getInterceptor(mock)).thenCallRealMethod();

        // Act
        MethodInterceptorFilter actualInterceptor = mockUtil.getInterceptor(mock);

        // Assert
        assertEquals(interceptor, actualInterceptor);
    }

    @Test
    public void testGetInterceptor_withInvalidMockObject_nullReturned() {
        // Arrange
        Object mock = "not a mock";
        when(mockUtil.getInterceptor(mock)).thenCallRealMethod();

        // Act
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        // Assert
        assertNull(interceptor);
    }

    @Test
    public void testGetMockName_withMockObject_mockNameReturned() {
        // Arrange
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);

        // Act
        MockName actualMockName = mockUtil.getMockName(mock);

        // Assert
        assertEquals(mockName, actualMockName);
    }

}