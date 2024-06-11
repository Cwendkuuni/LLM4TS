package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        JavaType javaType1 = new TypeReference<List<ResolvedRecursiveType>>(){}.getType();
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(List.class, null);
        recursiveType1.setReference(javaType1);

        JavaType javaType2 = new TypeReference<List<ResolvedRecursiveType>>(){}.getType();
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(List.class, null);
        recursiveType2.setReference(javaType2);

        assertEquals(recursiveType1, recursiveType1);
        assertEquals(recursiveType1, recursiveType1.getSelfReferencedType());
        assertEquals(recursiveType1, recursiveType1.getSelfReferencedType());
        assertNotEquals(recursiveType1, recursiveType2);
        assertNotEquals(recursiveType1, null);
        assertNotEquals(recursiveType1, new Object());

        // Test equals with different classes
        ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(String.class, null);
        assertNotEquals(recursiveType1, recursiveType3);

        // Test getGenericSignature and getErasedSignature
        StringBuilder sb = new StringBuilder();
        recursiveType1.getGenericSignature(sb);
        String genericSignature = sb.toString();
        assertEquals("[recursive type; UNRESOLVED]", genericSignature);

        sb.setLength(0);
        recursiveType1.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        assertEquals("[Ljava/util/List;", erasedSignature);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType javaType = new TypeReference<List<ResolvedRecursiveType>>(){}.getType();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(List.class, null);
        recursiveType.setReference(javaType);
        recursiveType.setReference(javaType);
    }

    @Test
    public void testToString() {
        JavaType javaType = new TypeReference<List<ResolvedRecursiveType>>(){}.getType();
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(List.class, null);
        recursiveType.setReference(javaType);

        String expected = "[recursive type; [Ljava.util.List;]";
        String actual = recursiveType.toString();
        assertEquals(expected, actual);
    }
}