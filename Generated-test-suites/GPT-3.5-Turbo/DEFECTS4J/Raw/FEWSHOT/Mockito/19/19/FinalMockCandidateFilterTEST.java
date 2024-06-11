package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Setup
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = null;
        Object fieldInstance = new Object();

        // Test case 1: Single mock
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        verifyNoInteractions(mockitoArgumentTypes);

        assertEquals(mocks.iterator().next(), result);

        // Test case 2: Multiple mocks
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));

        ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        result = ongoingInjecter.thenInject();

        verifyNoInteractions(mockitoArgumentTypes);

        assertNull(result);
    }
}