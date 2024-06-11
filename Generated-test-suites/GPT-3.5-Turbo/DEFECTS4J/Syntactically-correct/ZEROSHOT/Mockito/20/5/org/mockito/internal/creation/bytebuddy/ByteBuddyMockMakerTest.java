import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);

        // Verify that the createMock method is called
        verify(mockMaker, times(1)).createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastException() {
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any())).thenThrow(ClassCastException.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInstantiationException() {
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(any())).thenThrow(org.mockito.internal.creation.instance.InstantiationException.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<?> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);
        Class<?> mockedProxyType = mock(Class.class);
        Object mockInstance = "mockInstance";
        Class<?> typeToMock = mock(Class.class);
        when(settings.getTypeToMock()).thenReturn(typeToMock);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);

        // Verify that the correct cast is performed
        verify(typeToMock, times(1)).cast(mockInstance);
    }

    @Test
    public void testDescribeClassWithNull() {
        Class<?> type = null;

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String result = mockMaker.describeClass(type);

        // Verify that the correct description is returned
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass() {
        Class<?> type = mock(Class.class);
        when(type.getCanonicalName()).thenReturn("mockClass");
        when(type.getClassLoader()).thenReturn(mock(ClassLoader.class));

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String result = mockMaker.describeClass(type);

        // Verify that the correct description is returned
        assertEquals("'mockClass', loaded by classloader : 'java.lang.ClassLoader'", result);
    }

    @Test
    public void testDescribeClassWithInstance() {
        Object instance = mock(Object.class);
        Class<?> type = instance.getClass();
        when(type.getCanonicalName()).thenReturn("mockClass");
        when(type.getClassLoader()).thenReturn(mock(ClassLoader.class));

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String result = mockMaker.describeClass(instance);

        // Verify that the correct description is returned
        assertEquals("'mockClass', loaded by classloader : 'java.lang.ClassLoader'", result);
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);
        MockHandler handler = mock(MockHandler.class);
        when(mock instanceof InternalMockHandler.MockAccess).thenReturn(true);
        when(((InternalMockHandler.MockAccess) mock).getMockitoInterceptor().getMockHandler()).thenReturn(handler);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler result = mockMaker.getHandler(mock);

        // Verify that the correct handler is returned
        assertEquals(handler, result);
    }

    @Test
    public void testGetHandlerWithNonInternalMockHandler() {
        Object mock = mock(Object.class);
        when(mock instanceof InternalMockHandler.MockAccess).thenReturn(false);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler result = mockMaker.getHandler(mock);

        // Verify that null is returned
        assertNull(result);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.resetMock(mock, newHandler, settings);

        // Verify that the setMockitoInterceptor method is called
        verify(((InternalMockHandler.MockAccess) mock), times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNonInternalMockHandler() {
        MockHandler handler = mock(MockHandler.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.asInternalMockHandler(handler);
    }

    @Test
    public void testAsInternalMockHandler() {
        InternalMockHandler handler = mock(InternalMockHandler.class);

        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);

        // Verify that the correct InternalMockHandler is returned
        assertEquals(handler, result);
    }

    @Test
    public void testInitializeClassInstantiator() {
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        ByteBuddyMockMaker mockMaker = spy(new ByteBuddyMockMaker());
        doReturn(globalConfiguration).when(mockMaker).newGlobalConfigurationInstance();
        doReturn(true).when(globalConfiguration).enableClassCache();
        doThrow(IllegalStateException.class).when(mockMaker).throwNewIllegalStateException(any());

        ClassInstantiator result = mockMaker.initializeClassInstantiator();

        // Verify that the correct ClassInstantiator is returned
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithClassNotFoundException() throws ClassNotFoundException {
        ByteBuddyMockMaker mockMaker = spy(new ByteBuddyMockMaker());
        doThrow(ClassNotFoundException.class).when(mockMaker).createClassInstance(anyString());
        doThrow(IllegalStateException.class).when(mockMaker).throwNewIllegalStateException(any());

        mockMaker.initializeClassInstantiator();
    }

    @Test
    public void testInitializeClassInstantiatorWithObjenesisClassLoader() throws ClassNotFoundException {
        ByteBuddyMockMaker mockMaker = spy(ByteBuddyMockMaker.class);
        doThrow(ClassNotFoundException.class).when(mockMaker).createClassInstance(anyString());
        doReturn("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis").when(mockMaker).createObjenesisClassLoaderClassName();
        doReturn("newInstance").when(mockMaker).createObjenesisClassLoaderMethodName();
        doReturn("").when(mockMaker).createBooleanConstructorParameter();
        doReturn(mock(Constructor.class)).when(mockMaker).getDeclaredConstructor(any(), any());
        doReturn(mock(ClassInstantiator.UsingObjenesis.class)).when(mockMaker).createUsingObjenesisClassInstance();

        ClassInstantiator result = mockMaker.initializeClassInstantiator();

        // Verify that the correct ClassInstantiator is returned
        assertNotNull(result);
    }

    private class ByteBuddyMockMakerTestImplementation extends ByteBuddyMockMaker {

        @Override
        protected ClassInstantiator initializeClassInstantiator() {
            return mock(ClassInstantiator.class);
        }
    }
}