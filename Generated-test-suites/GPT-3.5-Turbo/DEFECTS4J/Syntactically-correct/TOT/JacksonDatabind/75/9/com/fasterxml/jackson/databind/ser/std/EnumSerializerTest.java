
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

@JacksonStdImpl
public class EnumSerializerTest {

    /* Test data */
    private enum Color {
        RED("Red Color"), GREEN("Green Color"), BLUE("Blue Color");

        private String colorName;
        Color(String colorName) {
            this.colorName = colorName;
        }

        public String getColorName() {
            return colorName;
        }
    }

    @SuppressWarnings("rawtypes")
    private Class enumClass = Color.class;
    private EnumValues enumValues = EnumValues.constructFromName(null, enumClass);
    private Boolean serializeAsIndex = null;
    private EnumSerializer enumSerializer = new EnumSerializer(enumValues, serializeAsIndex);

    private SerializerProvider serializerProvider = mock(SerializerProvider.class);
    private JsonGenerator jsonGenerator = mock(JsonGenerator.class);
    private JsonSerializer jsonSerializer = mock(JsonSerializer.class);

    private Type typeHint = Type.class;

    @Test
    public void testSerialize_WhenSerializeAsIndexEnabled() throws IOException {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(Boolean.TRUE);
        enumSerializer.serialize(Color.BLUE, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeNumber(Color.BLUE.ordinal());
    }

    @Test
    public void testSerialize_WhenSerializeAsIndexDisabledAndUseToStringEnabled() throws IOException {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(Boolean.FALSE);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(Boolean.TRUE);
        enumSerializer.serialize(Color.BLUE, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString(Color.BLUE.toString());
    }

    @Test
    public void testSerialize_WhenSerializeAsIndexDisabledAndUseToStringDisabled() throws IOException {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(Boolean.FALSE);
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(Boolean.FALSE);
        enumSerializer.serialize(Color.BLUE, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString(Color.BLUE.getColorName());
    }

    @Test
    public void testCreateContextual_WhenSerializeAsIndexNeedsToBeUpdated() throws JsonMappingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.ARRAY);
        when(beanProperty.getType().getRawClass()).thenReturn(enumClass);
        when(enumSerializer._isShapeWrittenUsingIndex(enumClass, format, false)).thenReturn(true);
        EnumSerializer newEnumSerializer = (EnumSerializer) enumSerializer.createContextual(serializerProvider, beanProperty);
        assertTrue(newEnumSerializer._serializeAsIndex);
    }

    @Test
    public void testCreateContextual_WhenSerializeAsIndexDoesNotNeedToBeUpdated() throws JsonMappingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.getShape()).thenReturn(JsonFormat.Shape.ARRAY);
        when(beanProperty.getType().getRawClass()).thenReturn(enumClass);
        when(enumSerializer._isShapeWrittenUsingIndex(enumClass, format, false)).thenReturn(false);
        EnumSerializer newEnumSerializer = (EnumSerializer) enumSerializer.createContextual(serializerProvider, beanProperty);
        assertEquals(enumSerializer, newEnumSerializer);
    }

    @Test
    public void testGetSchema_WhenSerializeAsIndexEnabled() {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(Boolean.TRUE);
        JsonNode schema = enumSerializer.getSchema(serializerProvider, typeHint);
        assertNotNull(schema);
        assertTrue(schema.isIntegralNumber());
    }

    @Test
    public void testGetSchema_WhenSerializeAsIndexDisabled() {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(Boolean.FALSE);
        JsonNode schema = enumSerializer.getSchema(serializerProvider, typeHint);
        assertNotNull(schema);
        assertTrue(schema.isTextual());
        assertTrue(schema.has("enum"));
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndexEnabled() throws JsonMappingException {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(Boolean.TRUE);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, javaType);
        verify(visitor, times(1)).visitIntFormat(eq(javaType), eq(JsonParser.NumberType.INT));
    }

    @Test
    public void testAcceptJsonFormatVisitor_WhenSerializeAsIndexDisabled() throws JsonMappingException {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(Boolean.FALSE);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);
        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        when(visitor.expectStringFormat(javaType)).thenReturn(stringVisitor);
        enumSerializer.acceptJsonFormatVisitor(visitor, javaType);
        verify(stringVisitor, times(1)).enumTypes(notNull());
    }
}
