package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

public class SettableAnyPropertyTest {

    private ObjectNode jsonNode;
    private DeserializationContext ctxt;

    @BeforeMethod
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        ctxt = mapper.deserializationContext();
        jsonNode = mapper.createObjectNode();
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(Foo.class, "anyProperty")),
                new AnnotatedField(Foo.class, "anyProperty"),
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class),
                null,
                null,
                null
        );

        property.deserializeAndSet(new JsonParser(jsonNode), ctxt, new Foo(), "anyProperty");
        assertNull(property.getProperty().get(new Foo()));
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializerPresent() throws Exception {
        JsonDeserializer<String> valueDeserializer = new StandardDeserializer<String>(String.class);
        SettableAnyProperty property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(Foo.class, "anyProperty")),
                new AnnotatedField(Foo.class, "anyProperty"),
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class),
                null,
                valueDeserializer,
                null
        );

        jsonNode.put("anyProperty", "test");
        Object value = property.deserialize(new JsonParser(jsonNode), ctxt);
        assertEquals("test", property.getProperty().get(new Foo()));
    }

    @Test
    public void testDeserializeAndSet_TypeDeserializerPresent() throws Exception {
        TypeDeserializer valueTypeDeserializer = new StdTypeDeserializer();
        SettableAnyProperty property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(Foo.class, "anyProperty")),
                new AnnotatedField(Foo.class, "anyProperty"),
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class),
                null,
                null,
                valueTypeDeserializer
        );

        JsonParser parser = new JsonParser(jsonNode);
        parser.nextToken(); // consume [
        parser.nextToken(); // consume "anyProperty":
        parser.nextToken(); // consume null
        parser.nextToken(); // consume "type":
        parser.nextValue("java.lang.Integer");
        parser.nextToken(); // consume "value":
        parser.nextValue(123);
        Object value = property.deserialize(parser, ctxt);
        assertSame(Integer.class, property.getType());
        assertEquals(123, property.getProperty().get(new Foo()));
    }

    @Test
    public void testDeserializeAndSet_KeyDeserializerPresent() throws Exception {
        KeyDeserializer keyDeserializer = new StandardKeyDeserializer();
        SettableAnyProperty property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(Foo.class, "anyProperty")),
                new AnnotatedField(Foo.class, "anyProperty"),
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class),
                keyDeserializer,
                null,
                null
        );

        jsonNode.put("anyProperty", Object.class, "testKey");
        Object key = property.deserializeKey(jsonNode, ctxt);
        assertEquals("testKey", property.getProperty().get(new Foo())
                .get(key));
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeAndSet_FailedDeserialization() throws Exception {
        SettableAnyProperty property = new SettableAnyProperty(
                new BeanProperty(new AnnotatedField(Foo.class, "anyProperty")),
                new AnnotatedField(Foo.class, "anyProperty"),
                ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Foo.class),
                null,
                null,
                null
        );

        jsonNode.put("anyProperty", "not a valid json");
        property.deserializeAndSet(new JsonParser(jsonNode), ctxt, new Foo(), "anyProperty");
    }
}

class Foo {
    private Map<Object, Object> map = new HashMap<>();

    public Map<Object, Object> getMap() {
        return map;
    }
}