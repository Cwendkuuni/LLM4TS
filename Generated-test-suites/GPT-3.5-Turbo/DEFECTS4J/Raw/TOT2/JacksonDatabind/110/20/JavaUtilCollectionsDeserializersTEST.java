import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.List;
import java.util.Set;
import java.util.Map;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StdDelegatingDeserializer);
    }

    @Test
    public void testConverterSingletonSet() {
        JavaType type = TypeFactory.defaultInstance().constructType(Set.class);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, type, Set.class);
        assertNotNull(converter);
    }

    @Test
    public void testConverterUnmodifiableMap() {
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, type, Map.class);
        assertNotNull(converter);
    }
}