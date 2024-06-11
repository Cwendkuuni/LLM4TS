
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private MockUtil mockUtil;
    private NameBasedCandidateFilter filter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        mockUtil = mock(MockUtil.class);
        filter = new NameBasedCandidateFilter(next);
        filter.mockUtil = mockUtil;
    }

    @Test
    public void testFilterCandidate_singleMockWithNameMatch() throws Exception {
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenReturn("mockName");
        when(field.getName()).thenReturn("mockName");

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyList(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocksWithNoNameMatch() throws Exception {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenReturn("mockName");
        when(field.getName()).thenReturn("differentName");

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocksWithNameMatch() throws Exception {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(any())).thenReturn("mockName");
        when(field.getName()).thenReturn("mockName");

        List<Object> mockNameMatches = new ArrayList<Object>();
        mockNameMatches.add(mocks.iterator().next());

        when(next.filterCandidate(anyList(), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(eq(mockNameMatches), eq(field), eq(fieldInstance));
    }

    // TODO: Add additional test cases for potential edge cases and exception handling scenarios

}
