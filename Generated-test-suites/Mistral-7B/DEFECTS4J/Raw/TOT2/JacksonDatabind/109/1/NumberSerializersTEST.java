import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NumberSerializersTest {

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializers(NumberSerializers.class);
        mapper.registerModule(module);

        JsonSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        Integer number = 123;
        byte[] serializedData = integerSerializer.serializeValues(number, provider, null);
        String jsonString = new String(serializedData);

        assertEquals("123", jsonString);
    }

    @Test
    public void testSerializeShort() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializers(NumberSerializers.class);
        mapper.registerModule(module);

        JsonSerializer<Short> shortSerializer = mapper.findValueSerializer(Short.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        Short number = 127;
        byte[] serializedData = shortSerializer.serializeValues(number, provider, null);
        String jsonString = new String(serializedData);

        assertEquals("127", jsonString);
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializers(NumberSerializers.class);
        mapper.registerModule(module);

        JsonSerializer<Long> longSerializer = mapper.findValueSerializer(Long.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        Long number = 9223372036854775807L;
        byte[] serializedData = longSerializer.serializeValues(number, provider, null);
        String jsonString = new String(serializedData);

        assertEquals("9223372036854775807", jsonString);
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializers(NumberSerializers.class);
        mapper.registerModule(module);

        JsonSerializer<Float> floatSerializer = mapper.findValueSerializer(Float.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        Float number = 3.14F;
        byte[] serializedData = floatSerializer.serializeValues(number, provider, null);
        String jsonString = new String(serializedData);

        assertEquals("3.14", jsonString);
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializers(NumberSerializers.class);
        mapper.registerModule(module);

        JsonSerializer<Double> doubleSerializer = mapper.findValueSerializer(Double.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        Double number = 1.79769313486231E308D;
        byte[] serializedData = doubleSerializer.serializeValues(number, provider, null);
        String jsonString = new String(serializedData);

        assertEquals("1.79769313486231E308", jsonString);
    }

    @Test(expected = RuntimeException.class)
    public void testNullNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializers(NumberSerializers.class);
        mapper.registerModule(module);

        JsonSerializer<Integer> integerSerializer = mapper.findValueSerializer(Integer.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        Integer number = null;
        integerSerializer.serializeValues(number, provider, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNonNumber() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializers(NumberSerializers.class);
        mapper.registerModule(module);

        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        SerializerProvider provider = mapper.getSerializerProvider();

        String nonNumber = "non_number";
        stringSerializer.serializeValues(nonNumber, provider, null);
    }
}