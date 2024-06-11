package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testEmptyFilter() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterWithMatchedMocks() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        FieldType fieldType = mock(FieldType.class);
        when(field.getType()).thenReturn(fieldType);
        when(fieldType.isAssignableFrom(String.class)).thenReturn(true);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterWithUnmatchedMocks() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(StringBuffer.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        FieldType fieldType = mock(FieldType.class);
        when(field.getType()).thenReturn(fieldType);
        when(fieldType.isAssignableFrom(String.class)).thenReturn(false);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterWithNextFilter() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        verify(next).filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }
}