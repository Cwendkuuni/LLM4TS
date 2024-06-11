package org.mockito.tests.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.stub.Answer;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidateWithMockMatchesType() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Object mock = Mockito.mock(String.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = Mockito.spy(Field.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        // then
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        verify(mock).mockingDetails(eq(field), anyObject());
    }

    @Test(expected = MockitoAssertionError.class)
    public void testFilterCandidateWithNoMockMatchesType() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Object mock = Mockito.mock(Number.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = Mockito.spy(Field.class);

        // when
        filter.filterCandidate(mocks, field, new Object());

        // then
        assertNull(filter.getMock());
        verifyZeroInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMockCollection() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = null;
        Field field = Mockito.spy(Field.class);
        Object fieldInstance = new Object();

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        fail("Mock collection should not be null");
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMockCollectionAndNullField() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = null;
        Field field = null;
        Object fieldInstance = new Object();

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        fail("Mock collection and field should not be null");
    }

    @Test
    public void testFilterCandidateWithMockMatchesTypeAndNextIsNull() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Object mock = Mockito.mock(String.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = Mockito.spy(Field.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        // then
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        verify(mock, times(2)).mockingDetails(eq(field), anyObject());
    }

    @Test
    public void testFilterCandidateWithMockMatchesTypeAndNextIsSet() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new TypeBasedCandidateFilter(null));
        Object mock = Mockito.mock(String.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = Mockito.spy(Field.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        // then
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        verify(mock, times(2)).mockingDetails(eq(field), anyObject());
        verify(filter.next).filterCandidate(mocks, field, new Object());
    }

    @Test
    public void testFilterCandidateWithMockMatchesTypeAndNextThrowsException() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new TypeBasedCandidateFilter(new ExceptionThrowingMockCandidateFilter(null)));
        Object mock = Mockito.mock(String.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = Mockito.spy(Field.class);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        // then
        assertNotNull(result);
        assertEquals(mock, result.getMock());
        verify(mock, times(2)).mockingDetails(eq(field), anyObject());
        verify(filter.next).filterCandidate(mocks, field, new Object());
        verifyNoMoreInteractions(filter.next);
    }

    @Test
    public void testFilterCandidateWithNoMockMatchesTypeAndNextIsNull() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.spy(Field.class);
        Object fieldInstance = new Object();

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNull(filter.getMock());
        verifyZeroInteractions(filter);
    }

    @Test
    public void testFilterCandidateWithNoMockMatchesTypeAndNextIsSet() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new TypeBasedCandidateFilter(null));
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.spy(Field.class);
        Object fieldInstance = new Object();

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNull(filter.getMock());
        verifyZeroInteractions(filter);
        verify(filter.next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithNoMockMatchesTypeAndNextThrowsException() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(new ExceptionThrowingMockCandidateFilter(new TypeBasedCandidateFilter(null)));
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.spy(Field.class);
        Object fieldInstance = new Object();

        // when
        filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNull(filter.getMock());
        verifyZeroInteractions(filter);
        verify(filter.next).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(filter.next);
    }

    private static class ExceptionThrowingMockCandidateFilter implements MockCandidateFilter {

        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            throw new RuntimeException();
        }
    }
}