import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        mockUtil = new MockUtil();
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_withMultipleMocksAndMatchingName_shouldReturnFilteredSet() throws NoSuchFieldException {
        // Setup
        Field field = NameBasedCandidateFilterTest.class.getDeclaredField("next");
        Set<Object> mocks = new HashSet<>(Arrays.asList(mock(Object.class), mock(Object.class)));
        Object mockWithName = mock(Object.class);
        when(mockUtil.getMockName(mockWithName)).thenReturn("next");

        // Execution
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        // Verification
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        assertEquals(1, ((NameBasedCandidateFilter.OngoingInjecter) result).getFilteredMocks().size());
        assertTrue(((NameBasedCandidateFilter.OngoingInjecter) result).getFilteredMocks().contains(mockWithName));
        verify(next).filterCandidate(anyCollection(), eq(field), isNull());
    }

    @Test
    public void testFilterCandidate_withMultipleMocksAndMatchingName_shouldReturnAllMocksWhenNameDoesNotMatch() throws NoSuchFieldException {
        // Setup
        Field field = NameBasedCandidateFilterTest.class.getDeclaredField("next");
        Set<Object> mocks = new HashSet<>(Arrays.asList(mock(Object.class), mock(Object.class)));
        Object mockWithDifferentName = mock(Object.class);
        when(mockUtil.getMockName(mockWithDifferentName)).thenReturn("different");

        // Execution
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        // Verification
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        assertEquals(2, ((NameBasedCandidateFilter.OngoingInjecter) result).getFilteredMocks().size());
        assertTrue(((NameBasedCandidateFilter.OngoingInjecter) result).getFilteredMocks().containsAll(mocks));
        verify(next).filterCandidate(anyCollection(), eq(field), isNull());
    }

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnFilteredSet() throws NoSuchFieldException {
        // Setup
        Field field = NameBasedCandidateFilterTest.class.getDeclaredField("next");
        Set<Object> mocks = new HashSet<>(Collections.singletonList(mock(Object.class)));
        Object mockWithName = mock(Object.class);
        when(mockUtil.getMockName(mockWithName)).thenReturn("next");

        // Execution
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        // Verification
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        assertEquals(1, ((NameBasedCandidateFilter.OngoingInjecter) result).getFilteredMocks().size());
        assertTrue(((NameBasedCandidateFilter.OngoingInjecter) result).getFilteredMocks().contains(mockWithName));
        verify(next).filterCandidate(anyCollection(), eq(field), isNull());
    }

    @Test
    public void testFilterCandidate_withEmptyMockSet_shouldReturnEmptySet() throws NoSuchFieldException {
        // Setup
        Field field = NameBasedCandidateFilterTest.class.getDeclaredField("next");
        Set<Object> mocks = new HashSet<>();

        // Execution
        NameBasedCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        // Verification
        assertTrue(result instanceof NameBasedCandidateFilter.OngoingInjecter);
        assertEquals(0, ((NameBasedCandidateFilter.OngoingInjecter) result).getFilteredMocks().size());
        verify(next).filterCandidate(anyCollection(), eq(field), isNull());
    }
}