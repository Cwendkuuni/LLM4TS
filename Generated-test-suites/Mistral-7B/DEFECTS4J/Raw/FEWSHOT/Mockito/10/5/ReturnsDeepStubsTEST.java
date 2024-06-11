package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{Integer.class, String.class};
            }

            @Override
            public Type getDeclaringType() {
                return List.class;
            }

            @Override
            public Type getGenericComponentType() {
                return Integer.class;
            }

            @Override
            public String getName() {
                return "List";
            }
        };

        Object mock = mock(List.class, new ReturnsDeepStubs());

        List<Integer> list = (List<Integer>) mock;
        list.add(1);
        list.add("foo"); // should throw error

        assertSame(mock, list.get(0));
        assertSame(mock, list.get(1));

        Mockito.verify(mock).add(Mockito.eq(1));
        Mockito.verify(mock).add(Mockito.eq("foo"));
    }

    @Test
    public void testDeepStubbing() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        Object deepStub = mock(String.class, new ReturnsDeepStubs());

        OngoingStubbing<Object> deepStubbing = Mockito.doReturn(deepStub).when(mock);

        String result = (String) mock.toString();

        assertSame(deepStub, result);
        deepStubbing.thenReturn("bar");
        assertSame("bar", mock.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedType() {
        Object mock = mock(IOException.class, new ReturnsDeepStubs());
        mock.toString();
    }

    @Test
    public void testAnswerWithExtraInterfaces() {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{Integer.class, String.class};
            }

            @Override
            public Type getDeclaringType() {
                return List.class;
            }

            @Override
            public Type[] getRawExtraInterfaces() {
                return new Type[]{Collection.class};
            }

            @Override
            public String getName() {
                return "List";
            }
        };

        Object mock = mock(List.class, new ReturnsDeepStubs());

        List<Integer> list = (List<Integer>) mock;
        list.add(1);
        list.add("foo"); // should throw error

        Collection<Integer> collection = (Collection<Integer>) mock;
        collection.add(1);

        assertSame(mock, list.get(0));
        assertSame(mock, list.get(1));
        assertSame(mock, collection.iterator().next());

        Mockito.verify(mock).add(Mockito.eq(1));
        Mockito.verify(mock).add(Mockito.eq("foo"));
        Mockito.verify(mock).add(Mockito.eq(1));
    }

    @Test
    public void testAnswerWithExtraInterfacesAndGenericComponentType() {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{Integer[].class, String.class};
            }

            @Override
            public Type getDeclaringType() {
                return List.class;
            }

            @Override
            public Type[] getRawExtraInterfaces() {
                return new Type[]{Collection.class, Cloneable.class};
            }

            @Override
            public Type getGenericComponentType() {
                return Integer[].class;
            }

            @Override
            public String getName() {
                return "List";
            }
        };

        Object mock = mock(List.class, new ReturnsDeepStubs());

        List<Integer[]> list = (List<Integer[]>) mock;
        Integer[] array = new Integer[]{1, 2, 3};
        list.add(array);
        list.add(new Integer[]{1, 2, 3, 4});

        Collection<Integer[]> collection = (Collection<Integer[]>) mock;
        collection.add(array);

        Cloneable cloneable = (Cloneable) mock;
        Integer[] clonedArray = (Integer[]) cloneable.clone();

        assertSame(mock, list.get(0));
        assertSame(mock, list.get(1));
        assertSame(mock, collection.iterator().next());
        assertSame(mock, clonedArray);

        Mockito.verify(mock).add(Mockito.eq(array));
        Mockito.verify(mock).add(Mockito.eq(new Integer[]{1, 2, 3, 4}));
        Mockito.verify(mock).add(Mockito.eq(array));
    }
}