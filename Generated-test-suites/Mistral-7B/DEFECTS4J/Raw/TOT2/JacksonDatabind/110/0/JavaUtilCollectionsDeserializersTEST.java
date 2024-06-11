import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    // 
    @Test
    public void testFindForCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, listType);
        assertNotNull(deserializer);
        // 
    }
}