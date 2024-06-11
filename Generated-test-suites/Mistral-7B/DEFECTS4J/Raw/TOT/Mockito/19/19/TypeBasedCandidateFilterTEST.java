import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter filter1;
    private MockCandidateFilter filter2;
    private TypeBasedCandidateFilter typeBasedFilter;

    @Before
    public void setUp() {
        filter1 = mock(MockCandidateFilter.class);
        filter2 = mock(MockCandidateFilter.class);
        typeBasedFilter = new TypeBasedCandidateFilter(filter1);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMockedTypes_ThenReturnFilteredMock() throws Exception {
        // Given
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = Whitebox.getInternalState(TypeBasedCandidateFilter.class, "field");

        // When
        Object filteredMock = typeBasedFilter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock1, filteredMock);
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsNotAssignableFromMockedTypes_ThenReturnNull() throws Exception {
        // Given
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = Whitebox.getInternalState(TypeBasedCandidateFilter.class, "field");

        // When
        Object filteredMock = typeBasedFilter.filterCandidate(mocks, Whitebox.getInternalState(Object.class, "getClass"), fieldInstance);

        // Then
        assertEquals(null, filteredMock);
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsNull_ThenReturnNull() throws Exception {
        // Given
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = Whitebox.getInternalState(TypeBasedCandidateFilter.class, "field");

        // When
        MockCandidateFilter nextFilter = spy(filter1);
        Whitebox.setInternalValue(nextFilter, "next", null);
        Object filteredMock = typeBasedFilter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, filteredMock);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WhenNextFilterIsNull_ThenThrowException() throws Exception {
        // Given
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList(mock1, mock2);
        Field field = Whitebox.getInternalState(TypeBasedCandidateFilter.class, "field");

        // When
        MockCandidateFilter nextFilter = null;
        Object filteredMock = typeBasedFilter.filterCandidate(mocks, field, fieldInstance);

        // Then
        // Expected exception
    }
}