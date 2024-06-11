package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValues = mock(EnumValues.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);

        assertEquals(enumValues, enumSerializer.getEnumValues());
        assertTrue(enumSerializer._serializeAsIndex);
    }

    @Test
    public void testConstruct() {
        EnumSerializer enumSerializer = EnumSerializer.construct(MyEnum.class, mock(SerializationConfig.class), mock(BeanDescription.class), mock(JsonFormat.Value.class));

        assertEquals(MyEnum.class, enumSerializer.getEnumClass());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        JsonSerializer<?> contextualSerializer = enumSerializer.createContextual(serializerProvider, null);

        assertEquals(enumSerializer, contextualSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        enumSerializer.serialize(MyEnum.VALUE1, jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        enumSerializer.getSchema(serializerProvider, null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);

        enumSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }

    @Test
    public void testHelperMethods() {
        EnumSerializer enumSerializer = new EnumSerializer(mock(EnumValues.class), true);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        assertTrue(enumSerializer._serializeAsIndex(serializerProvider));
        Boolean result = EnumSerializer._isShapeWrittenUsingIndex(MyEnum.class, mock(JsonFormat.Value.class), false);
        assertEquals(true, result);
    }
}