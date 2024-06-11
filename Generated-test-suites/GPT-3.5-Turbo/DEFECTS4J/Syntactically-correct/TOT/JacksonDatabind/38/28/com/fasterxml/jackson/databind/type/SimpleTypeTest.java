package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);

        //Edge case
        assertSame(SimpleTypeTest.class, simpleType.getRawClass());

        //Error scenario
        assertNotSame(HashMap.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructUnsafe() {
        //Typical use case
        SimpleType simpleType = SimpleType.constructUnsafe(SimpleTypeTest.class);

        //Edge case
        assertSame(SimpleTypeTest.class, simpleType.getRawClass());

        //Error scenario
        assertNotSame(HashMap.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        //Typical use case
        SimpleType simpleType = SimpleType.construct(SimpleTypeTest.class);

        //Edge case
        assertSame(SimpleTypeTest.class, simpleType.getRawClass());

        //Error scenario
        assertNotSame(HashMap.class, simpleType.getRawClass());
    }

    @Test
    public void testNarrow() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        SimpleType narrowedType = simpleType._narrow(SimpleTypeTest.class);

        //Edge case
        assertSame(SimpleTypeTest.class, narrowedType.getRawClass());

        //Error scenario
        assertNotSame(HashMap.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        simpleType.withContentType(SimpleTypeTest.class);
    }

    @Test
    public void testWithTypeHandler() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        SimpleType typeWithHandler = simpleType.withTypeHandler("handler");

        //Edge case
        assertEquals("handler", typeWithHandler.getTypeHandler());

        //Error scenario
        assertNotEquals("wrong handler", typeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        simpleType.withContentTypeHandler("handler");
    }

    @Test
    public void testWithvalueHandler() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        SimpleType typeWithValueHandler = simpleType.withValueHandler("handler");

        //Edge case
        assertEquals("handler", typeWithValueHandler.getValueHandler());

        //Error scenario
        assertNotEquals("wrong handler", typeWithValueHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        simpleType.withContentValueHandler("handler");
    }

    @Test
    public void testWithStaticTyping() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        SimpleType staticType = simpleType.withStaticTyping();

        //Edge case
        assertTrue(staticType.isStatic());

        //Error scenario
        assertFalse(staticType.isDynamic());
    }

    @Test
    public void testRefine() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        JavaType refinedType = simpleType.refine(SimpleTypeTest.class, TypeBindings.emptyBindings(), simpleType, null);

        //Edge case
        assertNull(refinedType);

        //Error scenario
        assertNotNull(simpleType.refine(HashMap.class, TypeBindings.emptyBindings(), simpleType, null));
    }

    @Test
    public void testIsContainerType() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        assertFalse(simpleType.isContainerType());

        //Edge case
        SimpleType arrayType = SimpleType.construct(SimpleTypeTest[].class);
        assertTrue(arrayType.isContainerType());

        //Error scenario
        SimpleType mapType = SimpleType.construct(HashMap.class);
        assertFalse(mapType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleTypeTest;", sb.toString());

        //Edge case
        sb = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleTypeTest;", sb.toString());

        //Error scenario
        sb = simpleType.getErasedSignature(new StringBuilder());
        assertNotEquals("wrong signature", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleTypeTest;;", sb.toString());

        //Edge case
        sb = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleTypeTest;;", sb.toString());

        //Error scenario
        sb = simpleType.getGenericSignature(new StringBuilder());
        assertNotEquals("wrong signature", sb.toString());
    }

    @Test
    public void testToString() {
        //Typical use case
        SimpleType simpleType = new SimpleType(SimpleTypeTest.class);
        assertEquals("[simple type, class com.fasterxml.jackson.databind.type.SimpleTypeTest]", simpleType.toString());

        //Edge case
        SimpleType arrayType = SimpleType.construct(SimpleTypeTest[].class);
        assertEquals("[simple type, class [Lcom.fasterxml.jackson.databind.type.SimpleTypeTest;]", arrayType.toString());

        //Error scenario
        SimpleType mapType = SimpleType.construct(HashMap.class);
        assertNotEquals("wrong string representation", mapType.toString());
    }

    @Test
    public void testEquals() {
        //Typical use case
        SimpleType simpleType1 = new SimpleType(SimpleTypeTest.class);
        SimpleType simpleType2 = new SimpleType(SimpleTypeTest.class);
        assertTrue(simpleType1.equals(simpleType2));

        //Edge case
        SimpleType arrayType = SimpleType.construct(SimpleTypeTest[].class);
        SimpleType arrayTypeCopy = SimpleType.construct(SimpleTypeTest[].class);
        assertTrue(arrayType.equals(arrayTypeCopy));

        //Error scenario
        assertFalse(simpleType1.equals(arrayTypeCopy));
    }

}