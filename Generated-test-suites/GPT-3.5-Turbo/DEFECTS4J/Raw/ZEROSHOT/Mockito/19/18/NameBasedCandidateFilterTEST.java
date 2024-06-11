import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        mockUtil = mock(MockUtil.class);
    }

    @Test
    public void testFilterCandidate_OnlyOneMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil, never()).getMockName(any());
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(mockCandidateFilter, result);
    }

    @Test
    public void testFilterCandidate_MultipleMocksNoNameMatches() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil, times(2)).getMockName(any());
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(mockCandidateFilter, result);
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithNameMatches() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn(field.getName());

        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mock2);

        when(mockCandidateFilter.filterCandidate(mockNameMatches, field, fieldInstance)).thenReturn(mockCandidateFilter);

        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockUtil, times(2)).getMockName(any());
        verify(mockCandidateFilter).filterCandidate(mockNameMatches, field, fieldInstance);
        assertEquals(mockCandidateFilter, result);
    }
}