import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.instance;
import static com.fasterxml.jackson.databind.SerializationFeature.*;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(AsPropertyTypeDeserializer.class);
        ObjectReader reader = mapper.readerForType(javaType);
        JsonNode jsonNode = instance.objectNode();
        jsonNode.put("typePropertyName", "someType");
        jsonNode.put("someField", "someValue");

        AsPropertyTypeDeserializer deserializer = reader.readValue(jsonNode);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());

        // Add more test cases as needed
    }
}