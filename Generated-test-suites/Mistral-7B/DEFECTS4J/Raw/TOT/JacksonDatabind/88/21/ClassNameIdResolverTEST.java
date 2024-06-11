package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {

    private ObjectMapper mapper;
    private ClassNameIdResolver resolver;

    @Before
    public void setup() throws IOException {
        ObjectMapper.DefaultTypingResolver defaultTypingResolver = new ObjectMapper.DefaultTypingResolver();
        mapper = new ObjectMapper();
        mapper.setDefaultTyping(defaultTypingResolver);
        resolver = new ClassNameIdResolver(Object.class.getTypeName(), mapper.getTypeFactory());
    }

    @Test
    public void testIdFromValue() throws IOException {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        Object value = new Object();
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Object", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdUnknownClass() throws IOException {
        String id = "com.nonexistent.class.Name";
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
        assertNull(javaType);
    }

    @Test
    public void testTypeFromIdWithGenerics() throws IOException {
        String id = "java.util.List<java.lang.String>";
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext().getDefaultContext(), id);
        TypeReference typeReference = new TypeReference<List<String>>() {};
        assertEquals(typeReference.getType(), javaType);
    }

    @Test
    public void testIdFromValueEnum() throws IOException {
        MyEnum value = MyEnum.ONE;
        String id = resolver.idFromValue(value);
        assertEquals(MyEnum.class.getName(), id);
    }

    @Test
    public void testIdFromValueInnerClass() throws IOException {
        OuterClass outer = new OuterClass();
        InnerClass inner = outer.new InnerClass();
        String id = resolver.idFromValue(inner);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.OuterClass$InnerClass", id);
    }
}

class MyEnum {
    static final MyEnum ONE = new MyEnum();
    static final MyEnum TWO = new MyEnum();
}

class OuterClass {
    static class InnerClass {}
}