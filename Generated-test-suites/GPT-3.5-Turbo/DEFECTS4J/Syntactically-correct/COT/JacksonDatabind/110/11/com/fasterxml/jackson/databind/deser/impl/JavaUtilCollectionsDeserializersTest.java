import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        JavaType type = TypeFactory.defaultInstance().constructType(Class.forName("java.util.ArrayList"));
        DeserializationContext ctxt = null;
        try {
            JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNotNull(result);
            assertEquals(StdDelegatingDeserializer.class, result.getClass());
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindForMap() {
        JavaType type = TypeFactory.defaultInstance().constructType(Class.forName("java.util.HashMap"));
        DeserializationContext ctxt = null;
        try {
            JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            assertNotNull(result);
            assertEquals(StdDelegatingDeserializer.class, result.getClass());
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindForCollectionWithInvalidType() {
        JavaType type = TypeFactory.defaultInstance().constructType(Class.forName("java.lang.String"));
        DeserializationContext ctxt = null;
        try {
            JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNull(result);
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindForMapWithInvalidType() {
        JavaType type = TypeFactory.defaultInstance().constructType(Class.forName("java.lang.String"));
        DeserializationContext ctxt = null;
        try {
            JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            assertNull(result);
        } catch (JsonMappingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}