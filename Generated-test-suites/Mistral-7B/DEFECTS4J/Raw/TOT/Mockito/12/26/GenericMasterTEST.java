import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GenericMasterTest {

 private GenericMaster genericMaster;
 private FieldTestDouble fieldTestDouble;

 @Before
 public void setup() {
 genericMaster = new GenericMaster();
 try {
 fieldTestDouble = new FieldTestDouble();
 } catch (NoSuchFieldException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetGenericTypeWithPrimitiveField() {
 // Test for a primitive field
 Field intField = getField(fieldTestDouble, "intField");
 Class expectedType = int.class;
 Class actualType = genericMaster.getGenericType(intField);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeWithObjectField() {
 // Test for a field of Object type
 Field objectField = getField(fieldTestDouble, "objectField");
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(objectField);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeWithArrayField() {
 // Test for a field of an array type
 Field intArrayField = getField(fieldTestDouble, "intArrayField");
 int[] expectedType = new int[10];
 Class<?> genericType = genericMaster.getGenericType(intArrayField);
 assertEquals(int[].class, genericType);
 }

 @Test
 public void testGetGenericTypeWithNestedClassField() {
 // Test for a field of a nested generic class
 Class<NestedClass<Integer>> nestedClass = new NestedClass<Integer>(Integer.class);
 Field nestedField = getField(nestedClass, "integerField");
 Class<?> expectedType = Integer.class;
 Class<?> actualType = genericMaster.getGenericType(nestedField);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWithNullField() {
 // Test for a null field
 Field nullField = null;
 genericMaster.getGenericType(nullField);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericTypeWithNonGenericField() {
 // Test for a non-generic field
 Field nonGenericField = getField(fieldTestDouble, "modifiers");
 genericMaster.getGenericType(nonGenericField);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericTypeWithFieldWithoutType() {
 // Test for a field without a type
 Field noTypeField = getField(fieldTestDouble, "serialVersionUID");
 genericMaster.getGenericType(noTypeField);
 }

 private Field getField(Object object, String fieldName) throws NoSuchFieldException {
 Field field = object.getClass().getDeclaredField(fieldName);
 field.setAccessible(true);
 return field;
 }

 static class NestedClass<T> {
 private T integerField;

 public NestedClass(Class<T> type) {
 this.integerField = null;
 }
 }
}