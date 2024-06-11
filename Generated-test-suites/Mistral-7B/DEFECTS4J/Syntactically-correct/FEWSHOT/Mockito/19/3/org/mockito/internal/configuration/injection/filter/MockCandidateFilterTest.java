package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullMocksCollection() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(null, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullField() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNull() {
        MockCandidateFilter filter = spy(new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(
                    Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return null;
            }
        });
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsInjectedMock() {
        MockCandidateFilter filter = spy(new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(
                    Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return mock(OngoingInjecter.class);
            }
        });
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNotNull(result);
    }
}