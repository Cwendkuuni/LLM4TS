package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.*;

@Test
public class DefaultDeserializationContextTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeMethod
    public void setup() {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testFindObjectId.json"), ObjectNode.class);
        Object id = rootNode.get("id");
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        ObjectIdGenerator idGen = context.findObjectIdGenerator(JsonDeserializer.None.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId objId = context.findObjectId(id, idGen, resolver);
        Assert.assertNotNull(objId);
        Assert.assertEquals(objId.getIdKey().key, id.getClass().getName());
    }

    @Test
    public void testFindObjectId_MissingId() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testFindObjectId_MissingId.json"), ObjectNode.class);
        Object id = rootNode.get("id");
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        ObjectIdGenerator idGen = context.findObjectIdGenerator(JsonDeserializer.None.class);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        try {
            context.findObjectId(id, idGen, resolver);
            Assert.fail();
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testDeserializerInstance.json"), ObjectNode.class);
        Object id = rootNode.get("id");
        AnnotatedClass idAnnotatedClass = MAPPER.getTypeFactory().constructType(id.getClass());
        JsonDeserializer<Object> deserializer = new StdDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<?> deser = context.deserializerInstance(idAnnotatedClass, deserializer);
        Assert.assertEquals(deser.getClass(), StdDeserializer.class);
    }

    @JsonSubTypes({
            @JsonSubTypes.Type(value = Subclass1.class, name = "subclass1"),
            @JsonSubTypes.Type(value = Subclass2.class, name = "subclass2")
    })
    static class Parent {
        @IdKey("subclassId")
        public String subclassId;
    }

    static class Subclass1 extends Parent {}

    static class Subclass2 extends Parent {}

    static class StdDeserializer extends JsonDeserializer<Parent> {

        @Override
        public Parent deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            ObjectIdGenerator idGen = ctxt.findObjectIdGenerator(JsonDeserializer.None.class);
            ObjectIdResolver resolver = new SimpleObjectIdResolver();
            ReadableObjectId objId = ctxt.findObjectId(jsonParser.getText(), idGen, resolver);
            if (objId != null) {
                return objId.getObject();
            }
            // Use default constructor when no id present
            return new Parent();
        }
    }

    @Test
    public void testDeserializerInstance_IdPresent() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testDeserializerInstance_IdPresent.json"), ObjectNode.class);
        Object id = rootNode.get("id");
        AnnotatedClass idAnnotatedClass = MAPPER.getTypeFactory().constructType(id.getClass());
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<?> deser = context.deserializerInstance(idAnnotatedClass, new StdDeserializer());
        Assert.assertEquals(deser.getClass(), StdDeserializer.class);

        JsonParser jsonParser = new JsonFactory().createParser(rootNode.getOutputStream());
        Parent deserialization = mapper.readValue(jsonParser, Parent.class);
        Assert.assertEquals(deserialization.getClass(), Subclass1.class);
    }

    @Test
    public void testDeserializerInstance_IdAbsent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<?> deser = context.deserializerInstance(Object.class, new StdDeserializer());
        Assert.assertNull(deser);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testKeyDeserializerInstance.json"), ObjectNode.class);
        Object id = rootNode.get("id");
        AnnotatedClass idAnnotatedClass = MAPPER.getTypeFactory().constructType(id.getClass());
        KeyDeserializer deserializer = new StdKeyDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        KeyDeserializer keyDeser = context.keyDeserializerInstance(idAnnotatedClass, deserializer);
        Assert.assertEquals(keyDeser.getClass(), StdKeyDeserializer.class);
    }

    static class StdKeyDeserializer extends KeyDeserializer {

        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }

    @Test
    public void testDeserializeValue() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testDeserializeValue.json"), ObjectNode.class);
        Object id = rootNode.get("id");
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser = new JsonFactory().createParser(rootNode.getOutputStream());
        Parent deserialization = mapper.readValue(jsonParser, Parent.class);
        Assert.assertEquals(deserialization.getClass(), Subclass1.class);
    }

    @Test
    public void testDeserializeValue_IdPresent() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testDeserializeValue_IdPresent.json"), ObjectNode.class);
        Object id = rootNode.get("id");
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser = new JsonFactory().createParser(rootNode.getOutputStream());
        Parent deserialization = mapper.readValue(jsonParser, Parent.class);
        Assert.assertEquals(deserialization.getClass(), Subclass2.class);
    }

    @Test
    public void testDeserializeValue_IdAbsent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        JsonParser jsonParser = new JsonFactory().createParser(getClass().getResourceAsStream("/testDeserializeValue_IdAbsent.json"));
        try {
            Parent deserialization = mapper.readValue(jsonParser, Parent.class);
            Assert.fail();
        } catch (JsonMappingException e) {
            Assert.assertEquals(e.getMessage(), "No Serializer found for class com.fasterxml.jackson.databind.test.Parent and no properties discovered to create BeanSerializer");
        }
    }

    @Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectNode rootNode = MAPPER.readValue(getClass().getResource("/testCheckUnresolvedObjectId.json"), ObjectNode.class);
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        try {
            context.checkUnresolvedObjectId();
            Assert.fail();
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }
}