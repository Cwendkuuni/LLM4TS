package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoAnnotation;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.ReturnsDeepStubs;
import org.mockito.internal.stubbing.ReturnsEmptyValues;
import org.mockito.internal.util.GenericMetadataSupport;
import org.mockito.internal.util.reflection.GenericTypeImpl;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWithContext;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithDeepStubsForSimpleType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        assertSame(mock, answerWithDeepStubs(mock, Object.class));
    }

    @Test
    public void testAnswerWithDeepStubsForGenericInterface() {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        assertSame(mock, answerWithDeepStubs(mock, Map.class));
    }

    @Test
    public void testAnswerWithDeepStubsForGenericClass() {
        Object mock = mock(List.class, new ReturnsDeepStubs());
        assertSame(mock, answerWithDeepStubs(mock, List.class));
    }

    @Test
    public void testAnswerWithDeepStubsForNestedGenericInterface() {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Object nestedMock = mock(Number.class, new ReturnsDeepStubs());
        Map<Number, List<Number>> mapMock = (Map<Number, List<Number>>) mock;
        List<Number> listMock = mock(List.class, new ReturnsDeepStubs());
        mapMock.put(nestedMock, listMock);

        assertSame(nestedMock, answerWithDeepStubs(mapMock.get(nestedMock), Number.class));
        assertSame(listMock, answerWithDeepStubs(mapMock.get(nestedMock), Map.Entry.class));
        assertSame(listMock, answerWithDeepStubs(mapMock.get(nestedMock).getValue(), List.class));
        assertSame(nestedMock, answerWithDeepStubs(mapMock.get(nestedMock).getKey(), Number.class));
    }

    @Test
    public void testAnswerWithDeepStubsForNestedGenericClass() {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Object nestedMock = mock(Number.class, new ReturnsDeepStubs());
        Map<Map<Number, Number>, Number> mapMock = (Map<Map<Number, Number>, Number>) mock;
        Map<Number, Number> innerMapMock = mock(Map.class, new ReturnsDeepStubs());
        Number numberMock = mock(Number.class, new ReturnsDeepStubs());
        mapMock.put(innerMapMock, numberMock);

        assertSame(numberMock, answerWithDeepStubs(mapMock.get(innerMapMock), Number.class));
        assertSame(innerMapMock, answerWithDeepStubs(mapMock.get(innerMapMock), Map.class));
        assertSame(nestedMock, answerWithDeepStubs(innerMapMock.get(nestedMock), Number.class));
    }

    @Test
    public void testAnswerWithDeepStubsForNestedGenericInterfaceWithExtraInterfaces() {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Object nestedMock = mock(Number.class, new ReturnsDeepStubs());
        Map<Number, List<Number>> mapMock = (Map<Number, List<Number>>) mock;
        List<Number> listMock = mock(List.class, new ReturnsDeepStubs());
        mapMock.put(nestedMock, listMock);

        @SuppressWarnings("unchecked")
        AnswerWithContext<Map<Number, List<Number>>> answerWithContext = answerWithDeepStubs(mapMock, Map.class);

        assertSame(nestedMock, answerWithContext.getContext()[0]);
        assertSame(listMock, answerWithContext.getContext()[1]);
    }

    @Test
    public void testAnswerWithDeepStubsForNestedGenericClassWithExtraInterfaces() {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Object nestedMock = mock(Number.class, new ReturnsDeepStubs());
        Map<Map<Number, Number>, Number> mapMock = (Map<Map<Number, Number>, Number>) mock;
        Map<Number, Number> innerMapMock = mock(Map.class, new ReturnsDeepStubs());
        Number numberMock = mock(Number.class, new ReturnsDeepStubs());
        mapMock.put(innerMapMock, numberMock);

        @SuppressWarnings("unchecked")
        AnswerWithContext<Map<Map<Number, Number>, Number>> answerWithContext = answerWithDeepStubs(mapMock, Map.class);

        Map<Number, Number> innerMap = answerWithContext.getContext()[0];
        Number number = answerWithContext.getContext()[1];
        Number nestedNumber = innerMap.get(nestedMock);

        assertSame(nestedNumber, number);
        assertSame(nestedMock, innerMap.keySet().iterator().next());
    }

    private Object answerWithDeepStubs(Object mock, Class<?> returnType) {
        InvocationOnMock invocation = mock(InvocationOnMock.class, new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return ReturnsDeepStubs.this.answer(invocation);
            }
        });
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(MockitoAnnotation.findSpecificMethod(InvocationOnMock.class, returnType));
        return InvocationOnMock.class.cast(invocation.invokeMethod());
    }
}