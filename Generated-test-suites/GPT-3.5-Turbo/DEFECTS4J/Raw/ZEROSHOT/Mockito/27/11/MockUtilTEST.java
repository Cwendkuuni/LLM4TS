import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Mock
    private MockCreationValidator creationValidator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock() {
        // Arrange
        Class<TestObject> classToMock = TestObject.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        TestObject mock = mockUtil.createMock(classToMock, settings);

        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof TestObject);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
    }

    @Test
    public void testResetMock() {
        // Arrange
        TestObject mock = mock(TestObject.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        verify(mock).setCallback(0, any());
    }

    @Test
    public void testGetMockHandler() {
        // Arrange
        TestObject mock = mock(TestObject.class);
        MockHandlerInterface<TestObject> mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getInterceptor(mock)).thenReturn(null);
        when(mockUtil.getInterceptor(mockHandler)).thenReturn(mockHandler);

        // Act
        MockHandlerInterface<TestObject> result1 = mockUtil.getMockHandler(mock);
        MockHandlerInterface<TestObject> result2 = mockUtil.getMockHandler(mockHandler);

        // Assert
        assertEquals(mockHandler, result1);
        assertEquals(mockHandler, result2);
    }

    @Test
    public void testIsMock() {
        // Arrange
        Object mock = mock(TestObject.class);
        Object notMock = new Object();

        // Act
        boolean result1 = mockUtil.isMock(mock);
        boolean result2 = mockUtil.isMock(notMock);

        // Assert
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testGetMockName() {
        // Arrange
        TestObject mock = mock(TestObject.class);
        MockName mockName = mock(MockName.class);
        MockHandlerInterface<TestObject> mockHandler = mock(MockHandlerInterface.class);
        when(mockHandler.getMockSettings().getMockName()).thenReturn(mockName);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertEquals(mockName, result);
    }

    private static class TestObject {}

}