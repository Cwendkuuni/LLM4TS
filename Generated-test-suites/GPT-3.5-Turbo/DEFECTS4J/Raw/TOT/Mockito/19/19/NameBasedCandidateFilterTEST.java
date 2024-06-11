import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private NameBasedCandidateFilter nameBasedCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_WhenNoMocks_ShouldReturnNextFilter() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Mockito.verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenOneMockAndMockNameMatchesField_ShouldReturnNextFilterWithMatchingMock() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Mockito.verify(mockCandidateFilter).filterCandidate(List.of(mock), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenMultipleMocksAndOneMockNameMatchesField_ShouldReturnNextFilterWithMatchingMocks() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Mockito.verify(mockCandidateFilter).filterCandidate(List.of(mock1), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenMultipleMocksAndNoMockNameMatchesField_ShouldReturnNextFilterWithAllMocks() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getClass().getDeclaredField("mocks");
        Object fieldInstance = new Object();

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        Mockito.verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }
}