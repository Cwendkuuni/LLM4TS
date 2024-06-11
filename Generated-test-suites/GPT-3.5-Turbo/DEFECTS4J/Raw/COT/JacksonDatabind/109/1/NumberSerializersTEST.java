
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assert allDeserializers.size() == 8; // There should be 8 serializers added
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(123, gen, provider);
        gen.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(123, gen, provider);
        gen.flush();
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize((short) 123, gen, provider);
        gen.flush();
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(123L, gen, provider);
        gen.flush();
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(123.45f, gen, provider);
        gen.flush();
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        serializer.serialize(123.45, gen, provider);
        gen.flush();
    }
}
