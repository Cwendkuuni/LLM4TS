package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        Class<Integer> integerType = Integer.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = null;
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<>(integerType, useTimestamp, customFormat);
        assertNotNull(serializer);
    }

    @Test
    public void testWithFormat() throws JsonMappingException {
        Class<Integer> integerType = Integer.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<>(integerType, useTimestamp, customFormat);
        DateTimeSerializerBase<Integer> withFormatSerializer = serializer.withFormat(useTimestamp, customFormat);
        assertEquals(serializer.getClass(), serializer.getClass());
        assertEquals(withFormatSerializer.getClass(), DateTimeSerializerBase.class);
        assertEquals(serializer._customFormat, null);
        assertNotNull(withFormatSerializer._customFormat);
    }

    @Test
    public void testCreateContextualEmpty() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = mock(DateTimeSerializerBase.class);
        when(serializer.createContextual(mock(SerializerProvider.class), mock(BeanProperty.class))).thenReturn(serializer);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("test", mapper.valueToTree(new Object()));
        JsonNode tree = mapper.treeToValues(rootNode, new TypeReference<Map<String, Object>>() {});
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<?> serializerInstance = serializer.createContextual(serializers, null);
        assertNotNull(serializerInstance);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualNullProperty() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = mock(DateTimeSerializerBase.class);
        when(serializer.createContextual(mock(SerializerProvider.class), null)).thenThrow(new JsonMappingException("Invalid property"));
        ObjectNode rootNode = mapper.createObjectNode();
        JsonNode tree = mapper.treeToValues(rootNode, new TypeReference<Map<String, Object>>() {});
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<?> serializerInstance = serializer.createContextual(serializers, null);
    }

    @Test
    public void testSerializeNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<String> serializer = mock(DateTimeSerializerBase.class);
        when(serializer.isEmpty(mock(SerializerProvider.class), null)).thenReturn(true);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializer.serialize(null, gen, serializers);
        verify(gen).writeNull();
    }

    @Test
    public void testSerializeDateValue() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>() {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };
        Date value = new Date();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mapper.getSerializerProvider();
        when(serializer._asTimestamp(serializers)).thenReturn(true);
        when(gen.writeNumber(eq(value.getTime()))).thenReturn(value);
        serializer.serialize(value, gen, serializers);
        verify(gen).writeNumber(eq(value.getTime()));
    }

    @Test
    public void testSerializeDateValueAsString() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>() {
            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                g.writeString(formatter.format(value));
            }
        };
        Date value = new Date();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mapper.getSerializerProvider();
        when(serializer._asTimestamp(serializers)).thenReturn(false);
        when(serializer._customFormat).thenReturn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        when(gen.writeString(anyString())).thenReturn(value.toString());
        serializer.serialize(value, gen, serializers);
        verify(gen).writeString(eq(value.toString()));
    }

    @Test(expected = IOException.class)
    public void testSerializeInvalidValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = mock(DateTimeSerializerBase.class);
        when(serializer.isEmpty(mock(SerializerProvider.class), mock(Object.class))).thenReturn(false);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializer.serialize(new Object(), gen, serializers);
        verify(gen).fail(any(IOException.class));
    }

    @Test
    public void testGetSchema() {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>() {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }
        };
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Date.class);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>() {
            @Override
            protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
                visitor.visitDateFormat(typeHint, "dateFormat", JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
            }
        };
        JsonNode schema = serializer.acceptJsonFormatVisitor(mapper.getSerializerProvider().getConfig(), Date.class);
        assertEquals("dateFormat", schema.get("properties").get("dateFormat").get("name").asText());
        assertEquals("number", schema.get("properties").get("dateFormat").get("type").asText());
    }
}