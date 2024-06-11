import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {
  
  @Test
  public void testInjectMocksOnFields() {
    DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
    
    Set<Field> injectMocksFields = new HashSet<>();
    Set<Object> mocks = new HashSet<>();
    Object testClassInstance = new Object();
    
    injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
  }
  
  @Test
  public void testInjectMockCandidate() {
    DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
    
    Class<?> awaitingInjectionClazz = Object.class;
    Set<Object> mocks = new HashSet<>();
    Object fieldInstance = new Object();
    
    injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
  }
  
  @Test
  public void testOrderedInstanceFieldsFrom() {
    DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();
    
    Class<?> awaitingInjectionClazz = Object.class;
    Field[] fields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
  }
}