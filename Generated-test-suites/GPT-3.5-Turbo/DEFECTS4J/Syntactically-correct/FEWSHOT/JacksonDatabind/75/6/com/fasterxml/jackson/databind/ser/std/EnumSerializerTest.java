package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        EnumValues enumValues = mock(EnumValues.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Enum en = mock(Enum.class);

        enumSerializer.serialize(en, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString(any());
    }

    @Test
    public void testGetSchema() {
        EnumValues enumValues = mock(EnumValues.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        Type type = mock(Type.class);

        enumSerializer.getSchema(serializerProvider, type);

        verify(serializerProvider, times(1)).constructType(type);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumValues enumValues = mock(EnumValues.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonStringFormatVisitor jsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        Set<String> enums = new LinkedHashSet<>();

        when(visitor.getProvider()).thenReturn(null);
        when(visitor.expectStringFormat(typeHint)).thenReturn(jsonStringFormatVisitor);
        when(enumValues.enums()).thenReturn(enums);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(jsonStringFormatVisitor, times(1)).enumTypes(enums);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        EnumValues enumValues = mock(EnumValues.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(property.getType()).thenReturn(mock(JavaType.class));
        when(enumValues.enums()).thenReturn(mock(Set.class));
        when(enumValues.values()).thenReturn(mock(Set.class));

        enumSerializer.createContextual(serializers, property);

        verify(property, times(2)).getType();
    }

    @Test
    public void testConstruct() {
        EnumSerializer enumSerializer = EnumSerializer.construct(Enum.class, mock(SerializationConfig.class), mock(BeanDescription.class), null);

        verify(enumSerializer._values, times(1)).getEnumClass();
    }
}