import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        // Given
        MockCreationSettings<String> settings = new MockCreationSettings<String>() {
            public Class<String> getTypeToMock() {
                return String.class;
            }

            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.NONE;
            }
        };
        MockHandler handler = new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        };

        // When
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String mock = mockMaker.createMock(settings, handler);

        // Then
        Assert.assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        // Given
        MockCreationSettings<String> settings = new MockCreationSettings<String>() {
            public Class<String> getTypeToMock() {
                return String.class;
            }

            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.ACROSS_CLASSLOADERS;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        };

        // When
        mockMaker.createMock(settings, handler);

        // Then - MockitoException is expected to be thrown
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastException() {
        // Given
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {
            public Class<Object> getTypeToMock() {
                return Object.class;
            }

            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.NONE;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        };

        // When
        mockMaker.createMock(settings, handler);

        // Then - MockitoException is expected to be thrown
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInstantiationException() {
        // Given
        MockCreationSettings<Exception> settings = new MockCreationSettings<Exception>() {
            public Class<Exception> getTypeToMock() {
                return Exception.class;
            }

            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.NONE;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        };

        // When
        mockMaker.createMock(settings, handler);

        // Then - MockitoException is expected to be thrown
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Given
        MockCreationSettings<String> settings = new MockCreationSettings<String>() {
            public Class<String> getTypeToMock() {
                return String.class;
            }

            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.NONE;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        String mock = mockMaker.createMock(settings, new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        });

        // When
        String result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Then
        Assert.assertEquals(mock, result);
    }

    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedTypeWithClassCastException() {
        // Given
        mockCreationSettings settings = new MockCreationSettings() {
            public Class getTypeToMock() {
                return String.class;
            }

            public Class[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.NONE;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(settings, new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        });

        // When
        mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Then - ClassCastException is expected to be thrown
    }

    @Test
    public void testGetHandler() {
        // Given
        InternalMockHandler handler = new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings<String>() {
            public Class<String> getTypeToMock() {
                return String.class;
            }

            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.NONE;
            }
        }, handler);

        // When
        MockHandler result = mockMaker.getHandler(mock);

        // Then
        Assert.assertEquals(result, handler);
    }

    @Test
    public void testResetMock() {
        // Given
        MockCreationSettings<String> settings = new MockCreationSettings<String>() {
            public Class<String> getTypeToMock() {
                return String.class;
            }

            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            public SerializableMode getSerializableMode() {
                return SerializableMode.NONE;
            }
        };
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler handler = new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        };
        String mock = mockMaker.createMock(settings, handler);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        MockMethodInterceptor mockitoInterceptor = mockAccess.getMockitoInterceptor();
        MockMethodInterceptor.MockAccess newMockAccess = null;

        // When
        mockMaker.resetMock(mock, new InternalMockHandler() {
            public Object handle() throws Throwable {
                return null;
            }
        }, settings);
        if (mock instanceof MockMethodInterceptor.MockAccess) {
            newMockAccess = (MockMethodInterceptor.MockAccess) mock;
        }

        // Then
        Assert.assertNotEquals(mockitoInterceptor, newMockAccess.getMockitoInterceptor());
    }

    @Test(expected = MockitoException.class)
    public void testResetMockWithNonMockAccessObject() {
        // Given
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // When
        mockMaker.resetMock(new Object(), null, null);

        // Then - MockitoException is expected to be thrown
    }

    @Test
    public void testInitializeClassInstantiator() {
        // Given
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // When
        ClassInstantiator result = mockMaker.initializeClassInstantiator();

        // Then
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWhenObjenesisIsMissing() throws Exception {
        // Given
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        Class<?> objenesisClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis", false, previous);
        ClassLoader testClassLoader = new CustomClassLoader(previous.getParent());
        Thread.currentThread().setContextClassLoader(testClassLoader);

        try {
            // When
            mockMaker.initializeClassInstantiator();
        } finally {
            // Finally
            Thread.currentThread().setContextClassLoader(previous);
        }

        // Then - IllegalStateException is expected to be thrown
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNonInternalMockHandlerObject() {
        // Given
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        // When
        mockMaker.asInternalMockHandler(null);

        // Then - MockitoException is expected to be thrown
    }

    

    // Public methods in ByteBuddyMockMaker are private, so no additional test cases can be generated.

    
}