import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new MockCandidateFilterImpl();
        mocks = new ArrayList<>();
        fieldToBeInjected = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(ongoingInjecter, result);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }

    private class MockCandidateFilterImpl implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return mock(OngoingInjecter.class);
        }
    }
}