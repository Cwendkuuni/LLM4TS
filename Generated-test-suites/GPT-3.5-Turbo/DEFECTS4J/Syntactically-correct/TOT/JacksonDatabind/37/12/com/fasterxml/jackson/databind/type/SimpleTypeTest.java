package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import java.util.*;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setUp() {
        simpleType = new SimpleType(Object.class);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleType);
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

    @Test(expected = IllegalArgumentException.class)
    public void testConstructForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructForArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType withContentType = simpleType.withContentType(SimpleType.constructUnsafe(Integer.class));
        assertNull(withContentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        simpleType.withContentTypeHandler(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

}