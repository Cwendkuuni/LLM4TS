import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new JavaType[0], simpleType.getSuperTypes());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType original = new SimpleType(Integer.class, null, null, null);
 SimpleType copy = new SimpleType(original);
 assertEquals(original.getRawClass(), copy.getRawClass());
 assertSame(original.getSuperClass(), copy.getSuperClass());
 assertArrayEquals(original.getSuperTypes(), copy.getSuperTypes());
 }

 @Test
 public void testSimpleTypeSuperClassConstruction() {
 SimpleType simpleType = new SimpleType(String[].class, null, null, null);
 assertEquals(String[].class, simpleType.getRawClass());
 assertSame(String.class, simpleType.getSuperClass());
 assertArrayEquals(new JavaType[0], simpleType.getSuperTypes());
 }

 @Test
 public void testSimpleTypeWithContentType() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.withContentType(IntegerType.INT_TYPE);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class, null, null, null);
 JavaType typeHandler = JavaType.constructType(int[].class);
 SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
 assertSame(simpleType.getRawClass(), withTypeHandler.getRawClass());
 assertSame(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class, null, null, null);
 JavaType valueHandler = JavaType.constructType(String.class);
 SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
 assertSame(simpleType.getRawClass(), withValueHandler.getRawClass());
 assertSame(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class, null, null, null);
 SimpleType withStaticTyping = simpleType.withStaticTyping();
 assertFalse(simpleType.isStaticTyping());
 assertTrue(withStaticTyping.isStaticTyping());
 }

 @Test
 public void testSimpleTypeEquals() {
 SimpleType simpleType1 = new SimpleType(String.class, null, null, null);
 SimpleType simpleType2 = new SimpleType(String.class, null, null, null);
 SimpleType simpleType3 = new SimpleType(Integer.class, null, null, null);
 SimpleType simpleType4 = new SimpleType(String.class, null, null, null);
 SimpleType simpleType5 = simpleType1;

 assertTrue(simpleType1.equals(simpleType1));
 assertTrue(simpleType1.equals(simpleType4));
 assertTrue(simpleType1.equals(simpleType5));
 assertFalse(simpleType1.equals(simpleType2));
 assertFalse(simpleType1.equals(simpleType3));
 }

 @Test
 public void testSimpleTypeCanonicalName() {
 SimpleType simpleType = new SimpleType(String[].class, null, null, null);
 assertEquals("[Ljava.lang.String;", simpleType.getCanonicalName());
 }

 @Test
 public void testSimpleTypeGenericSignature() {
 SimpleType simpleType = new SimpleType(List.class);
 assertEquals("java.util.List<E>", simpleType.getGenericSignature().toString());
 }

 @Test
 public void testSimpleTypeErasedSignature() {
 SimpleType simpleType = new SimpleType(ArrayList.class);
 assertEquals("java.util.ArrayList", simpleType.getErasedSignature().toString());
 }
}