import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    private DateTimeSerializerBase<Date> dateTimeSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dateTimeSerializer = createDateTimeSerializer();
    }

    @Test
    public void testCreateContextualWithNumericShape() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value value = new JsonFormat.Value().withShape(JsonFormat.Shape.NUMBER);
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<Date> serializer = dateTimeSerializer.createContextual(serializers, property);

        assertEquals(Boolean.TRUE, serializer._useTimestamp);
        assertEquals(null, serializer._customFormat);
    }

    @Test
    public void testCreateContextualWithPattern() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value value = new JsonFormat.Value().withPattern("yyyy-MM-dd");
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<Date> serializer = dateTimeSerializer.createContextual(serializers, property);

        assertEquals(Boolean.FALSE, serializer._useTimestamp);
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat)serializer._customFormat)._pattern);
        assertEquals(Locale.getDefault(), ((SimpleDateFormat)serializer._customFormat).getLocale());
    }

    @Test
    public void testCreateContextualWithLocale() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        SimpleDateFormat customFormat = new SimpleDateFormat();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
            }
        };
        MockitoAnnotations.initMocks(this);
        JsonFormat.Value value = new JsonFormat.Value().withLocale(Locale.US);
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<Date> result = serializer.createContextual(serializers, property);

        assertEquals(Boolean.FALSE, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
        assertEquals(Locale.US, ((SimpleDateFormat)serializer._customFormat).getLocale());
    }

    private DateTimeSerializerBase<Date> createDateTimeSerializer() {
        return new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {

            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {

            }

            @Override
            public boolean isEmpty(SerializerProvider serializers, Date value) {
                return false;
            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
                
            }
        };
    }
}