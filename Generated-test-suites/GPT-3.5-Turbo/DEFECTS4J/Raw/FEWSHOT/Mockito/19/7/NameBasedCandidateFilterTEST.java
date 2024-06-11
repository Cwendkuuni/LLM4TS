package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws NoSuchFieldException {
        // Set up mocks
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        MockUtil mockUtil = mock(MockUtil.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        
        // Set up test data
        Collection<Object> mocks = new ArrayList<>();
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        // Initialize mock behavior
        when(next.filterCandidate(anyList(), any(Field.class), any(Object.class))).thenReturn(null);
        when(mockUtil.getMockName(anyObject())).thenReturn(new StringBuilder("next"));

        // Perform the test
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));

        // Verify the mock interactions
        verify(next).filterCandidate(mocks, field, fieldInstance);
        verify(mockUtil).getMockName(anyObject());
    }
}