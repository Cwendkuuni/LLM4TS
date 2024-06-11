package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = new ArrayList<>();
        mocks.add(new MockClassA());
        mocks.add(new MockClassB());
        Field field = null;
        Object fieldInstance = null;
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mocks, result.getMocks());

        List<Object> filteredMocks = new ArrayList<>();
        filteredMocks.add(new MockClassA());
        result.setMocks(filteredMocks);

        assertEquals(filteredMocks, result.getMocks());
    }

    class MockClassA {
    }

    class MockClassB {
    }
}