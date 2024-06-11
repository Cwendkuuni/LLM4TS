package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.GenericMetadataSupport;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport.RawGenericType;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWithContext;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReturnsDeepStubsTest {

    private static final Class<?>[] EMPTY_TYPES = new Class[0];
    private static final Class<?>[] ARRAY_TYPE = new Class[]{Integer[].class};
    private static final Class<?>[] ARRAY_GENERIC_TYPE = new Class[]{Integer[].class, List.class};
    private static final Class<?>[] GENERIC_TYPE = new Class[]{Map.class, Map.class};
    private static final Class<?>[] GENERIC_NESTED_TYPE = new Class[]{Map.class, Map.class, Number.class, Number.class};
    private static final Class<?>[] GENERIC_NESTED_TYPE_WITH_ARRAY = new Class[]{Map.class, Map.class, Number[].class, Number[].class};

    private GenericMetadataSupport returnTypeMetadata;
    private Object mock;
    private InvocationContainerImpl container;

    @Before
    public void setup() {
        returnTypeMetadata = new GenericMetadataSupport(Map.class);
        mock = mock(Map.class, new ReturnsDeepStubs());
        container = new InvocationContainerImpl(new CreationSettings(Map.class, Collections.emptyList()));
    }

    @Test
    public void testAnswer() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, EMPTY_TYPES), EMPTY_TYPES));
        assertNotNull(answer);
    }

    @Test
    public void testAnswerWithEmptyType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, EMPTY_TYPES), EMPTY_TYPES));
        assertNotNull(answer);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testAnswerWithSameType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, Map.class), Map.class));
        assertNotNull(answer);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testAnswerWithDifferentType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, List.class), Map.class));
        assertNotNull(answer);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testAnswerWithPrimitiveTypes() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, int[].class), int[].class));
        assertNotNull(answer);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testAnswerWithArrayType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, ARRAY_TYPE), ARRAY_TYPE));
        assertNotNull(answer);
        Object nestedMock = mock(Integer.class, new ReturnsDeepStubs());
        when(mock.get(Collections.singletonMap(Integer.valueOf(1), Collections.singleton(nestedMock)))).thenReturn(nestedMock);
        verify(mock).get(Collections.singletonMap(Integer.valueOf(1), Collections.singleton(nestedMock)));
    }

    @Test
    public void testAnswerWithGenericArrayType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, ARRAY_GENERIC_TYPE), ARRAY_GENERIC_TYPE));
        assertNotNull(answer);
        Object nestedMock = mock(Number.class, new ReturnsDeepStubs());
        when(mock.get(Collections.singletonMap(Collections.singletonMap(Integer.valueOf(1), Collections.singleton(nestedMock)), Collections.singleton(Integer.valueOf(0))))).thenReturn(nestedMock);
        verify(mock).get(Collections.singletonMap(Collections.singletonMap(Integer.valueOf(1), Collections.singleton(nestedMock)), Collections.singleton(Integer.valueOf(0))));
    }

    @Test
    public void testAnswerWithGenericType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, GENERIC_TYPE[0], GENERIC_TYPE[1]), GENERIC_TYPE));
        assertNotNull(answer);
        Object nestedMapMock = mock(Map.class, new ReturnsDeepStubs());
        when(mock.entrySet().iterator().next().getValue()).thenReturn(nestedMapMock);
        Object nestedNumberMock = mock(Number.class, new ReturnsDeepStubs());
        when(nestedMapMock.entrySet().iterator().next().getValue()).thenReturn(nestedNumberMock);
        verify(mock).entrySet().iterator().next().getValue();
        verify(nestedMapMock).entrySet().iterator().next().getValue();
        verify(nestedNumberMock).intValue();
    }

    @Test
    public void testAnswerWithNestedGenericType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, GENERIC_NESTED_TYPE[0], GENERIC_NESTED_TYPE[1], Number.class, Number.class), GENERIC_NESTED_TYPE));
        assertNotNull(answer);
        Object nestedMapMock1 = mock(Map.class, new ReturnsDeepStubs());
        Object nestedMapMock2 = mock(Map.class, new ReturnsDeepStubs());
        Object nestedNumberMock1 = mock(Number.class, new ReturnsDeepStubs());
        Object nestedNumberMock2 = mock(Number.class, new ReturnsDeepStubs());
        when(mock.entrySet().iterator().next().getValue()).thenReturn(nestedMapMock1);
        when(nestedMapMock1.entrySet().iterator().next().getValue()).thenReturn(nestedNumberMock1);
        when(nestedMapMock1.entrySet().iterator().next().getKey()).thenReturn(Integer.valueOf(1));
        when(nestedMapMock2.entrySet().iterator().next().getValue()).thenReturn(nestedNumberMock2);
        when(nestedMapMock2.entrySet().iterator().next().getKey()).thenReturn(Integer.valueOf(2));
        verify(mock).entrySet().iterator().next().getValue();
        verify(nestedMapMock1).entrySet().iterator().next().getValue();
        verify(nestedMapMock1).entrySet().iterator().next().getKey();
        verify(nestedNumberMock1).intValue();
        verify(nestedMapMock2).entrySet().iterator().next().getValue();
        verify(nestedMapMock2).entrySet().iterator().next().getKey();
        verify(nestedNumberMock2).intValue();
    }

    @Test
    public void testAnswerWithNestedArrayGenericType() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, GENERIC_NESTED_TYPE_WITH_ARRAY[0], GENERIC_NESTED_TYPE_WITH_ARRAY[1], Number[].class, Number[].class), GENERIC_NESTED_TYPE_WITH_ARRAY));
        assertNotNull(answer);
        Object nestedMapMock1 = mock(Map.class, new ReturnsDeepStubs());
        Object nestedMapMock2 = mock(Map.class, new ReturnsDeepStubs());
        Object nestedNumberMock1 = mock(Number.class, new ReturnsDeepStubs());
        Object nestedNumberMock2 = mock(Number.class, new ReturnsDeepStubs());
        when(mock.get(Collections.singletonMap(Collections.singletonMap(Integer.valueOf(1), Collections.singleton(nestedMapMock1)), Collections.singleton(Integer.valueOf(0)))).get(Collections.singletonMap(Collections.singletonMap(Integer.valueOf(1), Collections.singleton(nestedMapMock2)), Collections.singleton(Integer.valueOf(0))))).thenReturn(Arrays.asList(nestedNumberMock1, nestedNumberMock2));
        when(nestedMapMock1.get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock2))).thenReturn(Arrays.asList(nestedNumberMock1, nestedNumberMock2));
        when(nestedMapMock1.get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock2))).thenReturn(Arrays.asList(nestedNumberMock1, nestedNumberMock2));
        when(nestedMapMock1.get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock2))).thenReturn(Arrays.asList(nestedNumberMock1, nestedNumberMock2));
        when(nestedMapMock2.get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock1))).thenReturn(Arrays.asList(nestedNumberMock1, nestedNumberMock2));
        when(nestedMapMock2.get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock1))).thenReturn(Arrays.asList(nestedNumberMock1, nestedNumberMock2));
        when(nestedMapMock2.get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock1))).thenReturn(Arrays.asList(nestedNumberMock1, nestedNumberMock2));
        Object nestedArrayMock = mock(Number[].class, new ReturnsDeepStubs());
        when(nestedNumberMock1.getClass()).thenReturn(Number[].class);
        when(nestedNumberMock1.getClass()).thenReturn(Number[].class);
        when(nestedNumberMock1.getClass()).thenReturn(Number[].class);
        when(nestedNumberMock2.getClass()).thenReturn(Number[].class);
        when(nestedNumberMock2.getClass()).thenReturn(Number[].class);
        when(nestedNumberMock2.getClass()).thenReturn(Number[].class);
        when(nestedNumberMock1.getClass()).thenReturn(nestedArrayMock.getClass());
        when(nestedNumberMock2.getClass()).thenReturn(nestedArrayMock.getClass());
        verify(mock).get(Collections.singletonMap(Collections.singletonMap(Integer.valueOf(1), nestedMapMock1), Collections.singleton(Integer.valueOf(0))).get(Collections.singletonMap(Collections.singletonMap(Integer.valueOf(1), nestedMapMock2), Collections.singleton(Integer.valueOf(0)))));
        verify(nestedMapMock1).get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock2));
        verify(nestedMapMock1).get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock2));
        verify(nestedMapMock1).get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock2));
        verify(nestedMapMock2).get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock1));
        verify(nestedMapMock2).get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock1));
        verify(nestedMapMock2).get(Collections.singletonMap(Integer.valueOf(1), nestedMapMock1));
        verify(nestedArrayMock, atLeastOnce()).getClass();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testAnswerWithTwoInvocations() throws Exception {
        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new Method(null, Map.class, Map.class), Map.class));
        assertNotNull(answer);
        Object nestedMapMock1 = mock(Map.class, new ReturnsDeepStubs());
        Object nestedMapMock2 = mock(Map.class, new ReturnsDeepStubs());
        when(mock.entrySet().iterator().next().getValue()).thenReturn(nestedMapMock1);
        when(nestedMapMock1.entrySet().iterator().next().getValue()).thenReturn(nestedMapMock2);
        verify(mock).entrySet().iterator().next().getValue();
        verify(nestedMapMock1).entrySet().iterator().next().getValue();
        verify(nestedMapMock2).isEmpty();
        verify(nestedMapMock2).isEmpty();
    }
}