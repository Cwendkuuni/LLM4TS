
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
        
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStaticTyping());
    }

    @Test
    public void test_narrow() {
        SimpleType simpleType = new SimpleType(String.class);
        assertSame(simpleType, simpleType._narrow(String.class));
        
        SimpleType subclassType = (SimpleType) simpleType._narrow(Object.class);
        assertNotNull(subclassType);
        assertEquals(Object.class, subclassType.getRawClass());
        assertNull(subclassType.getBindings());
        assertNull(subclassType.getSuperClass());
        assertNull(subclassType.getSuperInterfaces());
        assertNull(subclassType.getValueHandler());
        assertNull(subclassType.getTypeHandler());
        assertFalse(subclassType.isStaticTyping());
    }
    
    @Test
    public void testWith() {
        SimpleType simpleType = new SimpleType(String.class);
        
        SimpleType typeWithContentType = (SimpleType) simpleType.withContentType(Object.class);
        assertNotNull(typeWithContentType);
        assertEquals(String.class, typeWithContentType.getRawClass()); // No effect because SimpleType doesn't have content type
        
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
        
        SimpleType typeWithValueHandler = (SimpleType) simpleType.withValueHandler(new Object());
        assertNotNull(typeWithValueHandler);
        assertEquals(String.class, typeWithValueHandler.getRawClass());
        assertNull(typeWithValueHandler.getBindings());
        assertNull(typeWithValueHandler.getSuperClass());
        assertNull(typeWithValueHandler.getSuperInterfaces());
        assertNotNull(typeWithValueHandler.getValueHandler());
        assertNull(typeWithValueHandler.getTypeHandler());
        assertFalse(typeWithValueHandler.isStaticTyping());
        
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
        
        SimpleType staticType = (SimpleType) simpleType.withStaticTyping();
        assertNotNull(staticType);
        assertEquals(String.class, staticType.getRawClass());
        assertNull(staticType.getBindings());
        assertNull(staticType.getSuperClass());
        assertNull(staticType.getSuperInterfaces());
        assertNull(staticType.getValueHandler());
        assertNull(staticType.getTypeHandler());
        assertTrue(staticType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        
        JavaType BaseType = simpleType.refine(null, null, null, null);
        assertNull(BaseType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Object.class);
        
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
}

