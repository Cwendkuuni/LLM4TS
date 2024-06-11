
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    private static final JsonSerializer<?> MOCK_SERIALIZER = mock(JsonSerializer.class);

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.TYPE);

        integerSerializer.serialize(123, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(123);
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.TYPE);

        integerSerializer.serializeWithType(123, jsonGenerator, serializerProvider, typeSerializer);

        verify(jsonGenerator, times(1)).writeNumber(123);
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();

        shortSerializer.serialize((short) 123, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(123);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        NumberSerializers.IntLikeSerializer intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        intLikeSerializer.serialize((byte) 123, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(123);
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.TYPE);

        longSerializer.serialize(123L, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(123L);
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        NumberSerializers.FloatSerializer floatSerializer = NumberSerializers.FloatSerializer.instance;

        floatSerializer.serialize(123.45f, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(123.45f);
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.TYPE);

        doubleSerializer.serialize(123.45d, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(123.45d);
    }
    
    @Test
    public void testGetSchema() throws IOException {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.TYPE);

        JsonNode schema = integerSerializer.getSchema(serializerProvider, Integer.TYPE);

        assertEquals("integer", schema.get("type").asText());
        assertEquals("number", schema.get("format").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorForIntegerSerializer() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.TYPE);

        integerSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor, times(1)).expectIntFormat(typeHint, JsonParser.NumberType.INT);
    }

}
