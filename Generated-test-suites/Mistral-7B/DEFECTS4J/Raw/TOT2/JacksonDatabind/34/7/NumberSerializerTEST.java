import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());

        Number number = 1; // Replace this with any valid number
        numberSerializer.serialize(number, jsonGenerator, new StandardSerializerFinder());
        jsonGenerator.flush();

        String serializedNumber = jsonGenerator.getOutputFile().get(0).toString();
        assertEquals("1", serializedNumber);
    }
}