import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;

public class SettableAnyPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableAnyProperty property = createProperty();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"key\":\"value\"}");

        Map<Object, Object> instance = new HashMap<Object, Object>();
        property.deserializeAndSet(null, null, instance, "key");

        assertEquals("value", instance.get("key"));
    }

    @Test
    public void testDeserialize() throws JsonProcessingException, IOException {
        SettableAnyProperty property = createProperty();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"value\"");

        Object value = property.deserialize(null, null);

        assertEquals("value", value);
    }

    private SettableAnyProperty createProperty() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        SettableAnyProperty property = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
        return property;
    }
}