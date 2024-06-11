
import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        try {
            DeserializationContext ctxt = null;
            JavaType type = null;

            // Test for TYPE_AS_LIST
            type = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNotNull(deserializer);

            // Test for TYPE_SINGLETON_LIST
            type = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNotNull(deserializer);

            // Test for TYPE_SINGLETON_SET
            type = TypeFactory.defaultInstance().constructCollectionType(Set.class, String.class);
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNotNull(deserializer);

            // Test for TYPE_UNMODIFIABLE_LIST
            type = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNotNull(deserializer);

            // Test for TYPE_UNMODIFIABLE_SET
            type = TypeFactory.defaultInstance().constructCollectionType(Set.class, String.class);
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            assertNotNull(deserializer);

        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindForMap() {
        try {
            DeserializationContext ctxt = null;
            JavaType type = null;

            // Test for TYPE_SINGLETON_MAP
            type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            assertNotNull(deserializer);

            // Test for TYPE_UNMODIFIABLE_MAP
            type = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            assertNotNull(deserializer);

        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

}
