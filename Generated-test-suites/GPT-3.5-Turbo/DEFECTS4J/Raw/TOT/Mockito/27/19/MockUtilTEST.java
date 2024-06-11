import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void createMock_validClass_returnsMock() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, settings);

        // Assert
        assertThat(mock).isNotNull();
        assertThat(mock).isInstanceOf(ClassToMock.class);
    }

    @Test(expected = NotAMockException.class)
    public void createMock_nullClass_throwsNotAMockException() {
        // Arrange
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(null, settings);

        // Assert
        // Expects NotAMockException to be thrown
    }

    @Test(expected = NotAMockException.class)
    public void createMock_invalidClass_throwsNotAMockException() {
        // Arrange
        IllegalArgumentException classToMock = mock(IllegalArgumentException.class);
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        mockUtil.createMock(classToMock.getClass(), settings);

        // Assert
        // Expects NotAMockException to be thrown
    }

    @Test
    public void createMock_withSpiedInstance_copiesToMock() {
        // Arrange
        ClassToMock classToMock = mock(ClassToMock.class);
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(spiedInstance);

        // Act
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, settings);

        // Assert
        assertThat(mock).isNotNull();
        assertThat(mock).isInstanceOf(ClassToMock.class);
        assertThat(mock.getSpiedInstance()).isEqualTo(spiedInstance);
    }

    @Test
    public void resetMock_validMock_resetsMockHandler() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandlerInterface<T> newMockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, new MockSettingsImpl().defaultAnswer(Mockito.RETURNS_DEFAULTS));

        // Act
        mockUtil.resetMock(mock);

        // Assert
        assertThat(mockUtil.getMockHandler(mock)).isNotEqualTo(oldMockHandler);
        assertThat(((Factory) mock).getCallback(0)).isEqualTo(newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_nullMock_throwsNotAMockException() {
        // Arrange

        // Act
        mockUtil.getMockHandler(null);

        // Assert
        // Expects NotAMockException to be thrown
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandler_nonMockObject_throwsNotAMockException() {
        // Arrange
        Object nonMockObject = new Object();

        // Act
        mockUtil.getMockHandler(nonMockObject);

        // Assert
        // Expects NotAMockException to be thrown
    }

    @Test
    public void isMock_nullMock_returnsFalse() {
        // Arrange

        // Act
        boolean isMock = mockUtil.isMock(null);

        // Assert
        assertThat(isMock).isFalse();
    }

    @Test
    public void isMock_validMock_returnsTrue() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        assertThat(isMock).isTrue();
    }

    @Test
    public void isMock_nonMockObject_returnsFalse() {
        // Arrange
        Object nonMockObject = new Object();

        // Act
        boolean isMock = mockUtil.isMock(nonMockObject);

        // Assert
        assertThat(isMock).isFalse();
    }

    @Test
    public void getMockName_validMock_returnsMockName() {
        // Arrange
        ClassToMock mock = mock(ClassToMock.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(ClassToMock.class);
        when(mockUtil.getMockName(mock)).thenReturn(settings.getMockName());

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertThat(mockName).isEqualTo(settings.getMockName());
    }

    @Test(expected = NotAMockException.class)
    public void getMockName_nonMockObject_throwsNotAMockException() {
        // Arrange
        Object nonMockObject = new Object();

        // Act
        mockUtil.getMockName(nonMockObject);

        // Assert
        // Expects NotAMockException to be thrown
    }
}