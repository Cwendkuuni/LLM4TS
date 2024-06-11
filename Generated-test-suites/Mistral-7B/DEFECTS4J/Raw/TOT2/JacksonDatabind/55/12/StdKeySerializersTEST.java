import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class StdKeySerializersTest {

    private JsonSerializer<Object> defaultKeySerializer;
    private JsonSerializer<Object> stringKeySerializer;
    private JsonSerializer<Object> dynamicKeySerializer;
    private JsonSerializer<Object> defaultSerializer;
    private JsonSerializer<Object> dateSerializer;
    private JsonSerializer<Object> calendarSerializer;
    private JsonSerializer<Object> enumSerializer;
    private JsonSerializer<Object> toStringSerializer;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        defaultKeySerializer = mapper.getDefaultSerializerProvider().getSerializerProvider().getDefaultSerializer();
        stringKeySerializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
        dynamicKeySerializer = StdKeySerializers.getDefault();
        defaultSerializer = StdKeySerializers.DEFAULT_KEY_SERIALIZER;
        dateSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
        calendarSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Calendar.class, false);
        enumSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Enum.class, false);
        toStringSerializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);
    }

    @Test
    public void testGetStdKeySerializer() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);

        Object value = new Object();
        Class<?> rawKeyType = Object.class;
        JsonSerializer<Object> actual = StdKeySerializers.getStdKeySerializer(null, rawKeyType, false);

        assertSame(dynamicKeySerializer, actual);

        StdKeySerializers.getStdKeySerializer(null, String.class, false).serialize(value, gen, null);
        verify(gen).writeFieldName((String) value);

        StdKeySerializers.getStdKeySerializer(null, Date.class, false).serialize(new Date(), gen, null);
        verify(gen, never()).writeFieldName(anyString());
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("@type");
        verify(gen).writeString("Date");
        verify(gen).writeFieldName("value");
        verify(gen).writeValue(any(Date.class));
        verify(gen).writeEndObject();

        StdKeySerializers.getStdKeySerializer(null, Calendar.class, false).serialize(Calendar.getInstance(), gen, null);
        verify(gen, never()).writeFieldName(anyString());
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("@type");
        verify(gen).writeString("Calendar");
        verify(gen).writeFieldName("value");
        verify(gen).writeValue(any(Calendar.class));
        verify(gen).writeEndObject();
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);

        Object value = new Object();
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> actual = StdKeySerializers.getFallbackKeySerializer(null, rawKeyType);

        assertSame(toStringSerializer, actual);

        StdKeySerializers.getFallbackKeySerializer(null, rawKeyType).serialize(value, gen, null);
        verify(gen).writeFieldName((String) value);
    }

    // Add test cases for other methods
}