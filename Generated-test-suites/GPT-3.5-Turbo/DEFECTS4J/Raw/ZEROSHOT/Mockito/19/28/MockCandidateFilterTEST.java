import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Field field;

    @Before
    public void setUp() throws NoSuchFieldException {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        field = MockCandidateFilterTest.class.getDeclaredField("mockCandidateFilter");
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        OngoingInjecter injecter = mock(OngoingInjecter.class);

        when(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(injecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(injecter, result);
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }
}