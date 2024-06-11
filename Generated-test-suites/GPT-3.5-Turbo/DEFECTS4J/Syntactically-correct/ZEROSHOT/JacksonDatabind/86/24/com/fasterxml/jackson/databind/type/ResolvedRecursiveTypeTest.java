package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(ref, rrt.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType(null, null, null, null, 0, null, null, false);
        JavaType ref2 = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref1);
        rrt.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);

        assertEquals(expected.toString(), rrt.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);

        assertEquals(expected.toString(), rrt.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(rrt, rrt.withContentType(null));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(rrt, rrt.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(rrt, rrt.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(rrt, rrt.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(rrt, rrt.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(rrt, rrt.withStaticTyping());
    }

    @Deprecated
    @Test
    public void testNarrow() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertEquals(rrt, rrt._narrow(null));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertNull(rrt.refine(null, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        assertFalse(rrt.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType rrt = new ResolvedRecursiveType(null, null);

        StringBuilder sb = new StringBuilder()
                .append("[recursive type; UNRESOLVED]");
        assertEquals(sb.toString(), rrt.toString());

        JavaType ref = new JavaType(null, null, null, null, 0, null, null, false);
        rrt.setReference(ref);

        sb = new StringBuilder()
                .append("[recursive type; ")
                .append(ref.getRawClass().getName())
                .append("]");
        assertEquals(sb.toString(), rrt.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType rrt1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType rrt2 = new ResolvedRecursiveType(null, null);

        assertFalse(rrt1.equals(null));
        assertFalse(rrt1.equals(new Object()));

        assertEquals(rrt1, rrt1);
        assertEquals(rrt1, rrt2);

        JavaType ref1 = new JavaType(null, null, null, null, 0, null, null, false);
        JavaType ref2 = new JavaType(null, null, null, null, 0, null, null, false);

        rrt1.setReference(ref1);
        rrt2.setReference(ref2);

        assertFalse(rrt1.equals(rrt2));
    }
}