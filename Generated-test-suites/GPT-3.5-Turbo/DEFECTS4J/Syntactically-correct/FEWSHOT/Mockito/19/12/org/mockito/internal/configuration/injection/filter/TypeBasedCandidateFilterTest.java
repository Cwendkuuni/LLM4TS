package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Test
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        assertEquals(next, result);
    }

    @Test
    public void testFilterCandidate_withMatchingMocks() throws Exception {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mocks");
        MockTestClass mock1 = new MockTestClass();
        MockSubclassOfTestClass mock2 = new MockSubclassOfTestClass();
        mocks.add(mock1);
        mocks.add(mock2);

        // Test
        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        // Verify
        assertEquals(next, result);
    }

    @Test
    public void testFilterCandidate_withNonMatchingMocks() throws Exception {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mocks");
        NonMockTestClass nonMock = new NonMockTestClass();
        mocks.add(nonMock);

        // Test
        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        // Verify
        assertNull(result);
    }

    private static class MockTestClass {}
    private static class MockSubclassOfTestClass extends MockTestClass {}
    private static class NonMockTestClass {}

}