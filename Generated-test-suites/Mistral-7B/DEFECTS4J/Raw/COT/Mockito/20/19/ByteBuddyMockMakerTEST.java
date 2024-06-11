import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.ClassInstantiatorUsingObjenesis;
import org.mockito.internal.util.GlobalConfiguration;
import org.mockito.internal.util.MockCreationSettings;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.StringJoiner;
import org.mockito.invocation.MockMethodInterceptor;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockCreationSettings<String> settings = new MockCreationSettingsImpl<>("org.mockito.test.MockClass", "mockName");
        Class<String> typeToMock = String.class;

        MockHandler handler = Mockito.mock(MockHandler.class);
        String mockedInstance = byteBuddyMockMaker.createMock(settings, handler);
        assertEquals("mockName", mockedInstance);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Serializable> settings = new MockCreationSettingsImpl<>("org.mockito.test.MockSerializableClass", "mockName");
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
    }

    @Test
    public void testCreateMockClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettingsImpl<>("org.mockito.test.MockClass", "mockName");
        Object mockedInstance = byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));

        try {
            Assert.assertNotNull(mockedInstance);
            Assert.fail();
        } catch (ClassCastException e) {
            // expected
        }
    }

    @Test
    public void testCreateMockInstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettingsImpl<>("org.mockito.test.MockClassWithNoDefaultConstructor", "mockName");
        byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNull(handler);

        MockCreationSettings<Object> settings = new MockCreationSettingsImpl<>("org.mockito.test.MockClass", "mockName");
        Object mockedInstance = byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockedInstance;
        handler = byteBuddyMockMaker.getHandler(mockAccess);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        MockCreationSettings<Object> settings = new MockCreationSettingsImpl<>("org.mockito.test.MockClass", "mockName");
        Object mockedInstance = byteBuddyMockMaker.createMock(settings, handler);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockedInstance;

        byteBuddyMockMaker.resetMock(mockAccess, Mockito.mock(MockHandler.class), settings);
        handler = byteBuddyMockMaker.getHandler(mockAccess);
        assertNotNull(handler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
            Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
            byteBuddyMockMaker.classInstantiator = (ClassInstantiator) usingClassCacheConstructor.newInstance(true);
            assertTrue(byteBuddyMockMaker.classInstantiator instanceof ClassInstantiatorUsingObjenesis);
        } catch (Throwable throwable) {
            // MockitoException cannot be used at this point as we are early in the classloading chain and necessary dependencies may not yet be loadable by the classloader
            fail("InitializeClassInstantiator failed.");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.initializeClassInstantiator();
    }
}