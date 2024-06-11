import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType javaType = new TypeFactory().constructType(new TypeReference<List<Integer>>(){}.getType());

        JsonDeserializer<Object> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);
        Object result = mapper.readValue("[]", deserializer.getType());

        assertTrue(result instanceof List);
        List<Integer> list = (List<Integer>) result;
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).intValue());
    }

    // Add edge cases and exception handling scenarios here
}