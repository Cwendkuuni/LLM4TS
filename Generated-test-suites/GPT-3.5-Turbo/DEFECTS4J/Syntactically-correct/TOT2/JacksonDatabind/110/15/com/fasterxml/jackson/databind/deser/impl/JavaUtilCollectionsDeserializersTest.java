import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        ctxt = new DeserializationContext(null, null, null);
        type = TypeFactory.defaultInstance().constructType(Object.class);
    }

    @Test
    public void testFindForCollection_validType_returnDeserializer() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_invalidType_returnNull() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_validType_returnDeserializer() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_invalidType_returnNull() throws JsonMappingException {
        type = TypeFactory.defaultInstance().constructType(Double.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNull(deserializer);
    }

}