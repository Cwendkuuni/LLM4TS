package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.Assertgit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.collections.Iterators.asList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.collections.Arrays;

/**
 * Test class for TypeBasedCandidateFilter
 */
public class TypeBasedCandidateFilterTest {

    /**
     * Test filterCandidate method with empty collection
     */
    @Test
    public void testFilterCandidateWithEmptyCollection() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    /**
     * Test filterCandidate method with null next filter
     */
    @Test
    public void testFilterCandidateWithNullNextFilter() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    /**
     * Test filterCandidate method with null field instance
     */
    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
        Field field = mock(Field.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);
        assertNull(result);
    }

    /**
     * Test filterCandidate method with empty mock list and no match
     */
    @Test
    public void testFilterCandidateWithEmptyMockListAndNoMatch() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    /**
     * Test filterCandidate method with null field type
     */
    @Test
    public void testFilterCandidateWithNullFieldType() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
        Field field = mock(Field.class);
        field.setAccessible(true);
        Field typeField = ReflectionUtils.findField(field.getClass(), "type");
        typeField.setAccessible(true);
        try {
            typeField.set(field, null);
            OngoingInjecter result = filter.filterCandidate(mocks, field, mock(Object.class));
            assertNull(result);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test filterCandidate method with null mock class
     */
    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMockClass() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    /**
     * Test filterCandidate method with empty mock list and match
     */
    @Test
    public void testFilterCandidateWithEmptyMockListAndMatch() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    /**
     * Test filterCandidate method with single mock and no match
     */
    @Test
    public void testFilterCandidateWithSingleMockAndNoMatch() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    /**
     * Test filterCandidate method with single mock and match
     */
    @Test
    public void testFilterCandidateWithSingleMockAndMatch() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(fieldInstance, result.getMock());
        assertEquals(field, result.getField());
    }

    /**
     * Test filterCandidate method with multiple mocks and match
     */
    @Test
    public void testFilterCandidateWithMultipleMocksAndMatch() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(mocks.get(2), result.getMock());
        assertEquals(field, result.getField());
    }

    /**
     * Test filterCandidate method with multiple mocks and no match
     */
    @Test
    public void testFilterCandidateWithMultipleMocksAndNoMatch() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    /**
     * Test filterCandidate method with multiple mocks and multiple matches
     */
    @Test
    public void testFilterCandidateWithMultipleMocksAndMultipleMatches() {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = Arrays.asList(new Object() {}, new Object() {}, new Object() {});
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(mocks.get(1), result.getMock());
        assertEquals(field, result.getField());
    }
}