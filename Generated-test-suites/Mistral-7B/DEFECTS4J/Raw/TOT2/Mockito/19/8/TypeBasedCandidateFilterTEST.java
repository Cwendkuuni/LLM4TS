package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
        when(field.getType()).thenReturn(String.class);
    }

    @Test
    public void testFilterCandidateWithMocksOfCorrectType() {
        Object mock = mock(String.class);
        mocks.add(mock);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mock, result.getMock());
    }

    @Test
    public void testFilterCandidateWithNullMocks() {
        OngoingInjecter result = filter.filterCandidate(null, field, fieldInstance);
        assertEquals(next, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithNoMatchingMocks() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(next, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithMocksOfDifferentType() {
        Object mockInt = mock(Integer.class);
        mocks.add(mockInt);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(next, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithEmptyMocks() {
        OngoingInjecter result = filter.filterCandidate(new ArrayList<>(), field, fieldInstance);
        assertEquals(next, result.getFilter());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }
}