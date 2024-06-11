import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        // Typical use case
        Assert.assertEquals("1", serializeValue(serializer, 1));
        
        // Edge case
        Assert.assertEquals("0", serializeValue(serializer, 0));
        
        // Error scenario
        Assert.assertNull(serializeValue(serializer, null));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        // Typical use case
        Assert.assertEquals("1", serializeValue(serializer, (short) 1));
        
        // Edge case
        Assert.assertEquals("0", serializeValue(serializer, (short) 0));
        
        // Error scenario
        Assert.assertNull(serializeValue(serializer, null));
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        // Typical use case
        Assert.assertEquals("1", serializeValue(serializer, 1));
        
        // Edge case
        Assert.assertEquals("0", serializeValue(serializer, 0));
        
        // Error scenario
        Assert.assertNull(serializeValue(serializer, null));
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        // Typical use case
        Assert.assertEquals("1", serializeValue(serializer, 1L));
        
        // Edge case
        Assert.assertEquals("0", serializeValue(serializer, 0L));
        
        // Error scenario
        Assert.assertNull(serializeValue(serializer, null));
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();

        // Typical use case
        Assert.assertEquals("1.0", serializeValue(serializer, 1.0f));
        
        // Edge case
        Assert.assertEquals("0.0", serializeValue(serializer, 0.0f));
        
        // Error scenario
        Assert.assertNull(serializeValue(serializer, null));
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        // Typical use case
        Assert.assertEquals("1.0", serializeValue(serializer, 1.0));
        
        // Edge case
        Assert.assertEquals("0.0", serializeValue(serializer, 0.0));
        
        // Error scenario
        Assert.assertNull(serializeValue(serializer, null));
    }

    private String serializeValue(JsonSerializer<?> serializer, Object value) throws IOException {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        serializer.serialize(value, null, null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().setSerializers(allDeserializers));
        return mapper.writeValueAsString(value);
    }
}