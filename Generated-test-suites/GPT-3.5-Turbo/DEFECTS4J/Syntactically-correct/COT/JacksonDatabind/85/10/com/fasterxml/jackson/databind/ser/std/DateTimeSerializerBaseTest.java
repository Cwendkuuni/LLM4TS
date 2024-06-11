
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<TestClass> serializer;
    private JsonSerializer.ContextualSerializer contextualSerializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<TestDate>(TestClass.class, false, null) {
            @Override
            public DateTimeSerializerBase<TestDate> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestDate value) {
                return 0;
            }

            @Override
            public void serialize(TestDate value, TestGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        contextualSerializer = serializer.createContextual(null, null);
    }

    @Test
    public void testIsEmptyWithNullValue() {
        TestDate value = null;

        boolean result = serializer.isEmpty(null, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEmptyWithValueWithZeroTimestamp() {
        TestDate value = new TestDate(0);

        boolean result = serializer.isEmpty(null, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetSchemaWithTimestamp() {
        MockSerializerProvider provider = new MockSerializerProvider(true);

        JsonNode result = serializer.getSchema(provider, null);

        Assert.assertEquals(result.get("type").asText(), "number");
    }

    @Test
    public void testGetSchemaWithoutTimestamp() {
        MockSerializerProvider provider = new MockSerializerProvider(false);

        JsonNode result = serializer.getSchema(provider, null);

        Assert.assertEquals(result.get("type").asText(), "string");
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTimestamp() throws JsonMappingException {
        MockJsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper(true);
        SimpleDateFormat expectedDateFormat = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601);

        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertTrue(visitor.visitedIntFormat);
        Assert.assertFalse(visitor.visitedStringFormat);
        Assert.assertEquals(visitor.numberType, JsonParser.NumberType.LONG);
        Assert.assertEquals(visitor.valueFormat, JsonValueFormat.UTC_MILLISEC);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithoutTimestamp() throws JsonMappingException {
        MockJsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper(false);

        serializer.acceptJsonFormatVisitor(visitor, null);

        Assert.assertFalse(visitor.visitedIntFormat);
        Assert.assertTrue(visitor.visitedStringFormat);
        Assert.assertEquals(visitor.valueFormat, JsonValueFormat.DATE_TIME);
    }

    private static class TestClass {

    }

    private static class TestDate {

        private long timestamp;

        public TestDate(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    private static class TestGenerator {

    }

    private static class MockSerializerProvider extends SerializerProvider {

        private boolean writeDatesAsTimestamps;

        public MockSerializerProvider(boolean writeDatesAsTimestamps) {
            super(null, null, null);
            this.writeDatesAsTimestamps = writeDatesAsTimestamps;
        }

        @Override
        public boolean isEnabled(SerializationFeature feature) {
            return writeDatesAsTimestamps;
        }
    }

    private static class MockJsonFormatVisitorWrapper implements JsonFormatVisitorWrapper {

        private boolean writeDatesAsTimestamps;
        private boolean visitedIntFormat;
        private boolean visitedStringFormat;
        private JsonParser.NumberType numberType;
        private JsonValueFormat valueFormat;

        public MockJsonFormatVisitorWrapper(boolean writeDatesAsTimestamps) {
            this.writeDatesAsTimestamps = writeDatesAsTimestamps;
        }

        @Override
        public SerializerProvider getProvider() {
            return null;
        }

        @Override
        public JsonFormatVisitorWrapper setProvider(SerializerProvider provider) {
            return null;
        }

        @Override
        public Object expectAnyFormat(JavaType type) throws JsonMappingException {
            return null;
        }

        @Override
        public void expectAnyPropertyFormat(JavaType type) throws JsonMappingException {

        }

        @Override
        public void expectNumberFormat(JavaType type) throws JsonMappingException {
            visitedIntFormat = true;
            numberType = JsonParser.NumberType.LONG;
            valueFormat = JsonValueFormat.UTC_MILLISEC;
        }

        @Override
        public void expectStringFormat(JavaType type) throws JsonMappingException {
            visitedStringFormat = true;
            valueFormat = JsonValueFormat.DATE_TIME;
        }

        @Override
        public void expectBooleanFormat(JavaType type) throws JsonMappingException {

        }

        @Override
        public void expectIntegerFormat(JavaType type) throws JsonMappingException {

        }

        // Rest of the methods are not relevant for this test
    }
}
