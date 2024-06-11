package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_singleMatch() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Dummy.class.getDeclaredField("field");
        Object fieldInstance = new Dummy();

        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        List<Object> expected = new ArrayList<>();
        expected.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).getMockName(mock1);
        verify(mockUtil).getMockName(mock2);

        assertEquals(expected, result);
        verify(next).filterCandidate(expected, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMatches() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Dummy.class.getDeclaredField("field");
        Object fieldInstance = new Dummy();

        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        when(mockUtil.getMockName(mock1)).thenReturn("field");
        when(mockUtil.getMockName(mock2)).thenReturn("field");

        List<Object> expected = new ArrayList<>();
        expected.add(mock1);
        expected.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil, times(2)).getMockName(anyObject());

        assertEquals(expected, result);
        verify(next).filterCandidate(expected, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_noMatches() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Dummy.class.getDeclaredField("field");
        Object fieldInstance = new Dummy();

        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil).getMockName(mock1);
        verify(mockUtil).getMockName(mock2);

        assertEquals(mocks, result);
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    class Dummy {
        private Object field;
    }
}