
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.UnresolvedForwardReference;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setUp() {
        settableAnyProperty = createSettableAnyProperty();
    }

    @Test
    public void deserializeAndSet_ShouldSetProperty_WhenValueDeserializationSucceeds() throws IOException {
        DummyObject dummy = new DummyObject();

        settableAnyProperty.deserializeAndSet(createJsonParser(), createDeserializationContext(), dummy, "property1");

        assertEquals("value1", dummy.getProperties().get("property1"));
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeAndSet_ShouldThrowJsonMappingException_WhenValueDeserializationFails() throws IOException {
        DummyObject dummy = new DummyObject();

        settableAnyProperty.deserializeAndSet(createJsonParser(), createDeserializationContext(), dummy, "property2");
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void deserializeAndSet_ShouldThrowUnresolvedForwardReferenceException_WhenForwardReferenceIsUnresolved() throws IOException {
        DummyObject dummy = new DummyObject();

        settableAnyProperty.deserializeAndSet(createJsonParserForForwardReference(), createDeserializationContext(), dummy, "forwardReference");
    }

    @Test
    public void hasValueDeserializer_ShouldReturnTrue_WhenValueDeserializerIsNotNull() {
        assertTrue(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void hasValueDeserializer_ShouldReturnFalse_WhenValueDeserializerIsNull() {
        assertFalse(createSettableAnyPropertyWithoutValueDeserializer().hasValueDeserializer());
    }

    @Test
    public void getType_ShouldReturnExpectedType() {
        assertEquals(DummyObject.class, settableAnyProperty.getType().getRawClass());
    }

    @Test
    public void set_ShouldSetProperty_WhenPropertyIsMapAndField() throws IOException {
        DummyObject dummy = new DummyObject();

        settableAnyProperty.set(dummy, "property3", "value3");

        assertEquals("value3", dummy.getProperties().get("property3"));
    }

    @Test
    public void set_ShouldSetProperty_WhenPropertyIsMapAndMethod() throws IOException {
        DummyObject dummy = new DummyObject();

        settableAnyProperty.set(dummy, "property4", "value4");

        assertEquals("value4", dummy.getProperties().get("property4"));
    }

    @Test(expected = IOException.class)
    public void set_ShouldThrowIOException_WhenExceptionOccurs() throws IOException {
        DummyObject dummy = new DummyObject();

        settableAnyProperty.set(dummy, "property5", "value5");
    }

    private SettableAnyProperty createSettableAnyProperty() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(createJsonString(), SettableAnyProperty.class);
        } catch (IOException e) {
            fail("Failed to create SettableAnyProperty");
            return null;
        }
    }

    private SettableAnyProperty createSettableAnyPropertyWithoutValueDeserializer() {
        String jsonString = "{\"_property\": null, \"_setter\": null, \"_type\": null}";

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(jsonString, SettableAnyProperty.class);
        } catch (IOException e) {
            fail("Failed to create SettableAnyProperty");
            return null;
        }
    }

    private JsonParser createJsonParser() throws JsonParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.getFactory().createParser(createJsonString());
    }

    private JsonParser createJsonParserForForwardReference() throws JsonParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.getFactory().createParser(createJsonStringWithForwardReference());
    }

    private DeserializationContext createDeserializationContext() {
        // create a dummy deserialization context
        return new DeserializationContext(null, null, new ObjectMapper().getNodeFactory(), null, null, null, null);
    }

    private String createJsonString() {
        return "{\"_property\": null, \"_setter\": null, \"_type\": {\"rawClass\": \"DummyObject\"}, \"_valueDeserializer\": null, \"_valueTypeDeserializer\": null, \"_keyDeserializer\": null}";
    }

    private String createJsonStringWithForwardReference() {
        return "{\"_property\": null, \"_setter\": null, \"_type\": {\"rawClass\": \"DummyObject\"}, \"_valueDeserializer\": {\"ObjectIdReader\": null}, \"_valueTypeDeserializer\": null, \"_keyDeserializer\": null}";
    }

    private static class DummyObject {
        private Map<String, String> properties = new HashMap<>();

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
