
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testSerializeShort() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short number = 10;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
        } catch (IOException e) {
            fail("Serialization failed.");
        }
    }

    @Test
    public void testSerializeInteger() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer number = 10;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
        } catch (IOException e) {
            fail("Serialization failed.");
        }
    }

    @Test
    public void testSerializeIntLike() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number number = 10;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
        } catch (IOException e) {
            fail("Serialization failed.");
        }
    }

    @Test
    public void testSerializeLong() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        Long number = 10L;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
        } catch (IOException e) {
            fail("Serialization failed.");
        }
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float number = 10.0f;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
        } catch (IOException e) {
            fail("Serialization failed.");
        }
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double number = 10.0;
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        try {
            serializer.serialize(number, generator, null);
            generator.flush();
        } catch (IOException e) {
            fail("Serialization failed.");
        }
    }

}
