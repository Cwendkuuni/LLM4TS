package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.EnumValues;


public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private EnumValues enumValues;

    @Before
    public void setUp() {
        enumValues = mock(EnumValues.class);
        enumSerializer = new EnumSerializer(enumValues, true);
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen).writeNumber(en.ordinal());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        Enum<?> en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        // mock is not enabled so the toString method should be called instead
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen).writeString(en.toString());
    }

    @Test
    public void testGetSchemaAsString() throws IOException {
        Type typeHint = mock(Type.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        ObjectNode schemaNode = mock(ObjectNode.class);
        when(enumValues.values()).thenReturn(Arrays.asList("one", "two", "three"));
        when(provider.constructType(typeHint)).thenReturn(mock(JavaType.class));
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        when(schemaNode.putArray("enum")).thenReturn(mock(ArrayNode.class));

        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        assertNotNull(schema);
        assertTrue(schema.isArray());

        verify(schemaNode).putArray(eq("enum"));
    }

    @Test
    public void testGetSchemaAsIndex() throws IOException {
        Type typeHint = mock(Type.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        ObjectNode schemaNode = mock(ObjectNode.class);
        when(provider.constructType(typeHint)).thenReturn(mock(JavaType.class));
        when(schemaNode.get(eq("enum"))).thenReturn(mock(JsonNode.class));

        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        assertNotNull(schema);
        assertFalse(schema.isArray());

        verify(schemaNode).get(eq("enum"));
    }

    @Test
    public void testAcceptJsonFormatVisitorAsStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);

        // mock is not enabled so the toString method should be called instead
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(stringVisitor).enumTypes(anySet());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsIndexFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);

        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(stringVisitor, never()).enumTypes(anySet());
    }

    @Test
    public void testGetEnumValues() {
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testContextualSerializerWithProperty() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(property.getType().getRawClass()).thenReturn(String.class);
        when(format.getShape()).thenReturn(null);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);

        JsonSerializer<?> contextualSerializer = enumSerializer.createContextual(serializers, property);

        assertTrue(contextualSerializer instanceof EnumSerializer);

        EnumSerializer newSerializer = (EnumSerializer) contextualSerializer;

        assertTrue(newSerializer._serializeAsIndex);
    }

    @Test
    public void testContextualSerializerWithoutProperty() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = null;

        JsonSerializer<?> contextualSerializer = enumSerializer.createContextual(serializers, property);

        assertTrue(contextualSerializer instanceof EnumSerializer);

        EnumSerializer newSerializer = (EnumSerializer) contextualSerializer;

        assertEquals(enumSerializer._serializeAsIndex, newSerializer._serializeAsIndex);
    }
}