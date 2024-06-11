package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result.thenInject());

        verify(mocks, times(1)).iterator();
        verify(field, times(1)).getName();
        verify(field, times(1)).set(fieldInstance, matchingMock);
        verifyNoMoreInteractions(mocks, field);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(mocks.size()).thenReturn(2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertFalse(result.thenInject());

        verify(mocks, times(1)).size();
        verifyNoMoreInteractions(mocks, field);
    }

    @Test
    public void testFilterCandidate_exception() throws Exception {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result.thenInject());

        verify(mocks, times(1)).iterator();
        verify(field, times(1)).getName();
        verify(field, times(1)).set(fieldInstance, matchingMock);
        verifyNoMoreInteractions(mocks, field);

        throw new MockitoException();
    }
}