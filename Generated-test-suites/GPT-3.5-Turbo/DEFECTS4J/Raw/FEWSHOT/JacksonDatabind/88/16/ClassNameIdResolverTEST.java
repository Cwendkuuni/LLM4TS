package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClassNameIdResolverTest {
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // This method is not used in ClassNameIdResolver, so no test is needed
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("java.lang.String", resolver.idFromValue("foo"));
        assertEquals("java.lang.Integer", resolver.idFromValue(42));
        assertEquals("java.util.ArrayList[]", resolver.idFromValue(new ArrayList<>()));
        assertEquals("java.util.HashMap[]", resolver.idFromValue(new HashMap<>()));
        assertEquals("com.fasterxml.jackson.databind.ObjectMapper", resolver.idFromValue(new ObjectMapper()));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("java.lang.String", resolver.idFromValueAndType("foo", String.class));
        assertEquals("java.lang.Integer", resolver.idFromValueAndType(42, Integer.class));
        assertEquals("java.util.ArrayList[]", resolver.idFromValueAndType(new ArrayList<>(), ArrayList.class));
        assertEquals("java.util.HashMap[]", resolver.idFromValueAndType(new HashMap<>(), HashMap.class));
        assertEquals("com.fasterxml.jackson.databind.ObjectMapper", resolver.idFromValueAndType(new ObjectMapper(), ObjectMapper.class));
    }

    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertNull(resolver.typeFromId(null, "java.lang.String"));
        // Add more test cases for different classes
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("java.lang.String", resolver._idFrom("foo", String.class, null));
        assertEquals("java.lang.Integer", resolver._idFrom(42, Integer.class, null));
        // Add more test cases for different classes
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}