import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class GenericMasterTest {

 @Test
 public void testGetGenericType_simpleField() {
 Field testField = new FieldTestHelper(SomeClassWithGenericField.class, "testField").getField();
 Class expectedType = String.class;
 Class actualType = new GenericMaster().getGenericType(testField);

 Assert.assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_nullField() {
 Field testField = null;
 Class actualType = new GenericMaster().getGenericType(testField);

 Assert.assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericType_nonGenericField() {
 Field testField = new FieldTestHelper(SomeClassWithoutGenericField.class, "testField").getField();
 Class actualType = new GenericMaster().getGenericType(testField);

 Assert.assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericType_nestedGenericType() {
 Field testField = new FieldTestHelper(SomeNestedGenericType.class, "testField").getField();
 Class expectedType = List.class;
 Class<?> rawType = testField.getType().getRawType();
 Class<?> elementType = testField.getType().getComponentType();
 Class actualType = new GenericMaster().getGenericType(testField);

 Assert.assertEquals(expectedType, actualType.getComponentType());
 Assert.assertEquals(List.class, actualType);
 Assert.assertEquals(rawType.getTypeName(), "java.util.List");
 Assert.assertEquals(elementType.getTypeName(), "java.lang.String");
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_nullFieldType() {
 Field testField = new FieldTestHelper(SomeClassWithNullFieldType.class, "testField").getField();
 testField.setAccessible(true);
 testField.set(new SomeClassWithNullFieldType(), null);
 Class actualType = new GenericMaster().getGenericType(testField);
 }

 @Test
 public void testGetGenericType_fieldWithGenericArray() {
 Field testField = new FieldTestHelper(SomeClassWithGenericArrayField.class, "testField").getField();
 Class expectedType = int[].class;
 Class actualType = new GenericMaster().getGenericType(testField);

 Assert.assertTrue(actualType.isArray());
 Assert.assertEquals(int[].class, actualType);
 }
}

class FieldTestHelper {
 private final Field field;
 private final Class clazz;

 FieldTestHelper(Class clazz, String fieldName) {
 this.clazz = clazz;
 try {
 this.field = clazz.getDeclaredField(fieldName);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }

 this.field.setAccessible(true);
 }

 Field getField() {
 return this.field;
 }
}

class SomeClassWithGenericField {
 @SuppressWarnings("unused")
 private List<String> testField;
}

class SomeClassWithoutGenericField {
 @SuppressWarnings("unused")
 private int testField;
}

class SomeNestedGenericType {
 @SuppressWarnings("unused")
 private List<String> testField;
}

class SomeClassWithNullFieldType {
 @SuppressWarnings("unused")
 private List<String> testField;
}

class SomeClassWithGenericArrayField {
 @SuppressWarnings("unused")
 private int[] testField;
}