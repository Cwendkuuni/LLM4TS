package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.name.Name;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;
    private MockUtil mockUtilSpy;

    @Before
    public void setUp() {
        mockUtilSpy = Mockito.spy(new MockUtil());
        nextFilter = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_noConflictingFields_nextFilterCalled() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = Mockito.spy(Mockito.field(Object.class, "fieldName"));
        Name mockName = Mockito.mock(Name.class);
        Mockito.when(mockUtilSpy.getMockName(Mockito.anyObject())).thenReturn(mockName);
        Mockito.when(mockName.toString()).thenReturn("mockName");
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        verifyZeroInteractions(filter);
    }

    @Test
    public void testFilterCandidate_conflictingFields_filterCalledWithCorrectMock() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = Mockito.spy(Mockito.field(Object.class, "fieldName"));
        Name mockName1 = Mockito.mock(Name.class);
        Name mockName2 = Mockito.mock(Name.class);
        Mockito.when(mockUtilSpy.getMockName(mocks.get(0))).thenReturn(mockName1);
        Mockito.when(mockName1.toString()).thenReturn("mockName1");
        Mockito.when(mockUtilSpy.getMockName(mocks.get(1))).thenReturn(mockName2);
        Mockito.when(mockName2.toString()).thenReturn("mockName2");
        Mockito.when(nextFilter.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class))).thenCallRealMethod();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        OngoingInjecter expectedResult = nextFilter.filterCandidate(mocks, field, fieldInstance);
        verify(nextFilter).filterCandidate(Mockito.argThat(mocks1 -> mocks1.contains(mocks.get(1))), field, fieldInstance);
        verify(filter).filterCandidate(mocks, field, fieldInstance);
        verify(result).inject(field, fieldInstance);
        verify(expectedResult).inject(field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_conflictingFields_nextFilterCalledWithCorrectMocks() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = Mockito.spy(Mockito.field(Object.class, "fieldName"));
        Name mockName1 = Mockito.mock(Name.class);
        Name mockName2 = Mockito.mock(Name.class);
        Mockito.when(mockUtilSpy.getMockName(mocks.get(0))).thenReturn(mockName1);
        Mockito.when(mockName1.toString()).thenReturn("mockName1");
        Mockito.when(mockUtilSpy.getMockName(mocks.get(1))).thenReturn(mockName2);
        Mockito.when(mockName2.toString()).thenReturn("mockName2");

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Collection<Object> nextMocks = new ArrayList<>();
        nextMocks.add(mocks.get(1));
        verify(nextFilter).filterCandidate(nextMocks, field, fieldInstance);
        verify(filter).filterCandidate(mocks, field, fieldInstance);
    }
}