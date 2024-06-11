import static org.junit.Assert.*;
import org.junit.Test;
public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        // Test when the input map is empty
        Map<String, JsonSerializer<?>> allDeserializers1 = new HashMap<>();
        NumberSerializers.addAll(allDeserializers1);
        assertFalse(allDeserializers1.isEmpty());

        // Test when the input map is already populated
        Map<String, JsonSerializer<?>> allDeserializers2 = new HashMap<>();
        allDeserializers2.put("String", new StringSerializer());
        NumberSerializers.addAll(allDeserializers2);
        assertTrue(allDeserializers2.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers2.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers2.containsKey(Long.class.getName()));
        assertTrue(allDeserializers2.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers2.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers2.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers2.containsKey(Short.class.getName()));
        assertTrue(allDeserializers2.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers2.containsKey(Float.class.getName()));
        assertTrue(allDeserializers2.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers2.containsKey(Double.class.getName()));
        assertTrue(allDeserializers2.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers2.containsKey("String"));
    }

    @Test
    public void testShortSerializer() {
        // Test a typical use case
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gen.setOutputStream(out);
        serializer.serialize(value, gen, provider);
        assertEquals("10", out.toString());

        // Test a negative value
        value = -10;
        out.reset();
        serializer.serialize(value, gen, provider);
        assertEquals("-10", out.toString());
    }

    @Test
    public void testIntegerSerializer() {
        // Test a typical use case
        IntegerSerializer serializer = new IntegerSerializer();
        Integer value = 100;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gen.setOutputStream(out);
        serializer.serialize(value, gen, provider);
        assertEquals("100", out.toString());

        // Test a negative value
        value = -100;
        out.reset();
        serializer.serialize(value, gen, provider);
        assertEquals("-100", out.toString());
    }

    @Test
    public void testIntLikeSerializer() {
        // Test a typical use case
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 200;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gen.setOutputStream(out);
        serializer.serialize(value, gen, provider);
        assertEquals("200", out.toString());

        // Test a negative value
        value = -200;
        out.reset();
        serializer.serialize(value, gen, provider);
        assertEquals("-200", out.toString());
    }

    @Test
    public void testLongSerializer() {
        // Test a typical use case
        LongSerializer serializer = new LongSerializer();
        Long value = 1000L;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gen.setOutputStream(out);
        serializer.serialize(value, gen, provider);
        assertEquals("1000", out.toString());

        // Test a negative value
        value = -1000L;
        out.reset();
        serializer.serialize(value, gen, provider);
        assertEquals("-1000", out.toString());
    }

    @Test
    public void testFloatSerializer() {
        // Test a typical use case
        FloatSerializer serializer = new FloatSerializer();
        Float value = 3.14f;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gen.setOutputStream(out);
        serializer.serialize(value, gen, provider);
        assertEquals("3.14", out.toString());

        // Test a negative value
        value = -3.14f;
        out.reset();
        serializer.serialize(value, gen, provider);
        assertEquals("-3.14", out.toString());
    }

    @Test
    public void testDoubleSerializer() {
        // Test a typical use case
        DoubleSerializer serializer = new DoubleSerializer();
        Double value = 3.14159;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        gen.setOutputStream(out);
        serializer.serialize(value, gen, provider);
        assertEquals("3.14159", out.toString());

        // Test a negative value
        value = -3.14159;
        out.reset();
        serializer.serialize(value, gen, provider);
        assertEquals("-3.14159", out.toString());
    }

}