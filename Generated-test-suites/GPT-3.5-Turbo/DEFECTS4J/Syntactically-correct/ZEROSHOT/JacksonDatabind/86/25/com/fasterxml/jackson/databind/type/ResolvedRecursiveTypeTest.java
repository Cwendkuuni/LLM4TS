package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ResolvedRecursiveTypeTest {

  private ResolvedRecursiveType resolvedRecursiveType;

  @Before
  public void setUp() {
    resolvedRecursiveType = new ResolvedRecursiveType(null, null);
  }

  @Test
  public void testSetReference() {
    JavaType ref = new JavaType();
    resolvedRecursiveType.setReference(ref);
    Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetReferenceTwice() {
    JavaType ref1 = new JavaType();
    JavaType ref2 = new JavaType();
    resolvedRecursiveType.setReference(ref1);
    resolvedRecursiveType.setReference(ref2);
  }

  @Test
  public void testGetGenericSignature() {
    StringBuilder sb = new StringBuilder();
    JavaType ref = new JavaType();
    resolvedRecursiveType.setReference(ref);
    resolvedRecursiveType.getGenericSignature(sb);
    Assert.assertEquals(ref.getGenericSignature(sb), sb.toString());
  }

  @Test
  public void testGetErasedSignature() {
    StringBuilder sb = new StringBuilder();
    JavaType ref = new JavaType();
    resolvedRecursiveType.setReference(ref);
    resolvedRecursiveType.getErasedSignature(sb);
    Assert.assertEquals(ref.getErasedSignature(sb), sb.toString());
  }

  @Test
  public void testWithContentType() {
    JavaType contentType = new JavaType();
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));

  }

  @Test
  public void testWithTypeHandler() {
    Object h = new Object();
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
  }

  @Test
  public void testWithContentTypeHandler() {
    Object h = new Object();
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
  }

  @Test
  public void testWithValueHandler() {
    Object h = new Object();
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
  }

  @Test
  public void testWithContentValueHandler() {
    Object h = new Object();
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
  }

  @Test
  public void testWithStaticTyping() {
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
  }

  @Test
  public void testNarrow() {
    Class<?> subclass = new Class<>();
    Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
  }

  @Test
  public void testRefine() {
    Class<?> rawType = new Class<>();
    TypeBindings bindings = new TypeBindings();
    JavaType superClass = new JavaType();
    JavaType[] superInterfaces = new JavaType[]{new JavaType()};

    Assert.assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
  }

  @Test
  public void testIsContainerType() {
    Assert.assertFalse(resolvedRecursiveType.isContainerType());
  }

  @Test
  public void testToStringWhenReferenceIsNull() {
    Assert.assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
  }

  @Test
  public void testToStringWhenReferenceIsNotNull() {
    String expected = "[recursive type; " + resolvedRecursiveType.getSelfReferencedType().getRawClass().getName() + "]";
    Assert.assertEquals(expected, resolvedRecursiveType.toString());
  }

  @Test
  public void testEqualsWhenSameInstance() {
    Assert.assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
  }

  @Test
  public void testEqualsWhenNull() {
    Assert.assertFalse(resolvedRecursiveType.equals(null));
  }

  @Test
  public void testEqualsWhenDifferentClass() {
    Assert.assertFalse(resolvedRecursiveType.equals(new Object()));
  }

  @Test
  public void testEqualsWhenReferencedTypeIsNull() {
    ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
    Assert.assertFalse(resolvedRecursiveType.equals(other));
  }

  @Test
  public void testEqualsWhenSelfReferencedTypeIsDifferent() {
    JavaType selfReference1 = new JavaType();
    JavaType selfReference2 = new JavaType();
    ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
    resolvedRecursiveType.setReference(selfReference1);
    other.setReference(selfReference2);
    Assert.assertFalse(resolvedRecursiveType.equals(other));
  }

  @Test
  public void testEqualsWhenSelfReferencedTypeAreEqual() {
    JavaType selfReference = new JavaType();
    ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
    resolvedRecursiveType.setReference(selfReference);
    other.setReference(selfReference);
    Assert.assertTrue(resolvedRecursiveType.equals(other));
  }
}