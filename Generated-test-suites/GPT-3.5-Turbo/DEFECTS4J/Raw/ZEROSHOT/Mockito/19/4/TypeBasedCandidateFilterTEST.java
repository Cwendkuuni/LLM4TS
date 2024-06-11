import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter candidateFilter;
    private MockCandidateFilter nextFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private OngoingInjecter ongoingInjecter;

    @Before
    public void setUp() {
        nextFilter = mock(MockCandidateFilter.class);

        candidateFilter = new TypeBasedCandidateFilter(nextFilter);

        mocks = new ArrayList<>();
        field = getField();
        fieldInstance = new Object();
        ongoingInjecter = mock(OngoingInjecter.class);
    }

    @Test
    public void filterCandidate_withMatchingTypes_shouldReturnNextFilter() {
        // Arrange
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        when(nextFilter.filterCandidate(any(Collection.class), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(ongoingInjecter, result);
        verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_withNoMatchingTypes_shouldReturnNextFilter() {
        // Arrange
        mocks.clear();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        when(nextFilter.filterCandidate(any(Collection.class), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(ongoingInjecter, result);
        verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    private Field getField() {
        try {
            return SomeClass.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class SomeClass {
        private Object field;
    }
}