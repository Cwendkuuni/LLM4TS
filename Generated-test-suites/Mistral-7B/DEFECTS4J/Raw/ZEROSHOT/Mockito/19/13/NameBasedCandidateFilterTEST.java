package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.Name;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.MockUtil.getMockName;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_oneMock_noConflict() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        Field field = MockUtil.getAccessibleField(String.class, "value");
        Object fieldInstance = "test";

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(result, mocks.get(0));
        verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_oneMock_conflict() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class, "mock1"));
        mocks.add(Mockito.mock(String.class, "mock2"));
        Field field = MockUtil.getAccessibleField(String.class, "value");
        Object fieldInstance = "test";

        List<Object> expectedResult = new ArrayList<>();
        expectedResult.add(mocks.get(1));

        when(nextMock.filterCandidate(eq(mocks), eq(field), eq(fieldInstance)))
                .thenReturn( null );

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(result, expectedResult.get(0));
        verify(nextMock, never()).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks_conflict() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class, "mock1"));
        mocks.add(Mockito.mock(String.class, "mock2"));
        mocks.add(Mockito.mock(String.class, "mock3"));
        Field field = MockUtil.getAccessibleField(String.class, "value");
        Object fieldInstance = "test";

        List<Object> expectedResult = new ArrayList<>();
        expectedResult.add(mocks.get(1));

        when(nextMock.filterCandidate(anyCollection(), eq(field), eq(fieldInstance)))
                .thenAnswer(i -> null);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(result, expectedResult.get(0));
        verify(nextMock, never()).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks_noConflict() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class, "mock1"));
        mocks.add(Mockito.mock(String.class, "mock2"));
        mocks.add(Mockito.mock(String.class, "mock3"));
        Field field = MockUtil.getAccessibleField(String.class, "value");
        Object fieldInstance = "test";

        Object expectedResult = mocks.get(1);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(result, expectedResult);
        verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }
}