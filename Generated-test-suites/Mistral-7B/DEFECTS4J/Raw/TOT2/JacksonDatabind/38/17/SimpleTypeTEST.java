import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 // Test constructor with valid arguments
 @Test
 public void testSimpleTypeConstructor() {
 Class<?> intClass = Integer.TYPE;
 SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 assertEquals(intClass, intType.getRawClass());
 assertTrue(intType.isContainerType()); // should return false
 }

 // Test constructor with null arguments
 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeConstructorNullArguments() {
 new SimpleType(null);
 }

 // Test construct method
 @Test(expected = IllegalArgumentException.class)
 public void testConstructMethod() {
 SimpleType intType = SimpleType.construct(Integer.TYPE);
 fail("SimpleType.construct method should not be used by application code");
 }

 // Test _narrow method with valid arguments
 @Test
 public void testNarrowMethod() {
 Class<?> intClass = Integer.TYPE;
 SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 SimpleType shortType = new SimpleType(Short.TYPE, TypeBindings.emptyBindings(), null, null);
 JavaType narrowedIntType = intType.narrow(intClass);
 JavaType narrowedShortType = intType.narrow(Short.TYPE);
 assertEquals(intType, narrowedIntType);
 assertNotSame(intType, narrowedShortType);
 }

 // Test _narrow method with invalid arguments
 @Test(expected = IllegalArgumentException.class)
 public void testNarrowMethodInvalidArguments() {
 SimpleType intType = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 JavaType narrowedStringType = intType.narrow(String.class);
 }

 // Test withContentType method with valid arguments
 @Test
 public void testWithContentTypeMethod() {
 Class<?> listClass = ArrayList.class;
 SimpleType listType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
 JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType listOfStringsType = listType.withContentType(stringType);
 assertEquals("java.util.List<java.lang.String>", listOfStringsType.toCanonical());
 }

 // Test withContentType method with null argument
 @Test(expected = IllegalArgumentException.class)
 public void testWithContentTypeMethodNullArgument() {
 SimpleType listType = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType nullType = null;
 JavaType listOfNullsType = listType.withContentType(nullType);
 }

 // Test withTypeHandler method with valid arguments
 @Test
 public void testWithTypeHandlerMethod() {
 Class<?> intClass = Integer.TYPE;
 SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 Object intHandler = new Object() {
 @Override
 public void handle(Object t) {}
 };
 JavaType intTypeWithHandler = intType.withTypeHandler(intHandler);
 assertEquals(intType, intTypeWithHandler);
 }

 // Test withTypeHandler method with null argument
 @Test(expected = IllegalArgumentException.class)
 public void testWithTypeHandlerMethodNullArgument() {
 SimpleType intType = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 Object nullHandler = null;
 JavaType intTypeWithNullHandler = intType.withTypeHandler(nullHandler);
 }

 // Test withValueHandler method with valid arguments
 @Test
 public void testWithValueHandlerMethod() {
 Class<?> intClass = Integer.TYPE;
 SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 Object intHandler = new Object() {
 @Override
 public void handle(Object t) {}
 };
 JavaType intTypeWithHandler = intType.withValueHandler(intHandler);
 JavaType intTypeWithoutHandler = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 assertNotSame(intTypeWithHandler, intTypeWithoutHandler);
 }

 // Test withValueHandler method with null argument
 @Test(expected = IllegalArgumentException.class)
 public void testWithValueHandlerMethodNullArgument() {
 SimpleType intType = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 Object nullHandler = null;
 JavaType intTypeWithNullHandler = intType.withValueHandler(nullHandler);
 }

 // Test withStaticTyping method
 @Test
 public void testWithStaticTypingMethod() {
 Class<?> intClass = Integer.TYPE;
 SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 JavaType intTypeWithStaticTyping = intType.withStaticTyping();
 JavaType intTypeWithoutStaticTyping = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 assertNotSame(intTypeWithStaticTyping, intTypeWithoutStaticTyping);
 assertTrue(intTypeWithStaticTyping.isStatic());
 assertTrue(intTypeWithoutStaticTyping.isDynamic());
 }

 // Test refine method with valid arguments
 @Test
 public void testRefineMethod() {
 Class<?> intClass = Integer.TYPE;
 SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
 TypeBindings intArrayBindings = TypeBindings.emptyBindings().putBinding(intArrayType, 0);
 JavaType intArrayTypeWithIntBinding = intArrayType.withBinding(intType, 0);
 JavaType refinedIntType = intType.refine(intClass, intArrayBindings, null, null);
 assertNotNull(refinedIntType);
 assertEquals(intType, refinedIntType);
 }

 // Test refine method with null arguments
 @Test(expected = IllegalArgumentException.class)
 public void testRefineMethodNullArguments() {
 SimpleType intType = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 JavaType nullType = null;
 JavaType refinedIntType = intType.refine(null, null, null, null);
 }

 // Test buildCanonicalName method
 @Test
 public void testBuildCanonicalNameMethod() {
 Class<?> intClass = Integer.TYPE;
 SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
 assertEquals("[simple type, class java.lang.Integer]", intType.toString());
 assertEquals("[simple type, class java.lang.Integer]", intType.buildCanonicalName().toString());
 }

 // Test equals method with same object
 @Test
 public void testEqualsMethodSameObject() {
 SimpleType intType1 = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 SimpleType intType2 = intType1;
 assertTrue(intType1.equals(intType2));
 }

 // Test equals method with different objects but same type
 @Test
 public void testEqualsMethodDifferentObjectsSameType() {
 SimpleType intType1 = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 SimpleType intType2 = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 assertTrue(intType1.equals(intType2));
 }

 // Test equals method with different types
 @Test
 public void testEqualsMethodDifferentTypes() {
 SimpleType intType = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
 SimpleType stringType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
 assertFalse(intType.equals(stringType));
 }
}