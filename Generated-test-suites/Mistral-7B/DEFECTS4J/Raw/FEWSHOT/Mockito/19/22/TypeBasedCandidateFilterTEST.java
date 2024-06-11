package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.CollectionUtils;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        final Class<?> targetType = MockitoTypeAdapter.class;
        final Class<?> mockType = MockedStatic.class;

        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = newArrayList();
        mocks.add(new MockitoTypeAdapter());
        mocks.add(new MockedStatic<>());
        mocks.add(new Object());

        Field field = Fields.privateField(TypeBasedCandidateFilter.class, "next");

        Object fieldInstance = filter;

        Collection<Object> expectedMockMatches = newArrayList(new MockitoTypeAdapter());
        Collection<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(actualMockMatches);
        CollectionUtils.assertListsEqual(expectedMockMatches, actualMockMatches);
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        final Class<?> targetType = MockitoTypeAdapter.class;
        final Class<?> mockType = MockedStatic.class;

        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = CollectionUtils.emptyList();
        Field field = Fields.staticField(TypeBasedCandidateFilter.class, "next");
        Object fieldInstance = filter;

        Collection<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(actualMockMatches);
        CollectionUtils.isEmpty(actualMockMatches);
    }

    @Test
    public void testFilterCandidate_noTypeMatch() {
        final Class<?> targetType = MockitoTypeAdapter.class;
        final Class<?> mockType = String.class;

        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = newArrayList();
        mocks.add(new MockitoTypeAdapter());
        mocks.add(new String());
        mocks.add(new Object());

        Field field = Fields.privateField(TypeBasedCandidateFilter.class, "next");
        Object fieldInstance = filter;

        Collection<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        CollectionUtils.isEmpty(actualMockMatches);
    }

    @Test
    public void testFilterCandidate_singleTypeMatch() {
        final Class<?> targetType = MockitoTypeAdapter.class;
        final Class<?> mockType = MockitoTypeAdapter.class;

        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = newArrayList();
        mocks.add(new MockitoTypeAdapter());
        mocks.add(new String());
        mocks.add(new MockitoTypeAdapter());

        Field field = Fields.privateField(TypeBasedCandidateFilter.class, "next");
        Object fieldInstance = filter;

        Collection<Object> expectedMockMatches = newArrayList(new MockitoTypeAdapter());
        Collection<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        CollectionUtils.assertListsEqual(expectedMockMatches, actualMockMatches);
    }

    @Test
    public void testFilterCandidate_multipleTypeMatches() {
        final Class<?> targetType = MockitoTypeAdapter.class;
        final Class<?> mockType1 = MockitoTypeAdapter.class;
        final Class<?> mockType2 = MockedStatic.class;

        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        Collection<Object> mocks = newArrayList();
        mocks.add(new MockitoTypeAdapter());
        mocks.add(new MockedStatic<>());
        mocks.add(new MockitoTypeAdapter());

        Field field = Fields.privateField(TypeBasedCandidateFilter.class, "next");
        Object fieldInstance = filter;

        Collection<Object> expectedMockMatches = newArrayList(new MockitoTypeAdapter(), new MockedStatic<>());
        Collection<Object> actualMockMatches = filter.filterCandidate(mocks, field, fieldInstance);

        CollectionUtils.assertListsEqual(expectedMockMatches, actualMockMatches);
    }
}