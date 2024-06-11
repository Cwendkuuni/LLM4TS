import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DateTimeSerializerBaseTest {

    DateTimeSerializerBase<TestObject> serializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
    }

    @Test
    public void serialize_StringValueAsTimestamp_Success() throws IOException {
        // Arrange
        TestObject testObject = new TestObject("2021-01-01T12:00:00");

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        // Act
        serializer.serialize(testObject, gen, serializers);

        // Assert
        verify(gen, times(1)).writeString("2021-01-01T12:00:00");
    }

    @Test
    public void serialize_StringValueAsDate_Success() throws IOException {
        // Arrange
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        TestObject testObject = new TestObject("2021-01-01T12:00:00");

        DateTimeSerializerBase<TestObject> customSerializer = new DateTimeSerializerBase<TestObject>(
                TestObject.class, false, customFormat) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                _serializeAsString(customFormat.parse(value.getDateString()), gen, serializers);
            }
        };

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        // Act
        customSerializer.serialize(testObject, gen, serializers);

        // Assert
        verify(gen, times(1)).writeString("2021-01-01");
    }

    @Test
    public void acceptJsonFormatVisitor_WithNumericShape_VisitsIntFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.LONG),
                eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void acceptJsonFormatVisitor_WithStringShape_VisitsStringFormat() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).visitStringFormat(eq(typeHint), eq(JsonValueFormat.DATE_TIME));
    }

    @Test
    public void isEmpty_NotEmpty_ReturnsFalse() {
        // Arrange
        TestObject testObject = new TestObject("2021-01-01T12:00:00");

        SerializerProvider serializers = mock(SerializerProvider.class);

        // Act
        boolean result = serializer.isEmpty(serializers, testObject);

        // Assert
        assertEquals(false, result);
    }
}

public class TestObject {
    private String dateString;

    public TestObject(String dateString) {
        this.dateString = dateString;
    }

    public String getDateString() {
        return dateString;
    }
}