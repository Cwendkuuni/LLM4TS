import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    // IntegerSerializer

    @Test
    public void testIntegerSerializer_Serialize_IntegerValue_ReturnsSerializedInteger() {
        IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();

        Integer value = 10;

        try {
            serializer.serialize(value, generator, provider);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals("{\"value\":10}", generator.getOutput());
    }

    // IntLikeSerializer

    @Test
    public void testIntLikeSerializer_Serialize_IntegerValue_ReturnsSerializedIntegerValue() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();

        Integer value = 10;

        try {
            serializer.serialize(value, generator, provider);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals("10", generator.getOutput());
    }

    // LongSerializer

    @Test
    public void testLongSerializer_Serialize_LongValue_ReturnsSerializedLongValue() {
        LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();

        Long value = 10000000000L;

        try {
            serializer.serialize(value, generator, provider);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals("{\"value\":10000000000}", generator.getOutput());
    }

    // FloatSerializer

    @Test
    public void testFloatSerializer_Serialize_FloatValue_ReturnsSerializedFloatValue() {
        FloatSerializer serializer = FloatSerializer.instance;
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();

        Float value = 3.14f;

        try {
            serializer.serialize(value, generator, provider);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals("3.14", generator.getOutput());
    }

    // DoubleSerializer

    @Test
    public void testDoubleSerializer_Serialize_DoubleValue_ReturnsSerializedDoubleValue() {
        DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();

        Double value = 3.14159265359;

        try {
            serializer.serialize(value, generator, provider);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        assertEquals("{\"value\":3.14159265359}", generator.getOutput());
    }

    // Helper classes

    private static class JsonGeneratorStub extends JsonGenerator {

        private StringBuilder output = new StringBuilder();

        @Override
        public void writeNumber(int v) throws IOException {
            output.append(v);
        }

        @Override
        public void writeNumber(long v) throws IOException {
            output.append(v);
        }

        @Override
        public void writeNumber(double d) throws IOException {
            output.append(d);
        }

        @Override
        public void writeString(String text) throws IOException {
            output.append(text);
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }

        @Override
        public void flush() throws IOException {
            // Do nothing
        }
        
        public String getOutput() {
            return output.toString();
        }
    }

    private static class SerializerProviderStub extends SerializerProvider {

        // Stubbed methods

    }
}