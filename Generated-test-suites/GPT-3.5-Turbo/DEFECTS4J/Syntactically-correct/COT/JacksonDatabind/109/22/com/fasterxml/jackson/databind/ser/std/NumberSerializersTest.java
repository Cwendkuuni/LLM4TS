

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
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
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonGenerator(new JsonFactory(), baos);

        serializer.serialize(Short.valueOf("10"), generator, null);

        assertEquals("10", baos.toString());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonGenerator(new JsonFactory(), baos);

        serializer.serialize(Integer.valueOf("10"), generator, null);

        assertEquals("10", baos.toString());

        serializer.serializeWithType(Integer.valueOf("10"), generator, null, null);

        assertEquals("10", baos.toString());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonGenerator(new JsonFactory(), baos);

        serializer.serialize(10, generator, null);

        assertEquals("10", baos.toString());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer(Long.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonGenerator(new JsonFactory(), baos);

        serializer.serialize(Long.valueOf("1234567890"), generator, null);

        assertEquals("1234567890", baos.toString());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonGenerator(new JsonFactory(), baos);

        serializer.serialize(Float.valueOf("3.14"), generator, null);

        assertEquals("3.14", baos.toString());
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator generator = new JsonGenerator(new JsonFactory(), baos);

        serializer.serialize(Double.valueOf("3.14159"), generator, null);

        assertEquals("3.14159", baos.toString());

        serializer.serializeWithType(Double.valueOf("3.14159"), generator, null, null);

        assertEquals("3.14159", baos.toString());
    }

}

