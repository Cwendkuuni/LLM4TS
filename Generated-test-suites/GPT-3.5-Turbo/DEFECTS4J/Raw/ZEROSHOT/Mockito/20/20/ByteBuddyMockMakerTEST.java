import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.StringJoiner.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockCreationSettings<?> mockCreationSettings;
    private MockHandler mockHandler;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockCreationSettings = mock(MockCreationSettings.class);
        mockHandler = mock(MockHandler.class);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test
    public void testCreateMockWithValidSettings() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        Class<?> mockedProxyType = Object.class;
        Object mockInstance = mock(Object.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        Object castedMock = mock(Object.class);

        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(cachingMockBytecodeGenerator.get(mockedProxyType, new Class<?>[]{})).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        when(mockInstance).thenReturn(mockAccess);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));
        when(mockedProxyType.cast(mockInstance)).thenReturn(castedMock);

        Object result = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);

        verify(mockCreationSettings).getTypeToMock();
        verify(mockCreationSettings).getExtraInterfaces();
        verify(cachingMockBytecodeGenerator).get(mockedProxyType, new Class<?>[]{});
        verify(classInstantiator).instantiate(mockedProxyType);
        verify(mockAccess).getMockitoInterceptor();
        verify(mockedProxyType).cast(mockInstance);
        verify(mockHandler, never()).getMockHandler();

        assertEquals(castedMock, result);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastException() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        Class<?> mockedProxyType = Object.class;
        Object mockInstance = mock(Object.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);

        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(cachingMockBytecodeGenerator.get(mockedProxyType, new Class<?>[]{})).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        when(mockInstance).thenReturn(mockAccess);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));
        when(mockedProxyType.cast(mockInstance)).thenThrow(ClassCastException.class);

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInstantiationException() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        Class<?> mockedProxyType = Object.class;

        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(cachingMockBytecodeGenerator.get(mockedProxyType, new Class<?>[]{})).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenThrow(org.mockito.internal.creation.instance.InstantiationException.class);

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        Class<?> mockedProxyType = Object.class;
        Object mockInstance = mock(Object.class);
        Object castedMock = mock(Object.class);

        when(mockCreationSettings.getTypeToMock()).thenReturn(mockedProxyType);
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        when(mockedProxyType.cast(mockInstance)).thenReturn(castedMock);

        Object result = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);

        verify(mockCreationSettings).getTypeToMock();
        verify(classInstantiator).instantiate(mockedProxyType);
        verify(mockedProxyType).cast(mockInstance);
        verify(mockHandler, never()).getMockHandler();

        assertEquals(castedMock, result);
    }

    @Test
    public void testDescribeClassWithTypeNull() {
        String result = byteBuddyMockMaker.describeClass(null);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClassWithTypeNotNull() {
        Class<?> type = Object.class;

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClassWithInstanceNull() {
        String result = byteBuddyMockMaker.describeClass((Object) null);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClassWithInstanceNotNull() {
        Object instance = new Object();

        String result = byteBuddyMockMaker.describeClass(instance);

        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testGetHandlerWithInvalidMock() {
        Object mock = new Object();

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testGetHandlerWithValidMock() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor mockMethodInterceptor = mock(MockMethodInterceptor.class);
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);

        when(mock.getMockitoInterceptor()).thenReturn(mockMethodInterceptor);
        when(mockMethodInterceptor.getMockHandler()).thenReturn(internalMockHandler);

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertEquals(internalMockHandler, result);
    }

    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor mockMethodInterceptor = mock(MockMethodInterceptor.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);

        when(mock.getMockitoInterceptor()).thenReturn(mockMethodInterceptor);

        byteBuddyMockMaker.resetMock(mock, internalMockHandler, settings);

        verify(mockMethodInterceptor).setMockHandler(internalMockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testResetMockWithInvalidMockHandler() {
        MockHandler invalidMockHandler = mock(MockHandler.class);

        byteBuddyMockMaker.resetMock(mock(Object.class), invalidMockHandler, mock(MockCreationSettings.class));
    }

    @Test
    public void testInitializeClassInstantiatorWithObjenesisClassLoader() throws Exception {
        String objenesisClassLoaderClassName = "org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis";
        Class<?> objenesisClassLoaderClass = Class.forName(objenesisClassLoaderClassName);
        Constructor<?> objenesisClassLoaderConstructor = objenesisClassLoaderClass.getDeclaredConstructor(boolean.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);

        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        when(globalConfiguration.enableClassCache()).thenReturn(true);

        doReturn(classInstantiator).when(objenesisClassLoaderConstructor).newInstance(eq(true));
        PowerMockito.whenNew(GlobalConfiguration.class).withNoArguments().thenReturn(globalConfiguration);

        ClassInstantiator result = ByteBuddyMockMaker.initializeClassInstantiator();

        assertNotNull(result);
        verify(globalConfiguration).enableClassCache();
        verify(classInstantiator).getClass().getSimpleName();
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithMissingObjenesisClassLoader() throws Exception {
        String objenesisClassLoaderClassName = "org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis";
        Class<?> objenesisClassLoaderClass = Class.forName(objenesisClassLoaderClassName);
        Constructor<?> objenesisClassLoaderConstructor = objenesisClassLoaderClass.getDeclaredConstructor(boolean.class);

        doThrow(Exception.class).when(objenesisClassLoaderConstructor).newInstance(eq(true));
        PowerMockito.whenNew(Class.forName(objenesisClassLoaderClassName)).withNoArguments().thenReturn(objenesisClassLoaderConstructor);

        ByteBuddyMockMaker.initializeClassInstantiator();
    }

    private void assertEquals(Object expected, Object actual) {
        org.junit.Assert.assertEquals(expected, actual);
    }

    private void assertNull(Object object) {
        org.junit.Assert.assertNull(object);
    }

    private void assertNotNull(Object object) {
        org.junit.Assert.assertNotNull(object);
    }

    private void verify(Object mock) {
        org.mockito.Mockito.verify(mock);
    }

    private void verify(Object mock, VerificationMode mode) {
        org.mockito.Mockito.verify(mock, mode);
    }

    private <T> T mock(Class<T> classToMock) {
        return org.mockito.Mockito.mock(classToMock);
    }

    private <T> T when(T methodCall) {
        return org.mockito.Mockito.when(methodCall);
    }

    private void doReturn(Object toBeReturned, Object... toBeReturnedNext) {
        org.mockito.Mockito.doReturn(toBeReturned, toBeReturnedNext);
    }

    private void doThrow(Throwable toBeThrown) {
        org.mockito.Mockito.doThrow(toBeThrown);
    }

    private void doThrow(Throwable toBeThrown, Throwable... toBeThrownNext) {
        org.mockito.Mockito.doThrow(toBeThrown, toBeThrownNext);
    }

    private void doNothing() {
        org.mockito.Mockito.doNothing();
    }

    private void doAnswer(Answer<?> answer) {
        org.mockito.Mockito.doAnswer(answer);
    }

    private void doCallRealMethod() {
        org.mockito.Mockito.doCallRealMethod();
    }

    private Answer<Object> withSettings() {
        return org.mockito.Mockito.withSettings();
    }

    private static <T> Stubbing<T> when(T methodCall) {
        return org.mockito.Mockito.when(methodCall);
    }

}