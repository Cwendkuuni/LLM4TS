
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = NumberSerializer.instance;
    }

    @Test
    public void testSerializeBigDecimal() {
        BigDecimal number = new BigDecimal("1234.5678");
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("1234.5678", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeBigInteger() {
        BigInteger number = new BigInteger("1234567890");
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("1234567890", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeLong() {
        Long number = 1234567890L;
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("1234567890", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeDouble() {
        Double number = 1234.5678;
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("1234.5678", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeFloat() {
        Float number = 1234.5678f;
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("1234.5678", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeInteger() {
        Integer number = 1234567890;
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("1234567890", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeByte() {
        Byte number = 123;
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("123", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeShort() {
        Short number = 12345;
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("12345", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeOtherNumberType() {
        Number number = 1234.5678;
        JsonGenerator generator = createMockGenerator();
        SerializerProvider provider = createMockProvider();
        try {
            numberSerializer.serialize(number, generator, provider);
            assertEquals("1234.5678", generator.getSerializedValue());
        } catch(IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    private JsonGenerator createMockGenerator() {
        // Create and return mock generator
    }

    private SerializerProvider createMockProvider() {
        // Create and return mock provider
    }
}
