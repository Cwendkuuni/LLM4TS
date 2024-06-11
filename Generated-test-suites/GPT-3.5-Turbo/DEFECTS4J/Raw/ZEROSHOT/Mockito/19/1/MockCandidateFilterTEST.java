import org.junit.Test;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {

  @Test
  public void testFilterCandidate() {
    MockCandidateFilter filter = mock(MockCandidateFilter.class);
    Collection<Object> mocks = mock(Collection.class);
    Field field = mock(Field.class);
    Object instance = mock(Object.class);

    OngoingInjecter injecter = filter.filterCandidate(mocks, field, instance);

    verify(filter).filterCandidate(mocks, field, instance);
  }
}