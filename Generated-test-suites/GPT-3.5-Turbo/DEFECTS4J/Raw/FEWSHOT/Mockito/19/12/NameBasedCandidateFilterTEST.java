package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = spy(new MockUtil());
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        
        when(field.getName()).thenReturn("fieldName");
        when(mockUtil.getMockName(mock)).thenReturn(new StringBuilder("fieldName"));
        when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(null);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));

        verify(next).filterCandidate(Collections.singletonList(mock), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        
        when(field.getName()).thenReturn("fieldName");
        when(mockUtil.getMockName(mock1)).thenReturn(new StringBuilder("fieldName"));
        when(mockUtil.getMockName(mock2)).thenReturn(new StringBuilder("differentName"));
        when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(null);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));

        verify(next).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_noMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        Collection<Object> mocks = new ArrayList<>();
        
        when(field.getName()).thenReturn("fieldName");

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));

        verify(next).filterCandidate(mocks, field, fieldInstance);
    }
}