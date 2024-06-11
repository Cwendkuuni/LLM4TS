import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NumberSerializerTest {

    private NumberSerializer serializer;

    @Before
    public void setUp() {
        serializer = new NumberSerializer(Number.class);
    }

    @After
    public void tearDown() {
        serializer = null;
    }

    @Test
    public void testSerializeBigDecimal() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        BigDecimal value = new BigDecimal("1234.5678");

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("1234.5678", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeBigInteger() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        BigInteger value = new BigInteger("1234567890");

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("1234567890", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeLong() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Long value = 1234567890L;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("1234567890", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeDouble() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Double value = 1234.5678;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("1234.5678", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeFloat() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Float value = 1234.5678f;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("1234.5678", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeInteger() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Integer value = 123;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("123", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeByte() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Byte value = 123;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("123", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeShort() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Short value = 1234;

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("1234", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testSerializeInvalidNumberType() {
        JsonGeneratorStub generator = new JsonGeneratorStub();
        SerializerProviderStub provider = new SerializerProviderStub();
        Number value = new Number() {
            @Override
            public int intValue() {
                return 0;
            }
            @Override
            public long longValue() {
                return 0;
            }
            @Override
            public float floatValue() {
                return 0;
            }
            @Override
            public double doubleValue() {
                return 0;
            }
        };

        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("0", generator.getLastValue());
        assertNull(generator.getLastException());
    }

    @Test
    public void testCreateSchemaNodeForBigInteger() {
        SerializerProviderStub provider = new SerializerProviderStub();
        Type typeHint = BigInteger.class;

        JsonNode node = serializer.getSchema(provider, typeHint);

        assertEquals("integer", node.get("type").textValue());
        assertTrue(node.get("required").booleanValue());
    }

    @Test
    public void testCreateSchemaNodeForBigDecimal() {
        SerializerProviderStub provider = new SerializerProviderStub();
        Type typeHint = BigDecimal.class;

        JsonNode node = serializer.getSchema(provider, typeHint);

        assertEquals("number", node.get("type").textValue());
        assertTrue(node.get("required").booleanValue());
    }
}