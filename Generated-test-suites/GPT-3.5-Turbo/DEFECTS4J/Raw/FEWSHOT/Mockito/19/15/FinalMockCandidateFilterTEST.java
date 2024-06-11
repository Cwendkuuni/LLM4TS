package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Object mock = new Object();
        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);

        Object injected = result.thenInject();
        assertEquals(mock, injected);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        List<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);

        Object injected = result.thenInject();
        assertNull(injected);
    }
}