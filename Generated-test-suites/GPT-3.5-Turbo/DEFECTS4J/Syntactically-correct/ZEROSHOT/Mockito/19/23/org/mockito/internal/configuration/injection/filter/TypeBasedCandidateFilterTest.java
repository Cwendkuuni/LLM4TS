import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();

        Object mock1 = mock(Object.class);
        when(mock1.getClass()).thenReturn(Object.class);

        Object mock2 = mock(String.class);
        when(mock2.getClass()).thenReturn(String.class);

        Object mock3 = mock(Integer.class);
        when(mock3.getClass()).thenReturn(Integer.class);

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        List<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mock1);
        mockTypeMatches.add(mock2);
        mockTypeMatches.add(mock3);

        when(field.getType()).thenReturn(Object.class);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(next).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
    }
}