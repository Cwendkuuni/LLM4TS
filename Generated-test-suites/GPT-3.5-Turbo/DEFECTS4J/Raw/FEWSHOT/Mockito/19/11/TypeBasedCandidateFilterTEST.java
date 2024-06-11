package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = new Field();
        Object fieldInstance = new Object();

        // Test case 1: No mocks, should return an empty OngoingInjecter
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(result.isEmpty());

        // Test case 2: One mock that matches the field type
        Object mock1 = new Object();
        mocks.add(mock1);
        field.setType(Object.class);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        List<Object> expected1 = new ArrayList<Object>();
        expected1.add(mock1);
        assertEquals(expected1, result.getMocks());

        // Test case 3: Two mocks, both matching the field type
        Object mock2 = new Object();
        mocks.add(mock2);
        field.setType(Object.class);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        List<Object> expected2 = new ArrayList<Object>();
        expected2.add(mock1);
        expected2.add(mock2);
        assertEquals(expected2, result.getMocks());

        // Test case 4: Two mocks, only one matching the field type
        field.setType(Integer.class);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        List<Object> expected3 = new ArrayList<Object>();
        expected3.add(mock2);
        assertEquals(expected3, result.getMocks());
    }
}