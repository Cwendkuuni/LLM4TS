package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        MockCandidateFilter nextMock = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(new MockClass());
        
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = new Object();
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(new OngoingInjecter(), result);
    }
}