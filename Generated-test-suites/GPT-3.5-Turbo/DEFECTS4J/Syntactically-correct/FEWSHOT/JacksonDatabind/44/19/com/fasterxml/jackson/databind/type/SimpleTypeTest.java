package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class SimpleTypeTest {
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        JavaType narrowedType = simpleType.narrow(String.class);
        assertEquals(String.class, narrowedType.getRawClass());
        assertFalse(narrowedType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType withContentType = simpleType.withContentType(SimpleType.construct(Integer.class));
        assertEquals(SimpleType.construct(Integer.class), withContentType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentType(SimpleType.construct(String.class));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType withContentTypeHandler = simpleType.withContentTypeHandler("handler");
        assertEquals(simpleType, withContentTypeHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerThrowsException() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentTypeHandler(null);
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType withContentValueHandler = simpleType.withContentValueHandler("handler");
        assertEquals(simpleType, withContentValueHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerThrowsException() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isJavaLangObject());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(String.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.buildCanonicalName();
        assertEquals(String.class.getName(), simpleType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        SimpleType simpleType3 = SimpleType.construct(Integer.class);
        
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
    
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(SimpleType.construct(Object.class));
        assertEquals(Object.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        SimpleType simpleType = new SimpleType(SimpleType.construct(Object.class), TypeBindings.emptyBindings(), null, null, "valueHandler", "typeHandler", false);
        assertEquals(Object.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }
}