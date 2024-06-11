package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import static org.mockito.internal.util.MockUtil.getMockName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_SingleMock() {
        MockUtil mockUtil = new MockUtil();
        String mockName = "mockName";
        Object mock = Mockito.mock(String.class);
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = MockUtil.findField(mock.getClass(), "fieldName");

        Object result = filter.filterCandidate(mocks, field, mock);
        assertEquals(mock, result);
        assertEquals(mockName, getMockName(mock));
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithSameName() {
        MockUtil mockUtil = new MockUtil();
        String mockName = "mockName";

        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = MockUtil.findField(mock1.getClass(), "fieldName");

        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, null);
        assertEquals(mock1, result.get(0));
        assertEquals(mockName, getMockName(mock1));
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithDifferentNames() {
        MockUtil mockUtil = new MockUtil();
        String mockName1 = "mockName1";
        String mockName2 = "mockName2";

        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = MockUtil.findField(mock1.getClass(), "fieldName");

        List<Object> result1 = (List<Object>) filter.filterCandidate(mocks, field, null);
        assertEquals(mock1, result1.get(0));
        assertEquals(mockName1, getMockName(mock1));

        Field field2 = MockUtil.findField(mock2.getClass(), "fieldName");

        List<Object> result2 = (List<Object>) filter.filterCandidate(mocks, field2, null);
        assertEquals(mock2, result2.get(0));
        assertEquals(mockName2, getMockName(mock2));
    }

    @Test
    public void testFilterCandidate_ConflictingNamingFields() {
        MockUtil mockUtil = new MockUtil();
        String mockName1 = "mockName1";
        String mockName2 = "mockName2";

        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        MockCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field1 = MockUtil.findField(mock1.getClass(), "fieldName1");
        Field field2 = MockUtil.findField(mock2.getClass(), "fieldName1");

        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field1, null);
        assertEquals(mock2, result.get(0));
        assertEquals(mockName2, getMockName(mock2));
    }

    @Test
    public void testFilterCandidate_NextFilter() {
        MockUtil mockUtil = new MockUtil();
        String mockName = "mockName";

        Object mock = Mockito.mock(String.class);
        MockCandidateFilter filter = new NameBasedCandidateFilter(new NameBasedCandidateFilter(null));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = MockUtil.findField(mock.getClass(), "fieldName");

        MockCandidateFilter nextFilter = Mockito.mock(MockCandidateFilter.class);

        List<Object> result = (List<Object>) filter.filterCandidate(mocks, field, null);
        assertEquals(mock, result.get(0));
        assertEquals(mockName, getMockName(mock));

        when(nextFilter.filterCandidate(mocks, field, null)).thenReturn(null);

        result = (List<Object>) filter.filterCandidate(mocks, field, null);
        assertNull(result);
    }
}