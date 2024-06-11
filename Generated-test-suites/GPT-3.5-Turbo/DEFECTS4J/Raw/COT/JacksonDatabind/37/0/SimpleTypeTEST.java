package com.fasterxml.jackson.databind.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class SimpleTypeTest {

    private SimpleType simpleType;

    @BeforeEach
    void setUp() {
        simpleType = new SimpleType(String.class);
    }

    @Test
    void testConstructUnsafe() {
        SimpleType constructedType = SimpleType.constructUnsafe(Integer.class);
        Assertions.assertEquals(Integer.class, constructedType.getRawClass());
    }

    @Test
    void testConstruct() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(Map.class);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(ArrayList.class);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            SimpleType.construct(Object[].class);
        });
    }

    @Test
    void testConstructor() {
        SimpleType type = new SimpleType(String.class, null, null, null);
        Assertions.assertEquals(String.class, type.getRawClass());
    }

    @Test
    void testWithContentType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentType(String.class);
        });
    }
    
    @Test
    void testWithContentTypeHandler() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentTypeHandler(null);
        });
    }

    @Test
    void testWithStaticTyping() {
        SimpleType staticType = simpleType.withStaticTyping();
        Assertions.assertTrue(staticType.isStatic());
    }

    @Test
    void testRefine() {
        JavaType refinedType = simpleType.refine(String.class, null, null, null);
        Assertions.assertNull(refinedType);
    }

    @Test
    void testIsContainerType() {
        Assertions.assertFalse(simpleType.isContainerType());
    }

    @Test
    void testGetErasedSignature() {
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        Assertions.assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    void testGetGenericSignature() {
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        Assertions.assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    void testToString() {
        Assertions.assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    void testEquals() {
        SimpleType otherType = new SimpleType(String.class);
        Assertions.assertEquals(simpleType, otherType);

        SimpleType differentType = new SimpleType(Integer.class);
        Assertions.assertNotEquals(simpleType, differentType);

        Object obj = new Object();
        Assertions.assertNotEquals(simpleType, obj);
    }
}