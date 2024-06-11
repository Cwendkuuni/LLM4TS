package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter nextFilter;
    private TypeBasedCandidateFilter typeBasedCandidateFilter;

    @Before
    public void setUp() {
        nextFilter = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate() throws NoSuchFieldException, SecurityException {
        Collection<Object> mocks = new ArrayList<>();
        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mockField");
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);

        List<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(new Object());

        assertEquals(ongoingInjecter, typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance));
        assertSame(mockTypeMatches, ongoingInjecter.filterCandidate(mockTypeMatches, field, fieldInstance));
    }
}