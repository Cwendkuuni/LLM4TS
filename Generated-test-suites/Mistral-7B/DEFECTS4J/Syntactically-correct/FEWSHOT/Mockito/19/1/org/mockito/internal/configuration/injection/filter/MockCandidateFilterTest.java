package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullCollection() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(null, mockField(), mockFieldInstance()));
    }

    @Test
    public void testFilterCandidate_NullField() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(Collections.emptyList(), null, mockFieldInstance()));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(Collections.emptyList(), mockField(), null));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(Collections.emptyList(), mockField(), mockFieldInstance()));
    }

    @Test
    public void testFilterCandidate_SingleMock() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Object mock = mock(Object.class);
        when(filter.filterCandidate(Collections.singletonList(mock), mockField(), mockFieldInstance())).thenReturn(mock);

        Object result = filter.filterCandidate(Arrays.asList(mock), mockField(), mockFieldInstance()).getMockingInstance();
        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(filter.filterCandidate(Arrays.asList(mock1, mock2), mockField(), mockFieldInstance()))
                .thenReturn(mock1);

        Object result1 = filter.filterCandidate(Arrays.asList(mock1, mock2), mockField(), mockFieldInstance()).getMockingInstance();
        assertEquals(mock1, result1);

        Object result2 = filter.filterCandidate(Arrays.asList(mock1, mock2), mockField(), mockFieldInstance()).getMockingInstance();
        assertEquals(mock2, result2);
    }

    @Test
    public void testFilterCandidate_FilterCandidateReturned() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Object mock = mock(Object.class);
        when(filter.filterCandidate(Arrays.asList(mock, mock), mockField(), mockFieldInstance()))
                .thenReturn(mock);

        Object result = filter.filterCandidate(Arrays.asList(mock, mock), mockField(), mockFieldInstance()).getMockingInstance();
        assertEquals(mock, result);
    }

    private Field mockField() {
        try {
            return Mockito.mock(Field.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Object mockFieldInstance() {
        return mock(Object.class);
    }

}