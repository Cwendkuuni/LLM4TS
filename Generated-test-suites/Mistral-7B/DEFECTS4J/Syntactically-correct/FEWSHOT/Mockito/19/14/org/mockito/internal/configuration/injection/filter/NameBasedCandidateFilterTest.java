package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.MockUtil.getMockName;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_SingleMock() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("Mock1"));

        Field field = mock(Field.class);

        Object fieldInstance = mock(Object.class);

        OngoingInjecter expectedResult = null;

        OngoingInjecter actualResult = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithSameName() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("MockName"));
        mocks.add(createMock("MockName"));

        Field field = mock(Field.class);

        Object fieldInstance = mock(Object.class);

        Object expectedFirstResult = createMock("MockName");

        when(nextMockFilter.filterCandidate(eq(new ArrayList<>()), eq(field), eq(fieldInstance)))
                .thenReturn(expectedFirstResult);

        OngoingInjecter actualResult = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedFirstResult, actualResult.getMock());
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithDifferentNames() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("MockName1"));
        mocks.add(createMock("MockName2"));

        Field field = mock(Field.class);

        Object fieldInstance = mock(Object.class);

        Object expectedFirstResult = createMock("MockName1");

        when(nextMockFilter.filterCandidate(eq(new ArrayList<>()), eq(field), eq(fieldInstance)))
                .thenReturn(expectedFirstResult);

        OngoingInjecter actualResult = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedFirstResult, actualResult.getMock());
    }

    @Test
    public void testFilterCandidate_NoMatchingMock() {
        MockCandidateFilter nextMockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(nextMockFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(createMock("MockName1"));
        mocks.add(createMock("MockName2"));

        Field field = mock(Field.class);

        Object fieldInstance = mock(Object.class);

        String mockName = "MockName3";

        when(getMockName(mocks.get(0))).thenReturn(mockName + "_1");
        when(getMockName(mocks.get(1))).thenReturn(mockName + "_2");

        assertNull(nameBasedFilter.filterCandidate(mocks, field, fieldInstance));
    }

    private Object createMock(String name) {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();
        mockUtil.name(name);
        return mock;
    }
}