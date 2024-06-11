package com.fasterxml.jackson.databind.type; // import statements go here

import org.junit.Assert;
import org.junit.Test;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        Assert.assertFalse(empty.isEmpty());
        Assert.assertEquals(0, empty.size());
        Assert.assertNull(empty.findBoundType(""));
        Assert.assertFalse(empty.hasUnbound(""));
        Assert.assertEquals("", empty.toString());
    }

    @Test
    public void testCreateEmptyBinding() {
        TypeBindings empty = TypeBindings.create(Object.class, (JavaType) null);
        Assert.assertFalse(empty.isEmpty());
        Assert.assertEquals(0, empty.size());
        Assert.assertNull(empty.findBoundType(""));
        Assert.assertFalse(empty.hasUnbound(""));
        Assert.assertEquals("", empty.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypeParameters() {
        TypeBindings bindings = TypeBindings.create(String[].class, new JavaType[3]);
    }

    @Test
    public void testCreateBindingWithOneTypeParameter() {
        JavaType javaType = Reflections.newGenericType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType);
        Assert.assertFalse(bindings.isEmpty());
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(javaType, bindings.getBoundType(0));
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
        Assert.assertEquals("K[]", bindings.toString());
    }

    @Test
    public void testCreateBindingWithTwoTypeParameters() {
        JavaType javaTypeArg1 = Reflections.newGenericType(Map.class, Map.class, Integer.class);
        JavaType javaTypeArg2 = Reflections.newGenericType(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaTypeArg1, javaTypeArg2);
        Assert.assertFalse(bindings.isEmpty());
        Assert.assertEquals(2, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(javaTypeArg1, bindings.getBoundType(0));
        Assert.assertEquals("V", bindings.getBoundName(1));
        Assert.assertEquals(javaTypeArg2, bindings.getBoundType(1));
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
        Assert.assertEquals("K[]", bindings.toString());
    }

    @Test
    public void testCreateIfNeededBindingWithOneTypeParameter() {
        JavaType javaType = Reflections.newGenericType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, javaType);
        Assert.assertFalse(bindings.isEmpty());
        Assert.assertEquals(1, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(javaType, bindings.getBoundType(0));
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
        Assert.assertEquals("K[]", bindings.toString());
    }

    @Test
    public void testCreateIfNeededBindingWithTwoTypeParameters() {
        JavaType javaTypeArg1 = Reflections.newGenericType(Map.class, Map.class, Integer.class);
        JavaType javaTypeArg2 = Reflections.newGenericType(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, javaTypeArg1, javaTypeArg2);
        Assert.assertFalse(bindings.isEmpty());
        Assert.assertEquals(2, bindings.size());
        Assert.assertEquals("K", bindings.getBoundName(0));
        Assert.assertEquals(javaTypeArg1, bindings.getBoundType(0));
        Assert.assertEquals("V", bindings.getBoundName(1));
        Assert.assertEquals(javaTypeArg2, bindings.getBoundType(1));
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
        Assert.assertEquals("K[]", bindings.toString());
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType javaType = Reflections.newGenericType(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("V");
        Assert.assertEquals(2, bindings.size());
        Assert.assertFalse(bindings.hasUnbound(""));
        Assert.assertFalse(bindings.hasUnbound("K"));
        Assert.assertFalse(bindings.hasUnbound("V"));
        Assert.assertEquals("K[]", bindings.toString());
        Assert.assertEquals(3, bindingsWithUnboundVariable.size());
        Assert.assertEquals("K", bindingsWithUnboundVariable.getBoundName(0));
        Assert.assertEquals(javaType, bindingsWithUnboundVariable.getBoundType(0));
        Assert.assertEquals("V", bindingsWithUnboundVariable.getBoundName(1));
        Assert.assertEquals(javaType, bindingsWithUnboundVariable.getBoundType(1));
        Assert.assertEquals("V", bindingsWithUnboundVariable.getBoundName(2));
        Assert.assertNull(bindingsWithUnboundVariable.getBoundType(2));
        Assert.assertEquals("K[], V[]", bindingsWithUnboundVariable.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindBoundTypeWithUnboundVariable() {
        JavaType javaType = Reflections.newGenericType(Map.class, String.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType);
        bindings = bindings.withUnboundVariable("V");
        bindings.findBoundType("V");
    }

    @Test
    public void testToString() {
        JavaType javaType = Reflections.newGenericType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(Map.class, javaType);
        Assert.assertEquals("K[], V[]", bindings.toString());
    }

    
}