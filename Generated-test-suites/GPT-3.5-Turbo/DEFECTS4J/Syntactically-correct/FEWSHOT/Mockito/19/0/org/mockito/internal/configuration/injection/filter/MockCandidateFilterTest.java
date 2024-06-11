package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter = new MockCandidateFilterImpl();

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = null;

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(result);
    }
}