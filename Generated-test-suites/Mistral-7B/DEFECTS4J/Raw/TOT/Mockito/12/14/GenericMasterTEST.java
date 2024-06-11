import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class GenericMasterTest {

 private GenericMaster genericMaster;

 @Before
 public void setup() {
 genericMaster = new GenericMaster();
 }

 @Test
 public void testGetGenericType_simpleField() {
 Class clazz = SomeClass.class;
 try {
 Field field = clazz.getDeclaredField("fieldName");
 field.setAccessible(true);
 Class expectedType = String.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 } catch (NoSuchFieldException e) {
 // handle exception
 }
 }

 @Test
 public void testGetGenericType_fieldWithArrayType() {
 Class clazz = SomeClass.class;
 try {
 Field field = clazz.getDeclaredField("fieldName");
 field.setAccessible(true);
 Class<?>[] expectedTypes = new Class[]{Integer.class, String.class};
 Type genericType = field.getGenericType();
 ParameterizedType parameterizedType = (ParameterizedType) genericType;
 Class<?>[] actualTypes = parameterizedType.getActualTypeArguments();
 assertArrayEquals(expectedTypes, actualTypes);
 } catch (NoSuchFieldException e) {
 // handle exception
 }
 }

 @Test
 public void testGetGenericType_fieldWithListType() {
 Class clazz = SomeClass.class;
 try {
 Field field = clazz.getDeclaredField("fieldName");
 field.setAccessible(true);
 Type genericType = field.getGenericType();
 ParameterizedType parameterizedType = (ParameterizedType) genericType;
 Type listType = parameterizedType.getActualTypeArguments()[0];
 Class<?> listElementType = ((ParameterizedType) listType).getActualTypeArguments()[0];
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 } catch (NoSuchFieldException e) {
 // handle exception
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_nullField() {
 Field nullField = null;
 Class actualType = genericMaster.getGenericType(nullField);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericType_nonGenericField() {
 Class clazz = SomeClass.class;
 try {
 Field field = clazz.getDeclaredField("nonGenericField");
 field.setAccessible(true);
 Class actualType = genericMaster.getGenericType(field);
 fail("Should throw IllegalArgumentException for non-generic field");
 } catch (NoSuchFieldException e) {
 // handle exception
 }
 }

 @Test(expected = RuntimeException.class)
 public void testGetGenericType_reflectionFailure() {
 Class clazz = SomeClass.class;
 try {
 Field field = clazz.getDeclaredField("reflectionFailureField");
 Reflections reflections = new Reflections(clazz.getPackage().getName());
 GenericMaster spy = reflections.createSpy(GenericMaster.class);
 doReturn(null).when(spy).getGenericType(any(Field.class));
 field.setAccessible(true);
 Class actualType = spy.getGenericType(field);
 fail("Should throw reflection failure exception");
 } catch (NoSuchFieldException e) {
 // handle exception
 }
 }
}

 class SomeClass {
 private int nonGenericField;
 private List<Integer> fieldName;
 }