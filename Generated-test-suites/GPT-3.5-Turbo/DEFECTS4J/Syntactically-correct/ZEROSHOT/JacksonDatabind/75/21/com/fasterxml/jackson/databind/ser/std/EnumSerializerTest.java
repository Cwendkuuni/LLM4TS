package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.util.EnumMap;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

class EnumSerializerTest {

    @Test
    void testSerialize() throws IOException {
        EnumMap<JsonFormat.Shape, JsonSerializer<?>> map = new EnumMap<>(JsonFormat.Shape.class);
        EnumValues values = new EnumValues(map);
        EnumSerializer enumSerializer = new EnumSerializer(values, true);
        SerializerProvider provider = new SerializerProvider();
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(System.out);
        enumSerializer.serialize(Shape.STRING, generator, provider);
    }

    @Test
    void testConstruct() throws IOException {
        Class<JsonFormat.Shape> enumClass = JsonFormat.Shape.class;
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        JsonFormat.Value format = new JsonFormat.Value();
        EnumSerializer.construct(enumClass, config, beanDesc, format);
    }

    @Test
    void testCreateContextual() throws IOException {
        Class<JsonFormat.Shape> enumClass = JsonFormat.Shape.class;
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        JsonFormat.Value format = new JsonFormat.Value();
        EnumValues values = new EnumValues(new EnumMap<>(JsonFormat.Shape.class));
        EnumSerializer enumSerializer = new EnumSerializer(values, true);
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        enumSerializer.createContextual(provider, property);
        
    }

    @Test
    void testGetSchema() throws IOException {
        EnumMap<JsonFormat.Shape, JsonSerializer<?>> map = new EnumMap<>(JsonFormat.Shape.class);
        EnumValues values = new EnumValues(map);
        EnumSerializer enumSerializer = new EnumSerializer(values, true);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();
        enumSerializer.getSchema(provider, typeHint);
    }

    @Test
    void testAcceptJsonFormatVisitor() throws IOException {
        EnumMap<JsonFormat.Shape, JsonSerializer<?>> map = new EnumMap<>(JsonFormat.Shape.class);
        EnumValues values = new EnumValues(map);
        EnumSerializer enumSerializer = new EnumSerializer(values, true);
        SerializerProvider provider = new SerializerProvider();
        JavaType typeHint = new JavaType();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    void testSerializeAsIndex() throws IOException {
        EnumMap<JsonFormat.Shape, JsonSerializer<?>> map = new EnumMap<>(JsonFormat.Shape.class);
        EnumValues values = new EnumValues(map);
        EnumSerializer enumSerializer = new EnumSerializer(values, true);
        SerializerProvider provider = new SerializerProvider();
        enumSerializer._serializeAsIndex(provider);
    }

    @Test
    void testIsShapeWrittenUsingIndex() throws IOException {
        Class<JsonFormat.Shape> enumClass = JsonFormat.Shape.class;
        JsonFormat.Value format = new JsonFormat.Value();
        EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);
    }
}