package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

public class TypeBasedCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        ArrayList<Object> mocks = new ArrayList<Object>();
        Field field = null;
        Object fieldInstance = null;
        
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(result);
    }
}