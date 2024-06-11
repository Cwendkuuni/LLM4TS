import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializerSerialize() {
        IntegerSerializer serializer = new IntegerSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(10, gen, provider);
            gen.flush();
            String result = gen.getOutputTarget().toString();
            assertEquals("10", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testShortSerializerSerialize() {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize((short) 5, gen, provider);
            gen.flush();
            String result = gen.getOutputTarget().toString();
            assertEquals("5", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIntLikeSerializerSerialize() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(15, gen, provider);
            gen.flush();
            String result = gen.getOutputTarget().toString();
            assertEquals("15", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLongSerializerSerialize() {
        LongSerializer serializer = new LongSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(20l, gen, provider);
            gen.flush();
            String result = gen.getOutputTarget().toString();
            assertEquals("20", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFloatSerializerSerialize() {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(10.5f, gen, provider);
            gen.flush();
            String result = gen.getOutputTarget().toString();
            assertEquals("10.5", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDoubleSerializerSerialize() {
        DoubleSerializer serializer = new DoubleSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider();
        try {
            serializer.serialize(15.75, gen, provider);
            gen.flush();
            String result = gen.getOutputTarget().toString();
            assertEquals("15.75", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}