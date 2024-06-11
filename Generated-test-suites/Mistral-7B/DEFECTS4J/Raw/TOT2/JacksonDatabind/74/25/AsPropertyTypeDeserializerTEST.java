import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testJson = mapper.createObjectNode();
        testJson.put("typePropertyName", "StringType");
        Object result = mapper.readValue(testJson.toString(), AsPropertyTypeDeserializer.class);
        // Add assertions for the expected behavior here
    }
}