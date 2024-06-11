import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {
  
  MockCandidateFilter filter = new MockCandidateFilterImpl();

  @Test
  public void testFilterCandidate() {
    Collection<Object> mocks = new ArrayList<>();
    Field field = new Field();
    Object instance = new Object();
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, instance);
    assertNotNull(injecter);
  }

  @Test
  public void testFilterCandidate_NullMocks() {
    Field field = new Field();
    Object instance = new Object();
    OngoingInjecter injecter = filter.filterCandidate(null, field, instance);
    assertNull(injecter);
  }

  @Test
  public void testFilterCandidate_NullField() {
    Collection<Object> mocks = new ArrayList<>();
    Object instance = new Object();
    OngoingInjecter injecter = filter.filterCandidate(mocks, null, instance);
    assertNull(injecter);
  }

  @Test
  public void testFilterCandidate_NullInstance() {
    Collection<Object> mocks = new ArrayList<>();
    Field field = new Field();
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);
    assertNull(injecter);
  }

}