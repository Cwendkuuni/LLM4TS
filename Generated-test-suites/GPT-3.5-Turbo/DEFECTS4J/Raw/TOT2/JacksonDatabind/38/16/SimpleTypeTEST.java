package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

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
        SimpleType constructedType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, constructedType.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType anotherType = new SimpleType(Object.class);
        SimpleType narrowedType = simpleType.narrow(Object.class);
        assertEquals(anotherType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentType(String.class);
        });
    }

    @Test
    public void testWithContentValueHandler() {
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentValueHandler(null);
        });
    }

    @Test
    public void testRefine() {
        assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }

}