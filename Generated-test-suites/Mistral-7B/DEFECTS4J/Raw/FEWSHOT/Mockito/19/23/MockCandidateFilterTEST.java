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
    public void testFilterCandidate_NullMocks() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = null;
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = "fieldInstance";
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = "fieldInstance";
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullField() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = "fieldInstance";
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = null;
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNull() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = "fieldInstance";
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsMock() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = "fieldInstance";
        Object returnedMock = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(returnedMock);
    }

    @Test
    public void testFilterCandidate_FilterSpy() {
        MockCandidateFilter filter = spy(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = new Field("testField", String.class, null, null);
        Object fieldInstance = "fieldInstance";
        Object returnedMock = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(returnedMock);
    }
}