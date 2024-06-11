package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        List<Object> mocks = new ArrayList<Object>();

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(field.getName()).thenReturn("mock1");

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        verify(field, times(2)).getName();
        verify(mockUtil, times(2)).getMockName(anyObject());

        assertEquals(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidate_singleMock() {
        List<Object> mocks = new ArrayList<Object>();

        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(field.getName()).thenReturn("mock1");

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        verify(field).getName();
        verify(mockUtil).getMockName(anyObject());

        assertEquals(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidate_noMocks() {
        List<Object> mocks = new ArrayList<Object>();

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(field.getName()).thenReturn("mock1");

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        verify(field).getName();

        assertEquals(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidate_nullMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(field.getName()).thenReturn("mock1");

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(anyCollection(), eq(field), eq(fieldInstance))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(null, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        verify(field).getName();

        assertEquals(ongoingInjecter, result);
    }

}