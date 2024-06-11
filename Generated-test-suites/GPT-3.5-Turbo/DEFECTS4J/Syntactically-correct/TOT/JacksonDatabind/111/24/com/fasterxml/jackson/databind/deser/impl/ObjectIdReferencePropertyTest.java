
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = createProperty();
        assertNull(property.withNullProvider(null));
    }

    @Test
    public void testWithNoIdentityInfo() {
        ObjectIdReferenceProperty property = createProperty();
        try {
            property.deserializeAndSet(createJsonParser(), createDeserializationContext(), new Object());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unresolved forward reference but no identity info", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty property = createProperty();
        assertNull(property.deserializeSetAndReturn(createJsonParser(), createDeserializationContext(), new Object()));
    }

    @Test
    public void testDeserializeSetAndReturnWithForwardReference() throws Exception {
        ObjectIdReferenceProperty property = createProperty();
        property.deserializeSetAndReturn(createForwardReferenceJsonParser(), createDeserializationContext(), new Object());
    }

    @Test
    public void testDeserializeSetAndReturnWithInvalidForwardReference() {
        ObjectIdReferenceProperty property = createProperty();
        try {
            property.deserializeSetAndReturn(createInvalidForwardReferenceJsonParser(), createDeserializationContext(), new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Trying to resolve a forward reference with id [123] that wasn't previously seen as unresolved.", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty property = createProperty();
        try {
            property.deserializeAndSet(createJsonParser(), createDeserializationContext(), new Object());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty property = createProperty();
        try {
            assertNull(property.setAndReturn(new Object(), new Object()));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private ObjectIdReferenceProperty createProperty() {
        return new ObjectIdReferenceProperty(null, null);
    }

    private JsonParser createJsonParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        try {
            return jsonFactory.createParser("{}");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
            return null;
        }
    }

    private JsonParser createForwardReferenceJsonParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        try {
            return jsonFactory.createParser("{\"$ref\":\"ref-id\",\"id\":123,\"type\":\"type-id\"}");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
            return null;
        }
    }

    private JsonParser createInvalidForwardReferenceJsonParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        try {
            return jsonFactory.createParser("{\"$ref\":\"ref-id\",\"id\":123,\"type\":\"type-id\"}");
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
            return null;
        }
    }

    private DeserializationContext createDeserializationContext() {
        return new DeserializationContext(null, null, null);
    }
}
