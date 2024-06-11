import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        ShortSerializer shortSerializer = new ShortSerializer();
        shortSerializer.serialize((short) 10, gen, provider);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        IntegerSerializer integerSerializer = new IntegerSerializer();
        integerSerializer.serialize(10, gen, provider);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Number value = 10;
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        intLikeSerializer.serialize(value, gen, provider);
    }

    @Test
    public void testLongSerializer() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        LongSerializer longSerializer = new LongSerializer();
        longSerializer.serialize(100L, gen, provider);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        FloatSerializer floatSerializer = new FloatSerializer();
        floatSerializer.serialize(10.5f, gen, provider);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        doubleSerializer.serialize(10.5, gen, provider);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        // Assert statements for the result of addAll method
    }

}