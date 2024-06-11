import org.junit.*;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    // Test for IntegerSerializer
    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Integer value = 123;
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            assertEquals("123", generator.getOutputContext().getText());
        } catch (IOException e) {
            fail("IOException occurred during test");
        }
    }

    // Test for IntLikeSerializer
    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Number value = 123;
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            assertEquals("123", generator.getOutputContext().getText());
        } catch (IOException e) {
            fail("IOException occurred during test");
        }
    }

    // Test for ShortSerializer
    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Short value = 123;
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            assertEquals("123", generator.getOutputContext().getText());
        } catch (IOException e) {
            fail("IOException occurred during test");
        }
    }

    // Test for LongSerializer
    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Long value = 123L;
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            assertEquals("123", generator.getOutputContext().getText());
        } catch (IOException e) {
            fail("IOException occurred during test");
        }
    }

    // Test for FloatSerializer
    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Float value = 123.45f;
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            assertEquals("123.45", generator.getOutputContext().getText());
        } catch (IOException e) {
            fail("IOException occurred during test");
        }
    }

    // Test for DoubleSerializer
    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        Double value = 123.45;
        try {
            serializer.serialize(value, generator, provider);
            generator.flush();
            assertEquals("123.45", generator.getOutputContext().getText());
        } catch (IOException e) {
            fail("IOException occurred during test");
        }
    }

}