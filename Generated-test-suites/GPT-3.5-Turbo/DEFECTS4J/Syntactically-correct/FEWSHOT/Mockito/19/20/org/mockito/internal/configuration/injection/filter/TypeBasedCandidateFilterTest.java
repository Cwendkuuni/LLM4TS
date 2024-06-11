package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private List<Object> mocks;
    private Field field;

    @Before
    public void setUp() throws Exception {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
        mocks = new ArrayList<>();
        field = getClass().getDeclaredField("mocks");
    }

    @Test
    public void testFilterCandidate() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        OngoingInjecter result = mock(OngoingInjecter.class);
        
        assertEquals(result, filter.filterCandidate(mocks, field, ongoingInjecter));
    }
}