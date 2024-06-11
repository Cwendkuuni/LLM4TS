import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NumberSerializersTest {

    @Test
    public void testSerializeShort() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 10;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            String result = gen.toString();
            assertEquals("\"10\"", result);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testSerializeInteger() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.TYPE);
        Integer value = 100;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            String result = gen.toString();
            assertEquals("100", result);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testSerializeIntLike() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = 1000;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            String result = gen.toString();
            assertEquals("1000", result);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testSerializeLong() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 100000L;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            String result = gen.toString();
            assertEquals("100000", result);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 10.5f;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            String result = gen.toString();
            assertEquals("10.5", result);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 10.123;
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(value, gen, provider);
            gen.flush();
            String result = gen.toString();
            assertEquals("10.123", result);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

}