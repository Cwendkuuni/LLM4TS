
package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType unsafeSimpleType = SimpleType.constructUnsafe(SimpleTypeTest.class);
        assertNotNull(unsafeSimpleType);
        assertEquals(SimpleTypeTest.class, unsafeSimpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(int[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
        }

        SimpleType constructedType = SimpleType.construct(SimpleTypeTest.class);
        assertNotNull(constructedType);
        assertEquals(SimpleTypeTest.class, constructedType.getRawClass());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(SimpleTypeTest.class, null, null, null);
        SimpleType simpleType2 = new SimpleType(SimpleTypeTest.class, null, null, null);
        SimpleType simpleType3 = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);

        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }

    @Test
    public void testToString() {
        String expectedString = "[simple type, class com.fasterxml.jackson.databind.type.SimpleTypeTest]";
        assertEquals(expectedString, simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        simpleType.withContentType(new SimpleTypeTest().simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        simpleType.withContentTypeHandler(new SimpleTypeTest());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(new SimpleTypeTest());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertTrue(staticSimpleType.isStatic());
    }
}

