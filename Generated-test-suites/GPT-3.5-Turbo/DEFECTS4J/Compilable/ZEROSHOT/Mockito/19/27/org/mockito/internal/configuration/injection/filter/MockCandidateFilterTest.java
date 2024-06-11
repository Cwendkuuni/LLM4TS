package org.mockito.internal.configuration.injection.filter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return null;
            }
        };
    }

    @Test
    public void filterCandidate_shouldReturnNull() {
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNull(ongoingInjecter);
    }
}