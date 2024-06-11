import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;


public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock_ValidSettings_ReturnsMockInstance() throws Exception {
        // Arrange
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);
        ClassA typeToMock = ClassA.class;
        Class<? extends ClassA> mockedProxyType = ClassA.class;

        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});

        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockedProxyType.cast(new ClassA()));

        // Act
        ClassA mockInstance = mockMaker.createMock(settings, null);

        // Assert
        assertTrue(mockedProxyType.isInstance(mockInstance));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassLoaders_ThrowsException() {
        // Arrange
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);
        SettingsTypeToMock typeToMock = new SettingsTypeToMock();
        SerializableMode serializableMode = SerializableMode.ACROSS_CLASSLOADERS;

        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getSerializableMode()).thenReturn(serializableMode);

        // Act
        mockMaker.createMock(settings, null);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException_ThrowsException() throws Exception {
        // Arrange
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);
        ClassA typeToMock = ClassA.class;
        Class<? extends ClassA> mockedProxyType = ClassB.class;

        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});

        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockedProxyType.cast(new ClassB()));

        // Act
        mockMaker.createMock(settings, null);
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException_ThrowsException() throws Exception {
        // Arrange
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);
        ClassA typeToMock = ClassA.class;
        Class<? extends ClassA> mockedProxyType = ClassA.class;

        when(settings.getTypeToMock()).thenReturn(typeToMock);
        when(settings.getExtraInterfaces()).thenReturn(new Class[]{});

        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(mockedProxyType)).thenThrow(new InstantiationException());

        // Act
        mockMaker.createMock(settings, null);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_MockIsAssignable_ReturnsMock() {
        // Arrange
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);
        ClassA mockInstance = mock(ClassA.class);

        when(settings.getTypeToMock()).thenReturn(ClassA.class);

        // Act
        ClassA result = mockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);

        // Assert
        assertEquals(mockInstance, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedType_MockIsNotAssignable_ThrowsException() {
        // Arrange
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);
        ClassA mockInstance = mock(ClassB.class);

        when(settings.getTypeToMock()).thenReturn(ClassA.class);

        // Act
        mockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);
    }

    @Test
    public void testDescribeClass_ClassIsNull_ReturnsNullString() {
        // Arrange
        Class type = null;

        // Act
        String result = ByteBuddyMockMaker.describeClass(type);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_ClassIsNotNull_ReturnsClassDescription() {
        // Arrange
        Class type = ClassA.class;

        // Act
        String result = ByteBuddyMockMaker.describeClass(type);

        // Assert
        assertEquals("'org.example.ClassA', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_InstanceIsNull_ReturnsNullString() {
        // Arrange
        Object instance = null;

        // Act
        String result = ByteBuddyMockMaker.describeClass(instance);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_InstanceIsNotNull_ReturnsInstanceClassDescription() {
        // Arrange
        Object instance = new ClassA();

        // Act
        String result = ByteBuddyMockMaker.describeClass(instance);

        // Assert
        assertEquals("'org.example.ClassA', loaded by classloader : 'null'", result);
    }

    @Test
    public void testGetHandler_MockIsInstanceOfMockMethodInterceptorMockAccess_ReturnsMockHandler() {
        // Arrange
        Object mockInstance = mock(ClassA.class);

        // Act
        MockHandler result = mockMaker.getHandler(mockInstance);

        // Assert
        assertTrue(result instanceof MockMethodInterceptor.MockAccess);
    }

    @Test
    public void testGetHandler_MockIsNotInstanceOfMockMethodInterceptorMockAccess_ReturnsNull() {
        // Arrange
        Object mockInstance = new Object();

        // Act
        MockHandler result = mockMaker.getHandler(mockInstance);

        // Assert
        assertNull(result);
    }

    @Test
    public void testResetMock_ValidMockInstance_SetsNewMockHandler() {
        // Arrange
        Object mockInstance = mock(ClassA.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);

        // Act
        mockMaker.resetMock(mockInstance, newHandler, settings);

        // Assert
        assertEquals(newHandler, mockMaker.getHandler(mockInstance));
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_InvalidMockInstance_ThrowsException() {
        // Arrange
        Object mockInstance = new Object();
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<ClassA> settings = mock(MockCreationSettings.class);

        // Act
        mockMaker.resetMock(mockInstance, newHandler, settings);
    }

    @Test
    public void testInitializeClassInstantiator_ObjenesisPresent_ReturnsValidClassInstantiator() {
        // Arrange
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);

        // Act
        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator();

        // Assert
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ObjenesisNotPresent_ThrowsException() {
        // Arrange
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        when(globalConfiguration.enableClassCache()).thenReturn(true);

        // Act
        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_HandlerIsNotInternalMockHandler_ThrowsException() {
        // Arrange
        MockHandler handler = mock(MockHandler.class);

        // Act
        InternalMockHandler result = ByteBuddyMockMaker.asInternalMockHandler(handler);
    }

    private static class ClassA {

    }

    private static class ClassB extends ClassA {

    }

    private static class SettingsTypeToMock extends ClassA {

    }

}