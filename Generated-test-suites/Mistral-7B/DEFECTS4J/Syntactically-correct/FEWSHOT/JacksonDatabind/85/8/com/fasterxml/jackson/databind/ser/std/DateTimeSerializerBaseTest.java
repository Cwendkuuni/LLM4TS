package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Long> LONG_SERIALIZER = new StdScalarSerializer<Long>(Long.class) {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.longValue());
        }
    };

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<String> STRING_SERIALIZER = new StdScalarSerializer<String>(String.class) {
        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value);
        }
    };

    @Test
    public void testEmpty() {
        assertTrue(LONG_SERIALIZER.isEmpty(null));
        assertTrue(LONG_SERIALIZER.isEmpty(null, new ObjectMapper()));
        assertTrue(STRING_SERIALIZER.isEmpty(null));
        assertTrue(STRING_SERIALIZER.isEmpty(null, new ObjectMapper()));
        assertFalse(LONG_SERIALIZER.isEmpty(new Long(0)));
        assertFalse(LONG_SERIALIZER.isEmpty(new Long(0), new ObjectMapper()));
        assertFalse(STRING_SERIALIZER.isEmpty(null));
        assertFalse(STRING_SERIALIZER.isEmpty(null, new ObjectMapper()));
    }

    @Test
    public void testTimestamp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        DateTimeSerializerBase<Long> longSerializerWithTimestamp = longSerializer.withFormat(Boolean.TRUE, null);

        Long value = 1L;
        JsonNode result = mapper.writeValueAsTree(value);
        JsonNode resultWithTimestamp = longSerializerWithTimestamp.serialize(value, null, new ObjectMapper());

        assertEquals(result.toString(), resultWithTimestamp.toString());

        JsonParser parser = mapper.getFactory().createParser(result.toString());
        Long parsedValue = mapper.readValue(parser, Long.class);
        Long parsedValueWithTimestamp = mapper.readValue(resultWithTimestamp, Long.class);

        assertEquals(value, parsedValue);
        assertEquals(value, parsedValueWithTimestamp);

        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        result = mapper.writeValueAsTree(value);
        resultWithTimestamp = longSerializerWithTimestamp.serialize(value, null, new ObjectMapper());

        assertNotEquals(result.toString(), resultWithTimestamp.toString());

        parser = mapper.getFactory().createParser(result.toString());
        parsedValue = mapper.readValue(parser, Long.class);
        parsedValueWithTimestamp = mapper.readValue(resultWithTimestamp, Long.class);

        assertEquals(value, parsedValue);
        assertNotEquals(value, parsedValueWithTimestamp);

        ValueInputFactory inputFactory = mapper.getFactory().valueInputFactory();
        JsonParser jsonParser = inputFactory.jsonParser();
        jsonParser.readValueAs(Long.class);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(mapper.getSerializerProvider(), null, null);
        longSerializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.getFormat().isNumeric());

        longSerializerWithTimestamp.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.getFormat().isNumeric());
    }

    @Test
    public void testCustomFormat() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String dateFormatPattern = "dd-MM-yyyy";
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern, locale);
        dateFormat.setTimeZone(timeZone);

        DateTimeSerializerBase<String> dateSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
        DateTimeSerializerBase<String> dateSerializerWithCustomFormat = dateSerializer.withFormat(Boolean.FALSE, dateFormat);

        Long value = 1L;
        String expectedValue = "01-06-2023";
        JsonNode result = mapper.writeValueAsTree(value);
        JsonNode resultWithCustomFormat = dateSerializerWithCustomFormat.serialize(value, null, new ObjectMapper());

        assertEquals(expectedValue, resultWithCustomFormat.toString());

        JsonParser parser = mapper.getFactory().createParser(result.toString());
        Long parsedValue = mapper.readValue(parser, Long.class);
        String parsedValueWithCustomFormat = mapper.readValue(resultWithCustomFormat, String.class);

        assertEquals(value, parsedValue);
        assertEquals(expectedValue, parsedValueWithCustomFormat);
    }
}