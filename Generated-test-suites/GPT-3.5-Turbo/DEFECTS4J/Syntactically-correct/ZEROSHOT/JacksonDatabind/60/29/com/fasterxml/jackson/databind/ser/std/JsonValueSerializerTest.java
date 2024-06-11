package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializerTest.InnerClass;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Assert;

public class JsonValueSerializerTest {

    @JsonSerialize(using = MySerializer.class)
    public static class MyClass {
        private String name;

        public MyClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class MySerializer extends JsonSerializer<MyClass> {
        @Override
        public void serialize(MyClass myClass, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", myClass.getName());
            jsonGenerator.writeEndObject();
        }
    }

    @Test
    public void testSerialize() throws IOException {
        MyClass myClass = new MyClass("test");
        JsonValueSerializer serializer = new JsonValueSerializer("name", true);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(myClass, generator, null);
        generator.flush();
        String json = writer.toString();
        Assert.assertEquals("{\"name\":\"test\"}", json);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        JsonSerializer<Object> ser = null;
        MyClass myClass = new MyClass("test");
        JsonValueSerializer serializer = new JsonValueSerializer("name", ser, true);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        JsonSerializer<Object> ser2 = null;
        serializer.serializeWithType(myClass, generator, ser2, null);
        generator.flush();
        String json = writer.toString();
        Assert.assertEquals("{\"name\":\"test\"}", json);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<?> ser = null;
        BeanProperty property = null;
        boolean forceTypeInfo = true;
        MyClass myClass = new MyClass("test");
        JsonValueSerializer serializer = new JsonValueSerializer("name", ser, true);
        JsonSerializer<?> result = serializer.createContextual(null, property);
        Assert.assertEquals(serializer._property, result._property);
    }

    @Test
    public void testGetSchema() throws IOException, JsonMappingException {
        MyClass myClass = new MyClass("test");
        JsonSerializer<Object> var2 = null;
        JsonValueSerializer serializer = new JsonValueSerializer("name", var2, true);
        JsonNode result = serializer.getSchema((SerializerProvider) null, (Type) null);
        Assert.assertEquals("{\"type\":\"string\"}", result.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException, JsonMappingException {
        JavaType typeHint = null;
        MyClass myClass = new MyClass("test");
        JsonValueSerializer serializer = new JsonValueSerializer("name", true);
        JsonFormatVisitorWrapper visitor = new JsonFactory().getCodec().visitorProvider().writer(SerializerProvider.class, null);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        Assert.assertTrue(visitor.getProvider().isEnabled(MapperFeature.USE_STD_BEAN_NAMING));
    }

}