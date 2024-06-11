
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

    @Test
    public void testCreateMock_NormalSettings_SuccessfullyCreatesMock() {
        // Arrange
        MockCreationSettings<ExampleClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        ExampleClass mockedInstance = mock(ExampleClass.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);

        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(ExampleClass.class);
        when(classInstantiator.instantiate(any(Class.class))).thenReturn(mockedInstance);

        // Act
        ExampleClass result = byteBuddyMockMaker.createMock(settings, handler);

        // Assert
        verify(settings).getSerializableMode();
        verify(settings).getTypeToMock();
        verify(classInstantiator).instantiate(any(Class.class));
        verify(handler).getMockitoInterceptor();
        verify(mockedInstance.getMockitoInterceptor()).setMockitoInterceptor(any(MockMethodInterceptor.class));
        assertThat(result).isInstanceOf(ExampleClass.class);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassLoaders_ThrowsException() {
        // Arrange
        MockCreationSettings<ExampleClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Act
        byteBuddyMockMaker.createMock(settings, handler);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_MockIsAssignable_ReturnsSameMock() {
        // Arrange
        MockCreationSettings<ExampleClass> settings = mock(MockCreationSettings.class);
        ExampleClass mock = mock(ExampleClass.class);

        when(settings.getTypeToMock()).thenReturn(ExampleClass.class);

        // Act
        ExampleClass result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        verify(settings).getTypeToMock();
        assertThat(result).isEqualTo(mock);
    }

    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedType_MockIsNotAssignable_ThrowsException() {
        // Arrange
        MockCreationSettings<ExampleClass> settings = mock(MockCreationSettings.class);
        ExampleClass mock = mock(ExampleClass.class);

        when(settings.getTypeToMock()).thenReturn(AnotherClass.class);

        // Act
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        // Exception is thrown
    }

    @Test
    public void testDescribeClass_TypeIsNull_ReturnsNullDescription() {
        // Arrange
        Class type = null;

        // Act
        String result = byteBuddyMockMaker.describeClass(type);

        // Assert
        assertThat(result).isEqualTo("null");
    }

    @Test
    public void testDescribeClass_TypeIsNotNull_ReturnsTypeDescription() {
        // Arrange
        Class type = ExampleClass.class;

        // Act
        String result = byteBuddyMockMaker.describeClass(type);

        // Assert
        assertThat(result).isEqualTo("'org.mockito.internal.creation.bytebuddy.ExampleClass', loaded by classloader : 'null'");
    }

    @Test
    public void testDescribeClass_InstanceIsNull_ReturnsNullDescription() {
        // Arrange
        Object instance = null;

        // Act
        String result = byteBuddyMockMaker.describeClass(instance);

        // Assert
        assertThat(result).isEqualTo("null");
    }

    @Test
    public void testDescribeClass_InstanceIsNotNull_ReturnsInstanceDescription() {
        // Arrange
        Object instance = mock(ExampleClass.class);

        // Act
        String result = byteBuddyMockMaker.describeClass(instance);

        // Assert
        assertThat(result).isEqualTo("'org.mockito.internal.creation.bytebuddy.ExampleClass', loaded by classloader : 'null'");
    }

    private static class ExampleClass {

    }

    private static class AnotherClass {

    }
}
