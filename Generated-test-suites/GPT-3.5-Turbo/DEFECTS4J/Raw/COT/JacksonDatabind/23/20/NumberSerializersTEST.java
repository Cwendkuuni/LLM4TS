
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = null; // mocked JsonGenerator
        SerializerProvider provider = null; // mocked SerializerProvider
        Short value = 10;
        serializer.serialize(value, gen, provider);
        // Assertion
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        JsonGenerator gen = null; // mocked JsonGenerator
        SerializerProvider provider = null; // mocked SerializerProvider
        Integer value = 10;
        serializer.serialize(value, gen, provider);
        // Assertion
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator gen = null; // mocked JsonGenerator
        SerializerProvider provider = null; // mocked SerializerProvider
        Number value = 10;
        serializer.serialize(value, gen, provider);
        // Assertion
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer serializer = NumberSerializers.LongSerializer.instance;
        JsonGenerator gen = null; // mocked JsonGenerator
        SerializerProvider provider = null; // mocked SerializerProvider
        Long value = 10L;
        serializer.serialize(value, gen, provider);
        // Assertion
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator gen = null; // mocked JsonGenerator
        SerializerProvider provider = null; // mocked SerializerProvider
        Float value = 10.5f;
        serializer.serialize(value, gen, provider);
        // Assertion
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        JsonGenerator gen = null; // mocked JsonGenerator
        SerializerProvider provider = null; // mocked SerializerProvider
        Double value = 10.5;
        serializer.serialize(value, gen, provider);
        // Assertion
    }
}
