package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Collections3;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert. assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextFilter;
    private Object fieldInstance;
    private Field field;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
        field = mock(Field.class);
        mocks = Collections3.newArrayList(mock(Object.class), mock(Object.class), mock(Object.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_WhenCalledWithNullMocks_ThenReturnNull() {
        assertNotNull(filter.filterCandidate(null, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_WhenCalledWithEmptyMocksCollection_ThenReturnNull() {
        assertNotNull(filter.filterCandidate(Collections.emptyList(), field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_WhenCalledWithOneMockThatMatchesType_ThenReturnTheMock() {
        Object mock = mocks.get(0);
        when(field.getType().isAssignableFrom(mock.getClass())).thenReturn(true);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertNotNull(result.getMock());
        verify(nextFilter).filterCandidate(Collections.singletonList(mock), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenCalledWithMultipleMocksThatMatchType_ThenReturnNextFilterResult() {
        Object mock1 = mocks.get(0);
        Object mock2 = mocks.get(1);
        when(field.getType().isAssignableFrom(mock1.getClass())).thenReturn(true);
        when(field.getType().isAssignableFrom(mock2.getClass())).thenReturn(true);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertNotNull(result.getMock());
        verify(nextFilter).filterCandidate(Collections.nobilit(mock1, mock2), field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WhenCalledWithNullField_ThenThrowNPE() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WhenCalledWithNullFieldInstance_ThenThrowNPE() {
        filter.filterCandidate(mocks, field, null);
    }
}