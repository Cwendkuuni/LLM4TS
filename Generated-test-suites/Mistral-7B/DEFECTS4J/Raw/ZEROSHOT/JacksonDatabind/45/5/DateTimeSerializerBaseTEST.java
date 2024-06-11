package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseCreation() throws IOException, JsonProcessingException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        assertNotNull(serializer);
        assertEquals(Date.class, serializer.handledType());
    }

    @Test
    public void testDateTimeSerializerBaseSerializationAsTimestamp() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);

        Date date = new Date();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonGenerator gen = serializers.getJsonFactory().createJsonGenerator(System.out);

        serializer.serialize(date, gen, serializers);
        gen.flush();

        JsonParser parser = mapper.getFactory().createParser(System.in);
        JsonNode node = parser.readValueAsTree();
        assertNotNull(node.get("@type"));
        assertEquals("number", node.get("@type").asText());
        assertEquals(date.getTime(), node.get("value").asLong());
    }

    @Test
    public void testDateTimeSerializerBaseSerializationAsString() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        Date date = new Date();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonGenerator gen = serializers.getJsonFactory().createJsonGenerator(System.out);

        serializer.serialize(date, gen, serializers);
        gen.flush();

        JsonParser parser = mapper.getFactory().createParser(System.in);
        JsonNode node = parser.readValueAsTree();
        assertNotNull(node.get("@type"));
        assertEquals("string", node.get("@type").asText());
        assertEquals("\"2023-03-21T12:34:56.789Z\"", node.get("value").asText());
    }

    @Test
    public void testDateTimeSerializerBaseSerializationWithCustomFormat() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        Date date = new Date();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonGenerator gen = serializers.getJsonFactory().createJsonGenerator(System.out);

        serializer.serialize(date, gen, serializers);
        gen.flush();

        JsonParser parser = mapper.getFactory().createParser(System.in);
        JsonNode node = parser.readValueAsTree();
        assertNotNull(node.get("@type"));
        assertEquals("string", node.get("@type").asText());
        assertEquals("\"21-03-2023 12:34:56\"", node.get("value").asText());
    }

    @Test
    public void testDateTimeSerializerBaseDeserializationAsTimestamp() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonNode node = mapper.readValueAsTree("1678902000000");
        Date date = serializer.deserialize(node, serializers);
        assertEquals(new Date(1678902000000L), date);
    }

    @Test
    public void testDateTimeSerializerBaseDeserializationAsString() throws IOException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonNode node = mapper.readValueAsTree("\"2023-03-21T12:34:56.789Z\"");
        Date date = serializer.deserialize(node, serializers);
        assertEquals(new Date(1678956896789L), date);
    }

    @Test
    public void testDateTimeSerializerBaseDeserializationWithCustomFormat() throws IOException, JsonProcessingException, ParseException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonNode node = mapper.readValueAsTree("\"21-03-2023 12:34:56\"");
        Date date = serializer.deserialize(node, serializers);
        assertEquals(new Date(1678956896000L), date);
    }

    @Test
    public void testDateTimeSerializerBaseIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertFalse(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(new Date()));
        assertTrue(serializer.isEmpty(new DateTimeSerializerBase<Date>(Date.class, false, null)));
    }

    @Test
    public void testDateTimeSerializerBaseGetSchema() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        JsonNode schema = serializer.getSchema(new ObjectMapper().getSerializerProvider(), Date.class);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testDateTimeSerializerBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new JsonStringFormatVisitor(JsonFormatTypes.DATE_TIME, null, false));
        serializer.acceptJsonFormatVisitor(visitor, TypeFactory.defaultInstance.constructType(Date.class));

        assertEquals("DATE_TIME", visitor.getFormatType());
    }
}