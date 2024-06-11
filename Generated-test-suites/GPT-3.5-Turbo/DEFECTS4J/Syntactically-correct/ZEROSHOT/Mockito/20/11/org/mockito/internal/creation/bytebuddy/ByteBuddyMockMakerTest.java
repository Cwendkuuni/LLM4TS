import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_SerializationAcrossClassloaders_NotSupported() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = new MockCreationSettings<>(MockCreationSettings.class);
        MockHandler handler = org.mockito.internal.util.MockUtil.getMockHandler(settings);

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assert e.getMessage().equals("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
        }
    }

    @Test
    public void testCreateMock_CastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = new MockCreationSettings<>(MockCreationSettings.class);
        MockHandler handler = org.mockito.internal.util.MockUtil.getMockHandler(settings);

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assert e.getMessage().equals(join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : " + describeClass(org.mockito.internal.creation.mock.MockCreationSettings.class),
                    "  created class : " + describeClass(org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker.class),
                    "  proxy instance class : null",
                    "  instance creation by : " + org.mockito.internal.creation.instance.ClassInstantiator.class.getSimpleName(),
                    "",
                    "You might experience classloading issues, please ask the mockito mailing-list.",
                    ""
            ));
        }
    }

    @Test
    public void testCreateMock_Exception() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = new MockCreationSettings<>(MockCreationSettings.class);
        MockHandler handler = org.mockito.internal.util.MockUtil.getMockHandler(settings);

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assert e.getMessage().equals("Unable to create mock instance of type 'MockCreationSettings'");
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<?> settings = new MockCreationSettings<>(MockCreationSettings.class);
        Object mock = new Object();

        assert mockMaker.ensureMockIsAssignableToMockedType(settings, mock) == mock;
    }

    @Test
    public void testDescribeClass_Type() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class<?> type = MockCreationSettings.class;

        assert mockMaker.describeClass(type).equals("'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'");
    }

    @Test
    public void testDescribeClass_Instance() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = new Object();

        assert mockMaker.describeClass(instance).equals(describeClass(instance.getClass()));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mock = new MockMethodInterceptor.MockAccess() {
            @Override
            public MockMethodInterceptor getMockitoInterceptor() {
                return null;
            }

            @Override
            public void setMockitoInterceptor(MockMethodInterceptor mockitoInterceptor) {

            }
        };

        assert mockMaker.getHandler(mock) == null;
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = new MockMethodInterceptor(null, null);
        MockCreationSettings settings = new MockCreationSettings(MockCreationSettings.class);

        mockMaker.resetMock(mock, newHandler, settings);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ClassInstantiator classInstantiator = new ByteBuddyMockMaker().initializeClassInstantiator();

        assert classInstantiator != null;
    }

    private String describeClass(Class<?> type) {
        return type == null ? "null" : "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
    }
}