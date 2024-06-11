package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());

        String value = "hello world";
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);

        Enum myEnum = Enum.valueOf(MyEnum.class, "TEST");
        id = resolver.idFromValue(myEnum);
        String expected = ClassUtil.getCanonicalName(myEnum.getDeclaringClass());
        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());

        String value = "hello world";
        Class<?> type = Integer.class;
        String id = resolver.idFromValueAndType(value, type);
        assertNull(id);

        Enum myEnum = Enum.valueOf(MyEnum.class, "TEST");
        id = resolver.idFromValueAndType(myEnum, String.class);
        String expected = ClassUtil.getCanonicalName(myEnum.getDeclaringClass());
        assertEquals(expected, id);
    }

    @Test
    public void testTypeFromId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());

        String id = "java.lang.String";
        JavaType type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(String.class, type.getRawClass());

        id = "com.example.MyEnum";
        JavaType expected = mapper.getTypeFactory().constructClassType(MyEnum.class);
        JavaType typeFromId = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(expected, typeFromId);

        id = "java.util.ArrayList";
        JavaType typeFromId2 = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertEquals(java.util.ArrayList.class, typeFromId2.getRawClass());
    }

    enum MyEnum {
        TEST
    }
}