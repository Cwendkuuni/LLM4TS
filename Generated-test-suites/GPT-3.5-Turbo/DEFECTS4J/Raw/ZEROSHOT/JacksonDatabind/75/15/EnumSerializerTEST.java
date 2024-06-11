package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

@JacksonStdImpl
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
        Enum en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(true);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen, times(1)).writeNumber(en.ordinal());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        Enum en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen, times(1)).writeString(en.toString());
    }

    @Test
    public void testSerializeWithStringValue() throws IOException {
        Enum en = mock(Enum.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        SerializableString mockedValue = mock(SerializableString.class);

        when(enumSerializer._serializeAsIndex(serializers)).thenReturn(false);
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        when(enumValues.serializedValueFor(en)).thenReturn(mockedValue);

        enumSerializer.serialize(en, gen, serializers);

        verify(gen, times(1)).writeString(mockedValue);
    }

    @Test
    public void testGetSchemaWithIndex() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        when(enumSerializer._serializeAsIndex(provider)).thenReturn(true);

        enumSerializer.getSchema(provider, typeHint);

        verify(provider, times(1)).constructType(typeHint);
        verifyNoMoreInteractions(provider);
    }

    @Test
    public void testGetSchemaWithStrings() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JavaType type = mock(JavaType.class);
        ArrayNode arrayNode = mock(ArrayNode.class);
        ObjectNode objectNode = mock(ObjectNode.class);

        when(enumSerializer._serializeAsIndex(provider)).thenReturn(false);
        when(provider.constructType(typeHint)).thenReturn(type);
        when(type.isEnumType()).thenReturn(true);
        when(nodeFactory.arrayNode()).thenReturn(arrayNode);
        when(nodeFactory.objectNode()).thenReturn(objectNode);
        when(_values.values()).thenReturn(Collections.emptyList());

        enumSerializer.getSchema(provider, typeHint);

        verify(provider, times(1)).constructType(typeHint);
        verify(type, times(1)).isEnumType();
        verify(enumValues, times(1)).values();
        verify(arrayNode, times(0)).add();
        verify(objectNode, times(0)).putArray();
        verifyNoMoreInteractions(provider);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithIndex() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        when(enumSerializer._serializeAsIndex(any(SerializerProvider.class))).thenReturn(true);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).getProvider();
        verify(visitor.getProvider(), times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        verify(visitor, times(1)).expectIntFormat(any(JavaType.class), any(JsonParser.NumberType.class));
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithStringValues() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        Set<String> enums = new LinkedHashSet<String>();

        when(enumSerializer._serializeAsIndex(any(SerializerProvider.class))).thenReturn(false);
        when(visitor.expectStringFormat(typeHint)).thenReturn(stringVisitor);

        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).getProvider();
        verify(visitor.getProvider(), times(1)).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        verify(visitor, times(1)).expectStringFormat(any(JavaType.class));
        verify(enumValues, times(1)).enums();
        verify(stringVisitor, times(1)).enumTypes(enums);
        verifyNoMoreInteractions(visitor);
    }
}