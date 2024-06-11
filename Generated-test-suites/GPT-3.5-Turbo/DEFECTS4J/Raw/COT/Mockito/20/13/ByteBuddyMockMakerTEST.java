
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() throws Exception {
        MockCreationSettings<String> settings = new MockCreationSettings<>();
        MockHandler handler = new MockHandler() {
            public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableMode() throws Exception {
        MockCreationSettings<String> settings = new MockCreationSettings<>();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = new MockHandler() {
            public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastException() throws Exception {
        MockCreationSettings<String> settings = new MockCreationSettings<>();
        settings.setTypeToMock(Integer.class);
        MockHandler handler = new MockHandler() {
            public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInstantiationException() throws Exception {
        MockCreationSettings<String> settings = new MockCreationSettings<>();
        settings.setTypeToMock(AbstractList.class);
        MockHandler handler = new MockHandler() {
            public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() throws Exception {
        MockCreationSettings<String> settings = new MockCreationSettings<>();
        settings.setTypeToMock(String.class);
        String mock = "mock";
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test
    public void testGetHandler() throws Exception {
        Object mock = new Object();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.getHandler(mock);
    }

    @Test
    public void testResetMock() throws Exception {
        Object mock = new Object();
        MockHandler newHandler = new MockHandler() {
            public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.resetMock(mock, newHandler, settings);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator() throws Exception {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration.enableClassCache();
        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
        usingClassCacheConstructor.setAccessible(true);
        ClassInstantiator instantiator = (ClassInstantiator) usingClassCacheConstructor.newInstance(globalConfiguration.enableClassCache());
        MockitoAssert.INSTANCE.assertNotEquals(instantiator, classInstantiator);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithCustomMockHandler() throws Exception {
        MockHandler handler = new MockHandler() {
            public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.asInternalMockHandler(handler);
    }

    @Test
    public void testAsInternalMockHandlerWithInternalMockHandler() throws Exception {
        InternalMockHandler handler = new InternalMockHandler() {
            public Object handle(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }

            public MockCreationSettings getMockSettings() {
                return null;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.asInternalMockHandler(handler);
    }
}
