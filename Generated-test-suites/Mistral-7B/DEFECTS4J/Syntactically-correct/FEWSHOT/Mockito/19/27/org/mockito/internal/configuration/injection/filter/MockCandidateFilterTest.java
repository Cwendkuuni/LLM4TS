package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Arrays;
import org.mockito.internal.util.reflection.FieldAccessibilityContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    @SuppressWarnings("unchecked")
    private static <T> MockCandidateFilter<T> mockFilter() {
        return (MockCandidateFilter<T>) MockitoTestUtils.mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate_NullMocks_ReturnsNull() {
        MockCandidateFilter<Object> filter = mockFilter();
        Collection<Object> mocks = null;
        Object fieldInstance = new Object();
        Field fieldToBeInjected = new Field("testField", Object.class);

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance_ReturnsNull() {
        MockCandidateFilter<Object> filter = mockFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class);

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }

    @Test
    public void testFilterCandidate_NullField_ReturnsNull() {
        MockCandidateFilter<Object> filter = mockFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();

        Assert.assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyMocks_ReturnsNull() {
        MockCandidateFilter<Object> filter = mockFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = new Object();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsNull() {
        MockCandidateFilter<Object> filter = (MockCandidateFilter<Object>) (mock -> null);
        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = new Object();

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_FilterReturnsFilteredMock() {
        MockCandidateFilter<Object> filter = (MockCandidateFilter<Object>) (mock -> mock);
        Collection<Object> mocks = Arrays.asList(new Object(), new Object(), new Object());
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = new Object();

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Assert.assertNotNull(filteredMock);
        Assert.assertTrue(mocks.contains(filteredMock));
    }

    @Test
    public void testFilterCandidate_AccessibilityContext_AppliesFilter() throws IllegalAccessException {
        MockCandidateFilter<Object> filter = (MockCandidateFilter<Object>) (mock, context) -> {
            if (context == FieldAccessibilityContext.getAccessibleContext()) {
                return mock;
            }
            return null;
        };
        Collection<Object> mocks = Arrays.asList(new Object(), new Object(), new Object());
        Field fieldToBeInjected = new Field("testField", Object.class);
        Object fieldInstance = new Object();

        Object filteredMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Assert.assertNotNull(filteredMock);
        Assert.assertFalse(mocks.contains(filteredMock));

        // Make the test field accessible
        FieldAccessibilityContext.makeAccessible(fieldToBeInjected);
        Object accessibleMock = mocks.get(1);
        Object filteredAccessibleMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        Assert.assertNotNull(filteredAccessibleMock);
        Assert.assertEquals(accessibleMock, filteredAccessibleMock);
    }
}