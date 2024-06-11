package com.fasterxml.jackson.databind.type;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @After
    public void tearDown() {
        resolvedRecursiveType = null;
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
        JavaType ref2 = new JavaType();
        try {
            resolvedRecursiveType.setReference(ref2);
        } catch (IllegalStateException ex) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref2, ex.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(resolvedRecursiveType.getSelfReferencedType().getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        assertEquals(resolvedRecursiveType.getSelfReferencedType().getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void test_narrow() {
        Class<?> subclass = new Class<?>();
        assertSame(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; ");
        if (resolvedRecursiveType.getSelfReferencedType() == null) {
            sb.append("UNRESOLVED");
            assertEquals(sb.toString(), resolvedRecursiveType.toString());
        } else {
            sb.append(resolvedRecursiveType.getSelfReferencedType().getRawClass().getName());
            assertEquals(sb.toString(), resolvedRecursiveType.toString());
        }
    }

    @Test
    public void testEquals() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
        assertFalse(resolvedRecursiveType.equals(null));
        JavaType ref = new JavaType();
        JavaType ref2 = new JavaType();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType1.setReference(ref);
        resolvedRecursiveType2.setReference(ref2);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}