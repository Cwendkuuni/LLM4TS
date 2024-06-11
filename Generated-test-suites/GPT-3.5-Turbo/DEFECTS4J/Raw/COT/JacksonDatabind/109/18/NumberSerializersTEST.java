import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assert(allDeserializers.containsKey(Integer.class.getName()));
        assert(allDeserializers.containsKey(Integer.TYPE.getName()));
        assert(allDeserializers.containsKey(Long.class.getName()));
        assert(allDeserializers.containsKey(Long.TYPE.getName()));
        assert(allDeserializers.containsKey(Byte.class.getName()));
        assert(allDeserializers.containsKey(Byte.TYPE.getName()));
        assert(allDeserializers.containsKey(Short.class.getName()));
        assert(allDeserializers.containsKey(Short.TYPE.getName()));
        assert(allDeserializers.containsKey(Double.class.getName()));
        assert(allDeserializers.containsKey(Double.TYPE.getName()));
        assert(allDeserializers.containsKey(Float.class.getName()));
        assert(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize((short) 100, generator, provider);
        verify(generator).writeNumber(100);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(100, generator, provider);
        verify(generator).writeNumber(100);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(100, generator, provider);
        verify(generator).writeNumber(100);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(100L, generator, provider);
        verify(generator).writeNumber(100L);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(1.23f, generator, provider);
        verify(generator).writeNumber(1.23f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(1.23, generator, provider);
        verify(generator).writeNumber(1.23);
    }

}