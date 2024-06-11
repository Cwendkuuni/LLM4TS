import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();

        serializer.serialize((short) 1, gen, provider);
        assertEquals(1, gen.getNumberValue());

        serializer.serialize((short) 1000, gen, provider);
        assertEquals(1000, gen.getNumberValue());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);

        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();

        serializer.serialize(1, gen, provider);
        assertEquals(1, gen.getNumberValue());

        serializer.serialize(1000, gen, provider);
        assertEquals(1000, gen.getNumberValue());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;

        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();

        serializer.serialize(1, gen, provider);
        assertEquals(1, gen.getNumberValue());

        serializer.serialize(1000, gen, provider);
        assertEquals(1000, gen.getNumberValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);

        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();

        serializer.serialize(1L, gen, provider);
        assertEquals(1L, gen.getNumberValue());

        serializer.serialize(1000L, gen, provider);
        assertEquals(1000L, gen.getNumberValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;

        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();

        serializer.serialize(1.0f, gen, provider);
        assertEquals(1.0f, gen.getNumberValue());

        serializer.serialize(1000.5f, gen, provider);
        assertEquals(1000.5f, gen.getNumberValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);

        MockJsonGenerator gen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();

        serializer.serialize(1.0, gen, provider);
        assertEquals(1.0, gen.getNumberValue());

        serializer.serialize(1000.5, gen, provider);
        assertEquals(1000.5, gen.getNumberValue());
    }

    // Helper classes

    private static class MockJsonGenerator extends JsonGenerator {
        private Number numberValue;

        @Override
        public void writeNumber(short v) throws IOException {
            numberValue = v;
        }

        @Override
        public void writeNumber(int v) throws IOException {
            numberValue = v;
        }

        @Override
        public void writeNumber(long v) throws IOException {
            numberValue = v;
        }

        @Override
        public void writeNumber(float v) throws IOException {
            numberValue = v;
        }

        @Override
        public void writeNumber(double v) throws IOException {
            numberValue = v;
        }

        public Number getNumberValue() {
            return numberValue;
        }

        // Rest of the methods are not needed for this test case
    }

    private static class MockSerializerProvider extends SerializerProvider {

        // Rest of the methods are not needed for this test case
    }
}