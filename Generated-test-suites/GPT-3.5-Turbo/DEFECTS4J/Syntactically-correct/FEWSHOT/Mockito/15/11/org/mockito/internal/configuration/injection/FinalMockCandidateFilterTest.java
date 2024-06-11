package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        mocks.add(new Object());
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }
}