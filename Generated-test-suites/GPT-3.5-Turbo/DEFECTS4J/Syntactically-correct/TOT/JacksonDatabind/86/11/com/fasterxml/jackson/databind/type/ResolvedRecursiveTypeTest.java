package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

  @Test
  public void testSetReference() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    JavaType referencedType = new JavaType();
    
    resolvedRecursiveType.setReference(referencedType);
    
    Assert.assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetReference_multipleCalls_throwException() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    JavaType referencedType1 = new JavaType();
    JavaType referencedType2 = new JavaType();
    
    resolvedRecursiveType.setReference(referencedType1);
    resolvedRecursiveType.setReference(referencedType2);
  }

  @Test
  public void testGetGenericSignature() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    JavaType referencedType = new JavaType();
    
    resolvedRecursiveType.setReference(referencedType);
    
    Assert.assertEquals(referencedType.getGenericSignature(new StringBuilder()), resolvedRecursiveType.getGenericSignature(new StringBuilder()));
  }

  @Test
  public void testGetErasedSignature() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    JavaType referencedType = new JavaType();
    
    resolvedRecursiveType.setReference(referencedType);
    
    Assert.assertEquals(referencedType.getErasedSignature(new StringBuilder()), resolvedRecursiveType.getErasedSignature(new StringBuilder()));
  }

  @Test
  public void testWithContentType() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    JavaType contentType = new JavaType();
    
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
  }

  @Test
  public void testWithTypeHandler() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    Object handler = new Object();
    
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
  }

  @Test
  public void testWithContentTypeHandler() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    Object handler = new Object();
    
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
  }

  @Test
  public void testWithValueHandler() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    Object handler = new Object();
    
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
  }

  @Test
  public void testWithContentValueHandler() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    Object handler = new Object();
    
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
  }

  @Test
  public void testWithStaticTyping() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
  }

  @Test
  public void testRefine() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    JavaType refinedType = resolvedRecursiveType.refine(Object.class, new TypeBindings(), null, null);
    
    Assert.assertNull(refinedType);
  }

  @Test
  public void testIsContainerType() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    
    Assert.assertFalse(resolvedRecursiveType.isContainerType());
  }

  @Test
  public void testToString_unresolvedReference() {
    ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    
    Assert.assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
  }
}