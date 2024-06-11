import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);

        String jsonString1 = "[\"apple\", \"banana\"]";
        List<String> collection1 = mapper.readValue(jsonString1, javaType);
        Assert.assertEquals("Collection size mismatch", 2, collection1.size());
        Assert.assertEquals("Collection content mismatch", "apple", collection1.get(0));
        Assert.assertEquals("Collection content mismatch", "banana", collection1.get(1));

        String jsonString2 = "[1, 2, 3]";
        List<Integer> collection2 = mapper.readValue(jsonString2, javaType);
        Assert.assertEquals("Collection size mismatch", 3, collection2.size());
        Assert.assertEquals("Collection content mismatch", 1, collection2.get(0).intValue());
        Assert.assertEquals("Collection content mismatch", 2, collection2.get(1).intValue());
        Assert.assertEquals("Collection content mismatch", 3, collection2.get(2).intValue());
    }

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);

        String jsonString = "[]";
        List<String> collection = mapper.readValue(jsonString, javaType);
        Assert.assertEquals("Collection size mismatch", 0, collection.size());
    }

    @Test(expected = IOException.class)
    public void testNullDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);

        String jsonString = "null";
        mapper.readValue(jsonString, javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);

        String jsonString = "[[]";
        JsonProcessingException exception = mapper.readValue(new JsonFactory(), jsonString, javaType);
        Assert.assertNotNull("Exception should not be null", exception);
    }
}