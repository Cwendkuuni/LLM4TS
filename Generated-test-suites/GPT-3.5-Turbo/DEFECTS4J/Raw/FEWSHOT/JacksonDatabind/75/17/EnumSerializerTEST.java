package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;
import org.mockito.Mock;

public class EnumSerializerTest {
    @Mock
    private EnumValues mockEnumValues;

    @Test
    public void testConstructor() {
        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        assertEquals(mockEnumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testConstruct() {
        Class<?> enumClass = Enum.class;
        SerializationConfig config = mock(SerializationConfig.class);
        BeanDescription beanDesc = mock(BeanDescription.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        EnumSerializer enumSerializer = EnumSerializer.construct(enumClass, config, beanDesc, format);
        assertEquals(mockEnumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testCreateContextual() throws IOException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        JsonSerializer<?> result = enumSerializer.createContextual(serializers, property);

        assertEquals(enumSerializer, result);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        enumSerializer.serialize(Enum.ONE, gen, serializers);
    }

    @Test
    public void testGetSchema() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        enumSerializer.getSchema(provider, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException, JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerializeAsIndex() {
        SerializerProvider serializers = mock(SerializerProvider.class);

        EnumSerializer enumSerializer = new EnumSerializer(mockEnumValues, true);
        enumSerializer._serializeAsIndex(serializers);
    }

    @Test
    public void testIsShapeWrittenUsingIndex_Class() {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        Class<?> enumClass = Enum.class;

        EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);
    }

    @Test
    public void testIsShapeWrittenUsingIndex_Property() {
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        Class<?> enumClass = Enum.class;

        EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, false);
    }
}