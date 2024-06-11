package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.*;

public class ClassNameIdResolverTest {
    private static final JsonFactory JF = new ObjectMapper().getFactory();
    private static final TypeFactory TF = JF.getTypeFactory();

    @Test
    public void testIdFromValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(String.class);
        String json = "\"java.lang.String\"";
        String value = reader.readValue(JF.createParser(json)).getValueAsString();
        ClassNameIdResolver resolver = new ClassNameIdResolver(TF.constructType(String.class), TF);
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(String.class);
        String json = "\"java.lang.String\"";
        String value = "test";
        String typeId = "java.util.List";
        Object obj = reader.readValue(JF.createParser(json));
        JavaType valueType = TF.constructType(String.class);
        JavaType type = TF.constructType(List.class, String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(valueType, TF);
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.util.List<java.lang.String>", id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(String.class);
        String json = "\"java.util.ArrayList\"";
        String id = "java.util.ArrayList";
        Object obj = reader.readValue(JF.createParser(json));
        JavaType baseType = TF.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TF);
        JavaType type = resolver.typeFromId(JF.getContext(reader), id);
        assertEquals(List.class, type.getRawClass());
        assertTrue(type.isContainerType());
    }

    @Test
    public void testIdFromValueWithEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(String.class);
        String json = "\"java.lang.Enum\"";
        String value = "Color.RED";
        String expectedId = "java.lang.Enum";
        Object obj = reader.readValue(JF.createParser(json));
        JavaType valueType = TF.constructType(String.class);
        JavaType enumType = TF.constructType(Enum.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(valueType, TF);
        String id = resolver.idFromValueAndType(value, enumType);
        assertEquals(expectedId, id);
    }

    @Test
    public void testTypeFromIdWithEnum() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(String.class);
        String json = "\"java.lang.Enum\"";
        String id = "java.lang.Enum";
        Object obj = reader.readValue(JF.createParser(json));
        JavaType baseType = TF.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TF);
        JavaType type = resolver.typeFromId(JF.getContext(reader), id);
        assertEquals(Enum.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(String.class);
        String json = "\"java.lang.Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\"";
        String id = "java.lang.Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        Object obj = reader.readValue(JF.createParser(json));
        JavaType baseType = TF.constructType(String.class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, TF);
        JavaType type = resolver.typeFromId(JF.getContext(reader), id);
    }
}