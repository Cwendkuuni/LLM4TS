package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        Collection<Object> mocks = ExtendedCollections.emptyCollection();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_noTypeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new String("mock1"));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result.getFilter());
        verify(nextMock, times(1)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_typeMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result.getFilter());
        verify(nextMock, times(0)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullMocks() {
        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullField() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_nextNull() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(null, result.getFilter());
    }
}