package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class MockCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return mock(OngoingInjecter.class);
            }
        };

        Field fieldToBeInjected = getField("fieldToBeInjected");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_noMocks() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return mock(OngoingInjecter.class);
            }
        };

        Field fieldToBeInjected = getField("fieldToBeInjected");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_noMatchingMocks() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return mock(OngoingInjecter.class);
            }
        };

        Field fieldToBeInjected = getField("fieldToBeInjected");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(ongoingInjecter);
    }

    private Field getField(String fieldName) {
        try {
            return MockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}