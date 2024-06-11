package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidateNullCollection() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(null, new Field("testField", String.class, MockitoTest.class, null), new Object()));
    }

    @Test
    public void testFilterCandidateNullField() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        assertNull(filter.filterCandidate(mocks, null, new Object()));
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, MockitoTest.class, null);
        assertNull(filter.filterCandidate(mocks, field, null));
    }

    @Test
    public void testFilterCandidateEmptyCollection() {
        MockCandidateFilter filter = spy(MockitoCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField", String.class, MockitoTest.class, null);
        Object fieldInstance = new Object();
        assertSame(fieldInstance, filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateSingleMock() {
        MockCandidateFilter filter = spy(MockitoCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        Field field = new Field("testField", String.class, MockitoTest.class, null);
        Object fieldInstance = new Object();
        Object mock = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock, fieldInstance);
        assertEquals(1, mocks.size());
        assertEquals(mock, mocks.get(0));
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        MockCandidateFilter filter = spy(MockitoCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        Field field = new Field("testField", String.class, MockitoTest.class, null);
        Object fieldInstance = new Object();
        Object mock1 = filter.filterCandidate(mocks, field, fieldInstance);
        Object mock2 = filter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock1, fieldInstance);
        assertSame(mock2, fieldInstance);
        assertEquals(2, mocks.size());
        assertNotSame(mock1, mocks.get(1));
        assertNotSame(mock2, mocks.get(0));
    }
}