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
    public void testNullParameters() {
        assertNull(MockCandidateFilter.filterCandidate(null, null, null));
        assertNull(MockCandidateFilter.filterCandidate(null, null, mock(Object.class)));
        assertNull(MockCandidateFilter.filterCandidate(mock(Collection.class), null, null));
    }

    @Test
    public void testNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        assertNull(MockCandidateFilter.filterCandidate(mocks, field, null));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        assertNull(MockCandidateFilter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FieldNotAnnotated() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        assertNull(MockCandidateFilter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FieldAnnotated() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        MockCandidate filter = spy(new MockCandidate());
        MockitoTestUtils.setFieldValue(field, filter);
        assertSame(filter, MockCandidateFilter.filterCandidate(mocks, field, fieldInstance));
    }
}

class MockCandidate {
    // Marker interface or empty class
}