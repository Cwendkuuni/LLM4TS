import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty.AnySetterReferring;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class SettableAnyPropertyTest {
    
    private SettableAnyProperty settableAnyProperty;
    private JsonNode jsonNode;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        jsonNode = getObjectNode();
        settableAnyProperty = getSettableAnyProperty();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        TestUnresolvedForwardReference testUnresolvedForwardReference = new TestUnresolvedForwardReference();
        settableAnyProperty.deserializeAndSet(objectMapper.treeAsTokens(jsonNode), objectMapper.getDeserializationContext(), testUnresolvedForwardReference, "prop1");
        
        Map<Object, Object> expectedMap = new HashMap<>();
        expectedMap.put("prop1", "value1");
        assertEquals(expectedMap, testUnresolvedForwardReference.getProps());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_WithUnresolvedForwardReference() throws IOException {
        TestUnresolvedForwardReference testUnresolvedForwardReference = new TestUnresolvedForwardReference();
        settableAnyProperty.deserializeAndSet(objectMapper.treeAsTokens(jsonNode), objectMapper.getDeserializationContext(), testUnresolvedForwardReference, "prop2");
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeAndSet_WithInvalidJson() throws IOException {
        TestUnresolvedForwardReference testUnresolvedForwardReference = new TestUnresolvedForwardReference();
        ObjectMapper mapper = new ObjectMapper();
        String invalidJson = "{\"prop1\"}";
        JsonNode invalidJsonNode = mapper.readTree(invalidJson);
        settableAnyProperty.deserializeAndSet(mapper.treeAsTokens(invalidJsonNode), objectMapper.getDeserializationContext(), testUnresolvedForwardReference, "prop1");
    }

    @Test
    public void testDeserialize() throws IOException {
        TestUnresolvedForwardReference testUnresolvedForwardReference = new TestUnresolvedForwardReference();
        Object result = settableAnyProperty.deserialize(objectMapper.treeAsTokens(jsonNode), objectMapper.getDeserializationContext());  
        assertEquals("value1", result);
    }

    @Test
    public void testHasValueDeserializer_WithDeserializer() {
        assertTrue(settableAnyProperty.hasValueDeserializer());
    }

    @Test
    public void testHasValueDeserializer_WithoutDeserializer() {
        SettableAnyProperty property = new SettableAnyProperty(null, null, null, null, null);
        assertFalse(property.hasValueDeserializer());
    }

    @Test
    public void testGetType() {
        assertNotNull(settableAnyProperty.getType());
    }

    @Test(expected = IOException.class)
    public void testSet_WithException() throws IOException {
        TestUnresolvedForwardReference testUnresolvedForwardReference = new TestUnresolvedForwardReference();
        settableAnyProperty.set(testUnresolvedForwardReference, "prop1", null);
    }

    private JsonNode getObjectNode() {
        String json = "{\"prop1\":\"value1\"}";
        try {
            return objectMapper.readTree(json);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SettableAnyProperty getSettableAnyProperty() {
        return new SettableAnyProperty(null, null, null, null, null, null);
    }

    private static class TestUnresolvedForwardReference {

        private Map<Object, Object> props;

        public void setProps(Map<Object, Object> props) {
            this.props = props;
        }

        public Map<Object, Object> getProps() {
            return props;
        }
    }
}