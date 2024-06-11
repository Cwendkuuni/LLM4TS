import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine spyAnnotationEngine;
 private Object testClass;
 private Field field;
 private MockedStatic<MockUtil> mockUtilMock;

 @Before
 public void setup() {
 testClass = new Object() {};
 field = FieldUtils.getField(testClass, "fieldName");
 spyAnnotationEngine = new SpyAnnotationEngine();
 mockUtilMock = Mockito.mockStatic(MockUtil.class);
 }

 @Test
 public void testCreateMockForNullAnnotation() {
 // Test empty method
 spyAnnotationEngine.createMockFor(null, field);
 // No interaction with MockUtil.isMock() expected
 verifyZeroInteractions(mockUtilMock);
 }

 @Test
 public void testCreateMockForSpyAnnotation() {
 // Given: A field annotated with @Spy
 field.setAccessible(true);
 Mockito.doReturn(mock(Object.class)).when(mockUtilMock).isMock(Mockito.any());
 Mockito.doThrow(new MockitoException("Expected Exception"))
 .when(spyAnnotationEngine)
 .createMockFor(Spy.class, field);

 // When: process method is called
 spyAnnotationEngine.process(testClass.getClass(), testClass);

 // Then: Spy creation should throw the expected exception
 assertThrows(MockitoException.class, () -> field.get(testClass));
 // And: no interactions with Mockito.reset() or Mockito.spy() should happen
 verifyZeroInteractions(Mockito.class);
 }

 @Test
 public void testCreateMockForMockAnnotation() {
 // Given: A field annotated with @Mock
 field.setAnnotation(MockAnnotation.class);
 Mockito.doReturn(mock(Object.class)).when(mockUtilMock).isMock(Mockito.any());
 Mockito.doThrow(new MockitoException("Expected Exception"))
 .when(spyAnnotationEngine)
 .createMockFor(MockitoAnnotation.class, field);

 // When: process method is called
 spyAnnotationEngine.process(testClass.getClass(), testClass);

 // Then: Mock creation should throw the expected exception
 assertThrows(MockitoException.class, () -> field.get(testClass));
 // And: no interactions with Mockito.reset() or Mockito.spy() should happen
 verifyZeroInteractions(Mockito.class);
 }

 @Test
 public void testCreateMockForAnnotatedFieldWithInstance() {
 // Given: A field annotated with @Spy and an instance
 field.setAccessible(true);
 field.set(testClass, new Object());
 Mockito.doReturn(new Object()).when(mockUtilMock).isMock(Mockito.any());

 // When: process method is called
 spyAnnotationEngine.process(testClass.getClass(), testClass);

 // Then: The instance should not be changed
 verify(field).set(testClass, org.mockito.Mockito.spy(Mockito.any()));
 verifyNoMoreInteractions(field);
 }

 @Test
 public void testCreateMockForAnnotatedFieldWithoutInstance() {
 // Given: A field annotated with @Spy and no instance
 field.setAccessible(true);
 Mockito.doReturn(null).when(mockUtilMock).isMock(Mockito.any());

 // When: process method is called
 spyAnnotationEngine.process(testClass.getClass(), testClass);
 // Then: An error message should be thrown
 assertThrows(MockitoException.class, () -> field.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithAccessibleFieldsInDifferentOrder() {
 // Given: Two fields, annotated with @Spy and with different accessible order
 field = FieldUtils.getField(testClass, "field1");
 Field field2 = FieldUtils.getField(testClass, "field2");
 field.setAccessible(true);
 field2.setAccessible(true);
 field.set(testClass, new Object());
 field2.set(testClass, new Object());
 Mockito.doReturn(mock(Object.class)).when(mockUtilMock).isMock(Mockito.any());

 // When: process method is called with fields in different order
 spyAnnotationEngine.process(testClass.getClass(), testClass);

 // Then: An error message should be thrown
 // Note: It's an assertion error since the test method is already marked with the expected exception
 }

 @Test
 public void testProcessWithAccessibleFieldsInSameOrder() {
 // Given: Two fields, annotated with @Spy and with the same accessible order
 field = FieldUtils.getField(testClass, "field1");
 Field field2 = FieldUtils.getField(testClass, "field2");
 field.setAccessible(true);
 field2.setAccessible(true);
 field.set(testClass, new Object());
 field2.set(testClass, new Object());
 Mockito.doReturn(mock(Object.class)).when(mockUtilMock).isMock(Mockito.any());

 // When: process method is called with fields in the same order
 spyAnnotationEngine.process(testClass.getClass(), testClass);

 // Then: No exception should be thrown
 field.setAccessible(false);
 field2.setAccessible(false);
 verifyZeroInteractions(Mockito.class);
 }

 @Test
 public void testProcessWithDuplicateSpyAnnotations() {
 // Given: A field annotated with @Spy multiple times
 field.setAccessible(true);
 field.setAnnotation(new Annotation[]{new Spy(), new Spy(), new Spy()});
 Mockito.doReturn(mock(Object.class)).when(mockUtilMock).isMock(Mockito.any());

 // When: process method is called
 spyAnnotationEngine.process(testClass.getClass(), testClass);

 // Then: An error message should be thrown
 assertThrows(org.mockito.exceptions.base.MockitoException.class, () -> field.get(testClass));
 }
}