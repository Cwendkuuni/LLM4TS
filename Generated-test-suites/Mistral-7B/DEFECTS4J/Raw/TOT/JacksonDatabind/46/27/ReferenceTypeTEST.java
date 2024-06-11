package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testReferenceTypeConstructor() {
 ReferenceType referenceType = ReferenceType.construct(String.class, IntType.getInstance(), null, null);
 JavaType referencedType = referenceType.getReferencedType();
 assertTrue(referencedType instanceof IntType);
 }

 @Test
 public void testReferenceTypeWithTypeHandler() {
 JavaType intType = IntType.getInstance();
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(String.class, intType, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
 assertNotSame(referenceType, newReferenceType);
 assertEquals(handler, newReferenceType.getTypeHandler());
 }

 @Test
 public void testReferenceTypeNarrow() {
 Class<?> subclass = String[].class;
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 JavaType narrowType = referenceType._narrow(subclass);
 assertInstanceOf(ReferenceType.class, narrowType);
 assertEquals(subclass, narrowType.getRawClass());
 }

 @Test
 public void testReferenceTypeIsReferenceType() {
 assertTrue(new ReferenceType(String.class, IntType.getInstance(), null, null, false).isReferenceType());
 }

 @Test
 public void testReferenceTypeWithContentTypeHandler() {
 Object handler = new Object();
 JavaType intType = IntType.getInstance();
 ReferenceType referenceType = new ReferenceType(String.class, intType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(handler);
 assertNotSame(referenceType, newReferenceType);
 assertEquals(handler, newReferenceType.getContentTypeHandler());
 }

 @Test
 public void testReferenceTypeToString() {
 ReferenceType referenceType = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 String actualToString = referenceType.toString();
 String expectedToString = "[reference type, class [B]reference type, class java.lang.Integer]";
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testReferenceTypeEquals() {
 ReferenceType referenceType1 = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 ReferenceType referenceType2 = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 assertTrue(referenceType1.equals(referenceType2));

 ReferenceType referenceType3 = new ReferenceType(String.class, LongType.getInstance(), null, null, false);
 assertFalse(referenceType1.equals(referenceType3));
 }

 @Test
 public void testReferenceTypeHashCode() {
 ReferenceType referenceType1 = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 ReferenceType referenceType2 = new ReferenceType(String.class, IntType.getInstance(), null, null, false);
 assertEquals(referenceType1.hashCode(), referenceType2.hashCode());

 ReferenceType referenceType3 = new ReferenceType(String.class, LongType.getInstance(), null, null, false);
 assertNotEquals(referenceType1.hashCode(), referenceType3.hashCode());
 }
}