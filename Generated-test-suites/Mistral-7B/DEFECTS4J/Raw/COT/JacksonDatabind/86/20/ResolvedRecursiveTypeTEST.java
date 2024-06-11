package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType referenceType;
 private JavaType selfReferencedType;

 @Before
 public void setUp() {
 referenceType = new SimpleType("java.util.List");
 selfReferencedType = new ResolvedRecursiveType(List.class, new TypeBindings());
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(List.class, new TypeBindings());
 assertNull(recursiveType.getSelfReferencedType());
 recursiveType.setReference(referenceType);
 assertEquals(referenceType, recursiveType.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(List.class, new TypeBindings());
 recursiveType.setReference(referenceType);
 recursiveType.setReference(referenceType);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(List.class, new TypeBindings());
 assertNull(recursiveType.getSelfReferencedType());
 recursiveType.setReference(referenceType);
 assertEquals(referenceType, recursiveType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual = new StringBuilder();
 selfReferencedType.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder expected = new StringBuilder("java.util.List");
 StringBuilder actual = new StringBuilder();
 selfReferencedType.getErasedSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 JavaType result = selfReferencedType.withContentType(referenceType);
 assertEquals(selfReferencedType, result);
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 JavaType result = selfReferencedType.withTypeHandler(handler);
 assertEquals(selfReferencedType, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object();
 JavaType result = selfReferencedType.withContentTypeHandler(handler);
 assertEquals(selfReferencedType, result);
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object();
 JavaType result = selfReferencedType.withValueHandler(handler);
 assertEquals(selfReferencedType, result);
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object();
 JavaType result = selfReferencedType.withContentValueHandler(handler);
 assertEquals(selfReferencedType, result);
 }

 @Test
 public void testWithStaticTyping() {
 JavaType result = selfReferencedType.withStaticTyping();
 assertEquals(selfReferencedType, result);
 }

 @Test
 public void testNarrow() {
 try {
 selfReferencedType._narrow(List.class);
 fail("_narrow should not be called");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testRefine() {
 try {
 selfReferencedType.refine(List.class, new TypeBindings(), null, new JavaType[0]);
 fail("refine should not be called");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testIsContainerType() {
 assertFalse(selfReferencedType.isContainerType());
 }

 @Test
 public void testToString() {
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual = new StringBuilder();
 actual.append(selfReferencedType.toString());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(List.class, new TypeBindings());
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(List.class, new TypeBindings());
 assertFalse(recursiveType1.equals(recursiveType2));
 selfReferencedType.setReference(referenceType);
 ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(List.class, new TypeBindings());
 selfRecursiveType3.setReference(referenceType);
 assertTrue(recursiveType1.equals(recursiveType3));
 }
}