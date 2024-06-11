package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.JsonSubType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructParametricType(AtomicReference.class, Object.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                return new AtomicReference<Object>();
            }
        };
        TypeDeserializer typeDeserializer = new StandardDeserializer<>(Object.class);
        JsonDeserializer<?> valueDeserializer = new StdDeserializer<Object>(Object.class) {
            @Override
            protected Object doDeserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);

        assertNotNull(deserializer);
    }

    @Test
    public void testWithResolved() {
        JavaType fullType = ObjectMapper.getDefaultInstance().getTypeFactory()
                .constructParametricType(AtomicReference.class, Object.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                return new AtomicReference<Object>();
            }
        };
        TypeDeserializer typeDeserializer = new StandardDeserializer<>(Object.class);
        JsonDeserializer<?> valueDeserializer = new StdDeserializer<Object>(Object.class) {
            @Override
            protected Object doDeserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        AtomicReferenceDeserializer deserializer1 = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);
        AtomicReferenceDeserializer deserializer2 = deserializer1.withResolved(typeDeserializer, valueDeserializer);

        assertNotSame(deserializer1, deserializer2);
        assertEquals(deserializer1.getFullType(), deserializer2.getFullType());
        assertEquals(deserializer1.getValueInstantiator(), deserializer2.getValueInstantiator());
        assertEquals(deserializer1.getTypeDeserializer(), deserializer2.getTypeDeserializer());
        assertEquals(deserializer1.getValueDeserializer(), deserializer2.getValueDeserializer());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationConfig().getContextInstance();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        AtomicReference<Object> actual = deserializer.getNullValue(ctxt);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        Object emptyValue = deserializer.getEmptyValue(null);
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        assertTrue(((AtomicReference) emptyValue).isEmpty());
    }

    @Test
    public void testReferenceValue() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationConfig().getContextInstance();

        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        AtomicReference<Object> actual = deserializer.referenceValue(contents);
        assertEquals(contents, actual.get());
    }

    @Test
    public void testGetReferenced() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationConfig().getContextInstance();

        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        Object actual = deserializer.getReferenced(reference);
        assertEquals(reference.get(), actual);
    }

    @Test
    public void testUpdateReference() {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationConfig().getContextInstance();

        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        Object newValue = new Object();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
        assertEquals(reference, updatedReference);
        assertEquals(newValue, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        assertTrue(deserializer.supportsUpdate(null));
    }

    @Test(expected = JsonMappingException.class)
    public void testSupportsUpdateWithInvalidConfig() {
        DeserializationConfig config = ObjectMapper.getDefaultInstance().getDeserializationConfig();
        config.withFeature(JsonParser.Feature.FAIL_ON_EMPTY_BEANS, false);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        assertFalse(deserializer.supportsUpdate(config));
    }

    @Test(expected = JsonProcessingException.class)
    public void testUpdateReferenceWithNullReference() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationConfig().getContextInstance();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        deserializer.updateReference(null, new Object());
    }

    @Test(expected = JsonProcessingException.class)
    public void testUpdateReferenceWithNullValue() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        DeserializationContext ctxt = mapper.getDeserializationConfig().getContextInstance();

        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                null,
                null);

        deserializer.updateReference(reference, null);
    }

    @Test
    public void testToStringSerializer() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void configure(SerializationConfig config, SerializerFactory serializers) throws JsonMappingException {
                serializers.findValueSerializer(Object.class, null).setSerializerSupplier(
                        new BeanSerializerFactory.BeanSerializerSupplier(
                                new ToStringSerializer(Object.class), true));
            }
        });

        JsonSerializer<AtomicReference<Object>> serializer = mapper.findValueSerializer(AtomicReference.class, Object.class);
        ObjectWriter ow = mapper.writer();

        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        String json = ow.writeValueAsString(reference);

        JsonParser p = mapper.parser();
        JsonNode tree = p.parse(json);

        AtomicReference<Object> deserializedReference = mapper.readValue(tree, AtomicReference.class);
        assertEquals(reference, deserializedReference);
    }

    @Test
    public void testTypeDeserializer() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                null,
                new StdTypeDeserializer<Object>(Object.class),
                null);

        ObjectMapper.DefaultTyping typedMapper = new ObjectMapper() {
            @Override
            public JavaType resolveType(DeserializationContext ctxt, BeanDescription beanDesc, JsonParser p)
                    throws JsonMappingException, JsonProcessingException {
                return beanDesc.getType();
            }
        };
        ObjectMapper typedMapperWithTypeDeserializer = new ObjectMapper();
        typedMapperWithTypeDeserializer.setDefaultTyping(ObjectMapper.DefaultTyping.STANDARD_OBJECT);
        typedMapperWithTypeDeserializer.registerDeserializer(AtomicReference.class, deserializer);

        JsonNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("type", new JsonValue("AtomicReference<String>"));
        node.put("value", new JsonValue("Hello, World!"));

        AtomicReference<String> expected = new AtomicReference<>("Hello, World!");

        Object actual = typedMapperWithTypeDeserializer.readValue(node, AtomicReference.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testJsonDeserializer() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        JsonDeserializer<Object> jsonDeserializer = new StdDeserializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, Object.class),
                new ValueInstantiator() {
                    @Override
                    public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                        return new AtomicReference<Object>(jsonDeserializer.deserialize(null, null));
                    }
                },
                null,
                jsonDeserializer);

        JsonNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("value", new JsonValue("Hello, World!"));

        AtomicReference<Object> expected = new AtomicReference<>("Hello, World!");

        Object actual = deserializer.deserialize(null, null, node);
        assertEquals(expected, actual);
    }

    @Test
    public void testJsonDeserializerWithSubType() throws JsonMappingException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        JsonDeserializer<Object> jsonDeserializer = new StdDeserializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String typeName = p.getText();
                if ("StringAtomicReference".equals(typeName)) {
                    return new AtomicReference<>("Hello, World!");
                } else {
                    throw new JsonMappingException(p, "Unknown subtype: [" + typeName + "]");
                }
            }
        };

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory()
                        .constructParametricType(AtomicReference.class, String.class),
                new ValueInstantiator() {
                    @Override
                    public Object createValueInstance(DeserializationContext ctxt) throws JsonProcessingException {
                        return new AtomicReference<>("");
                    }
                },
                null,
                jsonDeserializer);

        JsonNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        JsonNode subTypeNode = node.deepGet("__type");
        if (subTypeNode != null) {
            JsonNode subType = subTypeNode.deepGet("value");
            if (subType != null && subType.isTextual()) {
                node.set("__type", new JsonNodeFactory().objectNode()
                        .set("value", new JsonValue("AtomicReference<String>"))
                        .set("items", new JsonNodeFactory().arrayNode()
                                .add(new JsonSubType(AtomicReference.class, new TypeReference<AtomicReference<String>>() {}))));
            }
        }
        node.put("value", new JsonValue("Hello, World!"));

        AtomicReference<String> expected = new AtomicReference<>("Hello, World!");

        Object actual = deserializer.deserialize(null, null, node);
        assertEquals(expected, actual);
    }
}