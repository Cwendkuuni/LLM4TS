
import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Test case for addAll method
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        // Test case for ShortSerializer serialize method
        ShortSerializer serializer = new ShortSerializer();
        Short value = 5;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, gen, null);
        gen.close();
        assertEquals(value.toString(), writer.toString());
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        // Test case for IntegerSerializer serialize method
        IntegerSerializer serializer = new IntegerSerializer();
        Integer value = 10;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, gen, null);
        gen.close();
        assertEquals(value.toString(), writer.toString());
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        // Test case for IntLikeSerializer serialize method
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 15;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, gen, null);
        gen.close();
        assertEquals(value.intValue(), Integer.valueOf(writer.toString()).intValue());
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        // Test case for LongSerializer serialize method
        LongSerializer serializer = new LongSerializer();
        Long value = 20L;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, gen, null);
        gen.close();
        assertEquals(value.toString(), writer.toString());
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        // Test case for FloatSerializer serialize method
        FloatSerializer serializer = new FloatSerializer();
        Float value = 25.0f;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, gen, null);
        gen.close();
        assertEquals(value.toString(), writer.toString());
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        // Test case for DoubleSerializer serialize method
        DoubleSerializer serializer = new DoubleSerializer();
        Double value = 30.0;
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, gen, null);
        gen.close();
        assertEquals(value.toString(), writer.toString());
    }
}
