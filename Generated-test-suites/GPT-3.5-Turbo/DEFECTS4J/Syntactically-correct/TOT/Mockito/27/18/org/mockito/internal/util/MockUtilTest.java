import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;


public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_typical() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        ClassToMock mock = mockUtil.createMock(classToMock.getClass(), settings);

        // Assert
        assertThat(mockUtil.isMock(mock)).isTrue();
        assertThat(mockUtil.getMockName(mock)).isNull();
    }

    @Test
    public void testCreateMock_edgeCase_NullClassToMock() {
        // Arrange
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act & Assert
        assertThatThrownBy(() -> mockUtil.createMock(null, settings))
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is null!");
    }

    @Test
    public void testCreateMock_edgeCase_NullSpiedInstance() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(null);

        // Act
        ClassToMock mock = mockUtil.createMock(classToMock.getClass(), settings);

        // Assert
        assertThat(mockUtil.isMock(mock)).isTrue();
    }

    @Test
    public void testResetMock_typical() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        assertThat(mockUtil.isMock(mock)).isTrue();
    }

    @Test
    public void testResetMock_edgeCase_NullMock() {
        // Act & Assert
        assertThatThrownBy(() -> mockUtil.resetMock(null))
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is null!");
    }

    @Test
    public void testGetMockHandler_typical() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getInterceptor(mock)).thenReturn(mockHandler);

        // Act
        MockHandlerInterface<ClassToMock> result = mockUtil.getMockHandler(mock);

        // Assert
        assertThat(result).isEqualTo(mockHandler);
    }

    @Test
    public void testGetMockHandler_edgeCase_NullMock() {
        // Arrange
        MockHandlerInterface<ClassToMock> mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getInterceptor(null)).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> mockUtil.getMockHandler(null))
                .isInstanceOf(NotAMockException.class)
                .hasMessage("Argument should be a mock, but is null!");
    }

    @Test
    public void testIsMock_typical() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void testIsMock_edgeCase_NullMock() {
        // Arrange
        when(mockUtil.isMockitoMock(null)).thenReturn(false);

        // Act
        boolean result = mockUtil.isMock(null);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void testIsMock_edgeCase_NoInterceptor() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        // Act
        boolean result = mockUtil.isMock(mock);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void testGetMockName_typical() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setMockName(MockName.defaultMockName("Mock"));
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        // Act
        MockName result = mockUtil.getMockName(mock);

        // Assert
        assertThat(result).isEqualTo(mockSettings.getMockName());
    }

    @Test
    public void testGetMockName_edgeCase_NullMock() {
        // Arrange
        when(mockUtil.getMockHandler(null)).thenReturn(null);

        // Act
        MockName result = mockUtil.getMockName(null);

        // Assert
        assertThat(result).isNull();
    }
}