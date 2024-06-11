package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setup() {
        filter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidateNullCollection() {
        assertNull(filter.filterCandidate(null, null, null));
    }

    @Test
    public void testFilterCandidateEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        assertNull(filter.filterCandidate(mocks, null, null));
    }

    @Test
    public void testFilterCandidateNullField() {
        Collection<Object> mocks = new ArrayList<>();
        assertNull(filter.filterCandidate(mocks, null, mock(Object.class)));
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        assertNull(filter.filterCandidate(mocks, new Field(null, "fieldName"), null));
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = spy(new Object() {
            // add some behaviour or state for testing
        });
        Field field = new Field(MockCandidateFilterTest.class, "fieldToBeInjected", Object.class);
        Object candidate = mock(Object.class);

        // when filterCandidate is called
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // then the result should be the candidate
        assertEquals(candidate, result);
    }
}