package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter nextMock;
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtilMock;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        mockUtilMock = Mockito.mock(MockUtil.class);
        filter = new NameBasedCandidateFilter(nextMock);
        Mockito.when(mockUtilMock.getMockName(Mockito.anyObject())).then(invocation -> "mockName");
    }

    @Test
    public void testFilterCandidateWithSingleMockAndMatchingFieldName() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
        assertEquals("mockName", result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndMatchingFieldName() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        mocks.add(Mockito.mock(Object.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mocks.get(0));

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(nextMock).filterCandidate(mockNameMatches, field, fieldInstance);
        assertEquals("mockName", result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndNonMatchingFieldName() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        mocks.add(Mockito.mock(Object.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mocks.get(1));

        Mockito.when(nextMock.filterCandidate(mockNameMatches, field, fieldInstance))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(nextMock).filterCandidate(mockNameMatches, field, fieldInstance);

        assertSame(Mockito.mock(OngoingInjecter.class), result);
    }

    @Test
    public void testFilterCandidateWithEmptyMockCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verifyNoInteractions(nextMock);
        assertNull(result);
    }
}