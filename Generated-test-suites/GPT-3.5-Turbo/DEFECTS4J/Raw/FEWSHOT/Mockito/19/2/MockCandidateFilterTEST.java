
package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_noMocks() throws Exception {
        Field field = MockCandidateFilterTest.class.getDeclaredField("mocks");
        Object fieldInstance = new ArrayList<>();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_withMocks() throws Exception {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        Field field = MockCandidateFilterTest.class.getDeclaredField("mocks");
        Object fieldInstance = new ArrayList<>();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_invalidField() throws Exception {
        Field field = MockCandidateFilterTest.class.getDeclaredField("nonExistentField");
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(ongoingInjecter);
    }
}
