import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<Integer> intClass = Integer.class;
 SimpleType intType = new SimpleType(intClass);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(intClass);
 assertEquals(expectedType, intType);
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType intType = new SimpleType(int.class);
 SimpleType intTypeCopy = new SimpleType(intType);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(intClass);
 assertEquals(expectedType, intTypeCopy);
 }

 @Test
 public void testSimpleTypeWithBindings() {
 Class<List<Integer>> listOfIntClass = new ArrayList<Integer>().getClass();
 TypeBindings bindings = TypeBindings.builder()
 .constructBinding(List.class, new TypeReference<List<Integer>>(){})
 .build();
 SimpleType listOfIntType = new SimpleType(listOfIntClass, bindings, null, null);
 JavaType expectedType = TypeFactory.defaultInstance().constructType(listOfIntClass);
 assertEquals(expectedType, listOfIntType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidConstruction() {
 // Test with invalid class
 SimpleType intType = new SimpleType(String.class);
 fail("Expected IllegalArgumentException but got: " + intType);
 }

 @Test
 public void testSimpleTypeEquals() {
 SimpleType intType1 = new SimpleType(int.class);
 SimpleType intType2 = new SimpleType(int.class);
 SimpleType intType3 = new SimpleType(String.class);
 assertTrue(intType1.equals(intType1));
 assertTrue(intType1.equals(intType2));
 assertFalse(intType1.equals(intType3));
 }

 @Test
 public void testSimpleTypeToString() {
 SimpleType intType = new SimpleType(int.class);
 String expectedToString = "[simple type, class class java.lang.Integer]";
 assertEquals(expectedToString, intType.toString());
 }

 @Test
 public void testSimpleTypeHashCode() {
 SimpleType intType1 = new SimpleType(int.class);
 SimpleType intType2 = new SimpleType(int.class);
 SimpleType listOfIntType = new SimpleType(List.class);
 int hashCodeInt = intType1.hashCode();
 int hashCodeInt2 = intType2.hashCode();
 int hashCodeList = listOfIntType.hashCode();
 assertEquals(hashCodeInt, hashCodeInt2);
 assertNotEquals(hashCodeInt, hashCodeList);
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 ObjectMapper mapper = new ObjectMapper();
 SimpleType intType = new SimpleType(int.class);
 SimpleType intTypeWithHandler = new SimpleType(intType, null, null, null, null, mapper.getFactory().valueHandlerInstance(IntDeserializer.class), false);
 assertEquals(intType.getClass(), intTypeWithHandler.getClass());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 ObjectMapper mapper = new ObjectMapper();
 SimpleType intType = new SimpleType(int.class);
 SimpleType intTypeWithHandler = new SimpleType(intType, null, null, null, null, null, true);
 SimpleType intTypeWithHandler2 = new SimpleType(intType, null, null, null, null, mapper.getFactory().valueHandlerInstance(IntSerializer.class), true);
 assertEquals(intTypeWithHandler.getClass(), intTypeWithHandler2.getClass());
 }

 @Test
 public void testSimpleTypeWithContentTypeHandler() {
 ObjectMapper mapper = new ObjectMapper();
 SimpleType listOfIntType = new SimpleType(List.class);
 SimpleType listOfIntTypeWithHandler = new SimpleType(listOfIntType, null, null, null, null, mapper.getFactory().valueHandlerInstance(ListContentDeserializer.class), false);
 assertEquals(listOfIntType.getClass(), listOfIntTypeWithHandler.getClass());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType intType = new SimpleType(int.class);
 SimpleType intTypeStatic = intType.withStaticTyping();
 assertEquals(intTypeStatic.isStatic(), true);
 }
}