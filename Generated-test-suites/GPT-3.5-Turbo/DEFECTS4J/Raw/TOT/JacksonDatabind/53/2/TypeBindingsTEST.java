package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.create(LinkedHashMap.class, new JavaType[]{});
        
        Assert.assertNull(typeBindings.findBoundType("name"));
    }

    @Test
    public void testSize() {
        TypeBindings typeBindings = TypeBindings.create(LinkedHashMap.class, new JavaType[]{});
        
        Assert.assertEquals(0, typeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertNull(typeBindings.getBoundName(0));
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertNull(typeBindings.getBoundType(0));
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertEquals(new ArrayList<>(), typeBindings.getTypeParameters());
    }

    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertFalse(typeBindings.hasUnbound("name"));
    }

    @Test
    public void testToString() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertEquals("<>", typeBindings.toString());
    }

    @Test
    public void testHashCode() {
        TypeBindings typeBindings1 = TypeBindings.create(ArrayList.class, new JavaType[]{});
        TypeBindings typeBindings2 = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertEquals(typeBindings1.hashCode(), typeBindings2.hashCode());
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings1 = TypeBindings.create(ArrayList.class, new JavaType[]{});
        TypeBindings typeBindings2 = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertEquals(typeBindings1, typeBindings2);
    }

    @Test
    public void testTypeParameterArray() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        
        Assert.assertArrayEquals(new JavaType[]{}, typeBindings.typeParameterArray());
    }

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        
        Assert.assertEquals(0, typeBindings.size());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, new JavaType[]{});
        typeBindings = typeBindings.withUnboundVariable("name");
        
        Assert.assertTrue(typeBindings.hasUnbound("name"));
    }
}