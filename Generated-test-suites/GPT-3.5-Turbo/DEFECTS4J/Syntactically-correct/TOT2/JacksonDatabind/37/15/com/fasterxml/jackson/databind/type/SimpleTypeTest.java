package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType._narrow(Object.class);
        assertNotEquals(simpleType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentType(new JavaType());
        });
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType updatedType = simpleType.withTypeHandler(handler);
        assertNotNull(updatedType);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType updatedType = simpleType.withStaticTyping();
        assertTrue(updatedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(Integer.class, new TypeBindings(), new JavaType(), new JavaType[0]);
        assertNull(refinedType);
    }

}