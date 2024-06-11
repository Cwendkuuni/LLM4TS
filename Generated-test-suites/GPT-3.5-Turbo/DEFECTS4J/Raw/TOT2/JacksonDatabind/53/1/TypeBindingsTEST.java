package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNotNull(bindings);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithEmptyTypeList() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, new ArrayList<JavaType>());
        assertNotNull(bindings);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithSingleType() {
        List<JavaType> typeList = new ArrayList<>();
        JavaType javaType = JavaType.construct(Object.class);
        typeList.add(javaType);

        TypeBindings bindings = TypeBindings.create(ArrayList.class, typeList);
        assertNotNull(bindings);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testFindBoundType() {
        List<JavaType> typeList = new ArrayList<>();
        JavaType javaType = JavaType.construct(Object.class);
        typeList.add(javaType);

        TypeBindings bindings = TypeBindings.create(ArrayList.class, typeList);
        JavaType boundType = bindings.findBoundType("E");
        assertNotNull(boundType);
        assertEquals(javaType, boundType);
    }

    @Test
    public void testWithUnboundVariable() {
        List<JavaType> typeList = new ArrayList<>();
        JavaType javaType = JavaType.construct(Object.class);
        typeList.add(javaType);

        TypeBindings bindings = TypeBindings.create(ArrayList.class, typeList);
        TypeBindings newBindings = bindings.withUnboundVariable("T");
        assertNotNull(newBindings);
        assertEquals(1, newBindings.size());
        assertEquals("E", newBindings.getBoundName(0));
        assertEquals(javaType, newBindings.getBoundType(0));
    }

}