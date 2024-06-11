import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

public class DefaultInjectionEngineTest {
  
  private DefaultInjectionEngine injectionEngine;
  
  private MockCandidateFilter mockCandidateFilter;
  
  @Before
  public void setup() {
    injectionEngine = new DefaultInjectionEngine();
    mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
  }
  
  @Test
  public void testInjectMocksOnFields_withNoFieldsToInject() {
    Set<Field> injectMocksFields = new HashSet<Field>();
    Set<Object> mocks = new HashSet<Object>();
    Object testClassInstance = new Object();
    
    injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    
    // Assertions
    // ...
  }
  
  @Test
  public void testInjectMocksOnFields_withFieldsToInject() {
    Set<Field> injectMocksFields = new HashSet<Field>();
    Set<Object> mocks = new HashSet<Object>();
    Object testClassInstance = new Object();
    
    Field field1 = mock(Field.class);
    injectMocksFields.add(field1);
    mocks.add(new Object());
    
    Field field2 = mock(Field.class);
    injectMocksFields.add(field2);
    mocks.add(new Object());
    
    Field field3 = mock(Field.class);
    injectMocksFields.add(field3);
    mocks.add(new Object());
    
    injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    
    // Assertions
    // ...
  }
  
  @Test
  public void testInjectMocksOnFields_withException() {
    Set<Field> injectMocksFields = new HashSet<Field>();
    Set<Object> mocks = new HashSet<Object>();
    Object testClassInstance = new Object();
    
    Field field1 = mock(Field.class);
    injectMocksFields.add(field1);
    mocks.add(new Object());
    
    Field field2 = mock(Field.class);
    injectMocksFields.add(field2);
    mocks.add(new Object());
    
    Field field3 = mock(Field.class);
    injectMocksFields.add(field3);
    mocks.add(new Object());
    
    Reporter reporter = mock(Reporter.class);
    doThrow(new MockitoException()).when(reporter).cannotInitializeForInjectMocksAnnotation(anyString(), any());
    
    injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
    
    // Assertions
    // ...
  }
  
  @Test
  public void testInjectMockCandidate() {
    Class<?> awaitingInjectionClazz = Object.class;
    Set<Object> mocks = new HashSet<Object>();
    Object fieldInstance = new Object();
    
    Field field = mock(Field.class);
    when(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(mockCandidateFilter);
    
    injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
    
    // Assertions
    // ...
  }
  
  @Test
  public void testOrderedInstanceFieldsFrom() {
    Class<?> awaitingInjectionClazz = Object.class;
    
    injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    
    // Assertions
    // ...
  }
  
}