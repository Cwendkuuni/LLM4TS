import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterResolver;

public class TypeBindingsTest {

 // Test case for create method with no type parameters
 @Test
 public void testEmptyTypeBindings() {
 TypeBindings bindings = TypeBindings.emptyBindings();
 assertEquals(0, bindings.size());
 }

 // Test case for create method with one type parameter
 @Test
 public void testSingleTypeParameterBindings() {
 Class<List<String>> listOfStringsClass = new ArrayList<String>().getClass();
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 TypeBindings bindings = TypeBindings.create(listOfStringsClass, stringType);
 assertEquals(1, bindings.size());
 assertEquals("java.lang.String", bindings.getBoundName(0));
 assertEquals(String.class, bindings.getBoundType(0));
 }

 // Test case for create method with two type parameters
 @Test
 public void testTwoTypeParameterBindings() {
 Class<Map<Integer, List<String>>> mapOfIntListStringClass = new HashMap<Integer, ArrayList<String>>().getClass();
 JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType listOfStringsType = TypeFactory.defaultInstance().constructType(List.class, String.class);
 TypeBindings bindings = TypeBindings.create(mapOfIntListStringClass, intType, listOfStringsType);
 assertEquals(2, bindings.size());
 assertEquals("java.lang.Integer", bindings.getBoundName(0));
 assertEquals(Integer.class, bindings.getBoundType(0));
 assertEquals("java.util.List", bindings.getBoundName(1));
 assertEquals(List.class, bindings.getBoundType(1));
 assertEquals("java.lang.String", bindings.getBoundName(1).getTypeParameters()[0].getType().getName());
 }

 // Test case for createIfNeeded method with one type parameter
 @Test
 public void testSingleTypeParameterOptionalBindings() {
 Class<List<String>> listOfStringsClass = new ArrayList<String>().getClass();
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 TypeBindings bindings = TypeBindings.createIfNeeded(listOfStringsClass, stringType);
 assertEquals(1, bindings.size());
 assertEquals("java.lang.String", bindings.getBoundName(0));
 assertEquals(String.class, bindings.getBoundType(0));
 }

 // Test case for createIfNeeded method with no type parameters
 @Test
 public void testNoTypeParameterOptionalBindings() {
 Class<Object> objectClass = Object.class;
 TypeBindings bindings = TypeBindings.createIfNeeded(objectClass, null);
 assertEquals(0, bindings.size());
 }

 // Test case for createIfNeeded method with two type parameters
 @Test
 public void testTwoTypeParameterOptionalBindings() {
 Class<Map<Integer, List<String>>> mapOfIntListStringClass = new HashMap<Integer, ArrayList<String>>().getClass();
 JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
 JavaType listOfStringsType = TypeFactory.defaultInstance().constructType(List.class, String.class);
 TypeBindings bindings = TypeBindings.createIfNeeded(mapOfIntListStringClass, intType, listOfStringsType);
 assertEquals(2, bindings.size());
 assertEquals("java.lang.Integer", bindings.getBoundName(0));
 assertEquals(Integer.class, bindings.getBoundType(0));
 assertEquals("java.util.List", bindings.getBoundName(1));
 assertEquals(List.class, bindings.getBoundType(1));
 assertEquals("java.lang.String", bindings.getBoundName(1).getTypeParameters()[0].getType().getName());
 }

 // Test case for findBoundType method
 @Test
 public void testFindBoundType() {
 String[] names = new String[] {"T", "U"};
 JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class) };
 TypeBindings bindings = new TypeBindings(names, types, null);
 assertEquals(Integer.class, bindings.findBoundType("T"));
 assertEquals(String.class, bindings.findBoundType("U"));
 assertEquals(null, bindings.findBoundType("X"));
 }

 // Test case for isEmpty method
 @Test
 public void testEmptyTypeBindingsIsEmpty() {
 TypeBindings emptyBindings = TypeBindings.emptyBindings();
 assertTrue(emptyBindings.isEmpty());
 }

 // Test case for isEmpty method with non-empty bindings
 @Test
 public void testNonEmptyTypeBindingsIsNotEmpty() {
 String[] names = new String[] {"T", "U"};
 JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class) };
 TypeBindings bindings = new TypeBindings(names, types, null);
 assertFalse(bindings.isEmpty());
 }

 // Test case for size method
 @Test
 public void testSize() {
 String[] names = new String[] {"T", "U"};
 JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class) };
 TypeBindings bindings = new TypeBindings(names, types, null);
 assertEquals(2, bindings.size());
 }

 // Test case for getBoundName method
 @Test
 public void testGetBoundName() {
 String[] names = new String[] {"T", "U"};
 JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class) };
 TypeBindings bindings = new TypeBindings(names, types, null);
 assertEquals("T", bindings.getBoundName(0));
 assertEquals("U", bindings.getBoundName(1));
 }

 // Test case for getBoundType method
 @Test
 public void testGetBoundType() {
 String[] names = new String[] {"T", "U"};
 JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class) };
 TypeBindings bindings = new TypeBindings(names, types, null);
 assertEquals(Integer.class, bindings.getBoundType(0));
 assertEquals(String.class, bindings.getBoundType(1));
 }

 // Test case for getTypeParameters method
 @Test
 public void testGetTypeParameters() {
 String[] names = new String[] {"T", "U"};
 JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class) };
 TypeBindings bindings = new TypeBindings(names, types, null);
 List<JavaType> typeParameters = bindings.getTypeParameters();
 assertEquals(2, typeParameters.size());
 assertEquals(Integer.class, typeParameters.get(0));
 assertEquals(String.class, typeParameters.get(1));
 }

 // Test case for hasUnbound method
 @Test
 public void testHasUnbound() {
 String[] names = new String[] {"T", "U"};
 JavaType[] types = new JavaType[] { TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class) };
 TypeBindings bindings = new TypeBindings(names, types, null);
 assertFalse(bindings.hasUnbound("T"));
 assertFalse(bindings.hasUnbound("U"));
 TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("V");
 assertTrue(bindingsWithUnbound.hasUnbound("V"));
 }
}