import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerMock(Date.class, true, new SimpleDateFormat("yyyy-MM-dd", Locale.US));
    }

    @Test
    public void testSerialize_ValidTimestamp_SuccessfulSerialization() throws Exception {
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2020-01-01");
        serializer.serialize(date, generator, provider);
    }

    @Test
    public void testSerialize_ValidDateTimeFormat_SuccessfulSerialization() throws Exception {
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2020-01-01");
        serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd", Locale.US));
        serializer.serialize(date, generator, provider);
    }

    @Test
    public void testCreateContextual_WithNullProperty_ReturnsSelf() throws Exception {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        DateTimeSerializerBase<Date> result = serializer.createContextual(provider, null);
        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_WithNumericShape_ReturnsWithTimestampFormat() throws Exception {
        BeanProperty property = new BeanPropertyMock();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.NUMBER);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(format));
        DateTimeSerializerBase<Date> result = serializer.createContextual(provider, property);
        assertTrue(result._useTimestamp);
        assertEquals(null, result._customFormat);
    }

    @Test
    public void testCreateContextual_WithStringShape_ReturnsWithDateFormat() throws Exception {
        BeanProperty property = new BeanPropertyMock();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(format));
        DateTimeSerializerBase<Date> result = serializer.createContextual(provider, property);
        assertFalse(result._useTimestamp);
        assertNotNull(result._customFormat);
    }

    @Test
    public void testCreateContextual_WithTimeZone_ReturnsDateFormatWithTimeZone() throws Exception {
        BeanProperty property = new BeanPropertyMock();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        format = format.withTimezone("GMT");
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(format));
        DateTimeSerializerBase<Date> result = serializer.createContextual(provider, property);
        assertNotNull(result._customFormat);
        assertEquals("GMT", result._customFormat.getTimeZone().getID());
    }

    @Test
    public void testCreateContextual_WithPattern_ReturnsDateFormatWithPattern() throws Exception {
        BeanProperty property = new BeanPropertyMock();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        format = format.withPattern("yyyy/MM/dd");
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(format));
        DateTimeSerializerBase<Date> result = serializer.createContextual(provider, property);
        assertNotNull(result._customFormat);
        assertEquals("yyyy/MM/dd", ((SimpleDateFormat)result._customFormat).toPattern());
    }

    @Test
    public void testCreateContextual_WithLocale_ReturnsDateFormatWithLocale() throws Exception {
        BeanProperty property = new BeanPropertyMock();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        format = format.withLocale(Locale.UK);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(format));
        DateTimeSerializerBase<Date> result = serializer.createContextual(provider, property);
        assertNotNull(result._customFormat);
        assertEquals(Locale.UK, result._customFormat.getLocale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_WithNullProvider_ThrowsIllegalArgumentException() throws Exception {
        BeanProperty property = new BeanPropertyMock();
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = null;
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(format));
        serializer.createContextual(provider, property);
    }

    @Test
    public void testIsEmpty_WithNullValue_ReturnsTrue() throws Exception {
        Date value = null;
        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty_WithValueEqualToNullTimestamp_ReturnsTrue() throws Exception {
        Date value = new Date(0L);
        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty_WithValueNotEqualToNullTimestamp_ReturnsFalse() throws Exception {
        Date value = new Date();
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchema_WithTimestampFormat_ReturnsNumberSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(null));
        JsonNode schema = serializer.getSchema(provider, Date.class);
        assertEquals(JsonFormatTypes.INTEGER, schema.get("type").textValue());
    }

    @Test
    public void testGetSchema_WithStringFormat_ReturnsStringSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        provider.setAnnotationIntrospector(new AnnotationIntrospectorMock(null));
        JsonNode schema = serializer.getSchema(provider, Date.class);
        assertEquals(JsonFormatTypes.STRING, schema.get("type").textValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithTimestampFormat_VisitsNumberFormat() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapperMock();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        serializer.acceptJsonFormatVisitor(visitor, Date.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor_WithStringFormat_VisitsStringFormat() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapperMock();
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.setEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        serializer.acceptJsonFormatVisitor(visitor, Date.class);
    }

    private class DateTimeSerializerMock extends DateTimeSerializerBase<Date> {

        public DateTimeSerializerMock(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerMock<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }
    }

    private class BeanPropertyMock extends BeanProperty {

        @Override
        public Annotated getMember() {
            try {
                Field field = BeanProperty.class.getDeclaredField("_member");
                field.setAccessible(true);
                field.set(this, null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AnnotationIntrospectorMock extends ObjectMapper.DefaultAnnotationIntrospector {

        private JsonFormat.Value format;

        public AnnotationIntrospectorMock(JsonFormat.Value format) {
            this.format = format;
        }

        @Override
        public JsonFormat.Value findFormat(Annotated annotated) {
            return format;
        }
    }

    private class JsonFormatVisitorWrapperMock extends JsonFormatVisitorWrapper.Base {

        @Override
        public SerializerProvider getProvider() {
            return new ObjectMapper().getSerializerProvider();
        }

        @Override
        public void expectStringFormat(JsonValueFormat format) {
            assertTrue(format == JsonValueFormat.DATE_TIME);
        }

        @Override
        public void expectIntFormat(JsonParser.NumberType type) {
            assertTrue(type == JsonParser.NumberType.LONG);
        }
    }
}