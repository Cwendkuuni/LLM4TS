package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 JavaType refType = new TypeFactory().constructType(String.class);
 type1.setReference(refType);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
 type2.setReference(type1);

 assertEquals(type1, type2.getSelfReferencedType());
 assertEquals(type2, type1.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
 JavaType refType1 = new TypeFactory().constructType(String.class);
 JavaType refType2 = new TypeFactory().constructType(Integer.class);
 type1.setReference(refType1);
 type1.setReference(refType2);
 }
}