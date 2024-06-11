package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        JavaType refType = new JavaType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);

        assertNotNull(resolvedType1);
        assertNotNull(resolvedType2);

        assertEquals(refType, resolvedType1.getSelfReferencedType());
        assertNull(resolvedType1._referencedType);

        resolvedType1.setReference(resolvedType2);
        assertEquals(resolvedType2, resolvedType1.getSelfReferencedType());
        assertEquals(resolvedType1, resolvedType2.getSelfReferencedType());

        StringBuilder sb = new StringBuilder();
        resolvedType1.getGenericSignature(sb);
        assertEquals("[recursive type; [Ljava.lang.Object;]", sb.toString());
        resolvedType1.getErasedSignature(sb);
        assertEquals("[Ljava.lang.Object;", sb.toString());

        assertNull(resolvedType1.withContentType(JavaType.constructCollectionType(List.class, Integer.TYPE)));
        assertNull(resolvedType1.withTypeHandler(null));
        assertNull(resolvedType1.withContentTypeHandler(null));
        assertNull(resolvedType1.withValueHandler(null));
        assertNull(resolvedType1.withContentValueHandler(null));
        assertNull(resolvedType1.withStaticTyping());

        ResolvedRecursiveType resolvedType3 = (ResolvedRecursiveType) resolvedType1.refine(ResolvedRecursiveType.class, null, null, null);
        assertNotNull(resolvedType3);
        assertEquals(resolvedType1, resolvedType3.getSelfReferencedType());

        assertFalse(resolvedType1.isContainerType());

        assertEquals("[" + ResolvedRecursiveType.class.getName() + "; UNRESOLVED]", resolvedType1.toString());

        assertFalse(resolvedType1.equals(new Object()));
        assertFalse(resolvedType1.equals(new Object[]{})[0]);
        assertFalse(resolvedType1.equals(new String("test")));
        assertFalse(resolvedType1.equals(new Integer(1)));
        assertFalse(resolvedType1.equals(new ResolvedRecursiveType(Integer.TYPE, null)));
        assertFalse(resolvedType1.equals(new ResolvedRecursiveType(String.class, null)));
        assertFalse(resolvedType1.equals(new TypeBase(Integer.TYPE, null, null, null, 0, null, null, false)));

        assertFalse(resolvedType1.equals(resolvedType2));
        assertFalse(resolvedType2.equals(resolvedType1));
    
        resolvedType1.setReference(resolvedType1);
        assertFalse(resolvedType1.equals(resolvedType2));
        assertFalse(resolvedType2.equals(resolvedType1));
        
        try {
            resolvedType1.setReference(resolvedType2);
            resolvedType1.setReference(resolvedType1);
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetGenericSignatureAndErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType refType = new JavaType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);

        resolvedType.setReference(refType);

        resolvedType.getGenericSignature(sb);
        assertEquals("[recursive type; java.lang.reflect.Type]", sb.toString());
        sb.setLength(0);
        resolvedType.getErasedSignature(sb);
        assertEquals("java.lang.reflect.Type", sb.toString());
    }

    @Test
    public void testHashCode() {
        JavaType refType = new JavaType(ResolvedRecursiveType.class);
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);

        assertEquals(resolvedType1.hashCode(), resolvedType1.hashCode());
        assertEquals(resolvedType1.hashCode(), resolvedType2.hashCode());

        resolvedType1.setReference(refType);
        assertEquals(resolvedType1.hashCode(), resolvedType1.hashCode());
        resolvedType2.setReference(refType);
        assertEquals(resolvedType1.hashCode(), resolvedType2.hashCode());

        resolvedType1.setReference(resolvedType2);
        resolvedType2.setReference(resolvedType1);
        assertEquals(resolvedType1.hashCode(), resolvedType2.hashCode());
        assertEquals(resolvedType2.hashCode(), resolvedType1.hashCode());
    }
}