package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final Class<DateTimeSerializerBase<T>> CLASS = (Class<DateTimeSerializerBase<T>>) DateTimeSerializerBase.class;

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new StdDateSerializer(Object.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(serializer, null));
        assertTrue(serializer.isEmpty(new java.util.Date(0L)));
        assertTrue(serializer.isEmpty(serializer, new java.util.Date(0L)));
        assertFalse(serializer.isEmpty(new java.util.Date()));
        assertFalse(serializer.isEmpty(serializer, new java.util.Date()));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.NUMBER);
        JsonSerializer<Object> serializer1 = mapper.findSerializer(Object.class);
        assertEquals(serializer1.getClass(), DateTimeSerializerBase.class);
        JsonSerializer<Object> serializer2 = mapper.findSerializer(Object.class).withFormat(Boolean.TRUE, null);
        assertNotSame(serializer1, serializer2);

        JsonFormat jsonFormatString = new JsonFormat();
        jsonFormatString.shape(JsonFormat.Shape.STRING);
        JsonSerializer<Object> serializer3 = mapper.findSerializer(Object.class);
        assertEquals(serializer3.getClass(), DateTimeSerializerBase.class);
        JsonSerializer<Object> serializer4 = mapper.findSerializer(Object.class).withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd"));
        assertNotSame(serializer3, serializer4);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<java.util.Date> serializer = mapper.findSerializer(java.util.Date.class);

        JsonNode rootNode = mapper.createObjectNode();
        rootNode.put("useTimestamp", false);
        rootNode.put("customFormat", null);

        JsonNode result = mapper.writerWithRootValue(rootNode).writeValueAsTree(new java.util.Date());

        JsonParser parser = mapper.getFactory().createParser(result.traverse().get("value").traverse().get(0).asText());
        JsonToken token = parser.nextToken();
        assertEquals(JsonNodeType.NUMBER, token.getClass());
        long number = parser.getValueAsLong();
        assertEquals(System.currentTimeMillis(), number);

        rootNode = mapper.createObjectNode();
        rootNode.put("useTimestamp", true);
        rootNode.put("customFormat", null);

        result = mapper.writerWithRootValue(rootNode).writeValueAsTree(new java.util.Date());

        parser = mapper.getFactory().createParser(result.traverse().get("value").traverse().get(0).asText());
        token = parser.nextToken();
        assertEquals(JsonNodeType.NUMBER, token.getClass());
        number = parser.getValueAsLong();
        assertEquals(System.currentTimeMillis(), number);

        rootNode = mapper.createObjectNode();
        rootNode.put("useTimestamp", false);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        rootNode.put("customFormat", customFormat);

        result = mapper.writerWithRootValue(rootNode).writeValueAsTree(new java.util.Date());

        parser = mapper.getFactory().createParser(result.traverse().get("value").traverse().get(0).asText());
        token = parser.nextToken();
        assertEquals(JsonNodeType.STRING, token.getClass());
        String string = parser.getValueAsText();
        assertEquals(customFormat.format(new java.util.Date()), string);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().findIntrospector().findRootValueSerializer(Object.class);
        visitor.acceptJsonFormatVisitor(null, Object.class, false);
        assertTrue(visitor.getFormatVisitor().isNumeric());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getFormatVisitor().getValueFormat());

        visitor = mapper.getSerializationConfig().findIntrospector().findRootValueSerializer(Object.class);
        visitor.acceptJsonFormatVisitor(null, Object.class, true);
        assertTrue(visitor.getFormatVisitor().isNumeric());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getFormatVisitor().getValueFormat());

        visitor = mapper.getSerializationConfig().findIntrospector().findRootValueSerializer(Object.class);
        visitor.acceptJsonFormatVisitor(null, Object.class, false);
        assertFalse(visitor.getFormatVisitor().isNumeric());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormatVisitor().getValueFormat());
    }
}