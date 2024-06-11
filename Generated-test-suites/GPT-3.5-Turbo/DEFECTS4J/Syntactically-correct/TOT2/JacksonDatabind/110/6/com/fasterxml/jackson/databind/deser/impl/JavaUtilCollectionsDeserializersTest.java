import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        ctxt = new DeserializationContext();
        type = JavaType.construct(Object.class);
    }

    @Test
    public void testFindForCollection_AsArraysList() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException {
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindForCollection_InvalidType() throws JsonMappingException {
        JavaType invalidType = JavaType.construct(String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, invalidType);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap_InvalidType() throws JsonMappingException {
        JavaType invalidType = JavaType.construct(String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, invalidType);
        assertNull(deserializer);
    }
}