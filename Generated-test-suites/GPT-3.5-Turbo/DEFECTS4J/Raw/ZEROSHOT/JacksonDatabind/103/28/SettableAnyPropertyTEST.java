import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setup() {
        BeanProperty property = null;
        AnnotatedMember setter = null;
        JavaType type = null;
        KeyDeserializer keyDeser = null;
        JsonDeserializer<Object> valueDeser = null;
        TypeDeserializer typeDeser = null;

        settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    }

    @Test
    public void testGetProperty() {
        BeanProperty property = null;

        settableAnyProperty.setProperty(property);

        Assert.assertEquals(property, settableAnyProperty.getProperty());
    }

    @Test
    public void testHasValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer = null;
        settableAnyProperty.setDeserializer(valueDeserializer);

        Assert.assertTrue(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testGetType() {
        JavaType type = null;
        settableAnyProperty.setType(type);

        Assert.assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        String propName = "";
        try {
            settableAnyProperty.deserializeAndSet(p, ctxt, instance, propName);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNode node = null;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("test", "value");
        p = mapper.treeAsTokens(objectNode);

        Assert.assertEquals("value", settableAnyProperty.deserialize(p, ctxt));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object propName = new Object();
        Object value = new Object();

        settableAnyProperty.set(instance, propName, value);

        Assert.assertEquals(new HashMap<Object, Object>() {{
            put(propName, value);
        }}, ((AnnotatedField) settableAnyProperty.getSetter()).getValue(instance));
    }
}