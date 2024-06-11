package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;
import static org.mockito.internal.util.reflection.FieldUtils.getFieldValue;
import static org.mockito.internal.util.reflection.FieldUtils.setFieldValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.collections.CollectionUtils;
import org.mockito.internal.util.reflection.FieldUtils;

public class TypeBasedCandidateFilterTest {
    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        Collection<Object> mocks = CollectionUtils.emptyList();

        OngoingInjecter result = filter.filterCandidate(mocks, mockField(), mockFieldInstance());
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_noMatches() {
        Collection<Object> mocks = newArrayList(new Object(), new Integer(1), new String("foo"));

        OngoingInjecter result = filter.filterCandidate(mocks, mockField(), mockFieldInstance());
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_oneMatch() {
        Collection<Object> mocks = newArrayList(new Object(), new MockitoType(), new String("foo"));

        OngoingInjecter result = filter.filterCandidate(mocks, mockField(), mockFieldInstance());
        assertEquals(mockitoType(), result.getMock());
    }

    @Test
    public void testFilterCandidate_multipleMatches() {
        Collection<Object> mocks = newArrayList(new Object(), new MockitoType(), new MockitoType(), new String("foo"));

        OngoingInjecter result = filter.filterCandidate(mocks, mockField(), mockFieldInstance());
        assertEquals(mockitoType(), result.getMock());
    }

    @Test
    public void testFilterCandidate_nextFilter() {
        Collection<Object> mocks = newArrayList(new Object(), new MockitoType(), new Object(), new String("foo"));

        Object nextMock = mock(MockCandidateFilter.class);
        when(next.filterCandidate(CollectionUtils.newArrayList(newMockitoType(), newMockitoType()), mockField(), mockFieldInstance()))
                .thenReturn(new OngoingInjecter(newMockitoType(), mockitoTypeMock()));

        OngoingInjecter result = filter.filterCandidate(mocks, mockField(), mockFieldInstance());
        assertEquals(mockitoType(), result.getMock());
        verify(next).filterCandidate(CollectionUtils.newArrayList(newMockitoType(), newMockitoType()), mockField(), mockFieldInstance());
    }

    private static Field mockField() {
        try {
            return FieldUtils.getField(TypeBasedCandidateFilter.class, "field");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object mockFieldInstance() {
        try {
            return getFieldValue(mockField(), filter);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object mockitoType() {
        try {
            return getFieldValue(mockField(), filter);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object mockitoTypeMock() {
        MockitoType mockitoType = mock(MockitoType.class);
        setFieldValue(mockField(), filter, mockitoType);
        return mockitoType;
    }

    private static class MockitoType {
        // empty class to represent a mockito type
    }
}