import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import java.lang.reflect.Field;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 // Test for createMockFor method
 @Test(expected = MockitoException.class)
 public void testCreateMockForNullAnnotation() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Field field = null;
 assertThrows(MockitoException.class, () -> engine.createMockFor(null, field));
 }

 @Test
 public void testCreateMockForNoAnnotation() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Integer.class, context) {};
 engine.process(context, testClass);
 assertNull(engine.createMockFor(new Annotation() {}, field));
 }

 // Test for process method
 @Test
 public void testProcessNoSpyAnnotation() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", String.class, context) {};
 engine.process(context, testClass);
 verify(testClass, never()).getClass();
 }

 @Test
 public void testProcessSameInstance() throws IllegalAccessException {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Object instance = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {
 @Override
 public Object get(Object obj) {
 return instance;
 }
 };
 field.setAccessible(true);
 engine.process(context, testClass);
 Object spiedInstance = (Object) field.get(testClass);
 assertEquals(instance, spiedInstance);
 }

 @Test(expected = MockitoException.class)
 public void testProcessMissingInstance() throws IllegalAccessException {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 engine.process(context, testClass);
 assertThrows(MockitoException.class, () -> field.set(testClass, null));
 }

 @Test(expected = MockitoException.class)
 public void testProcessAlreadySpyedInstance() throws IllegalAccessException {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Object instance = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 field.setAccessible(true);
 field.set(testClass, instance);
 engine.process(context, testClass);
 try {
 field.set(testClass, instance);
 } catch (IllegalAccessException e) {
 }
 assertThrows(MockitoException.class, () -> engine.process(context, testClass));
 }

 @Test
 public void testProcessSpyAnnotation() throws IllegalAccessException {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 Object instance = mock(Object.class);
 Field spyAnnotation = new Field("testSpyAnnotation", Spy.class, context) {};
 field.setAccessible(true);
 spyAnnotation.setAccessible(true);
 field.set(testClass, instance);
 spyAnnotation.set(testClass, new Spy() {});
 engine.process(context, testClass);
 assertEquals(instance, field.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithMockAnnotation() throws IllegalAccessException {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 Object instance = mock(Object.class);
 field.setAccessible(true);
 assertThrows(MockitoException.class, () -> engine.process(context, testClass));
 field.set(testClass, instance);
 assertThrows(MockitoException.class, () -> engine.process(context, testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithCaptorAnnotation() throws IllegalAccessException {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 Object instance = mock(Object.class);
 field.setAccessible(true);
 Field captorAnnotation = new Field("testCaptorAnnotation", Captor.class, context) {};
 captorAnnotation.setAccessible(true);
 captorAnnotation.set(testClass, new ArgumentCaptor<Object>() {});
 engine.process(context, testClass);
 assertThrows(MockitoException.class, () -> engine.process(context, testClass));
 }

 // Test for assertNoAnnotations method
 @Test
 public void testAssertNoAnnotationsNoAnnotations() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 engine.process(context, testClass);
 engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotationsSpyAnnotation() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 engine.process(context, testClass);
 Field spyAnnotation = new Field("testSpyAnnotation", Spy.class, context) {};
 spyAnnotation.setAccessible(true);
 spyAnnotation.set(testClass, new Spy() {});
 try {
 engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 } catch (AssertionError e) {
 }
 fail("AssertionError expected but not thrown");
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotationsMockAnnotation() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 engine.process(context, testClass);
 Field mockAnnotation = new Field("testMockAnnotation", Mockito.Mock.class, context) {};
 mockAnnotation.setAccessible(true);
 mockAnnotation.set(testClass, mock(Object.class));
 try {
 engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 } catch (AssertionError e) {
 }
 fail("AssertionError expected but not thrown");
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotationsCaptorAnnotation() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 engine.process(context, testClass);
 Field captorAnnotation = new Field("testCaptorAnnotation", Captor.class, context) {};
 captorAnnotation.setAccessible(true);
 captorAnnotation.set(testClass, new ArgumentCaptor<Object>() {});
 try {
 engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 } catch (AssertionError e) {
 }
 fail("AssertionError expected but not thrown");
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotationsDuplicateAnnotations() {
 SpyAnnotationEngine engine = new SpyAnnotationEngine();
 Class<?> context = getClass();
 Object testClass = mock(Object.class);
 Field field = new Field("testField", Object.class, context) {};
 engine.process(context, testClass);
 Field spyAnnotation = new Field("testSpyAnnotation", Spy.class, context) {};
 Field mockAnnotation = new Field("testMockAnnotation", Mockito.Mock.class, context) {};
 Field captorAnnotation = new Field("testCaptorAnnotation", Captor.class, context) {};
 spyAnnotation.setAccessible(true);
 mockAnnotation.setAccessible(true);
 captorAnnotation.setAccessible(true);
 spyAnnotation.set(testClass, new Spy() {});
 mockAnnotation.set(testClass, mock(Object.class));
 captorAnnotation.set(testClass, new ArgumentCaptor<Object>() {});
 try {
 engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 } catch (AssertionError e) {
 }
 fail("AssertionError expected but not thrown");
 }
}