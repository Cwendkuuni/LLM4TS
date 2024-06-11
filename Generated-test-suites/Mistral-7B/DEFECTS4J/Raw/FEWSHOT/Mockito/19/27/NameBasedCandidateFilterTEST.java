package org.mockito.internal.configuration.injection.filter;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.mock;

public class NameBasedCandidateFilterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testFilterCandidate_SingleMock_NoConflict() {
        String mockName = "MyMock";
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class, mockName));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidate_SingleMock_Conflict() {
        String mockName = "MyMock";
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object1.class, mockName));
        mocks.add(mock(Object2.class, mockName));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(mock(Object1.class, mockName));

        NameBasedCandidateFilter spyFilter = spy(NameBasedCandidateFilter.class);
        doReturn(expectedResult).when(spyFilter).filterCandidate(mocks, field, fieldInstance);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(expectedResult.get(0), result);
        verify(spyFilter, times(1)).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoConflict() {
        String mockName1 = "Mock1";
        String mockName2 = "Mock2";

        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object1.class, mockName1));
        mocks.add(mock(Object2.class, mockName2));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Conflict() {
        String mockName = "MyMock";
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object1.class, mockName));
        mocks.add(mock(Object1.class, mockName));

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(mocks.get(0));

        NameBasedCandidateFilter spyFilter = spy(NameBasedCandidateFilter.class);
        doReturn(expectedResult).when(spyFilter).filterCandidate(mocks, field, fieldInstance);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(expectedResult.get(0), result);
        verify(spyFilter, times(1)).filterCandidate(mocks, field, fieldInstance);
    }
}

class Object1 {
    @Override
    public String toString() {
        return mockName;
    }
}

class Object2 {
    @Override
    public String toString() {
        return mockName;
    }
}