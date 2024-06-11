import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.StringJoiner;
import org.mockito.invocation.MockHandlerImpl;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Type mockType = Mockito.mock(Type.class);

        try {
            Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {
                @Override
                public Class<?> getTypeToMock() {
                    return (Class<?>) mockType;
                }

                @Override
                public Class<?>[] getExtraInterfaces() {
                    return new Class[0];
                }

                @Override
                public SerializableMode getSerializableMode() {
                    return SerializableMode.DEFAULT;
                }
            }, mockHandler);

            Assert.assertNotNull(mock);
            Assert.assertSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
        } catch (MockitoException e) {
            Assert.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMockSerializationAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Type mockType = Mockito.mock(Type.class);

        try {
            ByteBuddyMockMaker.SerializableMode serializableMode = ByteBuddyMockMaker.SerializableMode.ACROSS_CLASSLOADERS;
            Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {
                @Override
                public Class<?> getTypeToMock() {
                    return (Class<?>) mockType;
                }

                @Override
                public Class<?>[] getExtraInterfaces() {
                    return new Class[0];
                }

                @Override
                public SerializableMode getSerializableMode() {
                    return serializableMode;
                }
            }, mockHandler);

            Assert.fail(); // This test should fail with MockitoException
        } catch (MockitoException e) {
            Assert.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Object mock = Mockito.mock(Object.class);
        Mockito.doReturn(mockHandler).when(mock).getMockitoInterceptor();

        Assert.assertSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Object mock = Mockito.mock(Object.class);
        Mockito.doReturn(mockHandler).when(mock).getMockitoInterceptor();

        byteBuddyMockMaker.resetMock(mock, mockHandler, new MockCreationSettings<Object>() {
            @Override
            public Class<?> getTypeToMock() {
                return Mockito.mock(Type.class);
            }

            @Override
            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            @Override
            public SerializableMode getSerializableMode() {
                return SerializableMode.DEFAULT;
            }
        });

        Assert.assertSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockInvalidHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        Object mock = Mockito.mock(Object.class);
        MockHandler invalidMockHandler = new Object();

        byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {}, invalidMockHandler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        try {
            byteBuddyMockMaker.initializeClassInstantiator();
            Assert.fail(); // this test should not reach here
        } catch (Exception e) {
            // Expected exception
        }

        try {
            ByteBuddyMockMaker.ClassInstantiator classInstantiator = byteBuddyMockMaker.initializeClassInstantiator();
            Assert.assertNotNull(classInstantiator);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidType() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Object invalidType = new Object() {
        };

        byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {
            @Override
            public Class<?> getTypeToMock() {
                return invalidType;
            }

            @Override
            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            @Override
            public SerializableMode getSerializableMode() {
                return SerializableMode.DEFAULT;
            }
        }, mockHandler);
    }
}