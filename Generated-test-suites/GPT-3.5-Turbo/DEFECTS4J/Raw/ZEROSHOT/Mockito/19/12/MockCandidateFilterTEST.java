import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;
    private OngoingInjecter ongoingInjecter;

    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        mocks = mock(Collection.class);
        fieldToBeInjected = mock(Field.class);
        fieldInstance = mock(Object.class);

        ongoingInjecter = mock(OngoingInjecter.class);
    }

    @Test
    public void testFilterCandidate() {
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);

        verifyZeroInteractions(mocks);
        verifyZeroInteractions(fieldToBeInjected);
        verifyZeroInteractions(fieldInstance);

        verifyZeroInteractions(ongoingInjecter);
    }
}