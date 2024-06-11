import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.builders.MockitoMockBuilder;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;

 @Mock
 private Annotation annotation;

 @Mock
 private Field field;

 @Mock
 private Object testClass;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 engine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockFor() {
 assertNull(engine.createMockFor(annotation, field));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_InstanceMissing() {
 engine.process(Object.class, testClass);
 Whitebox.setFieldValue(field, testClass, null);
 engine.process(Object.class, testClass);
 }

 @Test
 public void testProcess_MockInstance() {
 Object mock = mock(SomeClass.class);
 Whitebox.setFieldValue(field, testClass, mock);
 engine.process(Object.class, testClass);
 verify(mock).reset();
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcess_IllegalAccessException() {
 Whitebox.setFieldAccessible(true, field, testClass.getClass());
 engine.process(Object.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_DuplicateAnnotation() {
 Whitebox.setFieldValue(field, testClass, mock(SomeClass.class));
 field.setAnnotation(new SpyAnnotation());
 engine.process(Object.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_UndesiredAnnotation() {
 field.setAnnotation(new MockAnnotation());
 engine.process(Object.class, testClass);
 }
}