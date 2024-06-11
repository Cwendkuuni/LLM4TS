
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_noMatchingMocks() {
        // Arrange
        Object mock = mock(Object.class);
        when(field.getName()).thenReturn("mockField");

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_singleMatchingMock() {
        // Arrange
        Object matchingMock = mock(Object.class);
        when(mockUtil.getMockName(matchingMock)).thenReturn("mockField");
        mocks.add(matchingMock);
        when(field.getName()).thenReturn("mockField");

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        List<Object> expectedMocks = Collections.singletonList(matchingMock);
        verify(mockCandidateFilter).filterCandidate(expectedMocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMatchingMocks() {
        // Arrange
        Object matchingMock1 = mock(Object.class);
        Object matchingMock2 = mock(Object.class);
        when(mockUtil.getMockName(matchingMock1)).thenReturn("mockField");
        when(mockUtil.getMockName(matchingMock2)).thenReturn("mockField");
        mocks.add(matchingMock1);
        mocks.add(matchingMock2);
        when(field.getName()).thenReturn("mockField");

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        List<Object> expectedMocks = Arrays.asList(matchingMock1, matchingMock2);
        verify(mockCandidateFilter).filterCandidate(expectedMocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_noMatchingMocks_multipleMocksWithDifferentNames() {
        // Arrange
        Object nonMatchingMock1 = mock(Object.class);
        Object nonMatchingMock2 = mock(Object.class);
        when(field.getName()).thenReturn("mockField");

        // Act
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }
}
