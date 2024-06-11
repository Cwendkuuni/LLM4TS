package org.mockito.internal.configuration.injection.filter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };

        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);

        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockCandidateFilter, ongoingInjecter);
    }

}