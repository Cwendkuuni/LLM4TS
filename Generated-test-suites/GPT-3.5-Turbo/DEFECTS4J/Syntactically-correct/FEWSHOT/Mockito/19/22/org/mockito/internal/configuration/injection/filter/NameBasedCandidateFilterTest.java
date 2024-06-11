package org.mockito.internal.configuration.injection.filter;

import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NameBasedCandidateFilterTest {
    @Mock
    private MockCandidateFilter mockCandidateFilter;

    private NameBasedCandidateFilter nameBasedCandidateFilter;

    @Before
    public void setUp() {
        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_singleMock_noMockNameMatches() {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field, fieldInstance);
        assertNull(ongoingInjecter);
    }
    
    @Test
    public void testFilterCandidate_singleMock_mockNameMatches() {
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock = mock(Object.class);
        when(mockUtil.getMockName(mock).toString()).thenReturn("field");

        mocks.add(mock);

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("field");

        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field, fieldInstance);
        assertNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMockNameMatches() {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field, fieldInstance);
        assertNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_multipleMocks_mockNameMatches() {
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(mockUtil.getMockName(mock1).toString()).thenReturn("field");
        when(mockUtil.getMockName(mock2).toString()).thenReturn("field");

        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("field");

        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockCandidateFilter, times(1)).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertNull(ongoingInjecter);
    }
}