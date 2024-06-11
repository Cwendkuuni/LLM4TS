package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        type1.setReference(refType);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        type2.setReference(type1);

        JavaType selfType1 = type1.getSelfReferencedType();
        JavaType selfType2 = type2.getSelfReferencedType();

        assertNotNull(selfType1);
        assertNotNull(selfType2);
        assertEquals(type1, selfType2);
        assertEquals(type2, selfType1);
    }

    @Test
    public void testIllegalStateException() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(String.class);
        type1.setReference(refType1);
        type1.setReference(refType2);

        try {
            ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
            type2.setReference(type1);
            type2.setReference(type1);
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = "+refType1+", new = "+refType2, e.getMessage());
        }
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(type.getSelfReferencedType());
    }
}