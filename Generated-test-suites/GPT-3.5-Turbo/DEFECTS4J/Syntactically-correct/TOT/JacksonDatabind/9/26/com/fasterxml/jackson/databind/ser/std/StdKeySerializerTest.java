import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jgen = jsonFactory.createGenerator(System.out);

        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(date, jgen, provider);

        // Ensure the serialized result is as expected
        assertEquals("\"" + date.toString() + "\"", jgen.getOutputContext().getCurrentValue().toString());
    }

    @Test
    public void testSerialize_String() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "test";

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jgen = jsonFactory.createGenerator(System.out);

        SerializerProvider provider = new SerializerProvider();
        serializer.serialize(str, jgen, provider);

        // Ensure the serialized result is as expected
        assertEquals("\"" + str + "\"", jgen.getOutputContext().getCurrentValue().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);

        // Ensure the returned schema is of type "string"
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);

        // Ensure the visitor expects a string format
        assertTrue(visitor.getExpectedType().isStringType());
    }

    @Test(expected = IOException.class)
    public void testSerialize_IOError() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jgen = jsonFactory.createGenerator(System.out);

        // Close the generator before serialization
        jgen.close();

        SerializerProvider provider = new SerializerProvider();
        serializer.serialize("test", jgen, provider);
    }
}