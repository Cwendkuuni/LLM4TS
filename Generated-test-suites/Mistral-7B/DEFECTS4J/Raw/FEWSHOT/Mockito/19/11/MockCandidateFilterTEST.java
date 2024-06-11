package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_whenMocksCollectionIsNull_thenReturnNull() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(null, mock(Field.class), mock(Object.class)));
    }

    @Test
    public void testFilterCandidate_whenFieldInstanceIsAnswer_thenReturnAnswer() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Answer.class);

        Answer answer = mock(Answer.class);
        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(answer);

        assertEquals(answer, filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldInstanceIsAnswer2_thenReturnAnswer2() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Answer2.class);

        Answer2 answer = mock(Answer2.class);
        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(answer);

        assertEquals(answer, filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldInstanceIsMock_thenReturnMock() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mock = mock(Object.class);
        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock);

        assertEquals(mock, filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldInstanceIsSpy_thenReturnSpy() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = spy(mock(Object.class));

        Object spy = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(fieldInstance, spy);
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenMocksCollectionIsEmpty_thenReturnNull() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsFinal_thenReturnNull() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        when(field.isFinal()).thenReturn(true);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsPrivate_thenReturnNull() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        when(field.isAccessible()).thenReturn(false);
        when(field.isPrivate()).thenReturn(true);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsProtected_thenReturnNull() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        when(field.isAccessible()).thenReturn(false);
        when(field.isProtected()).thenReturn(true);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsPublic_thenReturnMock() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mock = mock(Object.class);
        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock);

        assertEquals(mock, filter.filterCandidate(mocks, field, fieldInstance));
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }
}