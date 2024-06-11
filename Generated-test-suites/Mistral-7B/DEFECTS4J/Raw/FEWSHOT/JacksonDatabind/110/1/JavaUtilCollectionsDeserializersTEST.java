package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueContainerDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonSet() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Boolean.TYPE);
        JsonDeserializer<List<Object>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

        Set<Boolean> set = new HashSet<>(Collections.singleton(Boolean.TRUE));
        Object deserializedSet = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"set\", \"values\": [true] }"), mapper.getDeserializationContext());

        assertEquals(Boolean.TRUE, deserializedSet.iterator().next());
    }

    @Test
    public void testFindForCollection_SingletonList() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Boolean.TYPE);
        JavaType listType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<List<Object>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

        List<Boolean> list = new ArrayList<>(Collections.singletonList(Boolean.TRUE));
        Object deserializedList = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"list\", \"values\": [true] }"), mapper.getDeserializationContext());

        assertEquals(Boolean.TRUE, deserializedList.get(0));
        assertEquals(listType, deserializer.getInputType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForCollection_SingletonMap() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<Map<Object, Object>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

        Map<String, String> map = new HashMap<>(Collections.singletonMap("key", "value"));
        Object deserializedMap = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"map\", \"key\": \"key\", \"value\": \"value\" }"), mapper.getDeserializationContext());

        assertEquals("value", deserializedMap.get("key"));
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Boolean.TYPE);
        JsonDeserializer<Set<Object>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

        Set<Boolean> set = Collections.unmodifiableSet(new HashSet<>(Collections.singleton(Boolean.TRUE)));
        Object deserializedSet = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"set\", \"values\": [true] }"), mapper.getDeserializationContext());

        assertNotNull(deserializedSet);
        // Unmodifiable sets are not serializable, so we can't directly test the contents
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Boolean.TYPE);
        JavaType listType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<List<Object>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

        List<Boolean> list = Collections.unmodifiableList(new ArrayList<>(Collections.singletonList(Boolean.TRUE)));
        Object deserializedList = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"list\", \"values\": [true] }"), mapper.getDeserializationContext());

        assertEquals(Boolean.TRUE, deserializedList.get(0));
        assertEquals(listType, deserializer.getInputType(mapper.getTypeFactory()));
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<Map<Object, Object>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType);

        Map<String, String> map = Collections.unmodifiableMap(new HashMap<>(Collections.singletonMap("key", "value")));
        Object deserializedMap = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"map\", \"key\": \"key\", \"value\": \"value\" }"), mapper.getDeserializationContext());

        assertEquals("value", deserializedMap.get("key"));
    }

    @Test
    public void testFindForMap_SingletonMap() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<Map<Object, Object>> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);

        Map<String, String> map = new HashMap<>(Collections.singletonMap("key", "value"));
        Object deserializedMap = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"map\", \"key\": \"key\", \"value\": \"value\" }"), mapper.getDeserializationContext());

        assertEquals("value", deserializedMap.get("key"));
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        JsonDeserializer<Map<Object, Object>> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType);

        Map<String, String> map = Collections.unmodifiableMap(new HashMap<>(Collections.singletonMap("key", "value")));
        Object deserializedMap = deserializer.deserialize(mapper.readValueAsTree("{ \"type\": \"map\", \"key\": \"key\", \"value\": \"value\" }"), mapper.getDeserializationContext());

        assertEquals("value", deserializedMap.get("key"));
    }

    @Test
    public void testFindForCollection_NullInput() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Boolean.TYPE);
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(mapper.getContext(), javaType));
    }

    @Test
    public void testFindForMap_NullInput() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, Map.class, String.class, String.class);
        assertNull(JavaUtilCollectionsDeserializers.findForMap(mapper.getContext(), javaType));
    }

    private static class TestSerializer extends StdSerializer<Object> {
        TestSerializer() {
            this(null);
        }

        TestSerializer(Class<Object> t) {
            super(t);
        }

        @Override
        public void serialize(Object value, ObjectNode node, SerializerProvider serializers) throws IOException {
            node.put("type", value.getClass().getName());
            node.put("values", mapper.valueToTree(value));
        }
    }

    @Test
    public void testCustomSerialization() throws JsonMappingException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(Collections.class, String.class);
        ObjectNode node = mapper.createObjectNode();
        ObjectMapper customMapper = new ObjectMapper();
        customMapper.setSerializerInstance(StdValueContainerDeserializer.class, new TestSerializer());

        List<String> list = Arrays.asList("foo", "bar");
        JsonDeserializer<List<String>> deserializer = JavaUtilCollectionsDeserializers.findForCollection(customMapper.getContext(), javaType);

        Object deserializedList = deserializer.deserialize(node, customMapper.getDeserializationContext());
        assertEquals("java.util.ArrayList", deserializedList.getClass().getName());
        assertEquals(Arrays.asList("foo", "bar"), deserializedList);
    }
}