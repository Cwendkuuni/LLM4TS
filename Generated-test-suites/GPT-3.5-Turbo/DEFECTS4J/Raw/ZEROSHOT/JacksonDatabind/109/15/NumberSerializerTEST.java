import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        BigDecimal value = new BigDecimal("123.45");
        try {
            serializer.serialize(value, generator, provider);
            assertEquals("123.45", generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        BigInteger value = new BigInteger("12345");
        try {
            serializer.serialize(value, generator, provider);
            assertEquals("12345", generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeLong() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Long value = 12345L;
        try {
            serializer.serialize(value, generator, provider);
            assertEquals(12345L, generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeDouble() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Double value = 123.45;
        try {
            serializer.serialize(value, generator, provider);
            assertEquals(123.45, generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeFloat() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Float value = 123.45f;
        try {
            serializer.serialize(value, generator, provider);
            assertEquals(123.45f, generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeInteger() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Integer value = 12345;
        try {
            serializer.serialize(value, generator, provider);
            assertEquals(12345, generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeByte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Byte value = (byte) 123;
        try {
            serializer.serialize(value, generator, provider);
            assertEquals((byte) 123, generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSerializeShort() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = createJsonGenerator();
        SerializerProvider provider = createSerializerProvider();
        Short value = (short) 12345;
        try {
            serializer.serialize(value, generator, provider);
            assertEquals((short) 12345, generator.getCurrentValue());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    private JsonGenerator createJsonGenerator() {
        // implementation of JsonGenerator creation
    }

    private SerializerProvider createSerializerProvider() {
        // implementation of SerializerProvider creation
    }
}