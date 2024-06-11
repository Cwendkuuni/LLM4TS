package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock");

        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        Field field = null;
        Object fieldInstance = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException();
            }
        };

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject(); // Exception should be thrown here
    }

}