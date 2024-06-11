import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter candidateFilter;
    private MockCandidateFilter next;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        candidateFilter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = null;
        Object fieldInstance = null;

        // Execute
        candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(next).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
    }

    @Test
    public void testFilterCandidateWithMatchingName() {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getFieldWithMatchingName(mock);
        Object fieldInstance = null;

        // Execute
        candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(next).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
    }

    @Test
    public void testFilterCandidateWithNonMatchingName() {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getFieldWithNonMatchingName();
        Object fieldInstance = null;

        // Execute
        candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(next).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
    }

    // Helper methods

    private Field getFieldWithMatchingName(Object mock) {
        try {
            Field field = mock.getClass().getField("mockFieldName");
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field getFieldWithNonMatchingName() {
        try {
            Field field = mock(Object.class).getClass().getField("nonMatchingFieldName");
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}