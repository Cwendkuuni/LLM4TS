package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private List<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockCandidateFilter = new TypeBasedCandidateFilter(null);
        mocks = new ArrayList<Object>();
        field = null;
        fieldInstance = null;
    }

    @Test
    public void testFilterCandidate() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        Object mock3 = new Object();

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mocks.size(), result.getMockTypeMatches().size());
        assertTrue(result.getMockTypeMatches().containsAll(mocks));
    }

}