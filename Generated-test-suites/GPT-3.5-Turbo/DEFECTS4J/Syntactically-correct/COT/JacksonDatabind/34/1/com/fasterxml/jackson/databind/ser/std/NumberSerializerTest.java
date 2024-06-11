import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


public class NumberSerializerTest {
    private NumberSerializer numberSerializer = NumberSerializer.instance;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigDecimal("10.22"), jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigInteger("20"), jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(42, jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(42L, jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(3.14, jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(2.7182f, jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize((byte) 1, jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize((short) 10, jsonGenerator, objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializeOtherNumberType() throws IOException {
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        numberSerializer.serialize(12345L, jsonGenerator, objectMapper.getSerializerProvider());
    }
}