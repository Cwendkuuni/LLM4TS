import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.internal.util.ClassLoadingUtils;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerUtil;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.StringJoiner;
import org.mockito.internal.util.StringUtil;
import org.mockito.internal.util.TypeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.reflect.AccessibleObject.setAccessible;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
    private ClassInstantiator classInstantiator;
    private InternalMockHandler internalMockHandler;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        cachingMockBytecodeGenerator = MockitoSpies.spy(new CachingMockBytecodeGenerator());
        byteBuddyMockMaker.cachingMockBytecodeGenerator = cachingMockBytecodeGenerator;
        classInstantiator = MockitoSpies.spy(byteBuddyMockMaker.classInstantiator);
        internalMockHandler = Mockito.mock(InternalMockHandler.class);
    }

    @Test
    public void testCreateMock_NoExtraInterfaces_Success() {
        MockCreationSettings<Object> settings = MockMakerUtil.createMockCreationSettings(Object.class);
        settings.setSerializableMode(SerializableMode.NORMAL);

        Object mock = byteBuddyMockMaker.createMock(settings, internalMockHandler);
        Assert.assertNotNull(mock);
        Assert.assertEquals(Object.class, mock.getClass());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ExtraInterfaces_SerializationAcrossClassloadersNotSupported() {
        MockCreationSettings<Object> settings = MockMakerUtil.createMockCreationSettings(Object.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(settings, internalMockHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_ClassCastException() throws Exception {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        MockCreationSettings<Object> settings = MockMakerUtil.createMockCreationSettings(objectClass);
        MockHandler handler = mock(MockHandler.class);

        when(cachingMockBytecodeGenerator.get(eq(stringClass), anyArray()))
                .thenReturn(ClassLoadingUtils.loadClass(String.class, byteBuddyMockMaker.getClass().getClassLoader()));
        when(classInstantiator.instantiate(eq(ClassLoadingUtils.loadClass(String.class, byteBuddyMockMaker.getClass().getClassLoader()))))
                .thenReturn(new String("Hello World"));

        byteBuddyMockMaker.createMock(settings, handler);

        Object mockedProxyType = byteBuddyMockMaker.cachingMockBytecodeGenerator.get(stringClass, new Class[0]);
        String mockInstance = (String) byteBuddyMockMaker.classInstantiator.instantiate(mockedProxyType);

        Assert.assertEquals("Hello World", mockInstance);

        Class<? extends Object> mockedMockType = TypeUtil.getRawType(mockedProxyType);
        Class<? extends Object> expectedType = String.class;
        Assert.assertEquals(expectedType, mockedMockType);

        Throwable throwable = Mockito.expectedThrows(ClassCastException.class, () -> mockedMockType.cast(mockInstance));
        byteBuddyMockMaker.createMock(settings, handler);

        throwable.printStackTrace();
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMock_InstantiationException() throws Exception {
        MockCreationSettings<Object> settings = MockMakerUtil.createMockCreationSettings(Object.class);
        settings.setSerializableMode(SerializableMode.NORMAL);

        Class<?> clazz = Class.forName("org.mockito.exceptions.base.InstantiationExceptionTest");
        when(cachingMockBytecodeGenerator.get(eq(Object.class), anyArray()))
                .thenReturn(ClassLoadingUtils.loadClass(clazz, byteBuddyMockMaker.getClass().getClassLoader()));
        when(classInstantiator.instantiate(eq(ClassLoadingUtils.loadClass(clazz, byteBuddyMockMaker.getClass().getClassLoader()))))
                .thenThrow(new InstantiationException("Unable to create mock instance of type 'InstantiationExceptionTest'"));

        byteBuddyMockMaker.createMock(settings, internalMockHandler);
    }

    @Test
    public void testGetHandler() throws Exception {
        Object mock = mock(Object.class);

        Assert.assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testGetHandler_NullMock() throws Exception {
        Assert.assertNull(byteBuddyMockMaker.getHandler(null));
    }

    @Test
    public void testResetMock() throws Exception {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = MockMakerUtil.createMockCreationSettings(Object.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mock;
        Assert.assertEquals(internalMockHandler, mockAccess.getMockitoInterceptor().getMockHandler());

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        Assert.assertEquals(newHandler, mockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test
    public void testResetMock_NullMock() throws Exception {
        Object mock = null;
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = MockMakerUtil.createMockCreationSettings(Object.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        Assert.assertNull(mock);
    }

    @Test
    public void testDescribeClass() throws Exception {
        Class<?> clazz = String.class;
        ClassLoader classLoader = String.class.getClassLoader();

        Assert.assertEquals("class 'java.lang.String', loaded by classloader : 'bootstrap'", byteBuddyMockMaker.describeClass(clazz));
        Assert.assertEquals("class 'java.lang.String', loaded by classloader : " + classLoader.getName(), byteBuddyMockMaker.describeClass(clazz));

        Object obj = new Object();
        Assert.assertEquals("class 'java.lang.Object', loaded by classloader : bootstrap", byteBuddyMockMaker.describeClass(obj.getClass()));
        Assert.assertEquals("class 'java.lang.Object', loaded by classloader : " + classLoader.getName(), byteBuddyMockMaker.describeClass(obj.getClass()));
    }

    @Test
    public void testDescribeClass_Null() throws Exception {
        Assert.assertEquals("null", byteBuddyMockMaker.describeClass(null));
    }

    @Test
    public void testInitializeClassInstantiator() throws Exception {
        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
        ClassInstantiator instance = (ClassInstantiator) usingClassCacheConstructor.newInstance(true);

        Assert.assertEquals(true, instance.enableClassCache());
        Assert.assertNotNull(instance.getClass().getSuperclass());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ObjenesisMissing() throws Exception {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Mockito could not create mock: Objenesis is missing on the classpath.", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAsInternalMockHandler() throws Exception {
        MockHandler mockHandler = mock(MockHandler.class);

        Assert.assertNotNull(byteBuddyMockMaker.asInternalMockHandler(mockHandler));
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_NullMockHandler() throws Exception {
        Assert.assertNull(byteBuddyMockMaker.asInternalMockHandler(null));

        MockitoException exception = Mockito.expectedThrows(MockitoException.class, () -> byteBuddyMockMaker.asInternalMockHandler(null));
        Assert.assertEquals("At the moment you cannot provide own implementations of MockHandler.", exception.getMessage());
    }
}