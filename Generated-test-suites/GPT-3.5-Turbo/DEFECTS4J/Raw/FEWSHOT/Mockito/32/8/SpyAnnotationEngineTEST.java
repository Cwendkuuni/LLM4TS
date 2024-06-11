package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {
  
  @Test
  public void testCreateMockFor() {
    SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
    
    Annotation annotation = Mockito.mock(Annotation.class);
    Field field = Mockito.mock(Field.class);
    
    Object result = spyAnnotationEngine.createMockFor(annotation, field);
    
    assertNull(result);
  }
  
  @Test
  public void testProcess() throws IllegalAccessException {
    SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
    
    Class<?> context = SpyAnnotationEngine.class;
    Object testClass = new Object();
    
    try {
      spyAnnotationEngine.process(context, testClass);
    } catch (MockitoException e) {
      fail("Exception thrown");
    }
  }
  
  @Test
  public void testAssertNoAnnotations() {
    SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    Class annotation = Mockito.mock(Class.class);
    Field field = Mockito.mock(Field.class);
    Class undesiredAnnotation = Mockito.mock(Class.class);

    spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation);
  }
  
}