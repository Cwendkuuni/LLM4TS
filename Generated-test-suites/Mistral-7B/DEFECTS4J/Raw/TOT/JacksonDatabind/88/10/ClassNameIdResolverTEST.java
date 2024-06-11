package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        SimpleModule module = new SimpleModule();
        module.addTypeHandler(String.class, new ClassNameIdResolver(String.class, new TypeFactory()));
        mapper = new ObjectMapper(new AnnotationIntrospector(true), module);
    }

    @Test
    public void testIdFromValue() throws IOException {
        String value = "Hello World";
        JsonTreeBeans jsonTreeBeans = new JsonTreeBeans();
        jsonTreeBeans.setName("JsonTreeBeans");
        jsonTreeBeans.setValue(value);

        String id = mapper.writerWithTypeIdResolver(ClassNameIdResolver.class).writeValueAsString(jsonTreeBeans);
        JsonTreeBeans deserialized = mapper.readValue(id, JsonTreeBeans.class);

        assertEquals(value, deserialized.getValue());
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.JsonTreeBeans", deserialized.getClass().getName());
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        String value = "Hello World";
        Integer number = 42;

        JsonTreeBeans jsonTreeBeans = new JsonTreeBeans();
        jsonTreeBeans.setName("JsonTreeBeans");
        jsonTreeBeans.setValue(value);

        JsonNumberBeans jsonNumberBeans = new JsonNumberBeans();
        jsonNumberBeans.setName("JsonNumberBeans");
        jsonNumberBeans.setNumber(number);

        ObjectMapper mapperWithTypeId = mapper.writerWithTypeIdResolver(ClassNameIdResolver.class);

        String jsonValueWithClassId = mapperWithTypeId.writeValueAsString(jsonTreeBeans);
        String jsonNumberWithClassId = mapperWithTypeId.writeValueAsString(jsonNumberBeans);

        JsonTreeBeans deserializedTreeBeans = mapperWithTypeId.readValue(jsonValueWithClassId, JsonTreeBeans.class);
        JsonNumberBeans deserializedNumberBeans = mapperWithTypeId.readValue(jsonNumberWithClassId, JsonNumberBeans.class);

        assertEquals(value, deserializedTreeBeans.getValue());
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.JsonTreeBeans", deserializedTreeBeans.getClass().getName());

        assertEquals(number, deserializedNumberBeans.getNumber());
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.JsonNumberBeans", deserializedNumberBeans.getClass().getName());
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        JsonTreeBeans jsonTreeBeans = new JsonTreeBeans();
        jsonTreeBeans.setValue("Hello World");

        String jsonValue = mapper.writerWithTypeIdResolver(ClassNameIdResolver.class).writeValueAsString(jsonTreeBeans);

        JavaType javaType = mapper.readerForUpdating(JsonTreeBeans.class).getType(new TypeReference<JsonTreeBeans>() {});
        TypeIdResolver typeIdResolver = new ClassNameIdResolver(javaType, mapper.getTypeFactory());

        JavaType typeFromId = typeIdResolver.typeFromId(mapper.getDeserializationContext(), id);

        assertEquals(String.class, typeFromId.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeId() throws IOException {
        String id = "InvalidTypeId";

        TypeIdResolver typeIdResolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
        JavaType typeFromId = typeIdResolver.typeFromId(mapper.getDeserializationContext(), id);

        fail("Expected IllegalArgumentException, but no exception was thrown.");
    }

    static class JsonTreeBeans {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static class JsonNumberBeans {
        private String name;
        private Integer number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }
}