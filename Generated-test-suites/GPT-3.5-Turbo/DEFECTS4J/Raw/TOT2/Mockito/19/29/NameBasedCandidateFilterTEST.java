import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private MockUtil mockUtil;
    private MockCandidateFilter nextFilter;

    @Before
    public void setUp() {
        mockUtil = mock(MockUtil.class);
        nextFilter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new MockUtil();

        when(mockUtil.getMockName(mock1)).thenReturn("mock1");
        when(mockUtil.getMockName(mock2)).thenReturn("mock2");

        assertNotNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    // Edge case: Empty mocks collection
    @Test
    public void testFilterCandidate_EmptyMocks() throws Exception {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new MockUtil();

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    // Edge case: Multiple mocks with the same name
    @Test
    public void testFilterCandidate_MultipleMocksSameName() throws Exception {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new MockUtil();

        when(mockUtil.getMockName(mock1)).thenReturn("mock");
        when(mockUtil.getMockName(mock2)).thenReturn("mock");

        assertNotNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

}