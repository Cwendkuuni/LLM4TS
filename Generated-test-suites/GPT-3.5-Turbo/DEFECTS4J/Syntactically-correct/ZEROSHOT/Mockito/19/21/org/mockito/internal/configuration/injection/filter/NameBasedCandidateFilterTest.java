import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    @Mock
    private MockCandidateFilter nextFilter;

    @Spy
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testFilterCandidate_singleMockNoNameMatch() throws Exception {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));
        Field field = getClass().getDeclaredField("nextFilter");
        Object fieldInstance = nextFilter;

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_singleMockWithNameMatch() throws Exception {
        Object mock = mock(Object.class);
        Mockito.when(mockUtil.getMockName(mock)).thenReturn("mockFieldName");
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(mock);
        Field field = getClass().getDeclaredField("nextFilter");
        Object fieldInstance = nextFilter;

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocksNoNameMatch() throws Exception {
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = getClass().getDeclaredField("nextFilter");
        Object fieldInstance = nextFilter;

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocksWithNameMatch() throws Exception {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Mockito.when(mockUtil.getMockName(mock1)).thenReturn("mockFieldName");
        Mockito.when(mockUtil.getMockName(mock2)).thenReturn("mockFieldName");
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getClass().getDeclaredField("nextFilter");
        Object fieldInstance = nextFilter;

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocksWithConflictingNameMatch() throws Exception {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Mockito.when(mockUtil.getMockName(mock1)).thenReturn("mockFieldName");
        Mockito.when(mockUtil.getMockName(mock2)).thenReturn("otherMockFieldName");
        List<Object> mocks = new ArrayList<Object>();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getClass().getDeclaredField("nextFilter");
        Object fieldInstance = nextFilter;

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMocks = new ArrayList<Object>();
        expectedMocks.add(mock1);
        verify(nextFilter).filterCandidate(eq(expectedMocks), eq(field), eq(fieldInstance));
    }
}