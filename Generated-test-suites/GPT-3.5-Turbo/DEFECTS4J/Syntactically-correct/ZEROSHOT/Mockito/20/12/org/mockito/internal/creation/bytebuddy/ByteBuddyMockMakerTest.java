import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.creation.bytebuddy.*;

import java.lang.reflect.Constructor;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock_SerializableModeACROSS_CLASSLOADERS() {
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = new MockHandler();
        
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Should throw MockitoException");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_ValidSettings() {
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.NONE);
        settings.setTypeToMock(ByteBuddyMockMaker.class);
        settings.setExtraInterfaces(new Class[]{MockMaker.class});
        MockHandler handler = new MockHandler();

        ByteBuddyMockMaker spyByteBuddyMockMaker = spy(byteBuddyMockMaker);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(spyByteBuddyMockMaker.initializeClassInstantiator()).thenReturn(classInstantiator);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(spyByteBuddyMockMaker.getCachingMockBytecodeGenerator()).thenReturn(cachingMockBytecodeGenerator);
        Class<?> mockedProxyType = mock(Class.class);
        when(cachingMockBytecodeGenerator.get(ByteBuddyMockMaker.class, new Class[]{MockMaker.class})).thenReturn(mockedProxyType);
        Object mockInstance = new Object();
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mockInstance).thenReturn(mockAccess);
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        when(spyByteBuddyMockMaker.asInternalMockHandler(handler)).thenReturn(internalMockHandler);
        MockMethodInterceptor.MockAccess mockAccessSpy = spy(mockAccess);
        when(mockInstance).thenReturn(mockAccessSpy);

        Object result = spyByteBuddyMockMaker.createMock(settings, handler);

        assertSame(result, mockInstance);
        verify(mockAccessSpy).setMockitoInterceptor(mockitoInterceptor, settings);
        verify(spyByteBuddyMockMaker).ensureMockIsAssignableToMockedType(settings, mockInstance);
    }

    @Test
    public void testCreateMock_ClassCastException() {
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.NONE);
        settings.setTypeToMock(ByteBuddyMockMaker.class);
        settings.setExtraInterfaces(new Class[]{MockMaker.class});
        MockHandler handler = new MockHandler();

        ByteBuddyMockMaker spyByteBuddyMockMaker = spy(byteBuddyMockMaker);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(spyByteBuddyMockMaker.initializeClassInstantiator()).thenReturn(classInstantiator);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(spyByteBuddyMockMaker.getCachingMockBytecodeGenerator()).thenReturn(cachingMockBytecodeGenerator);
        Class<?> mockedProxyType = mock(Class.class);
        when(cachingMockBytecodeGenerator.get(ByteBuddyMockMaker.class, new Class[]{MockMaker.class})).thenReturn(mockedProxyType);
        Object mockInstance = new Object();
        when(classInstantiator.instantiate(mockedProxyType)).thenReturn(mockInstance);
        ClassCastException cce = new ClassCastException();
        doThrow(cce).when(mockInstance).setMockitoInterceptor(any());
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        Constructor<?> usingClassCacheConstructor = mock(Constructor.class);
        when(usingClassCacheConstructor.newInstance(globalConfiguration.enableClassCache())).thenReturn(classInstantiator);

        try {
            spyByteBuddyMockMaker.createMock(settings, handler);
            fail("Should throw MockitoException");
        } catch (MockitoException e) {
            assertEquals("ClassCastException occurred while creating the mockito mock :  class to mock : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker', loaded by classloader : 'null'", e.getMessage());
            assertSame(cce, e.getCause());
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.NONE);
        settings.setTypeToMock(ByteBuddyMockMaker.class);
        settings.setExtraInterfaces(new Class[]{MockMaker.class});
        MockHandler handler = new MockHandler();

        ByteBuddyMockMaker spyByteBuddyMockMaker = spy(byteBuddyMockMaker);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(spyByteBuddyMockMaker.initializeClassInstantiator()).thenReturn(classInstantiator);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(spyByteBuddyMockMaker.getCachingMockBytecodeGenerator()).thenReturn(cachingMockBytecodeGenerator);
        Class<?> mockedProxyType = mock(Class.class);
        when(cachingMockBytecodeGenerator.get(ByteBuddyMockMaker.class, new Class[]{MockMaker.class})).thenReturn(mockedProxyType);
        Object mockInstance = new Object();
        InstantiationException instantiationException = new InstantiationException();
        when(classInstantiator.instantiate(mockedProxyType)).thenThrow(instantiationException);
        Class<?> superclass = mock(Class.class);
        when(mockedProxyType.getSuperclass()).thenReturn(superclass);
        when(superclass.getSimpleName()).thenReturn("ByteBuddyMockMaker");

        try {
            spyByteBuddyMockMaker.createMock(settings, handler);
            fail("Should throw MockitoException");
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'ByteBuddyMockMaker'", e.getMessage());
            assertSame(instantiationException, e.getCause());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(ByteBuddyMockMaker.class);
        ByteBuddyMockMaker mock = mock(ByteBuddyMockMaker.class);

        ByteBuddyMockMaker result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertSame(mock, result);
    }

    @Test
    public void testDescribeClass_withType() {
        Class type = ByteBuddyMockMaker.class;

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals("'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker', loaded by classloader : '" + type.getClassLoader() + "'", result);
    }

    @Test
    public void testDescribeClass_withNullType() {
        Class type = null;

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_withInstance() {
        Object instance = new ByteBuddyMockMaker();

        String result = byteBuddyMockMaker.describeClass(instance);

        assertEquals("'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClass_withNullInstance() {
        Object instance = null;

        String result = byteBuddyMockMaker.describeClass(instance);

        assertEquals("null", result);
    }

    @Test
    public void testGetHandler_withMock() {
        Object mock = mock(Object.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mock).thenReturn(mockAccess);
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);
        MockHandler expectedHandler = mock(MockHandler.class);
        when(mockitoInterceptor.getMockHandler()).thenReturn(expectedHandler);

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertSame(expectedHandler, result);
    }

    @Test
    public void testGetHandler_withNonMock() {
        Object nonMock = new Object();

        MockHandler result = byteBuddyMockMaker.getHandler(nonMock);

        assertNull(result);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);
        when(mock).thenReturn(mockAccess);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings();
        MockMethodInterceptor mockitoInterceptor = mock(MockMethodInterceptor.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        verify(mockitoInterceptor).setMockHandler(newHandler);
    }

    private CachingMockBytecodeGenerator getCachingMockBytecodeGenerator() {
        return cachingMockBytecodeGenerator;
    }

    private ClassInstantiator initializeClassInstantiator() {
        return classInstantiator;
    }

}