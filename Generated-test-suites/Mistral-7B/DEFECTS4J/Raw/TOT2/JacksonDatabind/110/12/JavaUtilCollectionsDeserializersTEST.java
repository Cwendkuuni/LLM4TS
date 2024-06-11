import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonList() throws Exception {
        JavaType javaType = new TypeReference<List<String>>(){}.getType();
        ObjectNode inputNode = mapper.createObjectNode();
        inputNode.put("value", "[\"apple\", \"banana\"]");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

        List<String> result = mapper.readValue(inputNode, javaType);
        assertEquals(deserializer.getClass(), result.get(0).getClass());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
    }

    // ... Add other test cases for different types and edge cases here
}