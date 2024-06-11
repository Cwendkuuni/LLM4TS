package com.fasterxml.jackson.databind.ser.std;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    private DateTimeSerializerBaseTestImpl serializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        serializer = new DateTimeSerializerBaseTestImpl();
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.withFormat(true, null);
        Assert.assertEquals(true, actual._useTimestamp);
        Assert.assertNull(actual._customFormat);

        actual = serializer.withFormat(false, StdDateFormat.instance);
        Assert.assertEquals(false, actual._useTimestamp);
        Assert.assertEquals(StdDateFormat.instance, actual._customFormat);
    }

    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.createContextual(serializerProvider, null);
        Assert.assertEquals(serializer, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateContextualWithNoOverride() throws JsonMappingException {
        BeanProperty beanProperty = new BeanProperty() {
        };

        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.createContextual(serializerProvider, beanProperty);
        Assert.assertEquals(serializer, actual);
    }

    @Test
    public void testCreateContextualWithNumericShape() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);

        BeanProperty beanProperty = new BeanProperty() {
        };

        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.createContextual(serializerProvider, beanProperty);
        Assert.assertEquals(true, actual._useTimestamp);
        Assert.assertNull(actual._customFormat);
    }

    @Test
    public void testCreateContextualWithPattern() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setPattern("yyyy-MM-dd");

        BeanProperty beanProperty = new BeanProperty() {
        };

        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.createContextual(serializerProvider, beanProperty);
        Assert.assertEquals(false, actual._useTimestamp);
        Assert.assertNotNull(actual._customFormat);
    }

    @Test
    public void testCreateContextualWithLocale() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setLocale(Locale.US);

        BeanProperty beanProperty = new BeanProperty() {
        };

        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.createContextual(serializerProvider, beanProperty);
        Assert.assertEquals(false, actual._useTimestamp);
        Assert.assertNotNull(actual._customFormat);
    }

    @Test
    public void testCreateContextualWithTimeZone() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setTimeZone(TimeZone.getDefault());

        BeanProperty beanProperty = new BeanProperty() {
        };

        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.createContextual(serializerProvider, beanProperty);
        Assert.assertEquals(false, actual._useTimestamp);
        Assert.assertNotNull(actual._customFormat);
    }

    @Test
    public void testCreateContextualWithAsStringShape() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);

        BeanProperty beanProperty = new BeanProperty() {
        };

        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = serializer.createContextual(serializerProvider, beanProperty);
        Assert.assertEquals(false, actual._useTimestamp);
        Assert.assertNull(actual._customFormat);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = new DateTimeSerializerBaseTestImpl();

        Assert.assertFalse(actual.isEmpty(null, new DateTimeSerializerBaseTestImpl()));
    }

    @Test
    public void testGetSchemaWithTimestamp() {
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = new DateTimeSerializerBaseTestImpl();
        actual._useTimestamp = true;

        JsonNode expected = new ObjectMapper().createObjectNode().put("type", "number");
        JsonNode actualNode = actual.getSchema(serializerProvider, null);
        Assert.assertEquals(expected, actualNode);
    }

    @Test
    public void testGetSchemaWithString() {
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = new DateTimeSerializerBaseTestImpl();
        actual._useTimestamp = false;

        JsonNode expected = new ObjectMapper().createObjectNode().put("type", "string");
        JsonNode actualNode = actual.getSchema(serializerProvider, null);
        Assert.assertEquals(expected, actualNode);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAcceptJsonFormatVisitorWithNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitorWrapperImpl(serializerProvider);
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = new DateTimeSerializerBaseTestImpl();
        actual._useTimestamp = true;

        actual.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAcceptJsonFormatVisitorWithString() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitorWrapperImpl(serializerProvider);
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = new DateTimeSerializerBaseTestImpl();
        actual._useTimestamp = false;

        actual.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }

    @Test
    public void testSerializeWithCustomFormat() throws Exception {
        AtomicReference<DateFormat> reusedCustomFormat = new AtomicReference<>();
        reusedCustomFormat.set(new SimpleDateFormat());

        Date date = new SimpleDateFormat().parse("2020-01-01");
        DateTimeSerializerBase<DateTimeSerializerBaseTestImpl> actual = new DateTimeSerializerBaseTestImpl();
        actual._customFormat = new SimpleDateFormat();
        actual._reusedCustomFormat = reusedCustomFormat;

        actual.serialize(date, jsonGenerator, serializerProvider);
        Assert.assertNotNull(reusedCustomFormat.get());
    }

    public class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase<Date> {

        public DateTimeSerializerBaseTestImpl() {
            super(Date.class, false, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // do nothing
        }

    }

    private class JsonFormatVisitorWrapperImpl implements JsonFormatVisitorWrapper {

        private final SerializerProvider provider;

        public JsonFormatVisitorWrapperImpl(SerializerProvider provider) {
            this.provider = provider;
        }

        @Override
        public SerializerProvider getProvider() {
            return provider;
        }

        @Override
        public void setProvider(SerializerProvider p) {
            // do nothing
        }

        @Override
        public JsonFormatVisitorWrapper setProvider(SerializerProvider provider, BeanProperty property) {
            return null;
        }

        @Override
        public Object expectAnyFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonIntegerFormatVisitor expectIntegerFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonNullFormatVisitor expectNullFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonAnyFormatVisitor expectAnyFormat(JavaType type, boolean b) throws JsonMappingException {
            return null;
        }

    }
}