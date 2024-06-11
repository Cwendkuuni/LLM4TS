import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collection;
import org.junit.Test;

public class FinalMockCandidateFilterTest {

  @Test
  public void testFilterCandidate() {
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = mock(Collection.class);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    assertNotNull(injecter);
  }
  
  @Test
  public void testFilterCandidateWithOneMock() {
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = mock(Collection.class);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    
    when(mocks.size()).thenReturn(1);
    when(mocks.iterator().next()).thenReturn(mock(Object.class));
    
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    assertNotNull(injecter);
    Object injected = injecter.thenInject();
    assertNotNull(injected);
  }
  
  @Test
  public void testFilterCandidateWithMultipleMocks() {
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = mock(Collection.class);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    
    when(mocks.size()).thenReturn(2);
    
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    assertNotNull(injecter);
    Object injected = injecter.thenInject();
    assertNull(injected);
  }
}