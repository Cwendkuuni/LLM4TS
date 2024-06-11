import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter candidateFilter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        candidateFilter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() {
        // Setup
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock1 = new MockObject1();
        Object mock2 = new MockObject2();
        Object mock3 = new MockObject3();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        when(field.getType().isAssignableFrom(MockObject1.class)).thenReturn(true);
        when(field.getType().isAssignableFrom(MockObject2.class)).thenReturn(false);
        when(field.getType().isAssignableFrom(MockObject3.class)).thenReturn(true);

        // Execute
        candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(next).filterCandidate(mock1, field, fieldInstance);
        verify(next, never()).filterCandidate(mock2, field, fieldInstance);
        verify(next).filterCandidate(mock3, field, fieldInstance);
    }

    private static class MockObject1 {
    }

    private static class MockObject2 {
    }

    private static class MockObject3 {
    }
}