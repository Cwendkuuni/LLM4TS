import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = mock(MockCandidateFilter.class);
        mocks = mock(Collection.class);
        fieldToBeInjected = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(filter.filterCandidate(eq(mocks), eq(fieldToBeInjected), eq(fieldInstance))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidateReturnsNull() {
        when(filter.filterCandidate(eq(mocks), eq(fieldToBeInjected), eq(fieldInstance))).thenReturn(null);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidateWithEmptyMocks() {
        Collection<Object> emptyMocks = Collections.emptyList();
        when(filter.filterCandidate(eq(emptyMocks), eq(fieldToBeInjected), eq(fieldInstance))).thenReturn(null);

        OngoingInjecter result = filter.filterCandidate(emptyMocks, fieldToBeInjected, fieldInstance);

        assertNull(result);
    }

}