import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        HashMap<String, JsonSerializer<?>> allDeserializers = new HashMap<String, JsonSerializer<?>>();

        NumberSerializers.addAll(allDeserializers);

        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.class.getName()).getClass());
        assertEquals(IntegerSerializer.class, allDeserializers.get(Integer.TYPE.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.class.getName()).getClass());
        assertEquals(LongSerializer.class, allDeserializers.get(Long.TYPE.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.class.getName()).getClass());
        assertEquals(IntLikeSerializer.class, allDeserializers.get(Byte.TYPE.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.class.getName()).getClass());
        assertEquals(ShortSerializer.class, allDeserializers.get(Short.TYPE.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.class.getName()).getClass());
        assertEquals(DoubleSerializer.class, allDeserializers.get(Double.TYPE.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.class.getName()).getClass());
        assertEquals(FloatSerializer.class, allDeserializers.get(Float.TYPE.getName()).getClass());
    }

    @Test
    public void testShortSerialize() {
        ShortSerializer shortSerializer = new ShortSerializer();

        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        SerializerProviderStub providerStub = new SerializerProviderStub();

        try {
            shortSerializer.serialize((short) 10, generatorStub, providerStub);
        } catch (IOException e) {
            fail("Exception thrown");
        }

        assertEquals(10, generatorStub.getLastNumberValue());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);

        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        SerializerProviderStub providerStub = new SerializerProviderStub();

        try {
            integerSerializer.serialize((int) 100, generatorStub, providerStub);
        } catch (IOException e) {
            fail("Exception thrown");
        }

        assertEquals(100, generatorStub.getLastNumberValue());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;

        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        SerializerProviderStub providerStub = new SerializerProviderStub();

        try {
            intLikeSerializer.serialize((byte) 5, generatorStub, providerStub);
        } catch (IOException e) {
            fail("Exception thrown");
        }

        assertEquals(5, generatorStub.getLastNumberValue());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);

        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        SerializerProviderStub providerStub = new SerializerProviderStub();

        try {
            longSerializer.serialize((long) 1000, generatorStub, providerStub);
        } catch (IOException e) {
            fail("Exception thrown");
        }

        assertEquals(1000, generatorStub.getLastNumberValue());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = FloatSerializer.instance;

        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        SerializerProviderStub providerStub = new SerializerProviderStub();

        try {
            floatSerializer.serialize(3.14f, generatorStub, providerStub);
        } catch (IOException e) {
            fail("Exception thrown");
        }

        assertEquals(3.14f, generatorStub.getLastNumberValue(), 0.01);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);

        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        SerializerProviderStub providerStub = new SerializerProviderStub();

        try {
            doubleSerializer.serialize(3.1415, generatorStub, providerStub);
        } catch (IOException e) {
            fail("Exception thrown");
        }

        assertEquals(3.1415, generatorStub.getLastNumberValue(), 0.0001);
    }

    private class JsonGeneratorStub extends JsonGenerator {

        private float lastNumberValue;

        public float getLastNumberValue() {
            return lastNumberValue;
        }

        @Override
        public void writeNumber(int v) throws IOException {
            lastNumberValue = v;
        }

        @Override
        public void writeNumber(long v) throws IOException {
            lastNumberValue = v;
        }

        @Override
        public void writeNumber(double d) throws IOException {
            lastNumberValue = (float) d;
        }

        // other necessary overrides
    }

    private class SerializerProviderStub extends SerializerProvider {

        // overrides for necessary methods
    }

}