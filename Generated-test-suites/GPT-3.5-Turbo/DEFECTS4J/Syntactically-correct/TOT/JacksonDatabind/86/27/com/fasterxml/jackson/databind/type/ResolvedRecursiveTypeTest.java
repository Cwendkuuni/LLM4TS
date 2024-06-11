package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

  @Test
  public void testSetReference() {
    // Typical use case
    JavaType ref = new JavaType(new TypeBase());
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    rrt.setReference(ref);
    assertEquals(ref, rrt.getSelfReferencedType());

    // Error scenario - trying to re-set self reference
    try {
        rrt.setReference(new JavaType(new TypeBase()));
        fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
        // do nothing, expected exception
    }
}

  @Test
  public void testGetGenericSignature() {
    // Typical use case
    JavaType ref = new JavaType(new TypeBase());
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    rrt.setReference(ref);
    StringBuilder sb = new StringBuilder();
    assertEquals(ref.getGenericSignature(sb).toString(), rrt.getGenericSignature(sb).toString());
  }

  @Test
  public void testGetErasedSignature() {
    // Typical use case
    JavaType ref = new JavaType(new TypeBase());
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    rrt.setReference(ref);
    StringBuilder sb = new StringBuilder();
    assertEquals(ref.getErasedSignature(sb).toString(), rrt.getErasedSignature(sb).toString());
  }
  
  @Test
  public void testWithContentType() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertSame(rrt, rrt.withContentType(new JavaType(new TypeBase())));
  }

  @Test
  public void testWithTypeHandler() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertSame(rrt, rrt.withTypeHandler(new Object()));
  }

  @Test
  public void testWithContentTypeHandler() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertSame(rrt, rrt.withContentTypeHandler(new Object()));
  }

  @Test
  public void testWithValueHandler() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertSame(rrt, rrt.withValueHandler(new Object()));
  }

  @Test
  public void testWithContentValueHandler() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertSame(rrt, rrt.withContentValueHandler(new Object()));
  }

  @Test
  public void testWithStaticTyping() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertSame(rrt, rrt.withStaticTyping());
  }

  @Test
  public void testNarrow() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertNull(rrt._narrow(String.class));
  }

  @Test
  public void testRefine() {
    // Typical use case
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertNull(rrt.refine(Object.class, new TypeBindings(), null, null));
  }

  @Test
  public void testIsContainerType() {
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertFalse(rrt.isContainerType());
  }

  @Test
  public void testToString() {
    // Typical use case
    JavaType ref = new JavaType(new TypeBase());
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(String.class, new TypeBindings());
    rrt.setReference(ref);
    assertEquals("[recursive type; " + rrt.getSelfReferencedType().getRawClass().getName() + "]", rrt.toString());

    // Edge case - unresolved reference
    ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertEquals("[recursive type; UNRESOLVED]", rrt2.toString());
  }

  @Test
  public void testEquals() {
    // Typical use case
    JavaType ref = new JavaType(new TypeBase());
    ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(String.class, new TypeBindings());
    rrt1.setReference(ref);
    ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(String.class, new TypeBindings());
    rrt2.setReference(ref);
    assertTrue(rrt1.equals(rrt2));

    // Edge case - reference is unresolved
    ResolvedRecursiveType rrt3 = new ResolvedRecursiveType(String.class, new TypeBindings());
    assertFalse(rrt2.equals(rrt3));

    // Edge case - comparing with different type
    assertFalse(rrt3.equals("Not a ResolvedRecursiveType"));

    // Edge case - comparing with null
    assertFalse(rrt3.equals(null));
  }
}