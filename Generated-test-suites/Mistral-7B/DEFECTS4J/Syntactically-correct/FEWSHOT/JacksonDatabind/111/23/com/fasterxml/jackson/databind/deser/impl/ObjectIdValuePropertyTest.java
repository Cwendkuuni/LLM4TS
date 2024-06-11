package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = new ObjectIdReader() {
        @Override
        public PropertyName propertyName() {
            return PropertyName.construct("id");
        }

        @Override
        public Class<? extends IdType> idType() {
            return LongId.class;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return ObjectIdGenerators.IntSequenceGenerator.instance();
        }

        @Override
        public JsonDeserializer<ObjectId> idDeserializer() {
            return new StdDeserializer<ObjectId>(LongId.class) {};
        }

        @Override
        public SettableBeanProperty idProperty() {
            return new SimpleValueProperty.Std("idProperty");
        }
    };

    @Test
    public void testObjectIdValueProperty() {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        assertEquals("id", objectIdValueProperty.getName());
        assertEquals(LongId.class, objectIdValueProperty.getIdType());
        assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
        assertNull(objectIdValueProperty.getMember());

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"));
        assertEquals("newName", src.getName());

        JsonDeserializer<Object> jsonDeserializer = new StdDeserializer<Object>(Object.class) {};
        ObjectIdValueProperty deserializerObject = new ObjectIdValueProperty(objectIdValueProperty, jsonDeserializer, null);
        assertEquals(jsonDeserializer, deserializerObject.getDeserializer());

        NullValueProvider nullValueProvider = new NullValueProvider() {
            @Override
            public Object nullValue(DeserializationContext ctxt) {
                return null;
            }
        };
        ObjectIdValueProperty nullProviderObject = new ObjectIdValueProperty(objectIdValueProperty, null, nullValueProvider);
        assertEquals(nullValueProvider, nullProviderObject.getNullProvider());

        ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        JsonParser jsonParser = new SimpleModule().builder().addDeserializer(ObjectIdValueProperty.class, new ObjectIdValueProperty.Deserializer()).build().readerFor(ObjectNode.class).getParser();
        jsonParser.readValueAs(Object.class, jsonNode, null);
        ObjectIdValueProperty jsonObject = (ObjectIdValueProperty) jsonParser.getValueAs(ObjectIdValueProperty.class, null);
        assertEquals(objectIdValueProperty, jsonObject);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<? extends IdType> idType() {
                return LongId.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return ObjectIdGenerators.IntSequenceGenerator.instance();
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return new StdDeserializer<ObjectId>(LongId.class) {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SimpleValueProperty.Std("idProperty");
            }
        };
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> jsonDeserializer = new StdDeserializer<Object>(Object.class) {};
        ObjectIdValueProperty deserializerObject = new ObjectIdValueProperty(objectIdValueProperty, jsonDeserializer, null);

        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("id", 1L);
        Object id = deserializerObject.deserializeAndSet(jsonParserFromNode(jsonNode), new DeserializationContext(), new Object());
        assertEquals(1L, id);
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return PropertyName.construct("id");
            }

            @Override
            public Class<? extends IdType> idType() {
                return LongId.class;
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return ObjectIdGenerators.IntSequenceGenerator.instance();
            }

            @Override
            public JsonDeserializer<ObjectId> idDeserializer() {
                return new StdDeserializer<ObjectId>(LongId.class) {};
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new SimpleValueProperty.Std("idProperty");
            }
        };
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        JsonDeserializer<Object> jsonDeserializer = new StdDeserializer<Object>(Object.class) {};
        ObjectIdValueProperty deserializerObject = new ObjectIdValueProperty(objectIdValueProperty, jsonDeserializer, null);
        Object obj = new Object();
        deserializerObject.set(obj, 1L);
        assertEquals(1L, deserializerObject.get(obj));
    }

    private JsonParser jsonParserFromNode(ObjectNode jsonNode) {
        JsonFactory jsonFactory = new JsonFactory();
        return jsonFactory.createParser(jsonNode.traverse());
    }
}