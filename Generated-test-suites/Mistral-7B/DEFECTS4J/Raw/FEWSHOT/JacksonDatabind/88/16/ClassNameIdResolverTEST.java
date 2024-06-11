package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.JavaType;

public class ClassNameIdResolverTest {

    private static ObjectMapper mapper = new ObjectMapper();
    private static TypeFactory typeFactory = mapper.getTypeFactory();

    @Test
    public void testIdFromValue() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        String id = resolver.idFromValue("Hello World");
        assertEquals("java.lang.String", id);

        id = resolver.idFromValue(new Object());
        assertNull(id);
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Integer.class, typeFactory);

        String id = resolver.idFromValueAndType("42", Integer.class);
        assertEquals("int", id);

        id = resolver.idFromValueAndType(42, Integer.class);
        assertEquals("int", id);

        id = resolver.idFromValueAndType("42", String.class);
        assertNull(id);
    }

    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, typeFactory);

        JavaType type = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String");
        assertEquals(String.class, type.getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList");
        assertEquals(java.util.ArrayList.class, type.getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumSet");
        assertEquals(java.util.EnumSet.class, type.getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumMap");
        assertEquals(java.util.EnumMap.class, type.getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.Collections$UnmodifiableListMap");
        assertEquals(java.util.Collections.UnmodifiableListMap.class, type.getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Class<java.util.ArrayList>");
        assertEquals(Class.class, type.getRawClass());
        assertEquals(java.util.ArrayList.class, type.getComponentType().getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "com.fasterxml.jackson.core.JsonFactory");
        assertEquals(com.fasterxml.jackson.core.JsonFactory.class, type.getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Enum");
        assertEquals(Class.class, type.getRawClass());
        assertEquals(Enum.class, type.getComponentType().getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.HashMap$Entry");
        assertEquals(java.util.HashMap.Entry.class, type.getRawClass());

        type = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String[]");
        assertEquals(String[].class, type.getRawClass());

        // Test for invalid type id
        try {
            type = resolver.typeFromId(mapper.getDeserializationContext(), "InvalidTypeId");
            fail();
        } catch (IOException e) {
            // Expected
        }
    }
}