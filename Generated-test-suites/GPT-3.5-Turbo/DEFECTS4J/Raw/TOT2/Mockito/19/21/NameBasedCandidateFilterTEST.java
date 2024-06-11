import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    MockCandidateFilter mockCandidateFilterMock = mock(MockCandidateFilter.class);

    @Test
    public void testFilterCandidate() {
        NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilterMock);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock1");

        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecterMock = mock(OngoingInjecter.class);

        when(mockCandidateFilterMock.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(ongoingInjecterMock);

        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockCandidateFilterMock).filterCandidate(anyCollection(), any(Field.class), any(Object.class));

        assertTrue(result.equals(ongoingInjecterMock));
    }
}