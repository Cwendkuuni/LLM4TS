package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new Collection<Object>();
        Field field = new Field();
        Object fieldInstance = new Object();

        // Test case 1
        mocks.add("mockObject1");
        OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result1);
        assertEquals("mockObject1", result1.thenInject());

        // Test case 2
        mocks.clear();
        OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result2);
        assertNull(result2.thenInject());
    }
}