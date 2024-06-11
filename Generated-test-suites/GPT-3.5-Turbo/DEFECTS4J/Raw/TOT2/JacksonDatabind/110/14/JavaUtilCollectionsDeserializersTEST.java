import static org.junit.Assert.*;
import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        // typical use case
        // create a DeserializationContext and JavaType
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, javaType);
            assertNotNull(deserializer);
        } catch (JsonMappingException e) {
            fail("Exception caught:" + e.getMessage());
        }

        // edge case
        // test for an empty JavaType
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, TypeFactory.unknownType());
            assertNull(deserializer);
        } catch (JsonMappingException e) {
            fail("Exception caught:" + e.getMessage());
        }
    }

    @Test
    public void testFindForMap() {
         // typical use case
        // create a DeserializationContext and JavaType
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, javaType);
            assertNotNull(deserializer);
        } catch (JsonMappingException e) {
            fail("Exception caught:" + e.getMessage());
        }

        // edge case
        // test for a null JavaType
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, null);
            assertNull(deserializer);
        } catch (JsonMappingException e) {
            fail("Exception caught:" + e.getMessage());
        }
    }

}