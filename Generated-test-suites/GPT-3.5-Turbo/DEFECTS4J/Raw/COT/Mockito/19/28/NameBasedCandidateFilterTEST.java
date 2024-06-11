

// Complete JUnit 4 test file for the given NameBasedCandidateFilter class

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;

    @Test
    public void testFilterCandidate() throws NoSuchFieldException, IllegalAccessException {
        // Prepare mocks and field instance
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = this.getClass().getDeclaredField("filter");
        Object fieldInstance = this;

        // Test case for when the mocks have matching names
        NameBasedCandidateFilter next = mock(NameBasedCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        when(next.filterCandidate(anyList(), any(), any())).thenReturn(mock(OngoingInjecter.class));
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(any())).thenReturn(new ObjectName("filter"));
        whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyList(), any(), any());
        verify(mockUtil, times(2)).getMockName(any());
    }
}