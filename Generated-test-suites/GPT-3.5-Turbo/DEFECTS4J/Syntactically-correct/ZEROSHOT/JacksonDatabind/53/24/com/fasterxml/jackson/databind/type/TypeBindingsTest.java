package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {
    
    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }
    
    @Test
    public void testCreate() {
        Class<?> erasedType = ArrayList.class;
        List<JavaType> typeList = new ArrayList<>();
        JavaType type1 = new JavaType(String.class);
        JavaType type2 = new JavaType(Integer.class);
        typeList.add(type1);
        typeList.add(type2);
        
        TypeBindings bindings = TypeBindings.create(erasedType, typeList);
        
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundType(0).getRawClass().getName());
        assertEquals("java.lang.Integer", bindings.getBoundType(1).getRawClass().getName());
    }
    
    @Test
    public void testCreateIfNeeded() {
        Class<?> erasedType = ArrayList.class;
        JavaType type = new JavaType(String.class);
        
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, type);
        
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundType(0).getRawClass().getName());
    }
}