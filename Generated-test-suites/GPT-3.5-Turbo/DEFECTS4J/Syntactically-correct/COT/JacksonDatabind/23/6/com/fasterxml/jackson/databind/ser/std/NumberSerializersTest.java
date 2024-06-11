import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class NumberSerializersTest {

    // Test IntegerSerializer
    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Integer value = 10;
        serializer.serialize(value, gen, provider);
        assertEquals("10", gen.getText());
    }

    // Test ShortSerializer
    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Short value = 5;
        serializer.serialize(value, gen, provider);
        assertEquals("5", gen.getText());
    }

    // Test IntLikeSerializer
    @Test
    public void testSerializeIntLike() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Number value = 1000;
        serializer.serialize(value, gen, provider);
        assertEquals("1000", gen.getText());
    }

    // Test LongSerializer
    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Long value = 1000000L;
        serializer.serialize(value, gen, provider);
        assertEquals("1000000", gen.getText());
    }

    // Test FloatSerializer
    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Float value = 3.14f;
        serializer.serialize(value, gen, provider);
        assertEquals("3.14", gen.getText());
    }

    // Test DoubleSerializer
    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Double value = 3.14159;
        serializer.serialize(value, gen, provider);
        assertEquals("3.14159", gen.getText());
    }

    // Test addAll method
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(7, allDeserializers.size());
        assertEquals(NumberSerializers.IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(NumberSerializers.IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(NumberSerializers.LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(NumberSerializers.IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(NumberSerializers.ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(NumberSerializers.FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());
        assertEquals(NumberSerializers.DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(NumberSerializers.DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
    }

}