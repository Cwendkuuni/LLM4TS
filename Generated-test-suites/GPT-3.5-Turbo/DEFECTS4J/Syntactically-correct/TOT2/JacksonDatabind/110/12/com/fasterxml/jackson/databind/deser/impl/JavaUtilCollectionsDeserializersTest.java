import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection_SingletonList() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNotNull(deserializer);
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindForMap_SingletonMap() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            assertNotNull(deserializer);
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}