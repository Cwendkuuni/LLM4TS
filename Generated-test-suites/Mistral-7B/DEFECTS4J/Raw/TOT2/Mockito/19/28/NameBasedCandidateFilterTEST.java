import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {
    @Test
    public void testConstructor() {
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
        verify(nextMock).filterCandidate(mock(Collection.class), mock(Field.class), mock(Object.class));
    }
}