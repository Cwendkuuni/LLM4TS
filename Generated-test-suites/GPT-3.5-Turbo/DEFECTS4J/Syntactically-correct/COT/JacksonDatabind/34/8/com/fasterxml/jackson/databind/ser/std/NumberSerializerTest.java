
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("123.456");
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("1234567890");
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Integer value = 123;
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        Long value = 123L;
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        Double value = 123.456;
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        Float value = 123.456f;
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        Byte value = (byte) 123;
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        Short value = (short) 123;
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

    @Test
    public void testSerializeDefault() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        Number value = 123.456;
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(value, g, provider);
        // Check the functionality here
    }

}
