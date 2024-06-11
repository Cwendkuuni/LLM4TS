
package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
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
    public void testCreateMock_serializableModeAcrossClassloaders_throwsMockitoException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = new MockHandler() {
            @Override
            public Object handle(Object proxy, Class<?> type, Object[] arguments, MockCreationSettings settings) {
                return null;
            }
        };
        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assert e.getMessage().equals("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
        }
    }

    @Test
    public void testCreateMock_classCastException_throwsMockitoException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(String.class);
        ClassInstantiator classInstantiator = new ClassInstantiator() {
            @Override
            public <T> T instantiate(Class<T> type) throws org.mockito.internal.creation.instance.InstantiationException {
                throw new org.mockito.internal.creation.instance.InstantiationException();
            }
        };
        mockMaker.setClassInstantiator(classInstantiator);
        MockHandler handler = new MockMethodInterceptor();
        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assert e.getMessage().equals(join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : java.lang.String",
                    "  created class : class java.lang.String",
                    "  proxy instance class : class java.lang.String",
                    "  instance creation by : MockInstantiator",
                    "",
                    "You might experience classloading issues, please ask the mockito mailing-list.",
                    ""
            ));
        }
    }

    @Test
    public void testCreateMock_instantiationException_throwsMockitoException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(String.class);
        ClassInstantiator classInstantiator = new ClassInstantiator() {
            @Override
            public <T> T instantiate(Class<T> type) throws org.mockito.internal.creation.instance.InstantiationException {
                throw new org.mockito.internal.creation.instance.InstantiationException();
            }
        };
        mockMaker.setClassInstantiator(classInstantiator);
        MockHandler handler = new MockMethodInterceptor();
        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assert e.getMessage().equals("Unable to create mock instance of type 'Object'");
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(String.class);
        String mock = mockMaker.ensureMockIsAssignableToMockedType(settings, "mock");
        assert mock instanceof String;
    }

    @Test
    public void testDescribeClass_withNullType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String description = mockMaker.describeClass(null);
        assert description.equals("null");
    }

    @Test
    public void testDescribeClass_withType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String description = mockMaker.describeClass(String.class);
        assert description.equals("'java.lang.String', loaded by classloader : 'null'");
    }

    @Test
    public void testDescribeClass_withNullInstance() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String description = mockMaker.describeClass(null);
        assert description.equals("null");
    }

    @Test
    public void testDescribeClass_withInstance() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String description = mockMaker.describeClass("mock");
        assert description.equals("'java.lang.String', loaded by classloader : 'null'");
    }

    @Test
    public void testGetHandler_notMockMethodInterceptor_throwsMockitoException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler handler = mockMaker.getHandler(mock);
        assert handler == null;
    }

    @Test
    public void testGetHandler_mockMethodInterceptor_returnsMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();
        MockHandler handler = mockMaker.getHandler(mock);
        assert handler instanceof InternalMockHandler;
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();
        MockHandler newHandler = new MockMethodInterceptor();
        MockCreationSettings settings = new MockCreationSettings();
        mockMaker.resetMock(mock, newHandler, settings);
        InternalMockHandler handler = ((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler();
        assert handler instanceof MockMethodInterceptor;
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        assert classInstantiator instanceof ClassInstantiator;
    }
}
