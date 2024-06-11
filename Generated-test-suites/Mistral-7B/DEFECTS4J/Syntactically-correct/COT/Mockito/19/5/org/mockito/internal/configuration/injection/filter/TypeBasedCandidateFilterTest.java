package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedArrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = ExtendedArrays.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_NoTypeMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new String("mock1"));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullCollection() {
        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleTypeMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new String("mock1").getClass().forName("java.lang.String"));
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_NextFilterCalled() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }
}