package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Field field = MyClass.class.getDeclaredField("myField");
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        when(nextFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

        // Test case 1: no matching names
        filter.filterCandidate(mocks, field, fieldInstance);
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);

        // Test case 2: one matching name
        when(mock1.getName()).thenReturn("myField");
        filter.filterCandidate(mocks, field, fieldInstance);
        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mock1);
        verify(nextFilter).filterCandidate(mockNameMatches, field, fieldInstance);

        // Test case 3: multiple matching names
        when(mock2.getName()).thenReturn("myField");
        when(mock3.getName()).thenReturn("myField");
        filter.filterCandidate(mocks, field, fieldInstance);
        mockNameMatches.add(mock2);
        mockNameMatches.add(mock3);
        verify(nextFilter).filterCandidate(mockNameMatches, field, fieldInstance);
    }

    static class MyClass {
        Object myField;
    }
}