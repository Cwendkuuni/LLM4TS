
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {
    private final MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
    private final NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
    private final MockUtil mockUtil = new MockUtil();

    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        List<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks_sameName() throws Exception {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Object.class.getDeclaredField("field");
        when(mockUtil.getMockName(mock1)).thenReturn("mock");
        when(mockUtil.getMockName(mock2)).thenReturn("mock");

        Object fieldInstance = new Object();

        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMocks = new ArrayList<>();
        expectedMocks.add(mock1);
        expectedMocks.add(mock2);

        verify(mockCandidateFilter).filterCandidate(expectedMocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks_differentNames() throws Exception {
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Object.class.getDeclaredField("field");
        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        Object fieldInstance = new Object();

        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMocks = new ArrayList<>();
        expectedMocks.add(mock1);
        expectedMocks.add(mock2);

        verify(mockCandidateFilter).filterCandidate(expectedMocks, field, fieldInstance);
    }
}

