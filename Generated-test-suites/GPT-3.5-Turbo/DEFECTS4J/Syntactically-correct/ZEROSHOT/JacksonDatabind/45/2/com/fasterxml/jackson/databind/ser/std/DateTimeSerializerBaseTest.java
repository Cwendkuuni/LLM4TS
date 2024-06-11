import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Locale;
import java.util.Date;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("serial")
    private static class TestDateTimeSerializer extends DateTimeSerializerBase<Date> {

        protected TestDateTimeSerializer(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializer(Date.class, timestamp, customFormat);
        }

        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        TestDateTimeSerializer serializer = new TestDateTimeSerializer(Date.class, false, null);
        assert serializer._timestamp(new Date(1632577568000L)) == 1632577568000L;
        assert serializer._timestamp(null) == 0L;
        
        JsonSerializer<?> context = serializer.createContextual(null, null);
                            assert context.getClass() == TestDateTimeSerializer.class;
        
        SerializerProvider provider = new SerializerProvider(null, null, null);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
        assert serializer._asTimestamp(provider) == false;
        
        TestJsonFormatVisitor visitor = new TestJsonFormatVisitor(null);
        serializer.acceptJsonFormatVisitor(visitor, null);
        assert visitor.getAsNumber() == false;
    }
}

class TestJsonFormatVisitor implements JsonFormatVisitorWrapper {

    private Boolean asNumber;

    TestJsonFormatVisitor(Boolean asNumber) {
        this.asNumber = asNumber;
    }

    Boolean getAsNumber() {
        return asNumber;
    }

    @Override
    public void setVisitor(JsonFormatVisitorWrapper.VisitorWrapper visitor) {

    }

    @Override
    public VisitorWrapper getVisitor() {
        return null;
    }

    @Override
    public SerializerProvider getProvider() {
        return null;
    }

    @Override
    public JsonPrimitiveFormatVisitor expectPrimitiveFormat(JavaType convertedType) {
        return null;
    }

    @Override
    public JsonArrayFormatVisitor expectArrayFormat(JavaType convertedType) {
        return null;
    }

    @Override
    public JsonObjectFormatVisitor expectObjectFormat(JavaType convertedType) {
        return null;
    }

    @Override
    public JsonStringFormatVisitor expectStringFormat(JavaType convertedType) {
        return null;
    }

    @Override
    public JsonNumberFormatVisitor expectNumberFormat(JavaType convertedType) {
        return null;
    }

    @Override
    public JsonIntegerFormatVisitor expectIntegerFormat(JavaType convertedType) {
        return null;
    }

    @Override
    public JsonBooleanFormatVisitor expectBooleanFormat(JavaType convertedType) {
        return null;
    }

    @Override
    public JsonNullFormatVisitor expectNullFormat(JavaType convertedType) {
        return null;
    }
}