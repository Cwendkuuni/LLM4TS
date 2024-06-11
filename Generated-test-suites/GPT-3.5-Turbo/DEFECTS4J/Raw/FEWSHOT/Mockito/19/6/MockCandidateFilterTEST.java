package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;
import org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {
    private MockCandidateFilter mockCandidateFilter;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        fieldToBeInjected = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(result);
        assertEquals(ongoingInjecter, result);
    }
}