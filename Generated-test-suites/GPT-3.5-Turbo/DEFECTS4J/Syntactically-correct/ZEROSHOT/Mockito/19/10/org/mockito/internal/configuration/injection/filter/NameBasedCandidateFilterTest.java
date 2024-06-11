import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(any(Object.class))).thenReturn("mock");
    }

    @Test
    public void testFilterCandidate_singleMock_noMatch() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
        verify(mockUtil, never()).getMockName(any());
    }

    @Test
    public void testFilterCandidate_singleMock_match() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock");

        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mocks.iterator().next());

        verify(next, times(1)).filterCandidate(mockNameMatches, field, fieldInstance);
        verify(mockUtil, times(1)).getMockName(any());
    }

    @Test
    public void testFilterCandidate_multipleMocks_match() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock");

        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> mockNameMatches = new ArrayList<>();
        mockNameMatches.add(mocks.iterator().next());

        verify(next, times(1)).filterCandidate(mockNameMatches, field, fieldInstance);
        verify(mockUtil, times(1)).getMockName(any());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatch() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock");

        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
        verify(mockUtil, times(2)).getMockName(any());
    }
}