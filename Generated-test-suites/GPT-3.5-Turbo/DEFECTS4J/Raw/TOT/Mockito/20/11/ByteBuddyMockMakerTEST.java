import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockHandler;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ByteBuddyMockMakerTest {
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings<TestMockedClass> settings;
    private MockHandler handler;

    @Before
    public void setUp() {
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        mockMaker = new ByteBuddyMockMaker(classInstantiator, cachingMockBytecodeGenerator);
        settings = mock(MockCreationSettings.class);
        handler = mock(MockHandler.class);
    }

    @Test
    public void createMock_ValidSettings_ReturnsMockInstanceOfCorrectType() {
        // Arrange
        Class<TestMockedClass> typeToMock = TestMockedClass.class;
        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getExtraInterfaces()).thenReturn(new Class[] {});
        when(cachingMockBytecodeGenerator.get(typeToMock, new Class[] {})).thenReturn(typeToMock);
        when(classInstantiator.instantiate(typeToMock)).thenReturn(new TestMockedClass());

        // Act
        TestMockedClass result = mockMaker.createMock(settings, handler);

        // Assert
        assertNotNull(result);
        assertEquals(typeToMock, result.getClass());
    }

    @Test(expected = MockitoException.class)
    public void createMock_SerializableModeAcrossClassloaders_ThrowsMockitoException() {
        // Arrange
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Act
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void ensureMockIsAssignableToMockedType_MockIsAssignable_ReturnsSameMockInstance() {
        // Arrange
        TestMockedClass mock = mock(TestMockedClass.class);
        when(settings.getTypeToMock()).thenReturn(TestMockedClass.class);

        // Act
        TestMockedClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
    }

    @Test(expected = ClassCastException.class)
    public void ensureMockIsAssignableToMockedType_MockIsNotAssignable_ThrowsClassCastException() {
        // Arrange
        DifferentTestClass notAssignableMock = mock(DifferentTestClass.class);
        when(settings.getTypeToMock()).thenReturn(TestMockedClass.class);

        // Act
        mockMaker.ensureMockIsAssignableToMockedType(settings, notAssignableMock);
    }

    @Test
    public void describeClass_ClassIsNull_ReturnsNullDescription() {
        // Arrange
        Class type = null;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void describeClass_ClassIsNotNull_ReturnsClassDescription() {
        // Arrange
        Class type = TestMockedClass.class;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertEquals("'org.mockito.internal.creation.bytebuddy.TestMockedClass', loaded by classloader : 'null'", result);
    }

    @Test
    public void describeClass_InstanceIsNull_ReturnsNullDescription() {
        // Arrange
        Object instance = null;

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void describeClass_InstanceIsNotNull_ReturnsClassDescription() {
        // Arrange
        Object instance = new TestMockedClass();

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        assertEquals("'org.mockito.internal.creation.bytebuddy.TestMockedClass', loaded by classloader : 'null'", result);
    }

    @Test
    public void getHandler_MockIsNotOfCorrectType_ReturnsNull() {
        // Arrange
        Object mock = new Object();

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void getHandler_MockIsOfCorrectType_ReturnsMockHandler() {
        // Arrange
        TestMockedClass mock = mock(TestMockedClass.class);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler()).thenReturn(handler);

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNotNull(result);
        assertEquals(handler, result);
    }

    @Test
    public void resetMock_ValidParameters_MockHandlerIsSetToNewHandler() {
        // Arrange
        TestMockedClass mock = mock(TestMockedClass.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockMethodInterceptor);
        ((MockMethodInterceptor.MockAccess) mock).setMockitoInterceptor(mockMethodInterceptor);
        MockHandler newHandler = mock(MockHandler.class);
        when(settings.getTypeToMock()).thenReturn(TestMockedClass.class);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        assertEquals(newHandler, mockMaker.getHandler(mock));
    }

    @Test(expected = MockitoException.class)
    public void resetMock_InvalidParameters_ThrowsMockitoException() {
        // Arrange
        TestMockedClass mock = mock(TestMockedClass.class);
        MockHandler newHandler = mock(MockHandler.class);

        // Act
        mockMaker.resetMock(mock, newHandler, settings);
    }

    private class TestMockedClass {

    }

    private class DifferentTestClass {

    }
}