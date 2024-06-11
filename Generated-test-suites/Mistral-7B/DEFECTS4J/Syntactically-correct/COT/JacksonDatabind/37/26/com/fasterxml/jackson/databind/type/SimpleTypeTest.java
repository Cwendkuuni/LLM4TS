import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeResolverBuilder;

public class SimpleTypeTest {

 private static final TypeFactory typeFactory = new TypeFactory();

 @Test
 public void testConstructorWithClass() {
 SimpleType simpleType = new SimpleType(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test
 public void testConstructorWithClassBindings() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings());
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test
 public void testConstructorWithClassBindingsSuperClass() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), List.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test
 public void testCopyConstructor() {
 SimpleType original = new SimpleType(int[].class, TypeBindings.emptyBindings(), List.class, null, null, null, false);
 SimpleType copy = new SimpleType(original);
 assertNotNull(copy);
 assertEquals(original.getRawClass(), copy.getRawClass());
 }

 @Test
 public void testConstructUnsafe() {
 try {
 SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
 fail("Expected IllegalStateException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 SimpleType simpleType = SimpleType.constructUnsafe(String.class);
 fail("Expected IllegalStateException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 SimpleType simpleType = SimpleType.constructUnsafe(Boolean.class);
 assertNotNull(simpleType);
 assertEquals(Boolean.class, simpleType.getRawClass());
 }

 @Test
 public void testConstruct() {
 try {
 SimpleType simpleType = SimpleType.construct(int[].class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 SimpleType simpleType = SimpleType.construct(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithWrongSubclass() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType javaType = simpleType.narrow(int[].class);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testNarrowWithSameClass() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType javaType = simpleType.narrow(String.class);
 assertSame(simpleType, javaType);
 }

 @Test
 public void testNarrowWithSubclass() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType javaType = simpleType.narrow(StringBuffer.class);
 assertNotNull(javaType);
 assertEquals(StringBuffer.class, javaType.getRawClass());
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType javaType = simpleType.withTypeHandler(new MyTypeHandler());
 assertNotNull(javaType);
 assertNotSame(simpleType.getTypeHandler(), javaType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType javaType = simpleType.withValueHandler(new MyValueHandler());
 assertNotNull(javaType);
 assertNotSame(simpleType.getValueHandler(), javaType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class, false);
 JavaType javaType = simpleType.withStaticTyping();
 assertTrue(javaType.isStatic());
 }

 @Test
 public void testRefine() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType javaType = simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null);
 assertNull(javaType);
 }

 @Test
 public void testBuildCanonicalName() {
 SimpleType simpleType1 = new SimpleType(String[].class);
 SimpleType simpleType2 = new SimpleType(int[].class);
 SimpleType simpleType3 = new SimpleType(List.class);
 SimpleType simpleType4 = new SimpleType(Map.class);
 SimpleType simpleType5 = new SimpleType(String.class);
 SimpleType simpleType6 = new SimpleType(String.class, TypeBindings.emptyBindings(), List.class);
 SimpleType simpleType7 = new SimpleType(String.class, TypeBindings.emptyBindings(), List.class, null, null, null, true);

 assertEquals("[class java.lang.Object[]]", simpleType1.buildCanonicalName());
 assertEquals("[class java.lang.Integer]", simpleType2.buildCanonicalName());
 assertEquals("[class java.util.List]", simpleType3.buildCanonicalName());
 assertEquals("[class java.util.Map]", simpleType4.buildCanonicalName());
 assertEquals("[simple type, class java.lang.String]", simpleType5.buildCanonicalName());
 assertEquals("[simple type, class java.util.List, class java.lang.String]", simpleType6.buildCanonicalName());
 assertEquals("[simple type, class java.lang.String]", simpleType7.buildCanonicalName());
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = new SimpleType(List.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getErasedSignature(sb);
 assertEquals("[JavaType] [List]", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = new SimpleType(List.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getGenericSignature(sb);
 assertEquals("[JavaType<T>] [List<T>]", sb.toString());
 }

 @Test
 public void testToString() {
 SimpleType simpleType = new SimpleType(List.class);
 assertEquals("[simple type, class java.util.List]", simpleType.toString());
 }

 @Test
 public void testEquals() {
 SimpleType simpleType1 = new SimpleType(List.class);
 SimpleType simpleType2 = new SimpleType(List.class);
 SimpleType simpleType3 = new SimpleType(List[].class);
 SimpleType simpleType4 = new SimpleType(Map.class);
 SimpleType simpleType5 = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);

 assertTrue(simpleType1.equals(simpleType1));
 assertTrue(simpleType1.equals(simpleType2));
 assertFalse(simpleType1.equals(simpleType3));
 assertFalse(simpleType1.equals(simpleType4));
 assertTrue(simpleType1.equals(simpleType5));
 }

 @Test
 public void testHashCode() {
 SimpleType simpleType1 = new SimpleType(List.class);
 SimpleType simpleType2 = new SimpleType(List.class);
 SimpleType simpleType3 = new SimpleType(List[].class);
 SimpleType simpleType4 = new SimpleType(Map.class);
 SimpleType simpleType5 = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);

 assertEquals(simpleType1.hashCode(), simpleType1.hashCode());
 assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
 assertNotEquals(simpleType1.hashCode(), simpleType3.hashCode());
 assertNotEquals(simpleType1.hashCode(), simpleType4.hashCode());
 assertEquals(simpleType1.hashCode(), simpleType5.hashCode());
 }

 @Test
 public void testGetRawClass() {
 SimpleType simpleType = new SimpleType(List.class);
 assertEquals(List.class, simpleType.getRawClass());
 }

 @Test
 public void testGetBindings() {
 SimpleType simpleType = new SimpleType(List.class);
 assertTrue(simpleType.getBindings().isEmpty());
 }

 @Test
 public void testGetSuperClass() {
 SimpleType simpleType = new SimpleType(List.class);
 assertNull(simpleType.getSuperClass());
 }

 @Test
 public void testGetSuperInterfaces() {
 SimpleType simpleType = new SimpleType(List.class);
 assertNotNull(simpleType.getSuperInterfaces());
 assertEquals(0, simpleType.getSuperInterfaces().length);
 }

 @Test
 public void testGetValueHandler() {
 SimpleType simpleType = new SimpleType(List.class);
 assertNull(simpleType.getValueHandler());
 }

 @Test
 public void testgetTypeHandler() {
 SimpleType simpleType = new SimpleType(List.class);
 assertNotNull(simpleType.getTypeHandler());
 }

 @Test
 public void testIsStatic() {
 SimpleType simpleType1 = new SimpleType(List.class, false);
 SimpleType simpleType2 = new SimpleType(List.class, true);

 assertFalse(simpleType1.isStatic());
 assertTrue(simpleType2.isStatic());
 }
}

 class MyTypeHandler {
 // Empty placeholder for testing
 }

 class MyValueHandler {
 // Empty placeholder for testing
 }