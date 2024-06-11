
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new JavaType(getClass(), null, null, null, null, false);

        resolvedRecursiveType = new ResolvedRecursiveType(getClass(), null);
    }

      @Test
      public void testSetReference() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        resolvedRecursiveType.setReference(referencedType);

        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
      }

      @Test(expected = IllegalStateException.class)
      public void testSetReference_MultipleTimes() {
        resolvedRecursiveType.setReference(referencedType);

        resolvedRecursiveType.setReference(referencedType);
      }

      @Test
      public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();

        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        assertEquals(referencedType.getGenericSignature(sb), result);
      }

      @Test
      public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();

        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        assertEquals(referencedType.getErasedSignature(sb), result);
      }

      @Test
      public void testWithContentType() {
        JavaType contentType = new JavaType(getClass(), null, null, null, null, false);

        JavaType result = resolvedRecursiveType.withContentType(contentType);

        assertEquals(resolvedRecursiveType, result);
      }

      @Test
      public void testWithTypeHandler() {
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
      }

      @Test
      public void testWithContentTypeHandler() {
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);

        assertEquals(resolvedRecursiveType, result);
      }

      @Test
      public void testWithValueHandler() {
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
      }

      @Test
      public void testWithContentValueHandler() {
        Object handler = new Object();

        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);

        assertEquals(resolvedRecursiveType, result);
      }

      @Test
      public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();

        assertEquals(resolvedRecursiveType, result);
      }

      @Deprecated
      @Test
      public void test_narrow() {
        Class<?> subclass = getClass();

        JavaType result = resolvedRecursiveType._narrow(subclass);

        assertEquals(resolvedRecursiveType, result);
      }

      @Test
      public void testRefine() {
        Class<?> rawType = getClass();
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
      }

      @Test
      public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
      }

      @Test
      public void testToString_WhenReferencedTypeIsNull() {
        String expected = "[recursive type; UNRESOLVED]";

        String result = resolvedRecursiveType.toString();

        assertEquals(expected, result);
      }

      @Test
      public void testToString_WhenReferencedTypeIsNotNull() {
        resolvedRecursiveType.setReference(referencedType);

        String expected = "[recursive type; " + referencedType.getRawClass().getName() + "]";

        String result = resolvedRecursiveType.toString();

        assertEquals(expected, result);
      }

      @Test
      public void testEquals_WithSameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
      }

      @Test
      public void testEquals_WithNull() {
        assertFalse(resolvedRecursiveType.equals(null));
      }

      @Test
      public void testEquals_WithDifferentClass() {
        assertFalse(resolvedRecursiveType.equals(getClass()));
      }

      @Test
      public void testEquals_WithDifferentReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(getClass(), null);
        other.setReference(referencedType);

        assertFalse(resolvedRecursiveType.equals(other));
      }

      @Test
      public void testEquals_WithSameReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(getClass(), null);
        other.setReference(referencedType);

        resolvedRecursiveType.setReference(referencedType);

        assertTrue(resolvedRecursiveType.equals(other));
      }
}
