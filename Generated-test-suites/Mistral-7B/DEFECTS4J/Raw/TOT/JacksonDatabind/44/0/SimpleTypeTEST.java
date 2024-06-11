import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<Integer> intClass = Integer.class;
 JavaType intType = new SimpleType(intClass);

 assertEquals(intClass, intType.getRawClass());
 assertNull(intType.getSuperClass());
 assertNull(intType.getSuperInterfaces());
 assertFalse(intType.isContainerType());
 }

 @Test
 public void testSimpleTypeWithSuperClass() {
 Class<List<Integer>> listClass = ArrayList.class;
 JavaType listType = new SimpleType(listClass, TypeBindings.emptyBindings(),
 new SimpleType(Integer.class), null);

 assertEquals(listClass, listType.getRawClass());
 assertEquals(SimpleType.class, listType.getSuperClass().getRawClass());
 assertNull(listType.getSuperInterfaces());
 assertFalse(listType.isContainerType());
 }

 @Test
 public void testSimpleTypeWithSuperInterfaces() {
 Class<Map<String, List<Integer>>> mapClass = HashMap.class;
 JavaType mapType = new SimpleType(mapClass, TypeBindings.emptyBindings(),
 null, new TypeFactory().constructTypes(String.class, List.class, Integer.class));

 assertEquals(mapClass, mapType.getRawClass());
 assertNull(mapType.getSuperClass());
 assertArrayEquals(new Class[]{String.class, List.class, Integer.class}, mapType.getSuperInterfaces());
 assertFalse(mapType.isContainerType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalMapTypeConstruction() {
 Class<Map<String, String>> mapClass = HashMap.class;
 JavaType mapType = new SimpleType(mapClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArrayTypeConstruction() {
 Class<int[]> arrayClass = int[].class;
 JavaType arrayType = new SimpleType(arrayClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalCollectionTypeConstruction() {
 Class<List<Integer>> listClass = ArrayList.class;
 JavaType listType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
 listType.withSuperInterfaces(new TypeFactory().constructTypes(Collection.class));
 }
}