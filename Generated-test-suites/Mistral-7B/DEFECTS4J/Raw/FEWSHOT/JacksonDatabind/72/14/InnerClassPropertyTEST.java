package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class InnerClassPropertyTest {

    @Test
    public void testConstruction() {
        Class<?> innerClass = InnerClassWithCustomDeserializer.Inner.class;
        Constructor<?> innerConstructor = innerClass.getDeclaredConstructor();
        innerConstructor.setAccessible(true);

        Object instance = new Object();
        BeanProperty property = new InnerClassProperty(null, innerConstructor, new AnnotatedConstructor(null, innerConstructor, null, null));

        assertEquals(InnerClassWithCustomDeserializer.Inner.class, property.getType());
        assertNotNull(property._creator);
        assertEquals(innerConstructor, property._creator.getDeclaredConstructor());
        assertNull(property._annotated);

        BeanProperty propertyWithAnnotation = new InnerClassProperty(null, innerConstructor, new AnnotatedConstructor<>(new JacksonStdImpl(), innerConstructor, null, null));

        assertEquals(InnerClassWithCustomDeserializer.Inner.class, propertyWithAnnotation.getType());
        assertNotNull(propertyWithAnnotation._creator);
        assertEquals(innerConstructor, propertyWithAnnotation._creator.getDeclaredConstructor());
        assertNotNull(propertyWithAnnotation._annotated);
        assertEquals(InnerClassWithCustomDeserializer.Inner.class, propertyWithAnnotation._annotated.getClass());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"innerClass\": {\"value\": 42}}";
        InnerClassWithCustomDeserializer obj = mapper.readValue(json, InnerClassWithCustomDeserializer.class);

        assertEquals("John", obj.name);
        assertEquals(42, obj.innerClass.value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InnerClassWithCustomDeserializer obj = new InnerClassWithCustomDeserializer();
        ObjectWriter writer = mapper.writer();

        obj.innerClass = null;
        assertEquals(null, obj.innerClass);

        String json = writer.writeValueAsString(obj);
        obj = mapper.readValue(json, InnerClassWithCustomDeserializer.class);

        InnerClassWithCustomDeserializer.Inner inner = new InnerClassWithCustomDeserializer.Inner(5);
        obj.innerClass = inner;

        json = writer.writeValueAsString(obj);
        InnerClassWithCustomDeserializer objFromJson = mapper.readValue(json, InnerClassWithCustomDeserializer.class);

        assertEquals(inner, objFromJson.innerClass);
    }

    @Test
    public void testJdkSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        InnerClassWithCustomDeserializer obj = new InnerClassWithCustomDeserializer();
        obj.innerClass = new InnerClassWithCustomDeserializer.Inner(5);

        String json = writer.writeValueAsString(obj);
        InnerClassWithCustomDeserializer objFromJson = mapper.readValue(json, InnerClassWithCustomDeserializer.class);

        assertEquals(obj.innerClass, objFromJson.innerClass);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"innerClass\": {\"value\": 42}}";

        JsonDeserializer<InnerClassWithCustomDeserializer.Inner> customDeserializer = new StdDeserializer<InnerClassWithCustomDeserializer.Inner>() {
            @Override
            public InnerClassWithCustomDeserializer.Inner deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new InnerClassWithCustomDeserializer.Inner(p.getValueAsInt());
            }
        };

        JsonDeserializer<InnerClassWithCustomDeserializer> innerClassDeserializer = new JsonDeserializer<InnerClassWithCustomDeserializer>() {
            @Override
            public InnerClassWithCustomDeserializer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                InnerClassWithCustomDeserializer obj = new InnerClassWithCustomDeserializer();
                obj.name = p.getValueAsString();
                obj.innerClass = customDeserializer.deserialize(p, ctxt);
                return obj;
            }
        };

        InnerClassWithCustomDeserializer obj = mapper.readValue(json, InnerClassWithCustomDeserializer.class, innerClassDeserializer);

        assertEquals("John", obj.name);
        assertEquals(5, obj.innerClass.value);
    }

    @Test
    public void testDeserializeWithValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        InnerClassWithCustomDeserializer obj = new InnerClassWithCustomDeserializer();
        obj.innerClass = new InnerClassWithCustomDeserializer.Inner(5);

        String json = writer.writeValueAsString(obj);

        JsonDeserializer<InnerClassWithCustomDeserializer.Inner> customDeserializer = new StdDeserializer<InnerClassWithCustomDeserializer.Inner>() {
            @Override
            public InnerClassWithCustomDeserializer.Inner deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new InnerClassWithCustomDeserializer.Inner(p.getValueAsInt());
            }
        };

        JsonDeserializer<InnerClassWithCustomDeserializer> innerClassDeserializer = new JsonDeserializer<InnerClassWithCustomDeserializer>() {
            @Override
            public InnerClassWithCustomDeserializer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                InnerClassWithCustomDeserializer obj = new InnerClassWithCustomDeserializer();
                obj.name = p.getValueAsString();
                obj.innerClassDeserializer = customDeserializer;
                return obj;
            }

            @Override
            public InnerClassWithCustomDeserializer deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vfd) throws IOException {
                InnerClassWithCustomDeserializer obj = new InnerClassWithCustomDeserializer();
                obj.name = p.getValueAsString();
                obj.innerClassDeserializer = customDeserializer;
                return obj;
            }
        };

        InnerClassWithCustomDeserializer objFromJson = mapper.readValue(json, InnerClassWithCustomDeserializer.class, innerClassDeserializer);

        assertEquals("John", objFromJson.name);
        assertEquals(5, objFromJson.innerClass.value);
        assertEquals(customDeserializer, objFromJson.innerClassDeserializer);
    }

    static class InnerClassWithCustomDeserializer {
        String name;
        Inner innerClass;
        JsonDeserializer<Inner> innerClassDeserializer;

        static class Inner {
            int value;
        }
    }

    static class JacksonStdImpl extends StdConverter<String, Integer> {
        @Override
        public Integer convert(String text, Class<Integer> aClass) {
            return Integer.parseInt(text);
        }
    }
}