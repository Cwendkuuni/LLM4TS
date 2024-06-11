
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertNull(dateTimeSerializerBase.withFormat(Boolean.TRUE, DateFormat.getInstance()));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertNull(dateTimeSerializerBase.createContextual(null, null));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertFalse(dateTimeSerializerBase.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertNotNull(dateTimeSerializerBase.getSchema(null, null));
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertDoesNotThrow(() -> dateTimeSerializerBase.serialize(null, null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertDoesNotThrow(() -> dateTimeSerializerBase.acceptJsonFormatVisitor(null, null));
    }

    @Test
    public void testAsTimestamp() {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertTrue(dateTimeSerializerBase._asTimestamp(null));
    }

    @Test
    public void testAcceptJsonFormatVisitor2() throws JsonMappingException {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertDoesNotThrow(() -> dateTimeSerializerBase._acceptJsonFormatVisitor(null, null, false));
    }

    @Test
    public void testSerializeAsString() throws IOException {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, DateFormat.getInstance()) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertDoesNotThrow(() -> dateTimeSerializerBase._serializeAsString(new Date(), null, null));
    }
}

