package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidateNullMocks() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = spy(new Object());

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateEmptyMocks() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = new Object();

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateNoMatch() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = new Object();

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateSimpleMatch() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object[].class));
        Field fieldToBeInjected = new Field("testField", Object[].class);
        Object fieldInstance = new Object[]{1, 2, 3};

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(fieldInstance, result);
    }

    @Test
    public void testFilterCandidateArrayMatch() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object[].class));
        mocks.add(mock(String[].class));
        Field fieldToBeInjected = new Field("testField", Object[].class);
        Object fieldInstance = new Object[]{1, 2, 3};

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(fieldInstance, result);
    }

    @Test
    public void testFilterCandidateCollectionMatch() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Collection.class));
        Field fieldToBeInjected = new Field("testField", Collection.class);
        Object fieldInstance = mock(Collection.class);

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(fieldInstance, result);
    }

}