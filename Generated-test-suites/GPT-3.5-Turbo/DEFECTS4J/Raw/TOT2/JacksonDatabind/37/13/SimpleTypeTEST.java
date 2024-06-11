package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }
    
    @Test
    public void testConstruct() {
        // Test constructing SimpleType for different types
        SimpleType simpleType1 = SimpleType.construct(Integer.class);
        assertNotNull(simpleType1);
        
        // Test exception handling for constructing SimpleType for Map, Collection, and array types
        assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(Map.class);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(Collection.class);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(int[].class);
        });
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertSame(simpleType, simpleType._narrow(Object.class));
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentType(String.class);
        });
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        Object typeHandler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertSame(typeHandler, newSimpleType.getTypeHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }
}