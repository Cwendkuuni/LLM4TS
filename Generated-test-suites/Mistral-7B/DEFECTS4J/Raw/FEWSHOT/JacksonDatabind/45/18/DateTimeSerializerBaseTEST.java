package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        Class<Long> longType = Long.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = null;

        DateTimeSerializerBase<Long> dateTimeSerializerBase1 = new DateTimeSerializerBase<Long>(longType, useTimestamp, customFormat);
        DateTimeSerializerBase<Long> dateTimeSerializerBase2 = dateTimeSerializerBase1.withFormat(useTimestamp, customFormat);

        assertEquals(dateTimeSerializerBase1, dateTimeSerializerBase2);

        useTimestamp = false;
        customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.UTC);

        DateTimeSerializerBase<Long> dateTimeSerializerBase3 = new DateTimeSerializerBase<Long>(longType, useTimestamp, customFormat);
        DateTimeSerializerBase<Long> dateTimeSerializerBase4 = dateTimeSerializerBase3.withFormat(useTimestamp, customFormat);

        assertNotEquals(dateTimeSerializerBase1, dateTimeSerializerBase3);
        assertEquals(dateTimeSerializerBase3, dateTimeSerializerBase4);
    }

    @Test
    public void testIsEmpty() throws JsonProcessingException {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);

        Long nullValue = null;
        Long zeroValue = 0L;

        assertFalse(serializer.isEmpty(nullValue, null));
        assertFalse(serializer.isEmpty(zeroValue, null));
        assertFalse(serializer.isEmpty(node, null));
        assertFalse(serializer.isEmpty(serializer.valueFromString("0"), null));

        assertTrue(serializer.isEmpty(nullValue, new SerializerProvider() {}));
        assertTrue(serializer.isEmpty(zeroValue, new SerializerProvider() {}));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);

        ObjectNode schemaNode = serializer.getSchema(mapper.getSerializerProvider(), Long.class);
        assertEquals("number", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);

        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig()
                .getConfig()
                .findIntrospector()
                .findValueSerializer(Long.class);

        serializer.acceptJsonFormatVisitor(visitor, Long.class);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);

        JsonGenerator generator = mapper.getFactory()
                .createGenerator(new ByteArrayOutputStream(), null);

        Long value = System.currentTimeMillis();
        Long value2 = System.currentTimeMillis() + 1000;

        serializer.serialize(value, generator, mapper.getSerializerProvider());
        serializer.serialize(value2, generator, mapper.getSerializerProvider());
        generator.flush();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        String json = new String(byteArrayOutputStream.toByteArray());
        assertTrue(json.contains(String.valueOf(value)));
        assertTrue(json.contains(String.valueOf(value2)));
    }

    @Test
    public void testSerializeWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.ENGLISH));

        JsonGenerator generator = mapper.getFactory()
                .createGenerator(new ByteArrayOutputStream(), null);

        Long value = System.currentTimeMillis();

        serializer.serialize(value, generator, mapper.getSerializerProvider());
        generator.flush();

        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutput();
        String json = new String(byteArrayOutputStream.toByteArray());
        assertTrue(json.contains("\"2023-03-20 14:30:45.123+00:00\""));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);
        jsonFormat.timeZone(TimeZone.UTC);
        jsonFormat.pattern("yyyy-MM-dd HH:mm:ss.SSS z");

        JsonFormatAnnotation jsonFormatAnnotation = new JsonFormatAnnotation("2023-03-20 HH:mm:ss.SSS z");
        jsonFormatAnnotation.setShape(JsonFormat.Shape.STRING);
        jsonFormatAnnotation.setTimeZone(TimeZone.UTC);

        AnnotatedMember annotatedMember = new AnnotatedMember(JsonTest.class, "testSerializeWithCustomFormat", Long.class);
        annotatedMember.setAnnotation(jsonFormatAnnotation);

        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        BeanProperty beanProperty = new BeanProperty("testSerializeWithCustomFormat", Long.class, null, null, null, null);

        JsonSerializer<Long> jsonSerializer = serializerProvider.findValueSerializer(Long.class, null);
        JsonSerializer<Long> customSerializer = jsonSerializer.withResolved(new SerializerFactory(null, "com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase", "serialize", Long.class));

        JsonSerializer<Long> contextualSerializer = customSerializer.createContextual(serializerProvider, beanProperty);

        assertTrue(contextualSerializer instanceof DateTimeSerializerBase);
        assertTrue(contextualSerializer._useTimestamp == false);
        assertTrue(contextualSerializer._customFormat != null);
    }
}