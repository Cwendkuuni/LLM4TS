package com.yourpackage.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 // Test constructor
 @Test
 public void testConstructor() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, null);
 assertNotNull(instance);
 }

 // Test setReference method
 @Test
 public void testSetReference() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, null);
 JavaType ref = new JavaType(Integer.class, null);
 instance.setReference(ref);
 JavaType expected = ref;
 JavaType actual = instance.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 // Test getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, null);
 assertNull(instance.getSelfReferencedType());
 JavaType ref = new JavaType(Integer.class, null);
 instance.setReference(ref);
 JavaType expected = ref;
 JavaType actual = instance.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 // Test getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, null);
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual = new StringBuilder();
 instance.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 // Test getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, null);
 StringBuilder expected = new StringBuilder("[recursive type; ");
 StringBuilder actual = new StringBuilder();
 instance.getErasedSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 // Test isContainerType method
 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, null);
 assertFalse(instance.isContainerType());
 }

 // Test equals method
 @Test
 public void testEquals() {
 ResolvedRecursiveType instance1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType instance2 = new ResolvedRecursiveType(String.class, null);
 assertNotSame(instance1, instance2);
 JavaType ref = new JavaType(Integer.class, null);
 instance1.setReference(ref);
 ResolvedRecursiveType instance3 = new ResolvedRecursiveType(String.class, null);
 instance3.setReference(ref);
 assertTrue(instance1.equals(instance3));
 }

 // Test illegal state exception when trying to set reference multiple times
 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException() {
 ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, null);
 JavaType ref = new JavaType(Integer.class, null);
 instance.setReference(ref);
 instance.setReference(new JavaType(Double.class, null));
 }
}