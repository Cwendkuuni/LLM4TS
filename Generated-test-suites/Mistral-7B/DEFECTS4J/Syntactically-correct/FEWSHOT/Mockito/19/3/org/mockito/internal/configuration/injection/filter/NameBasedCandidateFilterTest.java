package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_OneMock_NoConflictingFields() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertNotNull(result);
        Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_OneMock_ConflictingField() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        mocks.add(mock2);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.when(mockUtil.getMockName(mock1)).thenReturn("field1");
        Mockito.when(mockUtil.getMockName(mock2)).thenReturn("field1");

        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mock1);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(mock1, result);
        Mockito.verify(nextMock).filterCandidate(mockNameMatches, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoConflictingFields() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(String.class));
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertNotNull(result);
        Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ConflictingFields() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(String.class));
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.when(mockUtil.getMockName(mock1)).thenReturn("field1");
        Mockito.when(mockUtil.getMockName(mock2)).thenReturn("field2");

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(mock1, result);

        // Verify that the next filter is called with the correct argument
        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mock2);
        Mockito.verify(nextMock).filterCandidate(mockNameMatches, field, fieldInstance);
    }
}