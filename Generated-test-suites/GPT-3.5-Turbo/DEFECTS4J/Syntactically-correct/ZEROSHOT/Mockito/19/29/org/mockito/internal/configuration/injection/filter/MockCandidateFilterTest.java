import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter injecter = mock(OngoingInjecter.class);

        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(injecter);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(result);
        assertEquals(injecter, result);

        verify(filter, times(1)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}