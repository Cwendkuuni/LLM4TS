package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

    private static final Class<?> GENERICS_NEST_CLASS = GenericsNest.class;
    private static final Class<?> NUMBER_CLASS = Number.class;
    private static final Class<?> SET_CLASS = Set.class;

    private ReturnsDeepStubs returnsDeepStubs;
    private GenericsNest mockGenericsNest;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mockGenericsNest = mock(GENERICS_NEST_CLASS, withSettings().defaultAnswer(returnsDeepStubs));
    }

    @Test
    public void testAnswer() throws Exception {
        when(mockGenericsNest.entrySet()).thenReturn(entrySetMock);
        when(entrySetMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.next()).thenReturn(valueMock);
        when(valueMock.iterator()).thenReturn(nestedIteratorMock);
        when(nestedIteratorMock.next()).thenReturn(numberMock);

        assertSame(numberMock, mockGenericsNest.entrySet().iterator().next().getValue().iterator().next());
    }

    @Test
    public void testDeepStub() throws Exception {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class, Collections.class);

        Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        Mockito.verify(invocation).getMock();
        assertSame(deepStub, mock(List.class, withSettings().defaultAnswer(new ReturnsDeepListAnswers())));
    }

    @Test
    public void testNewDeepStubMock() throws Exception {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class, Set.class);
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        assertSame(Map.class, mock.getClass());
        assertSame(returnTypeGenericMetadata, new ReturnsDeepStubs().actualParameterizedType(mock));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Exception {
        InvocationContainerImpl container = new InvocationContainerImpl();
        Object mock = mock(Map.class, withSettings().defaultAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new UnsupportedOperationException();
            }
        }));
        when(mockGenericsNest.entrySet()).thenReturn(entrySetMock);
        when(entrySetMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.next()).thenReturn(valueMock);
        when(valueMock.iterator()).thenReturn(nestedIteratorMock);
        when(nestedIteratorMock.next()).thenReturn(numberMock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Number.class, null);

        Object deepStubAnswer = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        assertSame(deepStub, deepStubAnswer);
        verify(mockGenericsNest).entrySet();
        verify(mockGenericsNest).entrySet();
        verify(mockGenericsNest).entrySet();
        verify(mockGenericsNest).entrySet();
        verify(mockGenericsNest).entrySet(); // verification of the deep stubbed answer
    }

    private MockitoCore mockitoCore() {
        return Mockito.mockitoCore();
    }

    private InvocationOnMock entrySetMock;
    private Iterator<Map.Entry<K, Set<Number>>> iteratorMock;
    private Iterator<Number> nestedIteratorMock;
    private Map.Entry<K, Set<Number>> valueMock;
    private Number numberMock;
    private List<Number> deepListMock;

}

public class ReturnsDeepListAnswers implements Answer<List<Number>> {
    @Override
    public List<Number> answer(InvocationOnMock invocation) throws Throwable {
        return Collections.singletonList(mock(Number.class, withSettings()));
    }
}