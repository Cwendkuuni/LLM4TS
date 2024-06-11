package org.mockito.internal.configuration.injection.filter;

import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<Object>();
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = mock(Object.class);

        // Test case 1: Field name matches mock name
        Object mock1 = mock(Object.class);
        when(mock1.getClass()).thenReturn(Object.class);
        when(mock1.toString()).thenReturn("next");
        mocks.add(mock1);

        Object mock2 = mock(Object.class);
        when(mock2.getClass()).thenReturn(Object.class);
        when(mock2.toString()).thenReturn("otherMock");
        mocks.add(mock2);

        OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(anyObject(), eq(field), eq(fieldInstance));
        assertEquals("next", result1);

        // Test case 2: Multiple mocks with same name
        Object mock3 = mock(Object.class);
        when(mock3.getClass()).thenReturn(Object.class);
        when(mock3.toString()).thenReturn("next");
        mocks.add(mock3);

        OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(2)).filterCandidate(anyObject(), eq(field), eq(fieldInstance));
        assertNotNull(result2);
    }
}