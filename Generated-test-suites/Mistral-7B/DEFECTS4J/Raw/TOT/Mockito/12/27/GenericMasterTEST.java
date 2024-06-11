import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldUtil;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

public class GenericMasterTest {

 @Test
 public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
 Field field = getField(Foo.class, "myList");
 Class expectedType = ArrayList.class;
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeWithoutGenericField() throws NoSuchFieldException {
 Field field = getField(Foo.class, "myString");
 Class expectedType = Object.class;
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeWithNullField() {
 Field field = null;
 try {
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Pass
 }
 }

 @Test
 public void testGetGenericTypeWithNullGenericType() throws NoSuchFieldException {
 Field field = getField(Foo.class, "myList");
 field.setAccessible(true);
 try {
 field.set(new Foo(), null);
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(Object.class, actualType);
 } finally {
 field.setAccessible(false);
 }
 }

 @Test
 public void testGetGenericTypeWithNestedGenerics() throws NoSuchFieldException {
 Field field = getField(Bar.class, "myMap");
 Class expectedType = Map.class;
 Class actualType = new GenericMaster().getGenericType(field);
 Assert.assertEquals(expectedType, actualType);

 Field mapType = getField(Map.class, "entrySet");
 Class mapEntryType = new GenericMaster().getGenericType(mapType);
 Class expectedMapEntryType = Set.class;
 Class actualMapEntryType = (Class) mapEntryType.getComponentType();
 Set<Class> actualTypes = new LinkedHashSet<>();
 actualTypes.add(String.class);
 actualTypes.add(Integer.class);
 Assert.assertEquals(expectedMapEntryType.isAssignableFrom(actualMapEntryType), true);
 Assert.assertEquals(actualTypes, actualMapEntryType.getTypeParameters());
 }

 private static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
 return clazz.getDeclaredField(fieldName);
 }

 static class Foo {
 private LinkedList<String> myList;
 private String myString;
 }

 static class Bar {
 private Map<String, Integer> myMap;
 }
}