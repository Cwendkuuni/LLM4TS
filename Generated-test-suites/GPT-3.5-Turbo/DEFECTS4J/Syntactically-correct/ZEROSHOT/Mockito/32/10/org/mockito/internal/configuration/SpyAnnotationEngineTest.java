import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {
  
  @Mock
  private Object mockObject;
  
  @Spy
  private Object spyObject;
  
  @Captor
  private ArgumentCaptor<Object> captorObject;
  
  @Test
  public void testCreateMockFor() {
    Object annotation = Mockito.mock(Annotation.class);
    Field field = Mockito.mock(Field.class);
    
    SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
    Object result = spyAnnotationEngine.createMockFor(annotation, field);
    
    verifyZeroInteractions(annotation);
    verifyZeroInteractions(field);
    assert result == null;
  }
  
  @Test
  public void testProcess() throws Exception {
    Class<?> context = getClass();
    Object testClass = this;
    
    SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
    spyAnnotationEngine.process(context, testClass);
    
    verifyZeroInteractions(context);
    
    Field[] fields = context.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Spy.class)) {
        field.setAccessible(true);
        Object instance = field.get(testClass);
        
        if (instance == null) {
          throw new MockitoException("Cannot create a @Spy for '" + field.getName() + "' field");
        }
        
        if (new MockUtil().isMock(instance)) { 
          verify(instance, times(1)).reset();
        } else {
          assert instance == Mockito.spy(instance);
        }
      }
    }
  }

  @Test
  public void testAssertNoAnnotations() throws Exception {
    Field field = Mockito.mock(Field.class);
    
    SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
    spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    
    verifyZeroInteractions(field);
  }
}