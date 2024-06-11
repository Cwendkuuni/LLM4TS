package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private Object fieldInstance;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;

    @Before
    public void setup() {
        fieldInstance = mock(Object.class);
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(Mockito.field(MockCandidateFilter.class, "fieldInstance"));
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsMock_thenReturnTheSameMock() {
        // given
        Object mock = mock(Object.class);
        mocks.add(mock);

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(mock, ongoingInjecter.getTarget());
        verify(mock).answer(Answer.RETURNS_DEEP_STUBS);
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsSpy_thenReturnTheSameSpy() {
        // given
        Object spy = spy(fieldInstance);
        mocks.add(spy);

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(spy, ongoingInjecter.getTarget());
        verify(spy).answer(Answer.RETURNS_DEEP_STUBS);
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsNeitherMockNorSpy_thenReturnNull() {
        // given
        Object notAMock = new Object();
        mocks.add(mock(Object.class));

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, notAMock);

        // then
        assertNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_whenMockIsAlreadyInjected_thenReturnNull() {
        // given
        Object mock = mock(Object.class);
        mocks.add(mock);
        Mockito.doAnswer(invocation -> fieldInstance).when(fieldInstance).answer(Answer.RETURNS_WITH(mock));

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsSameInstanceAsMock_thenReturnSameInstance() {
        // given
        Object mock = mock(Object.class);
        fieldInstance = mock;
        mocks.add(mock);

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // then
        assertSame(fieldInstance, ongoingInjecter.getTarget());
        verify(mock).answer(Answer.RETURNS_DEEP_STUBS);
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsAnswer_thenReturnTheSameAnswer() {
        // given
        Answer answer = Mockito.answer(Answer.RETURNS_DEEP_STUBS);
        mocks.add(answer);

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, answer);

        // then
        assertSame(answer, ongoingInjecter.getTarget());
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsAnswerWithInvokeRealMethod_thenReturnTheSameAnswer() {
        // given
        Answer answer = Mockito.answer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invokeRealMethod(invocation.getMock(), invocation.getMethod(), invocation.getArguments());
                return null;
            }
        });
        mocks.add(answer);

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, answer);

        // then
        assertSame(answer, ongoingInjecter.getTarget());
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsAnswerWithReturningAnswer_thenReturnTheSameAnswer() {
        // given
        Answer answer = Mockito.answer(Mockito.mock(Object.class));
        mocks.add(answer);

        // when
        OngoingInjecter ongoingInjecter = MockCandidateFilter.class.newInstance().filterCandidate(mocks, fieldToBeInjected, answer);

        // then
        assertSame(answer, ongoingInjecter.getTarget());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenPassedNullCollection_thenThrowIllegalArgumentException() {
        // given
        MockCandidateFilter filter = MockCandidateFilter.class.newInstance();

        // when
        filter.filterCandidate(null, fieldToBeInjected, fieldInstance);

        // then
        // exception is thrown in this test
    }
}