
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testFilterCandidate_withMatchingMockName_shouldReturnMockInstance() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<>();
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = mock(Object.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mockUtil.getMockName(mock1).when(field.getName());

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mockNameMatches, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_withNoMatchingMockName_shouldCallNextFilter() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<>();
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockNameMatches");
        Object fieldInstance = mock(Object.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }
}
