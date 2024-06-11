package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        next = Mockito.mock(MockCandidateFilter.class);
        mockUtil = spy(new MockUtil());
    }

    @Test
    public void testFilterCandidate_oneMock_noConflict() {
        Object mock = mock(Object.class);
        Field field = Mockito.field(Object.class, "fieldName");
        Object fieldInstance = mock(Object.class);

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        OngoingInjecter result = filter.filterCandidate(new ArrayList<>(), field, fieldInstance);

        assertEquals(mock, result.getCandidate());
        Mockito.verify(next, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks_noConflict() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = Mockito.field(Object.class, "fieldName");
        Object fieldInstance = mock(Object.class);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock1, result.getCandidate());
        Mockito.verify(next, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks_conflict() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = Mockito.field(Object.class, "fieldName");
        Object fieldInstance = mock(Object.class);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Mockito.when(mockUtil.getMockName(mock1)).thenReturn("mock1Name");
        Mockito.when(mockUtil.getMockName(mock2)).thenReturn("mock1Name");

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock2, result.getCandidate());
        Mockito.verify(next).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatch() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = Mockito.field(Object.class, "anotherFieldName");
        Object fieldInstance = mock(Object.class);

        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result.getCandidate());
        Mockito.verify(next).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
    }
}