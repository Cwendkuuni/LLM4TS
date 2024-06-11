package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class MockCandidateFilterTest {

    @Test
    public void testNullFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        assertNull(MockCandidateFilter.class.cast(null).filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testEmptyCollectionFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = null;

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullFieldToBeInjected() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithEmptyCollectionAndNonNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithCollectionAndNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = null;

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithCollectionAndNullFieldToBeInjected() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithEmptyCollectionAndNonNullFieldToBeInjectedAndNonNullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithCollectionContainingNull() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(null);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithCollectionContainingEmptyMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(MockitoMock.class));
        mocks.add(mock(MockitoMock.class, new MockitoMockConfig().mockOnlyDefaultConstructor()));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithCollectionContainingNonEmptyMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(MockitoMock.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(filteredMock);
    }

    @Test
    public void testFilterCandidateWithCollectionContainingExactMatchMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class, new Object() {
            @Override
            public void foo() {
            }
        }));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(mocks.get(0), filteredMock);
    }

    @Test
    public void testFilterCandidateWithCollectionContainingSubtypeMatchMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(ObjectSubtype.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(ObjectSubtype.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(mocks.get(0), filteredMock);
    }

    @Test
    public void testFilterCandidateWithCollectionContainingSuperclassMatchMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(ObjectSuperclass.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(ObjectSuperclass.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(mocks.get(0), filteredMock);
    }

    @Test
    public void testFilterCandidateWithCollectionContainingMatchingTypeMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(MockitoMock.class));
        Field fieldToBeInjected = mock(Field.class);
        MockitoMock fieldInstance = mock(MockitoMock.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(fieldInstance, filteredMock);
    }

    @Test
    public void testFilterCandidateWithCollectionContainingMultipleMatches() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(MockitoMock.class));
        mocks.add(mock(MockitoMock.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter filter = mock(MockCandidateFilter.class);

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(mocks.get(0), filteredMock);
    }

    private static class MockitoMock {
        public void foo() {
        }
    }

    private static class ObjectSubtype extends Object {
        // empty
    }

    private static class ObjectSuperclass {
        // empty
    }

}