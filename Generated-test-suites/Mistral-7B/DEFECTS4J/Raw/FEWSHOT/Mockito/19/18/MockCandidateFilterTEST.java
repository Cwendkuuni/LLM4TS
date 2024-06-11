package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidateNullMocks() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = null;
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateEmptyMocks() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = null;

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateNullFieldToBeInjected() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateFilterNull() {
        MockCandidateFilter filter = null;
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateFieldInstanceIsAMock() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(fieldInstance, result);
    }

    @Test
    public void testFilterCandidateFieldInstanceIsASpy() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(spy(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidateFieldInstanceIsNotAMockOrASpy() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(result);
    }

}