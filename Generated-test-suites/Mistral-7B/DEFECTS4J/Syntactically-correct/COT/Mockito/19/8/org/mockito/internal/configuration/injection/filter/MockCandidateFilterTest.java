package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockitoCore;
import org.mockito.internal.util.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullCollection() {
        MockCandidateFilter filter = MockitoCore.getSystemPropertyMockCandidateFilter();
        Collection<Object> mocks = null;
        Field field = null;
        Object fieldInstance = new Object();

        Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        MockCandidateFilter filter = MockitoCore.getSystemPropertyMockCandidateFilter();
        Collection<Object> mocks = CollectionUtils.newArrayList();
        Field field = null;
        Object fieldInstance = new Object();

        Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NonEmptyCollection() {
        MockCandidateFilter filter = MockitoCore.getSystemPropertyMockCandidateFilter();
        Collection<Object> mocks = CollectionUtils.newArrayList(new Object(), new Object(), new Object());
        Field field = null;
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(result);
        Assert.assertFalse(mocks.contains(result));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMocks() {
        MockCandidateFilter filter = MockitoCore.getSystemPropertyMockCandidateFilter();
        Collection<Object> mocks = null;
        Field field = null;
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldToBeInjected() {
        MockCandidateFilter filter = MockitoCore.getSystemPropertyMockCandidateFilter();
        Collection<Object> mocks = CollectionUtils.newArrayList();
        Field field = null;
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = MockitoCore.getSystemPropertyMockCandidateFilter();
        Collection<Object> mocks = CollectionUtils.newArrayList();
        Field field = null;
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

}