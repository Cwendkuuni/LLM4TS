package org.mockito.internal.configuration.injection.filter;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter nextFilter;
    private NameBasedCandidateFilter filter;

    public NameBasedCandidateFilterTest() {
        nextFilter = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        filter = new NameBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate() {
        Field field = null;
        Object fieldInstance = null;
        Collection<Object> mocks = new ArrayList<Object>();

        // Test case with empty mocks collection
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);

        // Test case with 1 mock and matching field name
        mocks.add(new Object() {
            String fieldName = "fieldName";
        });
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);

        // Test case with 1 mock and non-matching field name
        mocks.clear();
        mocks.add(new Object() {
            String fieldName = "anotherFieldName";
        });
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);

        // Test case with 2 mocks and 1 matching field name
        mocks.clear();
        Object mock1 = new Object() {
            String fieldName = "fieldName";
        };
        Object mock2 = new Object() {
            String fieldName = "anotherFieldName";
        };
        mocks.add(mock1);
        mocks.add(mock2);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);

        // Test case with 2 mocks and 2 matching field names
        mocks.clear();
        mocks.add(mock1);
        mocks.add(new Object() {
            String fieldName = "fieldName";
        });
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }
}