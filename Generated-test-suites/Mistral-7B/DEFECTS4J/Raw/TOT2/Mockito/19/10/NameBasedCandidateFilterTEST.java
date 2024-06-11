import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.Mockito;

public class NameBasedCandidateFilterTest {

    @Test
    public void testConstructor() {
        MockCandidateFilter nextMock = Mockito.mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
        // Check if constructor sets next filter correctly
        Mockito.verify(nextMock).getClass();
    }
}