package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setUp() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        classNameIdResolver = new ClassNameIdResolver(TypeFactory.defaultInstance().constructType(String.class), typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testIdFromValue() {
        Object value = "test";
        String id = classNameIdResolver.idFromValue(value);
        assertNotNull(id);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = "test";
        Class<?> type = String.class;
        String id = classNameIdResolver.idFromValueAndType(value, type);
        assertNotNull(id);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testTypeFromId() {
        try {
            assertNotNull(classNameIdResolver.typeFromId(null, "java.lang.String"));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void test_idFrom() {
        Object value = "test";
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertNotNull(id);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        assertEquals("class name used as type id", classNameIdResolver.getDescForKnownTypeIds());
    }

}