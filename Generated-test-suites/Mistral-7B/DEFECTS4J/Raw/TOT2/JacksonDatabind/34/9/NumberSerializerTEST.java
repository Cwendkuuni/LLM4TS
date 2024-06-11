import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    @Test
    public void testSerializationOfBigDecimalAndBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);

        BigDecimal bigDecimal = new BigDecimal("123.45");
        BigInteger bigInteger = new BigInteger("9876543210");

        serializer.serialize(bigDecimal, generator, new StandardSerializer.StandardSerializerProvider());
        assertEquals("123.45", generator.getOutputContentAsString());

        generator.reset();
        serializer.serialize(bigInteger, generator, new StandardSerializer.StandardSerializerProvider());
        assertEquals("9876543210", generator.getOutputContentAsString());

        generator.close();
    }
}