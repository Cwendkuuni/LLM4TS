package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @JsonId
            public Class<?> idType() {
                return Long.class;
            }

            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdDeserializer<Long>(Long.class) {};
            }

            public SettableBeanProperty idProperty;
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertNull(property.getIdProperty());

        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("id", 1L);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser(node.toString());

        Object instance = new Object() {};

        property.deserializeAndSet(parser, null, instance);
        assertEquals(1L, instance.getClass().getDeclaredField("id").get(instance));

        property.set(instance, 2L);
        assertEquals(2L, instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testObjectIdValuePropertyWithIdProperty() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @JsonId
            public Class<?> idType() {
                return Long.class;
            }

            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdDeserializer<Long>(Long.class) {};
            }

            public SettableBeanProperty idProperty = new SettableBeanProperty(
                    new PropertyName("name"), new StdSerializer<String>(String.class) {}, null);
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("id", 1L);

        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory()
                .createParser(node.toString());

        Object instance = new Object() {};

        property.deserializeAndSet(parser, null, instance);
        assertEquals(1L, instance.getClass().getDeclaredField("id").get(instance));
        assertEquals("", instance.getClass().getDeclaredField("name").get(instance));

        property.set(instance, 2L);
        assertEquals(2L, instance.getClass().getDeclaredField("id").get(instance));
        assertEquals("", instance.getClass().getDeclaredField("name").get(instance));

        ObjectNode node2 = ObjectMapper.getDefaultInstance().createObjectNode();
        node2.put("id", 1L);
        node2.put("name", "test");

        JsonParser parser2 = ObjectMapper.getDefaultInstance().getFactory()
                .createParser(node2.toString());

        property.deserializeAndSet(parser2, null, instance);
        assertEquals(1L, instance.getClass().getDeclaredField("id").get(instance));
        assertEquals("test", instance.getClass().getDeclaredField("name").get(instance));
    }

    @Test
    public void testObjectIdValuePropertySerializer() throws Exception {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @JsonId
            public Class<?> idType() {
                return Long.class;
            }

            public JsonDeserializer<Object> getIdDeserializer() {
                return new StdDeserializer<Long>(Long.class) {};
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object() {};
        instance.getClass().getDeclaredField("id").setAccessible(true);
        instance.getClass().getDeclaredField("name").setAccessible(true);
        instance.getClass().getDeclaredField("id").set(instance, 1L);
        instance.getClass().getDeclaredField("name").set(instance, "test");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll();
        NameTransformer transformer = new NameTransformer() {};
        ObjectMapper mapper = ObjectMapper.getDefaultInstance()
                .setSerializerFactory(new DefaultSerializerFactory())
                .setPropertyFilter(filter)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializer(property.getName(), ToStringSerializer.instance)
                .setSerializer(property.getName() + ".class", ToStringSerializer.instance);

        ObjectNode rootNode = mapper.readValue(ObjectMapper.getDefaultInstance()
                .getFactory().createParser(new ByteArrayInputStream(new byte[]{}))
                .nextValue().toString().getBytes(), ObjectNode.class);
        ObjectNode node = mapper.valueToTree(instance);
        node.put("name", node.get("id").getValueAsString());
        node.remove("id");

        ObjectNode expectedNode = mapper.createObjectNode();
        expectedNode.put("id", ObjectMapper.getDefaultInstance().writeValueAsString(instance.getId()));
        expectedNode.set("name", node.get("name"));

        assertEquals(expectedNode, rootNode);
    }
}