import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;

public class SpyAnnotationEngineTest {

  private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

  @Test
  public void testCreateMockFor_NullInstance_ReturnsNull() {
    Annotation annotation = mock(Annotation.class);
    Field field = mock(Field.class);

    Object result = spyAnnotationEngine.createMockFor(annotation, field);

    assertNull(result);
  }

  @Test
  public void testProcess_NoSpyAnnotation_IgnoresField() {
    Class<?> context = SpyAnnotationEngineTest.class;
    Object testClass = new Object();

    spyAnnotationEngine.process(context, testClass);
  }

  @Test
  public void testProcess_SpyAnnotation_MissingInstance_ThrowsMockitoException() {
    Class<?> context = SpyAnnotationEngineTest.class;
    Object testClass = new Object();

    Field field = getFieldByName(context, "fieldWithoutInstance");

    try {
      spyAnnotationEngine.process(context, testClass);
      fail("Expected MockitoException to be thrown");
    } catch (MockitoException e) {
      assertEquals(
          "Cannot create a @Spy for 'fieldWithoutInstance' field because the *instance* is missing\n"
              + "The instance must be created *before* initMocks();\n"
              + "Example of correct usage of @Spy:\n"
              + "   @Spy List mock = new LinkedList();\n"
              + "   //also, don't forget about MockitoAnnotations.initMocks();",
          e.getMessage());
    }
  }

  @Test
  public void testProcess_SpyAnnotation_InstanceIsMock_ResetMock() throws IllegalAccessException {
    Class<?> context = SpyAnnotationEngineTest.class;
    Object testClass = new Object();

    Field field = getFieldByName(context, "fieldWithMockInstance");
    Object instance = mock(Object.class);

    when(field.get(testClass)).thenReturn(instance);

    spyAnnotationEngine.process(context, testClass);

    verify(Mockito, times(1)).reset(instance);
  }

  @Test
  public void testProcess_SpyAnnotation_InstanceIsNotMock_SpyInstance()
      throws IllegalAccessException {
    Class<?> context = SpyAnnotationEngineTest.class;
    Object testClass = new Object();

    Field field = getFieldByName(context, "fieldWithoutMockInstance");
    Object instance = new Object();

    when(field.get(testClass)).thenReturn(instance);

    spyAnnotationEngine.process(context, testClass);

    verify(field, times(1)).set(testClass, Mockito.spy(instance));
  }

  @Test
  public void testAssertNoAnnotations_FieldWithNoUndesiredAnnotations_DoesNotThrowException()
      throws NoSuchFieldException {
    Field field = SpyAnnotationEngine.class.getDeclaredField("assertNoAnnotationsField");

    spyAnnotationEngine.assertNoAnnotations(
        SpyAnnotationEngine.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testAssertNoAnnotations_FieldWithUndesiredAnnotations_ThrowsUnsupportedOperationException()
      throws NoSuchFieldException {
    Field field = SpyAnnotationEngine.class.getDeclaredField("assertNoAnnotationsField");

    spyAnnotationEngine.assertNoAnnotations(
        SpyAnnotationEngine.class,
        field,
        AnotherUndesiredAnnotation.class,
        MockitoAnnotations.Mock.class,
        Captor.class);
  }

  private Field getFieldByName(Class<?> clazz, String fieldName) {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  // Test fields
  @Spy private Object fieldWithoutInstance;
  @Spy private Object fieldWithMockInstance = mock(Object.class);
  @Spy private Object fieldWithoutMockInstance = new Object();

  private @interface AnotherUndesiredAnnotation {}

  private @interface MockitoAnnotations {
    public @interface Mock {}
  }
}