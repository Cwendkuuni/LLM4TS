import org.junit.Test;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.Collection;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {
  
  @Test
  public void testFilterCandidate_withOneMatchingMock_shouldReturnOngoingInjecterWithMatchingMock() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Object mock = mock(Object.class);
    mocks.add(mock);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(mock, result);
    verify(fieldInstance, never()).set(mock);
    verify(fieldInstance, never()).set(matchingMock);
  }
  
  @Test
  public void testFilterCandidate_withMultipleMatchingMocks_shouldReturnOngoingInjecterWithNullResult() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(null, result);
    verify(fieldInstance, never()).set(mock1);
    verify(fieldInstance, never()).set(mock2);
  }
  
  @Test
  public void testFilterCandidate_withNoMatchingMocks_shouldReturnOngoingInjecterWithNullResult() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(null, result);
    verify(fieldInstance, never()).set(any());
  }
  
  @Test
  public void testFilterCandidate_withMatchingMockAndBeanPropertySetterNotWorking_shouldSetFieldUsingFieldSetter() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Object mock = mock(Object.class);
    mocks.add(mock);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenReturn(false);
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(mock, result);
    verify(fieldInstance, never()).set(mock);
    verify(fieldInstance).set(mock);
  }
  
  @Test
  public void testFilterCandidate_withMatchingMockAndBeanPropertySetterThrowsException_shouldCallCannotInjectDependency() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Object mock = mock(Object.class);
    mocks.add(mock);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());
    Reporter reporterMock = mock(Reporter.class);
    doNothing().when(reporterMock).cannotInjectDependency(field, mock, any(RuntimeException.class));
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(mock, result);
    verify(fieldInstance, never()).set(mock);
    verify(reporterMock).cannotInjectDependency(field, mock, any(RuntimeException.class));
  }
  
  @Test
  public void testFilterCandidate_withMatchingMockAndBeanPropertySetterThrowsException_shouldCallCannotInjectDependencyWithCorrectException() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Object mock = mock(Object.class);
    mocks.add(mock);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    RuntimeException exception = new RuntimeException();
    when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenThrow(exception);
    Reporter reporterMock = mock(Reporter.class);
    doNothing().when(reporterMock).cannotInjectDependency(field, mock, exception);
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(mock, result);
    verify(fieldInstance, never()).set(mock);
    verify(reporterMock).cannotInjectDependency(field, mock, exception);
  }
  
  @Test
  public void testFilterCandidate_withMatchingMockAndBeanPropertySetterReturningTrue_shouldNotCallFieldSetter() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Object mock = mock(Object.class);
    mocks.add(mock);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenReturn(true);
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(mock, result);
    verify(fieldInstance).set(mock);
  }
  
  @Test
  public void testFilterCandidate_withNoMatchingMocks_shouldCallFieldSetterWithEveryMock() {
    // Arrange
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    Field field = mock(Field.class);
    Object fieldInstance = mock(Object.class);
    
    // Act
    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    
    // Assert
    assertNotNull(injecter);
    Object result = injecter.thenInject();
    assertEquals(null, result);
    verify(fieldInstance).set(mock1);
    verify(fieldInstance).set(mock2);
  }
}