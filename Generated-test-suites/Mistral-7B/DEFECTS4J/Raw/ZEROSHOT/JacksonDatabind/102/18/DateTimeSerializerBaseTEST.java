package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer.StandardSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.ALWAYS);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Test default instance
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        // Test withFormat method
        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithCustomFormat = dateTimeSerializerBase.withFormat(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US));

        // Test isEmpty method
        Date nullDate = null;
        Date emptyDate = new Date(0L);
        assertTrue(dateTimeSerializerBase.isEmpty(mapper.getSerializationConfig(), nullDate));
        assertFalse(dateTimeSerializerBase.isEmpty(mapper.getSerializationConfig(), emptyDate));

        // Test createContextual method
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        JsonSerializer<Date> serializer = dateTimeSerializerBase.createContextual(mapper.getSerializerProvider(), property);
        assertEquals(dateTimeSerializerBase, serializer);

        property = new BeanProperty("testDate", Date.class);
        format.setShape(shape);
        format.setPattern("yyyy-MM-dd HH:mm:ss");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        serializer = dateTimeSerializerBaseWithCustomFormat.createContextual(mapper.getSerializerProvider(), property);
        assertNotSame(dateTimeSerializerBase, serializer);

        // Test serialize method with default instance
        Date testDate = new Date();
        String expectedResult = mapper.getDateFormat().format(testDate);
        String result = mapper.writeValueAsString(new Date[]{testDate});
        assertEquals(expectedResult, result.split(",")[0].replaceAll("\"", ""));

        // Test serialize method with custom format instance
        String expectedResultCustomFormat = "2023-01-01 12:01:01";
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> dateTimeSerializerBaseWithCustomFormatWithReusedInstance = dateTimeSerializerBaseWithCustomFormat.withFormat(false, customFormat);
        String resultCustomFormat = mapper.writeValueAsString(new Date[]{testDate});
        assertEquals(expectedResultCustomFormat, resultCustomFormat.split(",")[0].replaceAll("\"", ""));

        // Test _asTimestamp method
        assertTrue(dateTimeSerializerBase._asTimestamp(mapper.getSerializationConfig()));
        assertFalse(dateTimeSerializerBaseWithCustomFormat._asTimestamp(mapper.getSerializationConfig()));

        // Test _acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = mapper.schemaFactory().getJsonFormatVisitor(null, null, null);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Date.class));
        assertEquals("number", visitor.getCurrentName());
        assertEquals(JsonParser.NumberType.LONG, visitor.getCurrentType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getCurrentValueFormat());
        dateTimeSerializerBaseWithCustomFormat.acceptJsonFormatVisitor(visitor, mapper.getTypeFactory().constructType(Date.class));
        assertEquals("string", visitor.getCurrentName());
        assertEquals(JsonParser.NumberType.STRING, visitor.getCurrentType());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getCurrentValueFormat());
    }

    @Test
    public void testDateTimeSerializerBaseWithJsonFormat() throws JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.ALWAYS);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Test default instance with JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = Locale.US, timezone = "UTC")
        static class TestDate {
            private Date date;

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }
        }

        TestDate testDate = new TestDate();
        testDate.setDate(new Date());

        JsonSerializer<TestDate> serializer = mapper.findValueSerializer(TestDate.class, mapper.getSerializationConfig());
        assertEquals(DateTimeSerializerBase.class, serializer.getClass());
        assertEquals(DateTimeSerializerBase.class, serializer.getSerializers().nextValueSerializer().getClass());

        // Test withFormat method with JsonFormat annotation
        @JsonSerialize(using = DateTimeSerializerBase.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = Locale.US, timezone = "UTC")
        static class TestDateWithCustomFormat {
            private Date date;

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }
        }

        TestDateWithCustomFormat testDateWithCustomFormat = new TestDateWithCustomFormat();
        testDateWithCustomFormat.setDate(new Date());

        JsonSerializer<TestDateWithCustomFormat> serializerWithCustomFormat = mapper.findValueSerializer(TestDateWithCustomFormat.class, mapper.getSerializationConfig());
        assertEquals(DateTimeSerializerBase.class, serializerWithCustomFormat.getClass());
        assertEquals(DateTimeSerializerBase.class, serializerWithCustomFormat.getSerializers().nextValueSerializer().getClass());
        assertTrue(serializerWithCustomFormat.getSerializers().nextValueSerializer() instanceof DateTimeSerializerBase<Date>);
        assertEquals(DateTimeSerializerBase.class, ((DateTimeSerializerBase<?>) serializerWithCustomFormat.getSerializers().nextValueSerializer()).getClass());
        assertNotSame(dateTimeSerializerBase, ((DateTimeSerializerBase<?>) serializerWithCustomFormat.getSerializers().nextValueSerializer()));
    }
}