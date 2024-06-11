package org.mockito.internal.configuration.injection.filter;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testConstructor() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        // verify constructor is executed successfully
    }
}