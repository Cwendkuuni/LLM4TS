import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_multipleMocks() throws Exception {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(anyList(), any(Field.class), anyObject());

        List<Object> mockNameMatches = new ArrayList<Object>();
        mockNameMatches.add(mocks.iterator().next());

        verify(next).filterCandidate(eq(mockNameMatches), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(mock(Object.class));

        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_noMocks() throws Exception {
        Collection<Object> mocks = new ArrayList<Object>();

        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next, times(1)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }
}