package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithBindings() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new Class[0], simpleType.getSuperInterfaces());

 SimpleType simpleTypeWithBinding = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
 SimpleType simpleTypeWithStaticBinding = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, true);

 assertTrue(simpleTypeWithBinding.isStatic());
 assertFalse(simpleTypeWithStaticBinding.isStatic());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType copiedSimpleType = new SimpleType(simpleType);
 assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
 assertSame(simpleType.getBindings(), copiedSimpleType.getBindings());
 }

 @Test
 public void testSimpleTypeUnsafeConstruction() {
 try {
 SimpleType simpleType = SimpleType.constructUnsafe(String.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeDeprecatedConstruction() {
 SimpleType simpleType = SimpleType.construct(String.class);
 }

 @Test
 public void testSimpleTypeNarrowing() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType stringWrapperType = new SimpleType(String.class.getWrapperType());
 SimpleType narrowedStringWrapperType = stringType.narrow(StringWrapper.class);
 assertSame(stringWrapperType, narrowedStringWrapperType);
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithValueHandler = new SimpleType(String.class, null, null, null, null, new StringValueHandler(), false);
 assertNotSame(simpleType.getValueHandler(), simpleTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithTypeHandler = new SimpleType(String.class, null, null, null, null, null, false);
 simpleTypeWithTypeHandler = simpleTypeWithTypeHandler.withTypeHandler(new StringTypeHandler());
 assertNotSame(simpleType.getTypeHandler(), simpleTypeWithTypeHandler.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithContentTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithContentTypeHandler = new SimpleType(String.class, null, null, null, null, null, false);
 simpleTypeWithContentTypeHandler = simpleTypeWithContentTypeHandler.withContentTypeHandler(new StringContentTypeHandler());
 assertSame(simpleType, simpleTypeWithContentTypeHandler);
 }

 @Test
 public void testSimpleTypeWithValueHandlerAndTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithHandlers = new SimpleType(String.class, null, null, null, null, new StringValueHandler(), new StringTypeHandler(), false);
 assertNotSame(simpleType.getValueHandler(), simpleTypeWithHandlers.getValueHandler());
 assertNotSame(simpleType.getTypeHandler(), simpleTypeWithHandlers.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType staticSimpleType = simpleType.withStaticTyping();
 assertTrue(staticSimpleType.isStatic());
 }

 @Test
 public void testSimpleTypeRefine() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType stringWrapperType = new SimpleType(String.class.getWrapperType());
 SimpleType refinedStringWrapperType = stringType.refine(StringWrapper.class, null, null, null);
 assertNull(refinedStringWrapperType);
 }

 @Test
 public void testSimpleTypeCanonicalName() {
 SimpleType simpleType = new SimpleType(String[].class);
 String canonicalName = simpleType.buildCanonicalName();
 assertEquals("[[Ljava.lang.String;]", canonicalName);
 }

 @Test
 public void testSimpleTypeErasedSignature() {
 SimpleType simpleType = new SimpleType(String.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getErasedSignature(sb);
 assertEquals("String", sb.toString());
 }

 @Test
 public void testSimpleTypeGenericSignature() {
 SimpleType simpleType = new SimpleType(List.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getGenericSignature(sb);
 assertEquals("[Ljava.util.List;", sb.toString());
 }

 @Test
 public void testSimpleTypeToString() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals("[simple type, class java.lang.String]", simpleType.toString());
 }

 @Test
 public void testSimpleTypeEquals() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class);
 assertTrue(simpleType1.equals(simpleType2));
 }

 @Test
 public void testSimpleTypeHashCode() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class);
 int hashCode1 = simpleType1.hashCode();
 int hashCode2 = simpleType2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testSimpleTypeHashCodeWithDifferentClass() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(Integer.class);
 int hashCode1 = simpleType1.hashCode();
 int hashCode2 = simpleType2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testSimpleTypeHashCodeWithDifferentBindings() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String[].class);
 int hashCode1 = simpleType1.hashCode();
 int hashCode2 = simpleType2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testSimpleTypeHashCodeWithSameClassButDifferentBindings() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class, new TypeBindings(Arrays.asList(new SimpleType(Integer.class))));
 int hashCode1 = simpleType1.hashCode();
 int hashCode2 = simpleType2.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testSimpleTypeEqualsWithDifferentClass() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(Integer.class);
 assertFalse(simpleType1.equals(simpleType2));
 }

 @Test
 public void testSimpleTypeEqualsWithSameClassButDifferentBindings() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(String.class, new TypeBindings(Arrays.asList(new SimpleType(Integer.class))));
 assertFalse(simpleType1.equals(simpleType2));
 }
}