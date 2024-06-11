package com.fasterxml.jackson.databind.type;

import org.junit.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        Assert.assertNotNull(type);
        Assert.assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            SimpleType.construct(Collection.class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            SimpleType.construct(Object[].class);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        SimpleType type = SimpleType.construct(String.class);
        Assert.assertNotNull(type);
        Assert.assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.constructUnsafe(Object.class);
        SimpleType narrowType = (SimpleType)type.narrow(String.class);
        Assert.assertEquals(String.class, narrowType.getRawClass());
    }

}