import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {
    @Test
    public void testSerialize() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String testString = "testString";

        StringWriter stringWriter = new StringWriter(new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream())));
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
        SerializerProvider serializerProvider = new StandardSerializerProvider();

        serializer.serialize(testString, jsonGenerator, serializerProvider);
        jsonGenerator.close();

        String jsonString = stringWriter.toString();
        assertEquals("{\"testString\":null}", jsonString);
    }
}