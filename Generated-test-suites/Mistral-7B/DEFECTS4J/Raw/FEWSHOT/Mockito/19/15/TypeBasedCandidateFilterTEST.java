package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.collections.CollectionUtils;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(TypeBasedCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        Collection<Object> mocks = CollectionUtils.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_noMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(field.getType()).thenReturn(String.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_singleMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Foo());
        mocks.add(new Bar());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(field.getType()).thenReturn(Foo.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0), result.getCandidate());
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Foo());
        mocks.add(new Foo());
        mocks.add(new Bar());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(field.getType()).thenReturn(Foo.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0), result.getCandidate());
        verify(nextMock, times(1)).filterCandidate(CollectionUtils.subList(mocks, 1, mocks.size()), field, fieldInstance);
    }
}

class Foo {}