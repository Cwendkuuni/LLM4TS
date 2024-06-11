import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"name\": \"John\", \"type\": \"Person\", \"age\": 30}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{"Person"}, new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        Assert.assertTrue(handler.handleTypePropertyValue(jp, new TestContext(), "type", null));
        Assert.assertEquals("Person", handler._typeIds[0]);

        handler.complete(jp, new TestContext(), null);
    }

    @Test
    public void testHandleTypePropertyValueMissingTypeId() throws IOException {
        String json = "{\"name\": \"John\", \"type\": \"Person\"}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{"Person"}, new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        Assert.assertFalse(handler.handleTypePropertyValue(jp, new TestContext(), "type", null));
        Assert.assertNull(handler._typeIds[0]);

        handler.complete(jp, new TestContext(), null);
    }

    @Test
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        String json = "{\"name\": \"John\", \"type\": \"Person\"}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{"Person"}, new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        Assert.assertFalse(handler.handleTypePropertyValue(jp, new TestContext(), "nonexistent", null));
        Assert.assertNull(handler._typeIds[1]);

        handler.complete(jp, new TestContext(), null);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[3], new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        Assert.assertTrue(handler.handlePropertyValue(jp, new TestContext(), "age", null));
        Assert.assertEquals(30, handler._typeIds[1].intValue());

        handler.complete(jp, new TestContext(), null);
    }

    @Test
    public void testHandlePropertyValueMissingProperty() throws IOException {
        String json = "{\"name\": \"John\"}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[3], new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        Assert.assertFalse(handler.handlePropertyValue(jp, new TestContext(), "nonexistent", null));
        Assert.assertNull(handler._typeIds[2]);

        handler.complete(jp, new TestContext(), null);
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30, \"type\": \"Person\"}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{"Person"}, new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        handler.handleTypePropertyValue(jp, new TestContext(), "type", null);
        handler.handlePropertyValue(jp, new TestContext(), "age", null);
        handler.complete(jp, new TestContext(), null);

        Map<String, Object> result = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals("John", result.get("name"));
        Assert.assertEquals(30, result.get("age"));
        Assert.assertEquals("Person", result.get("type"));
    }

    @Test
    public void testCompleteWithNullValue() throws IOException {
        String json = "{\"name\": \"John\", \"age\": null}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{"Person"}, new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        handler.handleTypePropertyValue(jp, new TestContext(), "type", null);
        handler.handlePropertyValue(jp, new TestContext(), "age", null);
        handler.complete(jp, new TestContext(), null);

        Map<String, Object> result = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        Assert.assertEquals("John", result.get("name"));
        Assert.assertNull(result.get("age"));
        Assert.assertEquals("Person", result.get("type"));
    }

    @Test
    public void testCompleteWithMissingValues() throws IOException {
        String json = "{\"name\": \"John\"}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{"Person"}, new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        handler.handleTypePropertyValue(jp, new TestContext(), "type", null);
        handler.complete(jp, new TestContext(), null);

        Assert.assertNotNull(handler._typeIds[2]);
        Assert.assertNull(handler._tokens[2]);

        try {
            handler.complete(jp, new TestContext(), null);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCompleteWithNaturalType() throws IOException {
        String json = "[1, 2, 3]";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[3], new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        handler.handlePropertyValue(jp, new TestContext(), "array", null);
        handler.complete(jp, new TestContext(), null);

        List<Integer> result = mapper.readValue(json, new TypeReference<List<Integer>>() {});
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(1, result.get(0).intValue());
        Assert.assertEquals(2, result.get(1).intValue());
        Assert.assertEquals(3, result.get(2).intValue());
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        String json = "{\"name\": \"John\", \"ages\": [1, 2, 3]}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[3], new TokenBuffer[3]);
        handler.start();

        JsonParser jp = mapper.getFactory().createParser(json);

        handler.handleTypePropertyValue(jp, new TestContext(), "type", null);
        handler.handlePropertyValue(jp, new TestContext(), "name", null);

        PropertyBasedCreator creator = new TestCreator();
        Object result = handler.complete(jp, new TestContext(), null, creator);

        Map<String, Object> map = (Map<String, Object>) result;
        Assert.assertEquals("John", map.get("name"));
        List<Integer> list = (List<Integer>) map.get("ages");
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(1, list.get(0).intValue());
        Assert.assertEquals(2, list.get(1).intValue());
        Assert.assertEquals(3, list.get(2).intValue());
    }

    private List<ExtTypedProperty> createProperties() {
        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty("name", new TestStringDeserializer("name")));
        properties.add(new ExtTypedProperty("age", new TestNumberDeserializer("age")));
        properties.add(new ExtTypedProperty("type", new TestStringDeserializer("type")));
        properties.add(new ExtTypedProperty("array", new TestListDeserializer("array")));
        return properties;
    }

    private Map<String, Integer> createNameToPropertyIndex() {
        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("name", 0);
        nameToPropertyIndex.put("age", 1);
        nameToPropertyIndex.put("type", 2);
        nameToPropertyIndex.put("array", 3);
        return nameToPropertyIndex;
    }

    public static class TestStringDeserializer extends StdDeserializer<String> {
        private final String _name;

        public TestStringDeserializer(String name) {
            this._name = name;
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString();
        }

        @Override
        public String getPropertyName() {
            return _name;
        }
    }

    public static class TestNumberDeserializer extends StdDeserializer<Integer> {
        private final String _name;

        public TestNumberDeserializer(String name) {
            this._name = name;
        }

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }

        @Override
        public String getPropertyName() {
            return _name;
        }
    }

    public static class TestListDeserializer extends StdDeserializer<List<Integer>> {
        private final String _name;

        public TestListDeserializer(String name) {
            this._name = name;
        }

        @Override
        public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            List<Integer> list = new ArrayList<>();
            while (p.nextToken() != JsonToken.END_ARRAY) {
                list.add(p.getValueAsInt());
            }
            return list;
        }

        @Override
        public String getPropertyName() {
            return _name;
        }
    }

    public static class TestCreator implements PropertyBasedCreator<Map<String, Object>> {
        @Override
        public Map<String, Object> createUsingGuid(DeserializationContext ctxt) throws IOException {
            return new HashMap<>();
        }

        @Override
        public Map<String, Object> createFromPropertyValue(DeserializationContext ctxt, PropertyName propertyName) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            JsonParser p = mapper.getFactory().createParser(propertyName.getValueAsString());
            return new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[3], new TokenBuffer[3]).complete(p, ctxt, null);
        }
    }

    public static class TestContext extends DeserializationContext {
        @Override
        public ObjectMapper getContextualObjectMapper() {
            return new ObjectMapper();
        }

        @Override
        public ObjectReader getContextualValueTypeReader(Class<?> type, JsonValueType valueType) throws JsonMappingException {
            return null;
        }

        @Override
        public ObjectReader getValueTypeReader(Class<?> type) throws JsonMappingException {
            return null;
        }
    }
}