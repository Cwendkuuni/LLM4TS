package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(); // create a dummy JavaType
        rrt.setReference(ref);
        assertEquals(ref, rrt.getSelfReferencedType());
    }

    @Test(expected=IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(); // create a dummy JavaType
        rrt.setReference(ref);
        rrt.setReference(ref); // should throw IllegalStateException
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getGenericSignature(sb);
        assertSame(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = rrt.getErasedSignature(sb);
        assertSame(sb, result);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(); // create a dummy JavaType
        JavaType result = rrt.withContentType(contentType);
        assertSame(rrt, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        Object h = new Object(); // create a dummy object
        JavaType result = rrt.withTypeHandler(h);
        assertSame(rrt, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        Object h = new Object(); // create a dummy object
        JavaType result = rrt.withContentTypeHandler(h);
        assertSame(rrt, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        Object h = new Object(); // create a dummy object
        JavaType result = rrt.withValueHandler(h);
        assertSame(rrt, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        Object h = new Object(); // create a dummy object
        JavaType result = rrt.withContentValueHandler(h);
        assertSame(rrt, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType result = rrt.withStaticTyping();
        assertSame(rrt, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        Class<?> subclass = new Class<?>(); // create a dummy class
        JavaType result = rrt._narrow(subclass);
        assertSame(rrt, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        Class<?> rawType = new Class<?>(); // create a dummy class
        TypeBindings bindings = new TypeBindings(); // create a dummy TypeBindings
        JavaType superClass = new JavaType(); // create a dummy JavaType
        JavaType[] superInterfaces = new JavaType[0]; // create an empty array of JavaType
        JavaType result = rrt.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        assertFalse(rrt.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, rrt.toString());
        
        // Test when _referencedType is not null
        JavaType referencedType = new JavaType(); // create a dummy JavaType
        rrt.setReference(referencedType);
        expected = "[recursive type; " + referencedType.getRawClass().getName() + "]";
        assertEquals(expected, rrt.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(null, null);
        assertTrue(rrt1.equals(rrt1));
        assertFalse(rrt1.equals(null));
        assertFalse(rrt1.equals(new Object()));
        assertFalse(rrt1.equals(rrt2));
        
        // Test when _referencedType is null
        assertFalse(rrt1.equals(new ResolvedRecursiveType(null, null)));
        
        // Test when _referencedType is not null
        JavaType referencedType = new JavaType(); // create a dummy JavaType
        rrt1.setReference(referencedType);
        assertFalse(rrt1.equals(new ResolvedRecursiveType(null, null)));
        assertFalse(rrt1.equals(new ResolvedRecursiveType(null, null)));
        rrt2.setReference(referencedType);
        assertTrue(rrt1.equals(rrt2));
    }
}