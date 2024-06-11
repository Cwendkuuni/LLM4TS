package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToType;
import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JavaType singleSetType = mapper.getTypeFactory().constructType(Set.class, Boolean.TYPE);
        JavaType singletonSetType = mapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET);
        JavaType unmodifiableSetType = mapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singleSetType);
        assertEquals(Set.class, deserializer.getInputType(mapper.getTypeFactory()));

        ObjectNode input = mapper.createObjectNode();
        input.put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET.getName());
        input.put("value", mapper.valueToTree(Collections.singleton(Boolean.TRUE)));

        Object result = mapper.readValue(input.toString(), mapper.getTypeFactory().constructType(List.class));
        assertEquals(Arrays.asList(Boolean.TRUE), result);

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singletonSetType);
        assertEquals(Set.class, deserializer.getInputType(mapper.getTypeFactory()));

        result = mapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET);
        assertEquals(Collections.singleton(Boolean.TRUE), result);

        result = mapper.readValue(input.toString(), unmodifiableSetType);
        assertTrue(result instanceof Collections.unmodifiableSet);
        assertEquals(Collections.singleton(Boolean.TRUE), result);

        // test null input
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), null);
        assertNull(deserializer);

        JavaType singleListType = mapper.getTypeFactory().constructType(List.class, Boolean.TYPE);
        JavaType singletonListType = mapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST);
        JavaType unmodifiableListType = mapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST);

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singleListType);
        assertEquals(List.class, deserializer.getInputType(mapper.getTypeFactory()));

        input = mapper.createObjectNode();
        input.put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST.getName());
        input.put("value", mapper.valueToTree(Collections.singletonList(Boolean.TRUE)));

        result = mapper.readValue(input.toString(), mapper.getTypeFactory().constructType(List.class));
        assertEquals(Arrays.asList(Boolean.TRUE), result);

        result = mapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST);
        assertEquals(Collections.singletonList(Boolean.TRUE), result);

        result = mapper.readValue(input.toString(), unmodifiableListType);
        assertTrue(result instanceof Collections.unmodifiableList);
        assertEquals(Collections.singletonList(Boolean.TRUE), result);

        // test empty list
        JavaType emptyListType = mapper.getTypeFactory().constructType(List.class, String.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), emptyListType);
        result = mapper.readValue(mapper.createObjectNode().toString(), emptyListType);
        assertEquals(Collections.emptyList(), result);

        // test unsupported type
        JavaType unsupportedType = mapper.getTypeFactory().constructType(List.class, Integer.TYPE);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), unsupportedType);
        assertNull(deserializer);

        // test custom deserializer
        class CustomListDeserializer extends StdDeserializer<List<?>> {
            @Override
            public List<Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return Arrays.asList("foo", "bar");
            }
        }
        JavaType customListType = mapper.getTypeFactory().constructType(List.class, String.class);
        mapper.registerDeserializer(customListType, new CustomListDeserializer());

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), customListType);
        result = mapper.readValue(mapper.createObjectNode().toString(), customListType);
        assertEquals(Arrays.asList("foo", "bar"), result);

        // test custom deserializer with "findForCollection"
        mapper.registerDeserializer(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, new CustomListDeserializer());
        result = mapper.readValue(input.toString(), JavaUtilCollectionsDeserializers.TYPE_AS_LIST);
        assertEquals(Arrays.asList("foo", "bar"), result);

        // test findForCollection with Object input
        Object inputObject = new Object() {};
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), singleSetType);
        result = mapper.readValue(mapper.valueToTree(inputObject), singleSetType);
        assertNull(result);

        // test findForCollection with non-Collection input
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), String.class);
        assertNull(deserializer);

        // test findForMap
        JavaType singleMapType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.TYPE);
        JavaType singletonMapType = mapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP);
        JavaType unmodifiableMapType = mapper.getTypeFactory().constructType(JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP);

        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), singleMapType);
        assertEquals(Map.class, deserializer.getInputType(mapper.getTypeFactory()));

        ObjectNode inputMap = mapper.createObjectNode();
        inputMap.put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP.getName());
        inputMap.put("value", mapper.valueToTree(Collections.singletonMap("foo", 1)));

        result = mapper.readValue(inputMap.toString(), mapper.getTypeFactory().constructType(Map.class));
        assertEquals(Collections.singletonMap("foo", 1), result);

        result = mapper.readValue(inputMap.toString(), JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP);
        assertEquals(Collections.singletonMap("foo", 1), result);

        result = mapper.readValue(inputMap.toString(), unmodifiableMapType);
        assertTrue(result instanceof Collections.unmodifiableMap);
        assertEquals(Collections.singletonMap("foo", 1), result);

        // test null input
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), null);
        assertNull(deserializer);

        // test empty map
        JavaType emptyMapType = mapper.getTypeFactory().constructType(Map.class, Integer.TYPE, Integer.TYPE);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), emptyMapType);
        result = mapper.readValue(mapper.createObjectNode().toString(), emptyMapType);
        assertEquals(Collections.emptyMap(), result);

        // test unsupported type
        JavaType unsupportedMapType = mapper.getTypeFactory().constructType(Map.class, Double.TYPE, String.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), unsupportedMapType);
        assertNull(deserializer);

        // test custom deserializer
        class CustomMapDeserializer extends StdDeserializer<Map<?,?>> {
            @JsonCreator
            public Map<String, Integer> createMap(Map<String, ? extends Number> value) {
                return new HashMap<String, Integer>() {{
                    putAll(value);
                }};
            }

            @Override
            public Map<String, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                Map<String, Number> map = p.readValueAs(Map.class);
                Map<String, Integer> result = new HashMap<>();
                for (Map.Entry<String, Number> entry : map.entrySet()) {
                    result.put(entry.getKey(), entry.getValue().intValue());
                }
                return result;
            }
        }
        JavaType customMapType = mapper.getTypeFactory().constructType(Map.class, String.class, Integer.TYPE);
        mapper.registerDeserializer(customMapType, new CustomMapDeserializer());

        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), customMapType);
        result = mapper.readValue(mapper.createObjectNode().toString(), customMapType);
        assertEquals(new HashMap<String, Integer>() {{
            put("foo", 1);
            put("bar", 2);
        }}, result);

        // test findForMap with Object input
        Object inputMapObject = new Object() {};
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), singleMapType);
        result = mapper.readValue(mapper.valueToTree(inputMapObject), singleMapType);
        assertNull(result);

        // test findForMap with non-Map input
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), Integer.TYPE);
        assertNull(deserializer);

        // test findForMap with Collection input
        JavaType singleListType2 = mapper.getTypeFactory().constructType(List.class, String.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), singleListType2);
        assertNull(deserializer);

        // test findForMap with Array input
        JavaType singleArrayType = mapper.getTypeFactory().constructType(String[].class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), singleArrayType);
        assertNull(deserializer);

        // test findForMap with null Array input
        deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), null);
        assertNull(deserializer);
    }
}