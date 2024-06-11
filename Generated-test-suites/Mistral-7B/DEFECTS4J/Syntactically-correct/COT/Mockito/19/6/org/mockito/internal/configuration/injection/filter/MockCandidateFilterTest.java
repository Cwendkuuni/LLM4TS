package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullArguments() {
        MockCandidateFilter filter = MockitoJUnit.mock(MockCandidateFilter.class);
        Collection<Object> mocks = null;
        Field fieldToBeInjected = null;
        Object fieldInstance = null;

        try {
            filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
            Assert.fail("Expected NullPointerException not thrown.");
        } catch (NullPointerException e) {
            // Expected
        }

        mocks = ExtendedCollections.newArrayList();
        Assert.assertNull(filter.filterCandidate(mocks, null, fieldInstance));
        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        Assert.assertNull(filter.filterCandidate(null, fieldToBeInjected, fieldInstance));
        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        Assert.assertNull(filter.filterCandidate(mocks, null, null));
        Assert.assertNull(filter.filterCandidate(null, null, null));
    }

    @Test
    public void testFilterCandidate_BasicUsage() {
        MockCandidateFilter filter = MockitoJUnit.mock(MockCandidateFilter.class);
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        Field fieldToBeInjected = new Field("testField", String.class);
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNull(result);
        Assert.assertFalse(mocks.contains(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MocksAreNotModified() {
        MockCandidateFilter filter = MockitoJUnit.mock(MockCandidateFilter.class);
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        Field fieldToBeInjected = new Field("testField", String.class);
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertFalse(mocks.contains(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithSameField() {
        MockCandidateFilter filter = MockitoJUnit.mock(MockCandidateFilter.class);
        Collection<Object> mocks = ExtendedCollections.newArrayList();
        Field fieldToBeInjected = new Field("testField", String.class);

        Object mock1 = new Object();
        Object mock2 = new Object();
        Object fieldInstance1 = new Object();
        Object fieldInstance2 = new Object();

        mocks.add(mock1);
        mocks.add(mock2);

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance1);
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance2);

        Assert.assertFalse(mocks.get(0) instanceof OngoingInjecter);
        Assert.assertFalse(mocks.get(1) instanceof OngoingInjecter);
    }
}