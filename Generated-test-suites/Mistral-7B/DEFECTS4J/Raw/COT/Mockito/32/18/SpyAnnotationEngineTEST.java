import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.JUnit4;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.assertThatThrows;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;

@RunWith(JUnit4.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine = new SpyAnnotationEngine();

 @SuppressWarnings("unchecked")
 @Test
 public void testCreateMockFor() {
 SpyAnnotationEngine engine = spy(new SpyAnnotationEngine()); //spying the engine itself
 Annotation annotation = mock(Annotation.class);
 Field field = mock(Field.class);

 verify(engine, times(0)).createMockFor(annotation, field); //method should not be called as it returns null
 }

 @Test
 public void testProcess_NoSpyAnnotation() {
 Class context = mock(Class.class);
 Object testClass = mock(Object.class);

 engine.process(context, testClass);

 verify(context, never()).getDeclaredFields(); //no fields with @Spy annotation
 }

 @Test
 public void testProcess_FieldWithNullInstance() {
 Class context = mock(Class.class);
 Object testClass = mock(Object.class);
 Field field = mock(Field.class);
 doReturn(null).when(field).get(testClass);

 engine.process(context, testClass);

 verify(context, times(1)).getDeclaredFields();
 verify(field, never()).setAccessible(true);
 verify(testClass, never()).set(field, any());
 }

 @Test
 public void testProcess_FieldWithMockInstance() {
 Class context = mock(Class.class);
 Object testClass = mock(Object.class);
 Field field = mock(Field.class);
 Object instance = mock(Object.class);
 doReturn(instance).when(field).get(testClass);
 doReturn(true).when(new MockUtil()).isMock(instance);

 engine.process(context, testClass);

 verify(context, times(1)).getDeclaredFields();
 verify(field, times(1)).setAccessible(true);
 verify(testClass, times(1)).set(field, instance);
 verifyNoMoreInteractions(testClass, field);
 }

 @Test
 public void testProcess_FieldWithNonMockInstance_ThrowsMockitoException() {
 Class context = mock(Class.class);
 Object testClass = mock(Object.class);
 Field field = mock(Field.class);
 Object instance = mock(Object.class);
 doReturn(instance).when(field).get(testClass);
 doReturn(false).when(new MockUtil()).isMock(instance);

 engine.process(context, testClass);

 verify(context, times(1)).getDeclaredFields();
 verify(field, times(1)).setAccessible(true);
 verify(testClass, never()).set(field, any());

 assertThatThrows(MockitoException.class).isThrownBy(() -> engine.createMockFor(mock(Annotation.class), field));
 }

 @Test
 public void testProcess_FieldWithNonAccessibleInstance_ThrowsIllegalAccessException() {
 Class context = mock(Class.class);
 Object testClass = mock(Object.class);
 Field field = mock(Field.class);
 Object instance = mock(Object.class);
 doReturn(instance).when(field).get(testClass);
 doThrow(new IllegalAccessException("Test exception")).when(field).setAccessible(true);

 engine.process(context, testClass);

 verify(context, times(1)).getDeclaredFields();
 verify(field, times(1)).setAccessible(true);
 verify(testClass, never()).set(field, any());

 assertThatThrows(IllegalAccessException.class).isThrownBy(() -> engine.process(context, testClass));
 }

 @Test
 public void testProcess_FieldWithSpyAnnotationAndSameAnnotation_ThrowsReporterException() {
 Class context = mock(Class.class);
 Object testClass = mock(Object.class);
 Field field1 = mock(Field.class);
 Field field2 = mock(Field.class);
 Spy annotation1 = mock(Spy.class);
 Spy annotation2 = mock(Spy.class);
 doReturn(annotation1).when(field1).getAnnotation(Spy.class);
 doReturn(annotation2).when(field2).getAnnotation(Spy.class);
 doReturn(field1).when(field2).getName();
 doReturn(field1).when(field2).getName();
 doReturn(true).when(new MockUtil()).isMock(testClass);

 engine.process(context, testClass);

 verify(context, times(1)).getDeclaredFields();
 verify(field1, times(1)).setAccessible(true);
 verify(field2, times(1)).setAccessible(true);
 verify(testClass, times(1)).set(field1, any());
 verify(testClass, times(1)).set(field2, any());

 new Reporter().unsupportedCombinationOfAnnotations("Spy1", Spy.class.getSimpleName());
 }

 @Test
 public void testProcess_FieldWithSpyAnnotationAndDifferentAnnotations_DoesNothing() {
 Class context = mock(Class.class);
 Object testClass = mock(Object.class);
 Field field = mock(Field.class);
 Spy annotation = mock(Spy.class);
 Captor captor = mock(Captor.class);
 doReturn(annotation).when(field).getAnnotation(Spy.class);
 doReturn(captor).when(field).getAnnotation(Captor.class);
 doReturn(true).when(new MockUtil()).isMock(testClass);

 engine.process(context, testClass);

 verify(context, times(1)).getDeclaredFields();
 verify(field, times(1)).setAccessible(true);
 verify(testClass, never()).set(field, any());
 }

 @Test
 public void testResetSpy() {
 Object testClass = mock(Object.class);
 Field field = mock(Field.class);
 Object spy = mock(Object.class);
 doReturn(spy).when(field).get(testClass);
 doReturn(true).when(new MockUtil()).isMock(spy);

 engine.process(testClass.getClass(), testClass);
 engine.reset(spy);

 verify(spy, times(2)).toString(); //toString is called twice: once during creation and once during reset
 }
}