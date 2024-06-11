import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.StdDeserializationContext;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class ExternalTypeHandlerTest {

    private ObjectMapper objectMapper;
    private ExternalTypeHandler externalTypeHandler;

    @BeforeMethod
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode propertiesNode = objectMapper.createObjectNode();
        rootNode.set("properties", propertiesNode);
        Map<String, Object> bean = new HashMap<>();
        externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojoProperty("name"), new StringDeserializer(String.class))
                .addExternal(new SimplePojoProperty("type"), new StringDeserializer(String.class))
                .build());
        externalTypeHandler.start();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(ObjectNode.class.getName());
        jsonParser.readValueAsTree(objectMapper.writeValueAsString(rootNode));

        String typeId = "StringType";
        jsonParser.nextToken();
        jsonParser.nextToken(); // "name"
        jsonParser.nextToken(); // ":"
        jsonParser.nextToken(); // "type"
        jsonParser.nextToken(); // ":"
        jsonParser.nextToken(); // typeId

        boolean result = externalTypeHandler.handleTypePropertyValue(jsonParser, new StdDeserializationContext(), "type", bean);
        assertTrue(result);
        assertEquals(bean.get("type"), typeId);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(ObjectNode.class.getName());
        jsonParser.readValueAsTree(objectMapper.writeValueAsString(rootNode));

        jsonParser.nextToken(); // "[\"name\"]"
        jsonParser.nextToken(); // ":"
        jsonParser.nextToken(); // "\"value\""

        boolean result = externalTypeHandler.handlePropertyValue(jsonParser, new StdDeserializationContext(), "name", bean);
        assertTrue(result);
        assertNotNull(bean.get("name"));
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testMissingProperty() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(ObjectNode.class.getName());
        jsonParser.readValueAsTree(objectMapper.writeValueAsString(rootNode));

        jsonParser.nextToken(); // "[\"type\"]"
        jsonParser.nextToken(); // ":"
        jsonParser.nextToken(); // "\"InvalidType\""

        externalTypeHandler.handlePropertyValue(jsonParser, new StdDeserializationContext(), "name", bean);
        fail("Expected JsonMappingException");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testMissingTypeId() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(ObjectNode.class.getName());
        jsonParser.readValueAsTree(objectMapper.writeValueAsString(rootNode));

        jsonParser.nextToken(); // "[\"name\"]"
        jsonParser.nextToken(); // ":"
        jsonParser.nextToken(); // null

        externalTypeHandler.handlePropertyValue(jsonParser, new StdDeserializationContext(), "name", bean);
        fail("Expected JsonMappingException");
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(ObjectNode.class.getName());
        jsonParser.readValueAsTree(objectMapper.writeValueAsString(rootNode));

        jsonParser.nextToken(); // "]"
        jsonParser.nextToken(); // "}"

        Object result = externalTypeHandler.complete(jsonParser, new StdDeserializationContext(), null);
        assertEquals(result, bean);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(ObjectNode.class.getName());
        jsonParser.readValueAsTree(objectMapper.writeValueAsString(rootNode));

        PropertyBasedCreator creator = new PropertyBasedCreator();
        Object result = externalTypeHandler.complete(jsonParser, new StdDeserializationContext(), null, creator);
        assertEquals(result, creator.create());
    }

    @Test
    public void testCompleteWithBufferedCreatorProperties() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser(ObjectNode.class.getName());
        jsonParser.readValueAsTree(objectMapper.writeValueAsString(rootNode));

        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object result = externalTypeHandler.complete(jsonParser, new StdDeserializationContext(), buffer, new PropertyBasedCreator());
        assertEquals(result, buffer.getCreator().build(new StdDeserializationContext(), buffer));
    }

    static class SimplePojo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class SimplePojoProperty {
        private String name;
        private TypeDeserializer typeDeserializer;

        public SimplePojoProperty(String name, TypeDeserializer typeDeserializer) {
            this.name = name;
            this.typeDeserializer = typeDeserializer;
        }

        public String getName() {
            return name;
        }

        public TypeDeserializer getTypeDeserializer() {
            return typeDeserializer;
        }
    }

    static class StringDeserializer extends StdDeserializer<String> {
        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> vc) {
            super(String.class, vc);
        }

        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            return jsonParser.getText();
        }
    }

    static class PropertyBasedCreator {
        public Object create(DeserializationContext ctxt) throws IOException {
            SimplePojo pojo = new SimplePojo();
            pojo.setName("test");
            return pojo;
        }
    }
}