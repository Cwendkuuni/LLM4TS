package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.invocation.MockHandlerType.MOCKITO_JUNIT_RUNNER;
import static org.mockito.internal.util.StringJoiner.join;

import java.lang.reflect.Constructor;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public void handle(Object[] arguments, String methodName, Object target) {
                // Intentionally empty
            }
        };
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .withMockHandler(mockHandler)
                .build();
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler internalMockHandler = new InternalMockHandler() {
            @Override
            public MockHandler getMockHandler() {
                return new MockHandler() {
                    @Override
                    public void handle(Object[] arguments, String methodName, Object target) {
                        // Intentionally empty
                    }
                };
            }
        };
        MockCreationSettings<String> settings = new MockCreationSettings.Builder()
                .withTypeToMock(String.class)
                .withExtraInterfaces(String[].class)
                .withMockHandler(internalMockHandler)
                .build();
        String mock = byteBuddyMockMaker.createMock(settings, internalMockHandler);
        assertInstanceOf(String.class, mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public void handle(Object[] arguments, String methodName, Object target) {
                // Intentionally empty
            }
        };
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(Integer.class)
                .withMockHandler(mockHandler)
                .build();
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithInstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public void handle(Object[] arguments, String methodName, Object target) {
                // Intentionally empty
            }
        };
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .withMockHandler(mockHandler)
                .build();
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastExceptionDescription() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public void handle(Object[] arguments, String methodName, Object target) {
                // Intentionally empty
            }
        };
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(String.class)
                .withMockHandler(mockHandler)
                .build();
        throw new MockitoException(join(
                "ClassCastException occurred while creating the mockito mock :",
                "  class to mock : ",
                "'java.lang.String'",
                "  created class : ",
                "  proxy instance class : ",
                "  null",
                "  instance creation by : ",
                "  ByteBuddyMockMaker",
                "",
                "You might experience classloading issues, please ask the mockito mailing-list.",
                ""
        ), new ClassCastException(""));
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithInstantiationExceptionDescription() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public void handle(Object[] arguments, String methodName, Object target) {
                // Intentionally empty
            }
        };
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .withMockHandler(mockHandler)
                .build();
        throw new InstantiationException("Unable to create mock instance of type '" + Object.class.getName() + "'");
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object() {
            private final MockMethodInterceptor.MockAccess mockAccess = new MockMethodInterceptor.MockAccess() {
                private final InternalMockHandler internalMockHandler = new InternalMockHandler() {
                    private final MockHandler mockHandler = new MockHandler() {
                        @Override
                        public void handle(Object[] arguments, String methodName, Object target) {
                            // Intentionally empty
                        }
                    };

                    @Override
                    public MockHandler getMockHandler() {
                        return mockHandler;
                    }
                };
                @Override
                public InternalMockHandler getMockitoInterceptor() {
                    return mockAccess.getMockitoInterceptor();
                }
            };
            @Override
            public MockMethodInterceptor.MockAccess getMockitoInterceptor() {
                return mockAccess;
            }
        };
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object() {
            private final MockMethodInterceptor.MockAccess mockAccess = new MockMethodInterceptor.MockAccess() {
                private final InternalMockHandler internalMockHandler = new InternalMockHandler() {
                    private final MockHandler mockHandler = new MockHandler() {
                        @Override
                        public void handle(Object[] arguments, String methodName, Object target) {
                            // Intentionally empty
                        }
                    };

                    @Override
                    public MockHandler getMockHandler() {
                        return mockHandler;
                    }
                };
                @Override
                public InternalMockHandler getMockitoInterceptor() {
                    return mockAccess.getMockitoInterceptor();
                }
            };
            @Override
            public MockMethodInterceptor.MockAccess getMockitoInterceptor() {
                return mockAccess;
            }
        };
        InternalMockHandler newInternalMockHandler = new InternalMockHandler() {
            @Override
            public MockHandler getMockHandler() {
                return new MockHandler() {
                    @Override
                    public void handle(Object[] arguments, String methodName, Object target) {
                        // Intentionally empty
                    }
                };
            }
        };
        byteBuddyMockMaker.resetMock(mock, newInternalMockHandler, new MockCreationSettings.Builder()
                .withTypeToMock(Object.class)
                .withExtraInterfaces(new Class[0])
                .withMockHandler(newInternalMockHandler)
                .build());
    }

    @Test
    public void testDescribeClass() {
        assertEqauls("class java.lang.String, loaded by classloader : 'null'", byteBuddyMockMaker.describeClass(String.class));
        assertEqauls("class org.mockito.internal.creation.bytebuddy.ByteBuddyMockMakerTest$TestClass, loaded by classloader : 'null'", byteBuddyMockMaker.describeClass(TestClass.class));
        assertEqauls("null", byteBuddyMockMaker.describeClass(null));
    }

    private static class TestClass {
        // Empty class
    }

}