package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private DateTimeSerializerBase<T> serializer;

    @SuppressWarnings("rawtypes")
    private final Class<? extends DateTimeSerializerBase> clazz;

    public DateTimeSerializerBaseTest(Class<? extends DateTimeSerializerBase<T>> clazz) {
        this.clazz = clazz;
        try {
            Object obj = clazz.newInstance();
            this.serializer = (DateTimeSerializerBase<T>) obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        JsonSerializer<Object> serializer = this.serializer.createContextual(mapper.getSerializerProvider(), null);

        Annotated annotated = mapper.getSerializationConfig().getIntrospector().findAnnotation(JsonFormat.class, Object.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getDefault());
        format.setPattern(dateFormat.toPattern());
        format.setLocale(Locale.getDefault());
        annotated.setValue(format);

        assertEquals(serializer.getClass(), this.serializer.withFormat(Boolean.FALSE, dateFormat).getClass());

        JsonParser parser = mapper.getFactory().createParser("2022-12-31 23:59:59 +0100");
        SerializerProvider serializers = mapper.getSerializerProvider();
        Object deserializedObject = serializer.deserialize(parser, Object.class, serializers);
        assertNotNull(deserializedObject);

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(deserializedObject, generator, serializers);
        generator.flush();
    }

    @Test
    public void testIsEmpty() {
        assertNotNull(serializer);
        assertFalse(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(serializer, null));
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getDefaultSerializationConfig().acceptJsonFormatVisitor(SerializationFeature.FAIL_ON_EMPTY_BEANS, Object.class);
        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
        assertEquals("string", visitor.getFormatName());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitorWithNullProvider() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
        serializer.acceptJsonFormatVisitor(visitor, Object.class.getType());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(null, generator, mapper.getSerializerProvider());
        generator.flush();
    }
}