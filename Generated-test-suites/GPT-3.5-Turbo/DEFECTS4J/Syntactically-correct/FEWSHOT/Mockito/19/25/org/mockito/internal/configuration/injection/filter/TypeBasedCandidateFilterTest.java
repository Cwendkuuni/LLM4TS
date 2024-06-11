package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws NoSuchFieldException, IllegalAccessException {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mocks");
        Object fieldInstance = null;

        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);

        List<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        
        when(mockCandidateFilter.filterCandidate(anyList(), eq(field), eq(fieldInstance))).thenReturn(ongoingInjecter);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(ongoingInjecter, result);
        verify(mockCandidateFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
    }
}