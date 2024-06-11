package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        // Test constructor
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(type, bindings);

        // Test setReference method
        JavaType refType = new TypeFactory().constructType(Object.class);
        instance1.setReference(refType);
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(type, bindings);
        instance2.setReference(instance1);
        assertEquals(instance1, instance2.getSelfReferencedType());
        assertEquals(instance2, instance1.getSelfReferencedType());

        // Test getSelfReferencedType method
        assertEquals(instance1, instance1.getSelfReferencedType());
        assertNull(instance1.getSelfReferencedType()); // Before setting reference

        // Test getGenericSignature and getErasedSignature methods
        StringBuilder sb = new StringBuilder(30);
        instance1.getGenericSignature(sb);
        instance2.getGenericSignature(sb.append(" "));
        assertEquals("[recursive type; <T> java.lang.Object]", sb.toString());

        instance1.getErasedSignature(sb);
        instance2.getErasedSignature(sb.append(" "));
        assertEquals("[recursive type; UNRESOLVED]", sb.toString());
    }

    @Test
    public void testEqualsMethod() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(type, bindings);
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(type, bindings);

        // Test equals with same instance
        assertTrue(instance1.equals(instance1));

        // Test equals with different instance but same reference
        instance2.setReference(instance1);
        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));

        // Test equals with different instances and no reference
        assertFalse(instance1.equals(new Object()));
        assertFalse(instance1.equals(new ResolvedRecursiveType(String.class, bindings)));

        // Test equals with different instances and different references
        JavaType refType1 = new TypeFactory().constructType(Object.class);
        JavaType refType2 = new TypeFactory().constructType(String.class);
        instance1.setReference(refType1);
        instance2.setReference(refType2);
        assertFalse(instance1.equals(instance2));
        assertFalse(instance2.equals(instance1));
    }
}