package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(String.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType constructedType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, constructedType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType constructedType = SimpleType.construct(Object.class);
        assertEquals(Object.class, constructedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrowSameClass() {
        SimpleType narrowedType = simpleType._narrow(String.class);
        assertEquals(simpleType, narrowedType);
    }

    @Test
    public void testNarrowSubClass() {
        SimpleType narrowedType = simpleType._narrow(Object.class);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType withContentTypeType = simpleType.withContentType(simpleType);
        assertEquals(simpleType, withContentTypeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        simpleType.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType withValueHandlerType = simpleType.withValueHandler(simpleType);
        assertEquals(simpleType, withValueHandlerType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType withStaticTypingType = simpleType.withStaticTyping();
        assertTrue(withStaticTypingType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(null, refinedType);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(simpleType.equals("string"));
    }

    @Test
    public void testEqualsDifferentType() {
        SimpleType differentType = new SimpleType(Integer.class);
        assertFalse(simpleType.equals(differentType));
    }

    @Test
    public void testEqualsDifferentBindings() {
        SimpleType differentType = new SimpleType(String.class, TypeBindings.emptyBindings().addBinding("key", simpleType),
                null, null);
        assertFalse(simpleType.equals(differentType));
    }

    @Test
    public void testEqualsEqualSimpleTypes() {
        SimpleType equalType = new SimpleType(String.class);
        assertTrue(simpleType.equals(equalType));
    }
}