package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithDeepStubForBasicClass() {
        interface TestInterface {
            int method();
        }

        TestInterface mock = mock(TestInterface.class, new ReturnsDeepStubs());
        verify(mock).method();
        assertEquals(1, mock.method());
    }

    @Test
    public void testAnswerWithDeepStubForGenericClass() {
        interface TestGenericInterface<T> {
            T method(T arg);
        }

        TestGenericInterface<Integer> mock = mock(TestGenericInterface.class, new ReturnsDeepStubs());
        verify(mock).method(ArgumentMatchers.anyInt());
        TestGenericInterface<String> anotherMock = mock(TestGenericInterface.class, new ReturnsDeepStubs());
        assertEquals("test", anotherMock.method("test"));

        TestGenericInterface<String> mockWithStub = mock(TestGenericInterface.class, new Answers.Returns(new String("test")));
        TestGenericInterface<Integer> anotherMockWithStub = mock(TestGenericInterface.class, new Answers.Returns(1));
        assertEquals("test", mock.method("test"));
        assertEquals(1, anotherMock.method(1));
        assertEquals("test", mockWithStub.method("test"));
        assertEquals(1, anotherMockWithStub.method(1));
        assertEquals("test", mock.method(new String("test")));
        assertEquals(1, mock.method(1));

        TestGenericInterface<Integer> newMock = mock(TestGenericInterface.class, new ReturnsDeepStubs());
        TestGenericInterface<String> newAnotherMock = mock(TestGenericInterface.class, new ReturnsDeepStubs());
        TestGenericInterface<List<Integer>> newListMock = mock(TestGenericInterface.class, new ReturnsDeepStubs());
        TestGenericInterface<Map<String, TestGenericInterface<Integer>>> newMapMock = mock(TestGenericInterface.class, new ReturnsDeepStubs());

        TestGenericInterface<List<String>> listMock = mock(TestGenericInterface.class, new Answers.Returns(Collections.singletonList("test")));
        TestGenericInterface<Map<String, TestGenericInterface<Integer>>> mapMock = mock(TestGenericInterface.class, new Answers.Returns(Collections.singletonMap("test", new TestGenericInterface<Integer>() {
            @Override
            public Integer method(Integer arg) {
                return 1;
            }
        })));

        assertEquals(newMock, newMock.method(newMock));
        assertEquals(newAnotherMock, newAnotherMock.method(newAnotherMock));
        assertEquals(newListMock, newListMock.method(newListMock));
        assertEquals(newMapMock, newMapMock.method(newMapMock));
        assertEquals(listMock, newListMock.method(listMock));
        assertEquals(mapMock, newMapMock.method(mapMock));

        TestGenericInterface<List<String>> listMockWithStub = mock(TestGenericInterface.class, new Answers.Returns(Arrays.asList("test1", "test2")));
        assertEquals(Arrays.asList("test1", "test2"), listMockWithStub.method(listMockWithStub));

        TestGenericInterface<Map<String, TestGenericInterface<Integer>>> mapMockWithStub = mock(TestGenericInterface.class, new Answers.Returns(new TestGenericInterface<Map<String, TestGenericInterface<Integer>>>() {
            @Override
            public Map<String, TestGenericInterface<Integer>> method(Map<String, TestGenericInterface<Integer>> arg) {
                return arg;
            }
        }));

        assertEquals(mapMockWithStub, mapMockWithStub.method(mapMockWithStub));
    }

    @Test
    public void testAnswerWithDeepStubForNestedGenericTypes() {
        interface NestedGenericInterface<X, Y> {
            X method1(Y arg);
            Y method2();
        }

        NestedGenericInterface<String, Integer> mock = mock(NestedGenericInterface.class, new ReturnsDeepStubs());
        assertEquals("test", mock.method1(1));
        assertEquals(1, mock.method2());

        NestedGenericInterface<String, String> anotherMock = mock(NestedGenericInterface.class, new ReturnsDeepStubs());
        assertEquals("test", anotherMock.method1("test"));
        assertEquals("test", anotherMock.method2());

        NestedGenericInterface<String, String> newMock = mock(NestedGenericInterface.class, new ReturnsDeepStubs());
        assertEquals(newMock, newMock.method1(newMock));
        assertEquals(newMock, newMock.method2());
    }

    @Test
    public void testAnswerWithDeepStubForArray() {
        interface TestInterface {
            int[] method();
        }

        TestInterface mock = mock(TestInterface.class, new ReturnsDeepStubs());
        verify(mock).method();
        int[] result = mock.method();
        assertArrayEquals(new int[]{1, 2, 3}, result);

        TestInterface mockWithStub = mock(TestInterface.class, new Answers.Returns(new int[]{1, 2, 3}));
        int[] resultFromStub = mockWithStub.method();
        assertArrayEquals(new int[]{1, 2, 3}, resultFromStub);

        TestInterface newMock = mock(TestInterface.class, new ReturnsDeepStubs());
        int[] resultFromNewMock = newMock.method();
        assertEquals(newMock, resultFromNewMock[0]);
    }

    @Test
    public void testAnswerWithDeepStubForCollection() {
        interface TestInterface<T> {
            List<T> method();
        }

        TestInterface<String> mock = mock(TestInterface.class, new ReturnsDeepStubs());
        verify(mock).method();
        List<String> result = mock.method();
        assertEquals(Arrays.asList("test1", "test2"), result);

        TestInterface<String> mockWithStub = mock(TestInterface.class, new Answers.Returns(Arrays.asList("test1", "test2")));
        List<String> resultFromStub = mockWithStub.method();
        assertEquals(Arrays.asList("test1", "test2"), resultFromStub);

        TestInterface<List<String>> newMock = mock(TestInterface.class, new ReturnsDeepStubs());
        List<List<String>> resultFromNewMock = newMock.method();
        assertEquals(newMock, resultFromNewMock.get(0));
    }

    @Test
    public void testAnswerWithDeepStubForMap() {
        interface TestInterface<K, V> {
            Map<K, V> method();
        }

        TestInterface<String, Integer> mock = mock(TestInterface.class, new ReturnsDeepStubs());
        verify(mock).method();
        Map<String, Integer> result = mock.method();
        assertEquals(Collections.singletonMap("test", 1), result);

        TestInterface<String, Integer> mockWithStub = mock(TestInterface.class, new Answers.Returns(Collections.singletonMap("test", 1)));
        Map<String, Integer> resultFromStub = mockWithStub.method();
        assertEquals(Collections.singletonMap("test", 1), resultFromStub);

        TestInterface<Map<String, Integer>, String> newMock = mock(TestInterface.class, new ReturnsDeepStubs());
        Map<Map<String, Integer>, String> resultFromNewMock = newMock.method();
        assertEquals(newMock, resultFromNewMock.get(Collections.singletonMap("test", 1)));
    }
}