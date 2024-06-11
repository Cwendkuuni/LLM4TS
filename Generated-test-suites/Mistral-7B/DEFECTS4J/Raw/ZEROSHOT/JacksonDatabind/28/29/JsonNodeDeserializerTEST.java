package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class JsonNodeDeserializerTest {

    @Test
    public void testJsonNodeDeserializer() throws IOException {
        JsonNode node;

        // Test null value
        JsonParser parser = createParser("null");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        node = deserializer.deserialize(parser, new TestContext());
        Assert.assertEquals(NullNode.getInstance(), node);

        // Test object node deserialization
        parser = createParser("{\"name\":\"John\",\"age\":30}");
        deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        node = deserializer.deserialize(parser, new TestContext());
        ObjectNode objNode = (ObjectNode) node;
        Assert.assertEquals("John", objNode.get("name").asText());
        Assert.assertEquals(30, objNode.get("age").asInt());

        // Test array node deserialization
        parser = createParser("[1, 2, 3]");
        deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        node = deserializer.deserialize(parser, new TestContext());
        ArrayNode arrayNode = (ArrayNode) node;
        Assert.assertEquals(3, arrayNode.size());
        Assert.assertEquals(1, arrayNode.get(0).asInt());
        Assert.assertEquals(2, arrayNode.get(1).asInt());
        Assert.assertEquals(3, arrayNode.get(2).asInt());

        // Test deserialization of any JsonNode
        parser = createParser("{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}");
        deserializer = JsonNodeDeserializer.instance;
        node = deserializer.deserialize(parser, new TestContext());
        ObjectNode objNode2 = (ObjectNode) node;
        Assert.assertEquals("John", objNode2.get("name").asText());
        Assert.assertEquals(30, objNode2.get("age").asInt());
        ArrayNode arrayNode2 = (ArrayNode) objNode2.get("hobbies");
        Assert.assertEquals(2, arrayNode2.size());
        Assert.assertEquals("reading", arrayNode2.get(0).asText());
        Assert.assertEquals("swimming", arrayNode2.get(1).asText());
    }

    private JsonParser createParser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(jsonString));
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonNodeFactory nodeFactory() {
            return new StandardJsonNodeFactory();
        }

        @Override
        public ObjectReader getValueInstantiator() {
            return null;
        }

        @Override
        public ObjectReader getFactory() {
            return null;
        }

        @Override
        public JavaType getTypeFactory() {
            return null;
        }

        @Override
        public ObjectReader getContainerType(Class<?> containerType) {
            return null;
        }

        @Override
        public ValueInstantiator valueInstantiator() {
            return null;
        }

        @Override
        public ObjectReader getRootValueFactory() {
            return null;
        }

        @Override
        public JavaType getValueType() {
            return null;
        }

        @Override
        public TypeDeserializer typeDeserializer() {
            return null;
        }

        @Override
        public JaninoTypeValidator typeValidator() {
            return null;
        }

        @Override
        public JsonFactory jsonFactory() {
            return null;
        }

        @Override
        public AnnotationIntrospector introspector() {
            return null;
        }

        @Override
        public Locale activeLocale() {
            return null;
        }

        @Override
        public BeanDescription rootDescription() {
            return null;
        }

        @Override
        public BeanDesc defaultBeanDescForValueType() {
            return null;
        }

        @Override
        public JsonParser currentLocation() {
            return null;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public boolean isEnabled(SerializationFeature feature) {
            return true;
        }

        @Override
        public boolean isEnabled(Feature feature) {
            return true;
        }

        @Override
        public void reportError(JsonParser p, String msg) throws IOException {
            throw new IOException(msg);
        }

        @Override
        public void reportError(JsonParser p, String msg, Object args) throws IOException {
            throw new IOException(String.format(msg, args));
        }

        @Override
        public void reportError(JsonParser p, String msg, Throwable t) throws IOException {
            throw new IOException(msg, t);
        }

        @Override
        public void reportWarning(JsonParser p, int warningId, String msg) throws IOException {
            System.out.println("Warning: " + msg);
        }

        @Override
        public void reportWarning(JsonParser p, int warningId, String msg, Object args) throws IOException {
            System.out.println("Warning: " + String.format(msg, args));
        }

        @Override
        public void reportWarning(JsonParser p, int warningId, String msg, Object args, Throwable t) throws IOException {
            System.out.println("Warning: " + String.format(msg, args));
            t.printStackTrace();
        }

        @Override
        public void setPropertyValue(BeanProperty property, Object value) {
            // Empty implementation
        }

        @Override
        public void setBeanPropertyFilter(BeanPropertyFilter filter) {
            // Empty implementation
        }

        @Override
        public void setSerializerProvider(JsonSerializerProvider provider) {
            // Empty implementation
        }

        @Override
        public void setActiveValueAccessors(ValueAccessorProvider._ValueHandler valueHandler) {
            // Empty implementation
        }

        @Override
        public void setCurrentLocation(JsonLocation loc) {
            // Empty implementation
        }

        @Override
        public void setContextHandler(ContextHandler contextHandler) {
            // Empty implementation
        }

        @Override
        public void setDefaultValueHandler(DefaultValueProvider.DefaultValueProviderHandler handler) {
            // Empty implementation
        }

        @Override
        public void setNullValueHandler(NullValueHandler nullHandler) {
            // Empty implementation
        }

        @Override
        public void setRootValueHandler(RootValueHandler rootHandler) {
            // Empty implementation
        }

        @Override
        public void setDefaultSerializerProvider(JsonSerializerProvider provider) {
            // Empty implementation
        }

        @Override
        public void setDefaultSerializerProvider(JsonSerializerProvider provider, Class<?> rootType) {
            // Empty implementation
        }

        @Override
        public void setDefaultSerializerProvider(JsonSerializerProvider provider, JavaType valueType) {
            // Empty implementation
        }

        @Override
        public void setBeanDescription(BeanDescription description) {
            // Empty implementation
        }

        @Override
        public void reset() {
            // Empty implementation
        }
    }

}