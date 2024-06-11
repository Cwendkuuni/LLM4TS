
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.EnumValues;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EnumSerializerTest {

    private EnumSerializer enumSerializer;
    private EnumValues enumValues;
    private BeanProperty beanProperty;
    private JsonFormat.Value format;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        enumValues = EnumValues.constructFromName(null, TestEnum.class);
        format = new JsonFormat.Value();
        provider = mock(SerializerProvider.class);
        enumSerializer = new EnumSerializer(enumValues, false);
        beanProperty = mock(BeanProperty.class);
    }

    @Test
    public void testSerializeAsIndex() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        EnumSerializer spySerializer = spy(enumSerializer);

        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        when(spySerializer._serializeAsIndex(provider)).thenCallRealMethod();

        spySerializer.serialize(TestEnum.ONE, jsonGenerator, provider);

        verify(jsonGenerator, times(1)).writeNumber(0);
    }

    @Test
    public void testSerializeUsingToString() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        EnumSerializer spySerializer = spy(enumSerializer);

        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        when(spySerializer._serializeAsIndex(provider)).thenReturn(false);

        spySerializer.serialize(TestEnum.ONE, jsonGenerator, provider);

        verify(jsonGenerator, times(1)).writeString("ONE");
    }

    @Test
    public void testSerializeUsingSerializedValue() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        EnumSerializer spySerializer = spy(enumSerializer);

        when(jsonGenerator.isEnabled(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS)).thenReturn(false);
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        when(spySerializer._serializeAsIndex(provider)).thenReturn(false);

        spySerializer.serialize(TestEnum.ONE, jsonGenerator, provider);

        verify(jsonGenerator, times(1)).writeString("1");
    }

    @Test
    public void testCreateContextualSameFormat() throws IOException {
        EnumSerializer resultSerializer = enumSerializer.createContextual(provider, beanProperty);

        assertSame(resultSerializer, enumSerializer);
    }

    @Test
    public void testCreateContextualDifferentFormat() throws IOException {
        EnumSerializer resultSerializer = enumSerializer.createContextual(provider, beanProperty);

        assertNotSame(resultSerializer, enumSerializer);
        assertEquals(resultSerializer.getClass(), EnumSerializer.class);
    }

    @Test
    public void testGetSchemaAsIndex() {
        EnumSerializer spySerializer = spy(enumSerializer);
        when(spySerializer._serializeAsIndex(provider)).thenReturn(true);

        JsonNode result = spySerializer.getSchema(provider, null);

        assertNotNull(result);
        assertEquals("integer", result.get("type").asText());
    }

    @Test
    public void testGetSchemaAsString() {
        EnumSerializer spySerializer = spy(enumSerializer);
        when(spySerializer._serializeAsIndex(provider)).thenReturn(false);

        JsonNode result = spySerializer.getSchema(provider, null);

        assertNotNull(result);
        assertEquals("string", result.get("type").asText());
        assertTrue(result.has("enum"));
    }

    @Test
    public void testAcceptJsonFormatVisitorAsIndex() throws IOException {
        EnumSerializer spySerializer = spy(enumSerializer);
        when(spySerializer._serializeAsIndex(provider)).thenReturn(true);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JsonFormatVisitorWrapper.Visitor visitor = mock(JsonFormatVisitorWrapper.Visitor.class);
        when(jsonFormatVisitorWrapper.expectIntFormat(any())).thenReturn(visitor);

        spySerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);

        verify(visitor, times(1)).numberType(JsonParser.NumberType.INT);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws IOException {
        EnumSerializer spySerializer = spy(enumSerializer);
        when(spySerializer._serializeAsIndex(provider)).thenReturn(false);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JsonStringFormatVisitor jsonStringFormatVisitor = mock(JsonStringFormatVisitor.class);
        when(jsonFormatVisitorWrapper.expectStringFormat(any())).thenReturn(jsonStringFormatVisitor);

        spySerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);

        verify(jsonStringFormatVisitor, times(1)).enumTypes(any());
    }

    @Test
    public void testSerializeWithSerializeAsIndexProperty() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        EnumSerializer spySerializer = spy(enumSerializer);
        when(provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)).thenReturn(true);
        when(beanProperty.getType().getRawClass()).thenReturn(TestEnum.class);
        when(beanProperty.findFormatOverrides(provider)).thenReturn(format);

        spySerializer.serialize(TestEnum.ONE, jsonGenerator, provider);

        verify(jsonGenerator, times(1)).writeNumber(0);
    }

    private enum TestEnum {
        ONE, TWO, THREE
    }
}
