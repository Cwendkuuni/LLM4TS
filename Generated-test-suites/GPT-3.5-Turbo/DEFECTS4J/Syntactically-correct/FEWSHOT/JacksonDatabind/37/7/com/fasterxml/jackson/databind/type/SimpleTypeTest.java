package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Number.class);
        JavaType narrowedType = simpleType._narrow(Long.class);
        assertEquals(Long.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(List.class);
        try {
            simpleType.withContentType(String.class);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        Object typeHandler = new Object();
        SimpleType newType = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        try {
            simpleType.withContentTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType newType = simpleType.withStaticTyping();
        assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Long.class);
        JavaType refinedType = simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(null, refinedType);
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(Float.class);
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Double.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertEquals("D", erasedSignature.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Character.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Character;", genericSignature.toString());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(Boolean.class);
        String stringRepresentation = simpleType.toString();
        assertEquals("[simple type, class java.lang.Boolean]", stringRepresentation);
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(Integer.class);
        SimpleType simpleType2 = SimpleType.construct(Integer.class);
        assertTrue(simpleType1.equals(simpleType2));
        
        SimpleType simpleType3 = SimpleType.construct(String.class);
        assertFalse(simpleType1.equals(simpleType3));
    }
}