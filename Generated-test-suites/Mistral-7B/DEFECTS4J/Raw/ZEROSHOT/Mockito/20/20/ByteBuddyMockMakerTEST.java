package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockHandler;
import org.mockito.quality.Strictness;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Rule
    public MockMaker mockMaker = new MockMaker() {
        @Override
        public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
            return new MockMakerMockHandler<T>(handler).getMock();
        }

        @Override
        public MockHandler getHandler(Object mock) {
            return null;
        }

        @Override
        public void resetMock(Object mock, MockHandler newHandler, MockCreationSettings settings) {
            // Empty implementation
        }
    };

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = Mockito.spy(byteBuddyMockMaker.initializeClassInstantiator());
        cachingMockBytecodeGenerator = Mockito.spy(byteBuddyMockMaker.cachingMockBytecodeGenerator);
    }

    @org.junit.Test
    public void testCreateMockWithValidSettingsAndHandler() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        Class<?> mockedType = Mockito.mock(Class.class);

        when(cachingMockBytecodeGenerator.get(Mockito.eq(mockedType), Mockito.any()))
                .thenReturn(mockedType);

        when(classInstantiator.instantiate(Mockito.eq(mockedType)))
                .thenReturn(mockedType);

        T mock = (T) byteBuddyMockMaker.createMock(new MockCreationSettings<T>() {
            @Override
            public Class<T> getTypeToMock() {
                return mockedType;
            }

            @Override
            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            @Override
            public SerializableMode getSerializableMode() {
                return SerializableMode.SERIALIZABLE;
            }

            @Override
            public Strictness getStrictness() {
                return Strictness.LENIENT;
            }
        }, handler);

        verify(handler).handle(Mockito.isA(Mockito.eq(mockedType)));

        // Test that the mock is returned as expected
        Mockito.verify(cachingMockBytecodeGenerator).get(Mockito.eq(mockedType), Mockito.any());
        Mockito.verify(classInstantiator).instantiate(Mockito.eq(mockedType));
        Mockito.verify(mock).equals(mockedType);

        // Test that no further interactions are made with the handler
        verifyNoMoreInteractions(handler);
    }

    @org.junit.Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidSerializableMode() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        Class<?> mockedType = Mockito.mock(Class.class);

        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
                throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
            }
        };

        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {
                @Override
                public Class<Object> getTypeToMock() {
                    return mockedType;
                }

                @Override
                public Class<?>[] getExtraInterfaces() {
                    return new Class[0];
                }

                @Override
                public SerializableMode getSerializableMode() {
                    return SerializableMode.ACROSS_CLASSLOADERS;
                }

                @Override
                public Strictness getStrictness() {
                    return Strictness.LENIENT;
                }
            }, handler);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (IllegalAccessException e) {
            throw e;
        }
    }

    @org.junit.Test
    public void testGetHandlerReturnsNullForInvalidMock() {
        Object mock = mock(Object.class);

        assertNull(byteBuddyMockMaker.getHandler(mock));
    }

    @org.junit.Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        Class<?> mockedType = mock(Class.class);
        MockHandler handler = Mockito.mock(MockHandler.class);

        byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings<Object>() {
            @Override
            public Class<Object> getTypeToMock() {
                return mockedType;
            }

            @Override
            public Class<?>[] getExtraInterfaces() {
                return new Class[0];
            }

            @Override
            public SerializableMode getSerializableMode() {
                return SerializableMode.SERIALIZABLE;
            }

            @Override
            public Strictness getStrictness() {
                return Strictness.LENIENT;
            }
        });

        verify(handler).handle(Mockito.isA(Mockito.eq(mockedType)));
    }
}