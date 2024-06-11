import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

 private GenericMaster genericMaster;
 private Object testObject;

 @Before
 public void setup() {
 testObject = new TestClass<Integer, String>();
 genericMaster = new GenericMaster();
 }

 @Test
 public void testGetGenericType() {
 Field<List<String>> field = FieldUtil.getField(testObject, "listField");
 Class expectedType = String.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForEmptyArray() {
 Field<int[]> field = FieldUtil.getField(testObject, "intArrayField");
 Class expectedType = int[].class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForNullField() {
 Field field = null;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericTypeForNonGenericField() {
 Field<Object> field = FieldUtil.getField(testObject, "nonGenericField");
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericTypeForNestedGenericType() {
 Field<List<List<Integer>>> field = FieldUtil.getField(testObject, "nestedListField");
 Class expectedType = List.class;
 Class actualType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
 Class nestedExpectedType = List.class;
 Class nestedActualType = ((ParameterizedType) actualType).getActualTypeArguments()[0];
 assertEquals(expectedType, actualType.getComponentType());
 assertEquals(nestedExpectedType, nestedActualType);
 }

 @Test
 public void testGetGenericTypeForUnknownType() {
 try {
 Field unknownField = testObject.getClass().getDeclaredField("unknownField");
 unknownField.setAccessible(true);
 Class actualType = genericMaster.getGenericType(unknownField);
 assertEquals(Object.class, actualType);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 // do nothing
 }
 }

 @Test
 public void testGetGenericTypeForUnknownClass() {
 try {
 Class unknownClass = Class.forName("unknown.package.UnknownClass");
 Field field = unknownClass.getDeclaredField("unknownField");
 field.setAccessible(true);
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(Object.class, actualType);
 } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
 // do nothing
 }
 }
}

class TestClass<T, S> {
 List<T> listField;
 int[] intArrayField;
 List<List<Integer>> nestedListField;
 Object nonGenericField;
}