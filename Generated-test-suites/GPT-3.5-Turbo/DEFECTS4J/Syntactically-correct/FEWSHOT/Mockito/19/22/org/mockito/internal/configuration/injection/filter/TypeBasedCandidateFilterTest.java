package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {
    private TypeBasedCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new TypeBasedCandidateFilter(null);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        List<Object> mocks = new ArrayList<Object>();
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_withMatchingMocks() throws Exception {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add("Mock1");
        mocks.add("Mock2");
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(2, result.getMockTypeMatches().size());
    }

    @Test
    public void testFilterCandidate_withNoMatchingMocks() throws Exception {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add("Mock1");
        mocks.add("Mock2");
        Field field = getClass().getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result.getMockTypeMatches().isEmpty());
    }
}