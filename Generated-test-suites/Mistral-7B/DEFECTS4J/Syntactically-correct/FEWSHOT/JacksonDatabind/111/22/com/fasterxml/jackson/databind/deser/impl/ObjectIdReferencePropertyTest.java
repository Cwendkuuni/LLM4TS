package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleValueProperty;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.Arrays;

public class ObjectIdReferencePropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static <T> ObjectIdReferenceProperty<T> createProperty(ObjectIdInfo objectIdInfo, JsonDeserializer<T> deserializer) {
        return new ObjectIdReferenceProperty<>(new SimpleValueProperty.Std(null), objectIdInfo, deserializer, null);
    }

    @Test
    public void testBasic() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.PropertyGenerator.instance(), "id");
        ObjectIdReferenceProperty<String> property = createProperty(objectIdInfo, new StdSerializer<String>(String.class) {});

        String id = "id123";
        ObjectNode node = mapper().createObjectNode();
        node.put("id", id);

        JsonDeserializer<ObjectNode> nodeDeserializer = new JsonDeserializer<ObjectNode>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return node;
            }
        };

        ObjectNode instance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(node), ObjectNode.class);
        Object result = property.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(instance)), null, null);

        assertEquals(id, result);
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.PropertyGenerator.instance(), "id");
        ObjectIdReferenceProperty<String> property = createProperty(objectIdInfo, new StdSerializer<String>(String.class) {});

        ObjectIdReferenceProperty<String> propertyWithDeserializer = new ObjectIdReferenceProperty<>(property, new StdSerializer<String>(String.class) {});

        String id = "id123";
        ObjectNode node = mapper().createObjectNode();
        node.put("id", id);

        JsonDeserializer<ObjectNode> nodeDeserializer = new JsonDeserializer<ObjectNode>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return node;
            }
        };

        ObjectNode instance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(node), ObjectNode.class);
        Object result = propertyWithDeserializer.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(instance)), null, null);

        assertEquals(id, result);

        JsonDeserializer<String> stringDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return "foo";
            }
        };

        ObjectIdReferenceProperty<String> propertyWithCustomDeserializer = new ObjectIdReferenceProperty<>(propertyWithDeserializer, stringDeserializer);

        String inputId = "id123";
        ObjectNode inputNode = mapper().createObjectNode();
        inputNode.put("id", inputId);

        Object resultWithCustomDeserializer = propertyWithCustomDeserializer.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(inputNode)), null, null);

        assertEquals("foo", resultWithCustomDeserializer);
    }

    @Test
    public void testWithNullProvider() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.PropertyGenerator.instance(), "id");
        ObjectIdReferenceProperty<String> property = createProperty(objectIdInfo, new StdSerializer<String>(String.class) {});

        ObjectIdReferenceProperty<String> propertyWithNullProvider = new ObjectIdReferenceProperty<>(property, null, new NullValueProvider() {});

        Object result = propertyWithNullProvider.deserializeSetAndReturn(null, null, null);

        assertNull(result);
    }

    @Test
    public void testWithAnnotations() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.PropertyGenerator.instance(), "id");

        JsonIdentityInfo annotation = new JsonIdentityInfo(ObjectIdGenerators.IntArrayIdGenerator.instance(), "@com.example.IdGenerator");

        ObjectIdReferenceProperty<String> property = createProperty(objectIdInfo, new StdSerializer<String>(String.class) {});
        ObjectIdReferenceProperty<String> propertyWithAnnotation = new ObjectIdReferenceProperty<>(property, null, null, annotation);

        ObjectNode node = mapper().createObjectNode();
        node.put("id", Arrays.asList(new Long[] {1L, 2L, 3L}));

        JsonDeserializer<ObjectNode> nodeDeserializer = new JsonDeserializer<ObjectNode>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return node;
            }
        };

        ObjectNode instance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(node), ObjectNode.class);
        Object result = propertyWithAnnotation.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(instance)), null, null);

        assertEquals(Arrays.asList(new Long[] {1L, 2L, 3L}), result);
    }

    @Test
    public void testForwardReference() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.PropertyGenerator.instance(), "id");

        ObjectIdReferenceProperty<String> stringProperty = createProperty(objectIdInfo, new StdSerializer<String>(String.class) {});
        ObjectIdReferenceProperty<Integer> intProperty = createProperty(objectIdInfo, new StdSerializer<Integer>(Integer.class) {});

        ObjectIdReferenceProperty<String> stringPropertyWithIntReference = new ObjectIdReferenceProperty<>(stringProperty, intProperty, null);
        ObjectIdReferenceProperty<Integer> intPropertyWithStringReference = new ObjectIdReferenceProperty<>(intProperty, stringProperty, null);

        ObjectNode stringNode = mapper().createObjectNode();
        stringNode.put("id", "stringId");

        ObjectNode intNode = mapper().createObjectNode();
        intNode.put("id", 123);

        JsonDeserializer<ObjectNode> nodeDeserializer = new JsonDeserializer<ObjectNode>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return node;
            }
        };

        ObjectNode stringInstance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(stringNode), ObjectNode.class);
        ObjectNode intInstance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(intNode), ObjectNode.class);

        Object resultString = stringPropertyWithIntReference.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(stringInstance)), null, intInstance);
        Object resultInt = intPropertyWithStringReference.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(intInstance)), null, stringInstance);

        assertEquals("stringId", resultString);
        assertEquals(123, resultInt);
    }

    @Test
    public void testForwardReferenceResolution() throws IOException {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(ObjectIdGenerators.PropertyGenerator.instance(), "id");

        ObjectIdReferenceProperty<String> stringProperty = createProperty(objectIdInfo, new StdSerializer<String>(String.class) {});
        ObjectIdReferenceProperty<Integer> intProperty = createProperty(objectIdInfo, new StdSerializer<Integer>(Integer.class) {});

        ObjectIdReferenceProperty<String> stringPropertyWithIntReference = new ObjectIdReferenceProperty<>(stringProperty, intProperty, null);
        ObjectIdReferenceProperty<Integer> intPropertyWithStringReference = new ObjectIdReferenceProperty<>(intProperty, stringProperty, null);

        ObjectNode stringNode = mapper().createObjectNode();
        stringNode.put("id", "stringId");

        ObjectNode intNode = mapper().createObjectNode();
        intNode.put("id", 123);

        JsonDeserializer<ObjectNode> nodeDeserializer = new JsonDeserializer<ObjectNode>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return node;
            }
        };

        ObjectNode stringInstance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(stringNode), ObjectNode.class);
        ObjectNode intInstance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(intNode), ObjectNode.class);

        Object resultString = stringPropertyWithIntReference.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(stringInstance)), null, intInstance);
        Object resultInt = intPropertyWithStringReference.deserializeAndSet(mapper().createParser(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(intInstance)), null, stringInstance);

        Object idInstance = mapper().readValue(mapper().writerWithDefaultPrettyPrinter().writeValueAsString(mapper().createObjectNode()), Object.class);

        assertEquals("stringId", resultString);
        assertEquals(123, resultInt);

        assertEquals(intInstance, intPropertyWithStringReference.getReferring());
        assertEquals(stringInstance, stringPropertyWithIntReference.getReferring());
        assertEquals(intProperty, intPropertyWithStringReference._forward);
        assertEquals(stringProperty, stringPropertyWithIntReference._forward);
    }
}