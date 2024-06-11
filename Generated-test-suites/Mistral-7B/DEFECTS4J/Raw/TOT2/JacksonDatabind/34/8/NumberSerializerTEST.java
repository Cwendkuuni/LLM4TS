import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        numberSerializer.serialize(new BigDecimal("123.45"), jsonGenerator, new StandardSerializerFinder());

        String result = jsonGenerator.getOutputStream().toString();
        assertEquals("123.45", result);
    }
}