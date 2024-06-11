package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextFilter;

    @Before
    public void setup() {
        nextFilter = mock(TypeBasedCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithNoTypeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = mock(Field.class);
        field.getType().assignableFrom(Integer.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidateWithTypeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        field.getType().assignableFrom(Integer.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);

        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidateWithMultipleTypeMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        field.getType().assignableFrom(Integer.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testNextFilter() {
        Collection<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mock(Integer.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mockTypeMatches, field, fieldInstance);

        assertNotNull(result);

        TypeBasedCandidateFilter spyNextFilter = spy(nextFilter);
        OngoingInjecter nextResult = spyNextFilter.filterCandidate(mockTypeMatches, field, fieldInstance);

        assertNotNull(nextResult);

        // check that next filter was called
        spyNextFilter.filterCandidate(mockTypeMatches, field, fieldInstance);

        // check that next result is the same as the final result
        assertSame(nextResult, result);
    }
}