package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        // given
        Class<?> raw = SimpleType.class;
        
        // when
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        
        // then
        assertEquals(raw, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }
    
    @Test
    public void testConstruct() {
        // given
        Class<?> cls = SimpleType.class;
        
        try {
            // when
            SimpleType.construct(cls);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Can not construct SimpleType for a Map (class: com.fasterxml.jackson.databind.type.SimpleType)", e.getMessage());
        }
    }
    
    @Test
    public void testNarrow() {
        // given
        Class<?> subclass = SimpleType.class;
        SimpleType simpleType = new SimpleType(subclass);
        
        // when
        JavaType narrowedType = simpleType._narrow(subclass);
        
        // then
        assertEquals(simpleType, narrowedType);
    }
    
    @Test
    public void testWithContentType() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        try {
            // when
            simpleType.withContentType(null);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        // when
        SimpleType newSimpleType = simpleType.withTypeHandler(null);
        
        // then
        assertEquals(simpleType, newSimpleType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        try {
            // when
            simpleType.withContentTypeHandler(null);
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        // when
        SimpleType newSimpleType = simpleType.withStaticTyping();
        
        // then
        assertFalse(simpleType.isStatic());
        assertTrue(newSimpleType.isStatic());
    }
    
    @Test
    public void testRefine() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        // when
        JavaType refinedType = simpleType.refine(Object.class, null, null, null);
        
        // then
        assertNull(refinedType);
    }
    
    @Test
    public void testBuildCanonicalName() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        // when
        String canonicalName = simpleType.buildCanonicalName();
        
        // then
        assertEquals("com.fasterxml.jackson.databind.type.SimpleTypeTest", canonicalName);
    }
    
    @Test
    public void testIsContainerType() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        // when
        boolean isContainerType = simpleType.isContainerType();
        
        // then
        assertFalse(isContainerType);
    }
    
    @Test
    public void testGetErasedSignature() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        StringBuilder sb = new StringBuilder();
        
        // when
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        
        // then
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleTypeTest;", erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        StringBuilder sb = new StringBuilder();
        
        // when
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        
        // then
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleTypeTest<+Ljava/lang/Object;>;", genericSignature.toString());
    }
    
    @Test
    public void testToString() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        
        // when
        String string = simpleType.toString();
        
        // then
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SimpleTypeTest]", string);
    }
    
    @Test
    public void testEquals() {
        // given
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        SimpleType sameSimpleType = new SimpleType(SimpleTypeTest.class);
        SimpleType differentSimpleType = new SimpleType(String.class);
        
        // when
        boolean isEqual = simpleType.equals(sameSimpleType);
        boolean isDifferent = simpleType.equals(differentSimpleType);
        
        // then
        assertTrue(isEqual);
        assertFalse(isDifferent);
    }
}