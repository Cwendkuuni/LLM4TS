import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.StringJoiner;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassloaders_NotSupported() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // Expected MockitoException exception
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<?> mockedProxyType = Object.class;
        Object mockInstance = new Object();
        doReturn(mockedProxyType).when(settings).getTypeToMock();
        doReturn(null).when(settings).getExtraInterfaces();
        doThrow(ClassCastException.class).when(mockMaker.classInstantiator).instantiate(mockedProxyType);

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // Expected MockitoException exception
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<?> mockedProxyType = Object.class;
        Object mockInstance = new Object();
        doReturn(mockedProxyType).when(settings).getTypeToMock();
        doReturn(null).when(settings).getExtraInterfaces();
        doThrow(InstantiationException.class).when(mockMaker.classInstantiator).instantiate(mockedProxyType);

        // Act
        mockMaker.createMock(settings, handler);

        // Assert
        // Expected MockitoException exception
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_Success() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        Object mock = new Object();
        doReturn(Object.class).when(settings).getTypeToMock();

        // Act
        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertThat(result, instanceOf(Object.class));
    }

    @Test
    public void testGetHandler_MockMethodInterceptorMockAccess() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void testResetMock_MockMethodInterceptorMockAccess() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        // No exception expected
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_NonInternalMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        // Expected MockitoException exception
    }
}