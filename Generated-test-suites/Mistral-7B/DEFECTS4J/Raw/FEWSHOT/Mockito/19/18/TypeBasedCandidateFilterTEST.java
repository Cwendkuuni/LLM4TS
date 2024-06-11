package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.FieldUtils.getFieldValue;
import static org.mockito.junit.jupiter.MockitoExtension.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
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
    public void testFilterCandidate_EmptyMockCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MockCollectionWithOneMatch() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockTypeA());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_MockCollectionWithMultipleMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockTypeA());
        mocks.add(new MockTypeB());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_MockCollectionWithNoMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new ClassA());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_FieldInstanceOfFilter() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockTypeA());
        Field field = mock(Field.class);
        Object fieldInstance = new TypeBasedCandidateFilter(mock(MockCandidateFilter.class));
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_NextFilterReturnsNull() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new MockTypeA());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        when(next.filterCandidate(eq(mocks), eq(field), eq(fieldInstance))).thenReturn(null);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }
}

class MockTypeA {
}

class MockTypeB {
}

class ClassA {
}