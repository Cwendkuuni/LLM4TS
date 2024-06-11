package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private TypeFactory typeFactory;
    private ClassNameIdResolver resolver;

    @org.junit.Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        typeFactory = mapper.getTypeFactory();
        resolver = new ClassNameIdResolver(Object.class._defaultInit(), typeFactory);
    }

    @Test
    public void testIdFromValue() throws IOException {
        assertEquals("java.lang.String", resolver.idFromValue("Hello World"));
        assertEquals("java.util.List", resolver.idFromValue(Arrays.asList(1, 2, 3)));
        assertEquals("java.util.Map", resolver.idFromValue(new HashMap<>()));
        assertEquals("java.lang.Enum", resolver.idFromValue(Enum.valueOf(String.class, "RED")));
        assertNull(resolver.idFromValue(null));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        assertEquals("java.lang.String", resolver.idFromValueAndType("Hello World", String.class));
        assertEquals("java.util.List<java.lang.Integer>", resolver.idFromValueAndType(Arrays.asList(1, 2, 3), List.class));
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", resolver.idFromValueAndType(new HashMap<>(), Map.class));
        assertEquals("java.lang.Enum<com.test.MyEnum>", resolver.idFromValueAndType(Enum.valueOf(MyEnum.class, "RED"), MyEnum.class));
        assertNull(resolver.idFromValueAndType(null, Object.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType stringType = resolver.typeFromId(null, "java.lang.String");
        assertEquals(String.class, stringType.getRawClass());

        JavaType listType = resolver.typeFromId(null, "java.util.List<java.lang.Integer>");
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Integer.class, listType.getContentType());

        JavaType mapType = resolver.typeFromId(null, "java.util.Map<java.lang.String, java.lang.Integer>");
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());

        JavaType enumType = resolver.typeFromId(null, "java.lang.Enum<com.test.MyEnum>");
        assertEquals(MyEnum.class, enumType.getRawClass());

        assertNull(resolver.typeFromId(null, null));
        assertNull(resolver.typeFromId(null, "InvalidTypeId"));
    }

    // Add more test cases as needed to cover all methods and edge cases

    static class MyEnum {
        MyEnum(String name) {
            this.name = name;
        }

        public String name;
    }
}