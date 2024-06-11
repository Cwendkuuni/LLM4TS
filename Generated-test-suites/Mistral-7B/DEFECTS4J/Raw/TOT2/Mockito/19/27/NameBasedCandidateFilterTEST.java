import org.junit.Test;
import static org.mockito.Mockito.mock;

public class NameBasedCandidateFilterTest {
	@Test
	public void testNameBasedCandidateFilterConstructor() {
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(nextMock);
		// Additional assertions can be added here if necessary
	}
}