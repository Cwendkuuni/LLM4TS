
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.*;


public class NumberSerializersTest {

    private static final Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();

    @BeforeClass
    public static void setup() {
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Integer.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.IntegerSerializer);

        JsonParser.NumberType expectedType = JsonParser.NumberType.INT;
        String schemaType = "integer";

        NumberSerializers.IntegerSerializer integerSerializer = (NumberSerializers.IntegerSerializer) serializer;

        // Typical use case
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        integerSerializer.serialize(42, generator, null);
        generator.close();

        // Edge case
        generator = new JsonFactory().createGenerator(System.out);
        integerSerializer.serialize(Integer.MIN_VALUE, generator, null);
        generator.close();

        // Error scenario
        generator = new JsonFactory().createGenerator(System.out);
        try {
            integerSerializer.serialize(null, generator, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Exception is expected
        } finally {
            generator.close();
        }

        // Additional assertions
        Assert.assertEquals(expectedType, integerSerializer._numberType);
        Assert.assertEquals(schemaType, integerSerializer._schemaType);
    }

    @Test
    public void testShortSerializer() throws IOException {
        JsonSerializer<?> serializer = allDeserializers.get(Short.class.getName());
        Assert.assertNotNull(serializer);
        Assert.assertTrue(serializer instanceof NumberSerializers.ShortSerializer);

        JsonParser.NumberType expectedType = JsonParser.NumberType.INT;
        String schemaType = "number";

        NumberSerializers.ShortSerializer shortSerializer = (NumberSerializers.ShortSerializer) serializer;

        // Typical use case
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        shortSerializer.serialize((short) 42, generator, null);
        generator.close();

        // Edge case
        generator = new JsonFactory().createGenerator(System.out);
        shortSerializer.serialize(Short.MIN_VALUE, generator, null);
        generator.close();

        // Error scenario
        generator = new JsonFactory().createGenerator(System.out);
        try {
            shortSerializer.serialize(null, generator, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Exception is expected
        } finally {
            generator.close();
        }

        // Additional assertions
        Assert.assertEquals(expectedType, shortSerializer._numberType);
        Assert.assertEquals(schemaType, shortSerializer._schemaType);
    }

    // Additional test methods for LongSerializer, FloatSerializer, DoubleSerializer, and IntLikeSerializer

}

