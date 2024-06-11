import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FinalMockCandidateFilterTest {
  
  private FinalMockCandidateFilter finalMockCandidateFilter;
  
  @Before
  public void setUp() {
    finalMockCandidateFilter = new FinalMockCandidateFilter();
  }
  
  @Test
  public void testFilterCandidate_singleMock_success() {
    Collection<Object> mocks = new HashSet<>();
    Object mock = new Object();
    mocks.add(mock);
    
    Field field = getField("fieldName");
    Object fieldInstance = new Object();
    
    FinalMockCandidateFilter.OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    assertTrue(injecter.thenInject());
    
    assertEquals(mock, fieldInstance);
  }
  
  @Test
  public void testFilterCandidate_multipleMocks_failure() {
    Collection<Object> mocks = new HashSet<>();
    mocks.add(new Object());
    mocks.add(new Object());
    
    Field field = getField("fieldName");
    Object fieldInstance = new Object();
    
    FinalMockCandidateFilter.OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    assertFalse(injecter.thenInject());
  }
  
  @Test(expected = MockitoException.class)
  public void testFilterCandidate_exceptionThrown() {
    Collection<Object> mocks = new HashSet<>();
    Object mock = new Object();
    mocks.add(mock);
    
    Field field = getField("fieldName");
    Object fieldInstance = new Object();
    
    FinalMockCandidateFilter.OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    assertTrue(injecter.thenInject());
    
    assertEquals(mock, fieldInstance);
  }
  
  private Field getField(String fieldName) {
    try {
      return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      fail("Field not found: " + fieldName);
      return null;
    }
  }
}