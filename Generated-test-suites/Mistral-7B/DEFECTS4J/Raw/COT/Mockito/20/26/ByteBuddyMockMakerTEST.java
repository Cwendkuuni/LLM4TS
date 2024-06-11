import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandlerInvocation;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRegistry;
import org.mockito.plugins.MockMakerUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Mockito.initMocks(this);
    }

    @Test
    public void testCreateMock() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        Class<SomeInterface> interfaceType = SomeInterface.class;
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<>(interfaceType, null), handler);
        Assert.assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializableModeAcrossClassloaders() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<SomeInterface> settings = new MockCreationSettings<>(SomeInterface.class, null);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        SomeInterface mock = Mockito.mock(SomeInterface.class);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        SomeInterface mock = Mockito.mock(SomeInterface.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings<>(SomeInterface.class, null));
        MockHandler resetHandler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertSame(handler, resetHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidHandler() {
        Object invalidHandler = new Object();
        MockCreationSettings<SomeInterface> settings = new MockCreationSettings<>(SomeInterface.class, null);
        byteBuddyMockMaker.createMock(settings, invalidHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockClassCastException() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings<SomeOtherClass> settings = new MockCreationSettings<>(SomeOtherClass.class, null);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockInstantiationException() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        Class<SomeExceptionClass> exceptionClass = SomeExceptionClass.class;
        MockCreationSettings<SomeExceptionClass> settings = new MockCreationSettings<>(exceptionClass, null);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testDescribeClass() {
        Class<?> testClass = Object.class;
        String expected = "class 'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'";
        String result = ByteBuddyMockMaker.describeClass(testClass);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testInitializeClassInstantiator() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> objenesisClassLoaderClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoaderClass.getDeclaredConstructor(boolean.class);
        ClassInstantiator classInstantiator = (ClassInstantiator) usingClassCacheConstructor.newInstance(true);
        Assert.assertNotNull(classInstantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorMissingObjenesis() throws ClassNotFoundException {
        try {
            Class<?> objenesisClassLoaderClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
            Constructor<?> usingClassCacheConstructor = objenesisClassLoaderClass.getDeclaredConstructor(boolean.class);
            ClassInstantiator classInstantiator = (ClassInstantiator) usingClassCacheConstructor.newInstance(new GlobalConfiguration().enableClassCache());
        } catch (NoClassDefFoundError e) {
            throw new IllegalStateException("Mockito could not create mock: Objenesis is missing on the classpath.", e);
        }
    }
}