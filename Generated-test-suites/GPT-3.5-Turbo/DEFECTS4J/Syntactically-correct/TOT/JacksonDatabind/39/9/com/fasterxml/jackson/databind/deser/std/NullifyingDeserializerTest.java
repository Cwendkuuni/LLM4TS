import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;

public class NullifyingDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonFactory factory = new JsonFactory();

    @Test
    public void testDeserialize_SkipChildren() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = factory.createParser("{\"field1\": \"value1\", \"field2\": \"value2\", \"field3\": \"value3\"}");
        assertNull(deserializer.deserialize(parser, null));
    }

    @Test
    public void testDeserializeWithType_ArrayStartToken() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = factory.createParser("[1, 2, 3]");
        assertNull(deserializer.deserializeWithType(parser, null, null));
    }

    @Test
    public void testDeserializeWithType_ObjectStartToken() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = factory.createParser("{\"field1\": \"value1\"}");
        assertNull(deserializer.deserializeWithType(parser, null, null));
    }

    @Test
    public void testDeserializeWithType_FieldNameToken() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = factory.createParser("{\"field1\": \"value1\"}");
        assertNull(deserializer.deserializeWithType(parser, null, null));
    }
}