import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
    public void filterCandidate_singleMockWithMatchingName_shouldReturnSingleMock() {
        // Given
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getName()).thenReturn("mock");

        // When
        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(next, times(1)).filterCandidate(Collections.singletonList(mock), field, fieldInstance);
        verifyNoMoreInteractions(next);
        assertSame(result, next);
    }

    @Test
    public void filterCandidate_singleMockWithNonMatchingName_shouldReturnSingleMock() {
        // Given
        Object mock = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getName()).thenReturn("nonMock");

        // When
        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(next);
        assertSame(result, next);
    }

    @Test
    public void filterCandidate_multipleMocksWithMatchingName_shouldReturnFilteredMocks() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getName()).thenReturn("mock");

        // When
        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(next, times(1)).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
        verifyNoMoreInteractions(next);
        assertSame(result, next);
    }

    @Test
    public void filterCandidate_multipleMocksWithNonMatchingName_shouldReturnAllMocks() {
        // Given
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getName()).thenReturn("nonMock");

        // When
        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(next);
        assertSame(result, next);
    }

    @Test
    public void filterCandidate_noMocks_shouldReturnEmptyResult() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getName()).thenReturn("mock");

        // When
        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(next);
        assertSame(result, next);
    }
}