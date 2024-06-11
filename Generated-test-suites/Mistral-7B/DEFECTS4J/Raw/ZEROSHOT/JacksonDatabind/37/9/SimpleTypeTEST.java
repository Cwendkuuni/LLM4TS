import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(SimpleTypeTest.class.getDeclaredClass("SimpleTypeTest$SimpleType")); // Test inner class
 SimpleType simpleType3 = new SimpleType(int[].class); // Array type
 SimpleType simpleType4 = new SimpleType(List.class); // Collection type
 SimpleType simpleType5 = new SimpleType(Map.class); // Map type

 assertEquals(String.class, simpleType1.getRawClass());
 assertEquals(SimpleTypeTest.class.getName().replaceAll("$", "").replaceAll("SimpleTypeTest$", ""), simpleType2.getRawClass().getName());
 assertEquals(int[].class, simpleType3.getRawClass());
 assertEquals(Collection.class, simpleType4.getRawClass().getInterfaces()[0]);
 assertEquals(Map.class, simpleType5.getRawClass());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(simpleType1);

 assertEquals(simpleType1, simpleType2);
 }

 @Test
 public void testSimpleTypeNarrowing() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType stringSubType = new SimpleType(StringSubclass.class);
 JavaType narrowedType = stringType.narrow(StringSubclass.class);

 assertNotNull(narrowedType);
 assertEquals(stringSubType, narrowedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidNarrowing() {
 SimpleType stringType = new SimpleType(String.class);
 JavaType narrowedType = stringType.narrow(ArrayList.class);
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType stringTypeWithHandler = stringType.withTypeHandler(MyStringTypeHandler.class);

 assertNotSame(stringType, stringTypeWithHandler);
 assertEquals(MyStringTypeHandler.class, stringTypeWithHandler.getTypeHandler());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidWithTypeHandler() {
 SimpleType stringType = new SimpleType(String.class);
 stringType.withTypeHandler(int.class);
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType stringTypeWithHandler = stringType.withValueHandler(MyStringValueHandler.class);

 assertNotSame(stringType, stringTypeWithHandler);
 assertEquals(MyStringValueHandler.class, stringTypeWithHandler.getValueHandler());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidWithValueHandler() {
 SimpleType stringType = new SimpleType(String.class);
 stringType.withValueHandler(int.class);
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType stringTypeStatic = stringType.withStaticTyping();

 assertTrue(stringTypeStatic.isStatic());
 }

 @Test
 public void testSimpleTypeRefine() {
 SimpleType stringType = new SimpleType(String.class);
 JavaType refinedType = stringType.refine(StringSubclass.class, null, null, null);

 assertNull(refinedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSimpleTypeUnsafeConstruction() {
 SimpleType simpleType = SimpleType.constructUnsafe(String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidConstruction() {
 SimpleType simpleType = SimpleType.construct(int.class);
 }

 @Test(expected = DeprecatedException.class)
 public void testSimpleTypeDeprecatedConstruction() {
 SimpleType simpleType = SimpleType.construct(String.class);
 }

 @Test
 public void testSimpleTypeCanonicalName() {
 SimpleType stringType = new SimpleType(String.class);
 assertEquals("java.lang.String", stringType.buildCanonicalName());
 }

 @Test
 public void testSimpleTypeEquality() {
 SimpleType stringType1 = new SimpleType(String.class);
 SimpleType stringType2 = new SimpleType(String.class);
 SimpleType stringType3 = new SimpleType(String.class);

 assertTrue(stringType1.equals(stringType1));
 assertTrue(stringType1.equals(stringType2));
 assertTrue(stringType1.equals(stringType3));
 }
}