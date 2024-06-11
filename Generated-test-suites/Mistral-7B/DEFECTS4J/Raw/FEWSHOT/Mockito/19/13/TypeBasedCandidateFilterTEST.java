package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;

import org.junit.Test;
import org.mockito.MockCandidateFilter;
import org.mockito.internal.configuration.MockType;
import org.mockito.internal.configuration.injection.InjectableField;
import org.mockito.internal.configuration.injection.MockInjector;
import org.mockito.internal.util.collections.CollectionUtils;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidateWithEmptyMockCollection() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = CollectionUtils.emptyList();
        Field field = new InjectableField("fieldName", Int.class, Int.class);
        Object fieldInstance = new Integer(1);

        MockCandidateFilter next = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filterUnderTest = new TypeBasedCandidateFilter(next);

        assertNull(filterUnderTest.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNullMockCollection() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = null;
        Field field = new InjectableField("fieldName", Int.class, Int.class);
        Object fieldInstance = new Integer(1);

        MockCandidateFilter next = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filterUnderTest = new TypeBasedCandidateFilter(next);

        assertNull(filterUnderTest.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithNoMatchingTypes() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = newArrayList(new Object(), new String(), new Integer(1));
        Field field = new InjectableField("fieldName", Float.class, Float.class);
        Object fieldInstance = new Float(1.0f);

        MockCandidateFilter next = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filterUnderTest = new TypeBasedCandidateFilter(next);

        assertNull(filterUnderTest.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidateWithSingleMatchingType() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = newArrayList(new String(), new Integer(1), new MockTypeMock(Integer.class));
        Field field = new InjectableField("fieldName", Integer.class, Integer.class);
        Object fieldInstance = new Integer(1);

        MockCandidateFilter next = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filterUnderTest = new TypeBasedCandidateFilter(next);

        MockCandidate mockTypeMock = mock(MockCandidate.class);
        when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(mockTypeMock);

        Object candidate = filterUnderTest.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assertEquals(mockTypeMock, candidate);
    }

    @Test
    public void testFilterCandidateWithMultipleMatchingTypes() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = newArrayList(new Integer(1), new MockTypeMock(Integer.class), new Integer(2), new MockTypeMock(Integer.class));
        Field field = new InjectableField("fieldName", Integer.class, Integer.class);
        Object fieldInstance = new Integer(1);

        MockCandidateFilter next = mock(MockCandidateFilter.class);

        TypeBasedCandidateFilter filterUnderTest = new TypeBasedCandidateFilter(next);

        MockCandidate mockTypeMock1 = mock(MockCandidate.class);
        when(next.filterCandidate(CollectionUtils.newList(new Integer(1), new Integer(2)), field, fieldInstance)).thenReturn(mockTypeMock1);

        MockCandidate mockTypeMock2 = mock(MockCandidate.class);
        when(next.filterCandidate(CollectionUtils.newList(new Integer(2), new Integer(1)), field, fieldInstance)).thenReturn(mockTypeMock2);

        Object candidate1 = filterUnderTest.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate1);
        assertEquals(mockTypeMock1, candidate1);

        Object candidate2 = filterUnderTest.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate2);
        assertEquals(mockTypeMock2, candidate2);
    }

    @Test
    public void testFilterCandidateWhenNextIsNull() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = newArrayList(new Object(), new Integer(1));
        Field field = new InjectableField("fieldName", Integer.class, Integer.class);
        Object fieldInstance = new Integer(1);

        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(candidate);
    }
}

class MockTypeMock implements MockType, MockCandidate {

    private final Class<?> clazz;

    public MockTypeMock(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?> getMockType() {
        return clazz;
    }

    @Override
    public Object getMock() {
        throw new UnsupportedOperationException();
    }
}