
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();

        NumberSerializers.addAll(allDeserializers);

        // Verify that all expected serializers are added to the map
        assert allDeserializers.containsKey(Integer.class.getName());
        assert allDeserializers.containsKey(Integer.TYPE.getName());
        assert allDeserializers.containsKey(Long.class.getName());
        assert allDeserializers.containsKey(Long.TYPE.getName());
        assert allDeserializers.containsKey(Byte.class.getName());
        assert allDeserializers.containsKey(Byte.TYPE.getName());
        assert allDeserializers.containsKey(Short.class.getName());
        assert allDeserializers.containsKey(Short.TYPE.getName());
        assert allDeserializers.containsKey(Double.class.getName());
        assert allDeserializers.containsKey(Double.TYPE.getName());
        assert allDeserializers.containsKey(Float.class.getName());
        assert allDeserializers.containsKey(Float.TYPE.getName());
    }

    @Test
    public void testShortSerializer_serialize() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize((short) 10, gen, provider);

        // Verify that writeNumber method is called with correct value
        verify(gen).writeNumber(10);
    }

    @Test
    public void testIntegerSerializer_serialize() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(100, gen, provider);

        // Verify that writeNumber method is called with correct value
        verify(gen).writeNumber(100);
    }

    @Test
    public void testIntLikeSerializer_serialize() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(123456789, gen, provider);

        // Verify that writeNumber method is called with correct value
        verify(gen).writeNumber(123456789);
    }

    @Test
    public void testLongSerializer_serialize() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(10000000000L, gen, provider);

        // Verify that writeNumber method is called with correct value
        verify(gen).writeNumber(10000000000L);
    }

    @Test
    public void testFloatSerializer_serialize() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(3.14f, gen, provider);

        // Verify that writeNumber method is called with correct value
        verify(gen).writeNumber(3.14f);
    }

    @Test
    public void testDoubleSerializer_serialize() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(3.14159265359, gen, provider);

        // Verify that writeNumber method is called with correct value
        verify(gen).writeNumber(3.14159265359);
    }
}
