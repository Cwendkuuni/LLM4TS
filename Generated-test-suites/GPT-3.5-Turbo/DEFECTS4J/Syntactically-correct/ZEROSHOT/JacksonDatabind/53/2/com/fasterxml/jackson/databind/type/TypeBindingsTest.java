package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        JavaType result = typeBindings.findBoundType("T");
        Assert.assertEquals(JavaType.builder().build(), result);
    }

    @Test
    public void testIsEmpty() {
        TypeBindings typeBindings = new TypeBindings(new String[] {},
                new JavaType[] {}, null);
        boolean result = typeBindings.isEmpty();
        Assert.assertTrue(result);
    }

    @Test
    public void testSize() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        int result = typeBindings.size();
        Assert.assertEquals(1, result);
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        String result = typeBindings.getBoundName(0);
        Assert.assertEquals("T", result);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        JavaType result = typeBindings.getBoundType(0);
        Assert.assertEquals(JavaType.builder().build(), result);
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        JavaType[] javaTypes = { JavaType.builder().build() };
        Assert.assertEquals(javaTypes, typeBindings.getTypeParameters());
    }

    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = new TypeBindings(new String[] {},
                new JavaType[] {}, new String[] { "T" });
        boolean result = typeBindings.hasUnbound("T");
        Assert.assertTrue(result);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = new TypeBindings(new String[] {},
                new JavaType[] {}, null);
        TypeBindings result = typeBindings.withUnboundVariable("T");
        Assert.assertNotNull(result);
    }

    @Test
    public void testToString() {
        TypeBindings typeBindings = new TypeBindings(new String[] {},
                new JavaType[] {}, null);
        String result = typeBindings.toString();
        Assert.assertEquals("<>", result);
    }

    @Test
    public void testHashCode() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        int result = typeBindings.hashCode();
        Assert.assertEquals(31, result);
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        TypeBindings typeBindings2 = new TypeBindings(new String[] { "T" },
                new JavaType[] { JavaType.builder().build() }, null);
        boolean result = typeBindings.equals(typeBindings2);
        Assert.assertTrue(result);
    }
}