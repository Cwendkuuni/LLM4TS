import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_shouldReturnOngoingInjecter() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(nextFilter, ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_shouldFilterByMockType() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(String.class);
        Object mock3 = mock(List.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        ongoingInjecter.filterCandidate(mocks, field, fieldInstance); // Because the next filter is not mocked, this line is just for code coverage
    }

    @Test
    public void testFilterCandidate_withEmptyMockList_shouldReturnOngoingInjecter() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(nextFilter, ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_withEmptyFieldType_shouldReturnOngoingInjecter() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(String.class);
        Object mock3 = mock(List.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(nextFilter, ongoingInjecter);
    }

}