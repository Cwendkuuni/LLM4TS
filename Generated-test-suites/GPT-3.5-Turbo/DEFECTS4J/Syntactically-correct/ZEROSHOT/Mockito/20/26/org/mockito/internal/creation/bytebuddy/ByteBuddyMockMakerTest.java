import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiatorMock;
    private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;
    private MockCreationSettings mockCreationSettingsMock;
    private MockHandler mockHandlerMock;
    private Object mockInstanceMock;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiatorMock = mock(ClassInstantiator.class);
        cachingMockBytecodeGeneratorMock = mock(CachingMockBytecodeGenerator.class);
        mockCreationSettingsMock = mock(MockCreationSettings.class);
        mockHandlerMock = mock(MockHandler.class);
        mockInstanceMock = mock(Object.class);

        byteBuddyMockMaker.classInstantiator = classInstantiatorMock;
        byteBuddyMockMaker.cachingMockBytecodeGenerator = cachingMockBytecodeGeneratorMock;
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassLoaders() {
        SerializableMode serializableModeMock = mock(SerializableMode.class);
        when(mockCreationSettingsMock.getSerializableMode()).thenReturn(serializableModeMock);
        when(serializableModeMock == SerializableMode.ACROSS_CLASSLOADERS).thenReturn(true);

        byteBuddyMockMaker.createMock(mockCreationSettingsMock, mockHandlerMock);

        verifyZeroInteractions(classInstantiatorMock);
        verifyZeroInteractions(cachingMockBytecodeGeneratorMock);
    }

    @Test
    public void testCreateMock_Success() {
        Class<?> mockedProxyTypeMock = mock(Class.class);
        when(cachingMockBytecodeGeneratorMock.get(mockCreationSettingsMock.getTypeToMock(), mockCreationSettingsMock.getExtraInterfaces())).thenReturn(mockedProxyTypeMock);
        when(classInstantiatorMock.instantiate(mockedProxyTypeMock)).thenReturn(mockInstanceMock);
        when(mockInstanceMock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);

        byteBuddyMockMaker.createMock(mockCreationSettingsMock, mockHandlerMock);

        verify(classInstantiatorMock, times(1)).instantiate(mockedProxyTypeMock);
        verify(mockInstanceMock, times(1)).setMockitoInterceptor(any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        Class<?> mockedProxyTypeMock = mock(Class.class);
        when(cachingMockBytecodeGeneratorMock.get(mockCreationSettingsMock.getTypeToMock(), mockCreationSettingsMock.getExtraInterfaces())).thenReturn(mockedProxyTypeMock);
        when(classInstantiatorMock.instantiate(mockedProxyTypeMock)).thenReturn(mockInstanceMock);
        when(mockInstanceMock instanceof MockMethodInterceptor.MockAccess).thenReturn(false);

        byteBuddyMockMaker.createMock(mockCreationSettingsMock, mockHandlerMock);

        verify(classInstantiatorMock, times(1)).instantiate(mockedProxyTypeMock);
        verify(mockInstanceMock, times(0)).setMockitoInterceptor(any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException() {
        Class<?> mockedProxyTypeMock = mock(Class.class);
        when(cachingMockBytecodeGeneratorMock.get(mockCreationSettingsMock.getTypeToMock(), mockCreationSettingsMock.getExtraInterfaces())).thenReturn(mockedProxyTypeMock);
        when(classInstantiatorMock.instantiate(mockedProxyTypeMock)).thenThrow(new org.mockito.internal.creation.instance.InstantiationException());

        byteBuddyMockMaker.createMock(mockCreationSettingsMock, mockHandlerMock);

        verify(classInstantiatorMock, times(1)).instantiate(mockedProxyTypeMock);
        verify(mockInstanceMock, times(0)).setMockitoInterceptor(any());
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        Class mockClassMock = mock(Class.class);
        Object mockInstance = mock(Object.class);
        when(mockCreationSettingsMock.getTypeToMock()).thenReturn(mockClassMock);

        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettingsMock, mockInstance);

        verify(mockClassMock, times(1)).cast(mockInstance);
    }

    @Test
    public void testDescribeClass_NullInput() {
        String result = byteBuddyMockMaker.describeClass(null);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_ClassInput() {
        Class classMock = mock(Class.class);
        ClassLoader classLoaderMock = mock(ClassLoader.class);
        when(classMock.getCanonicalName()).thenReturn("CanonicalName");
        when(classMock.getClassLoader()).thenReturn(classLoaderMock);
        String expectedResult = "'CanonicalName', loaded by classloader : '" + classLoaderMock + "'";

        String result = byteBuddyMockMaker.describeClass(classMock);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDescribeClass_ObjectInput() {
        Object instanceMock = mock(Object.class);
        Class classMock = mock(Class.class);
        when(instanceMock.getClass()).thenReturn(classMock);
        ClassLoader classLoaderMock = mock(ClassLoader.class);
        when(classMock.getCanonicalName()).thenReturn("CanonicalName");
        when(classMock.getClassLoader()).thenReturn(classLoaderMock);
        String expectedResult = "'CanonicalName', loaded by classloader : '" + classLoaderMock + "'";

        String result = byteBuddyMockMaker.describeClass(instanceMock);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetHandler() {
        MockMethodInterceptor.MockAccess mockAccessMock = mock(MockMethodInterceptor.MockAccess.class);
        when(mockAccessMock.getMockitoInterceptor()).thenReturn(mockHandlerMock);

        MockHandler result = byteBuddyMockMaker.getHandler(mockAccessMock);

        assertEquals(mockHandlerMock, result);
    }

    @Test
    public void testGetHandler_NotMockAccess() {
        Object mockObjectMock = mock(Object.class);

        MockHandler result = byteBuddyMockMaker.getHandler(mockObjectMock);

        assertNull(result);
    }

    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mockAccessMock = mock(MockMethodInterceptor.MockAccess.class);
        byteBuddyMockMaker.resetMock(mockAccessMock, mockHandlerMock, mockCreationSettingsMock);

        verify(mockAccessMock, times(1)).setMockitoInterceptor(any());
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_NotMockAccess() {
        Object mockObjectMock = mock(Object.class);

        byteBuddyMockMaker.resetMock(mockObjectMock, mockHandlerMock, mockCreationSettingsMock);

        verify(mockObjectMock, times(0)).setMockitoInterceptor(any());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_Exception() throws Throwable {
        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenThrow(new ClassNotFoundException());

        byteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testInitializeClassInstantiator_Success() throws Throwable {
        Constructor constructorMock = mock(Constructor.class);
        Object objenesisClassLoaderMock = mock(Object.class);
        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(objenesisClassLoaderMock.getClass());
        when(objenesisClassLoaderMock.getDeclaredConstructor(boolean.class)).thenReturn(constructorMock);
        when(globalConfigurationMock.enableClassCache()).thenReturn(true);

        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        verify(constructorMock, times(1)).newInstance(eq(true));
        assertNotNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_Exception() {
        MockHandler mockHandler = mock(MockHandler.class);

        byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        verifyZeroInteractions(mockHandler);
    }

    @Test
    public void testAsInternalMockHandler_Success() {
        InternalMockHandler internalMockHandlerMock = mock(InternalMockHandler.class);
        when(internalMockHandlerMock instanceof InternalMockHandler).thenReturn(true);

        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(internalMockHandlerMock);

        assertEquals(internalMockHandlerMock, result);
    }
}