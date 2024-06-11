import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

public class NameBasedCandidateFilterTest {
	// 
	@Test
	public void testNameBasedCandidateFilterConstructor() {
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
		assertNotNull(filter);
	}
	// 
}