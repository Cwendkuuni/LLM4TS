package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(getClass());
        assertNotNull(simpleType);
    }
    
    @Test
    public void testConstructorWithBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(getClass(), bindings, null, null);
        assertNotNull(simpleType);
    }
    
    @Test
    public void testConstructorWithSuperClass() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(Collection.class);
        SimpleType simpleType = new SimpleType(getClass(), bindings, superClass, null);
        assertNotNull(simpleType);
        assertEquals(superClass, simpleType.getSuperClass());
    }
    
    @Test
    public void testConstructorWithSuperInterfaces() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType[] superInterfaces = { new SimpleType(Map.class) };
        SimpleType simpleType = new SimpleType(getClass(), bindings, null, superInterfaces);
        assertNotNull(simpleType);
        assertEquals(superInterfaces, simpleType.getInterfaces());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(Collection.class);
        JavaType[] superInterfaces = { new SimpleType(Map.class) };
        SimpleType simpleType = new SimpleType(getClass(), bindings, superClass, superInterfaces);
        assertNotNull(simpleType);
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInterfaces, simpleType.getInterfaces());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(getClass());
        JavaType refinedType = simpleType.refine(getClass(), TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(getClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(getClass());
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        assertNotNull(result);
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(getClass());
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        assertNotNull(result);
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(getClass());
        try {
            simpleType.withContentType(new SimpleType(Collection.class));
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(getClass());
        Object typeHandler = new Object();
        SimpleType result = simpleType.withTypeHandler(typeHandler);
        assertNotNull(result);
        assertEquals(typeHandler, result.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(getClass());
        try {
            simpleType.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(getClass());
        SimpleType result = simpleType.withStaticTyping();
        assertNotNull(result);
        assertTrue(result.isStaticTyping());
    }
    
    @Test
    public void testWithSuperClass() {
        SimpleType simpleType = new SimpleType(getClass());
        JavaType superClass = new SimpleType(Map.class);
        SimpleType result = simpleType.withSuperClass(superClass);
        assertNotNull(result);
        assertEquals(superClass, result.getSuperClass());
    }
    
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(getClass());
        Object valueHandler = new Object();
        SimpleType result = simpleType.withValueHandler(valueHandler);
        assertNotNull(result);
        assertEquals(valueHandler, result.getValueHandler());
    }
    
    @Test
    public void testWithInterfaces() {
        SimpleType simpleType = new SimpleType(getClass());
        JavaType[] interfaces = { new SimpleType(Map.class) };
        SimpleType result = simpleType.withInterfaces(interfaces);
        assertNotNull(result);
        assertEquals(interfaces, result.getInterfaces());
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(getClass());
        assertNotNull(simpleType);
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
        
        try {
            SimpleType.construct(Collection.class);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
        
        try {
            SimpleType.construct(getClass().getSuperclass());
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
        
        try {
            SimpleType.construct(getClass().getInterfaces()[0]);
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(getClass());
        JavaType narrowedType = simpleType.narrow(getClass());
        assertNotNull(narrowedType);
        assertEquals(getClass(), narrowedType.getRawClass());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(getClass());
        assertNotNull(simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(getClass());
        SimpleType simpleType2 = new SimpleType(getClass());
        SimpleType simpleType3 = new SimpleType(Map.class);
        
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(""));
        assertFalse(simpleType1.equals(simpleType3));
    }
    
}