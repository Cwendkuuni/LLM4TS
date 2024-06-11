import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Integer value = 10;

        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(10);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Number value = 10;

        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(10);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Short value = 10;

        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(10);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Long value = 10L;

        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(10L);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Float value = 10.5f;

        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(10.5f);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Double value = 10.5;

        try {
            serializer.serialize(value, gen, provider);
            verify(gen).writeNumber(10.5);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

}