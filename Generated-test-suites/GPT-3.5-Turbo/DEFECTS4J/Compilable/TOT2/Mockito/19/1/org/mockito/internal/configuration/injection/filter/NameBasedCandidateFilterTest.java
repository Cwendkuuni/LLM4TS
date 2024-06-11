package org.mockito.internal.configuration.injection.filter;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Typical Use Case
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;
        assertEquals(null, filter.filterCandidate(mocks, field, fieldInstance));

        // Edge Case - checks if filterCandidate returns the correct result when passing non-matching mock names
        NameBasedCandidateFilter filter2 = new NameBasedCandidateFilter(null);
        Collection<Object> mocks2 = new ArrayList<>();
        Field field2 = null;
        Object fieldInstance2 = null;
        mocks2.add(new Object());
        assertEquals(null, filter2.filterCandidate(mocks2, field2, fieldInstance2));

        // Error Scenario - checks if filterCandidate handles null mocks collection
        NameBasedCandidateFilter filter3 = new NameBasedCandidateFilter(null);
        Field field3 = null;
        Object fieldInstance3 = null;
        assertThrows(NullPointerException.class, () -> {
            filter3.filterCandidate(null, field3, fieldInstance3);
        });
    }
}