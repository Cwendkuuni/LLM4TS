package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * JavaUtilCollectionsDeserializersTest is a JUnit test file for the JavaUtilCollectionsDeserializers class.
 */
public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForCollection(DeserializationContext, JavaType)}.
     * Test case for singleton Set.
     */
    @Test
    public void testFindForCollectionSingletonSet() throws Exception {
        JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Collections.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeSet);

        JsonNode jsonNodeSet = new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET.getName())
                .put("value", new Boolean(true));
        JsonNode jsonNodeExpectedSet = new ObjectNode(objectMapper.getFactory()).put("value", new Boolean(true));

        Object deserializedSet = objectMapper.readValue(jsonNodeSet, Object.class);
        ObjectMapper expectedMapper = new ObjectMapper();
        ObjectMapper expectedMapper = new ObjectMapper();
        expectedMapper.registerValueHandlerForTypes(Set.class, new StdSerializer<Set<Boolean>>(HashSet.class) {
            @Override
            public Set<Boolean> getEmptyValue(SerializerProvider serializers) {
                return new HashSet<Boolean>();
            }
        });
        Object actualSet = expectedMapper.readValue(jsonNodeExpectedSet, Set.class);

        assertEquals(deserializer.getClass().getSimpleName(), "StdDelegatingDeserializer");
        assertEquals(actualSet, deserializedSet);
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForCollection(DeserializationContext, JavaType)}.
     * Test case for unmodifiable Set.
     */
    @Test
    public void testFindForCollectionUnmodifiableSet() throws Exception {
        JavaType javaTypeUnmodifiableSet = TypeFactory.defaultInstance().constructType(Collections.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeUnmodifiableSet);

        JsonNode jsonNodeUnmodifiableSet = new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET.getName())
                .put("value", new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_SET.getName())
                        .put("value", new Boolean(true)));
        JsonNode jsonNodeExpectedUnmodifiableSet = new ObjectNode(objectMapper.getFactory())
                .put("value", new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_SET.getName())
                        .put("value", new Boolean(true)));

        Object deserializedUnmodifiableSet = objectMapper.readValue(jsonNodeUnmodifiableSet, Object.class);
        ObjectMapper expectedMapper = new ObjectMapper();
        ObjectMapper expectedMapper = new ObjectMapper();
        expectedMapper.registerValueHandlerForTypes(Set.class, new StdSerializer<Set<Boolean>>(HashSet.class) {
            @Override
            public Set<Boolean> getEmptyValue(SerializerProvider serializers) {
                return new HashSet<Boolean>(Collections.singleton(new Boolean(true)));
            }
        });
        Object actualUnmodifiableSet = expectedMapper.readValue(jsonNodeExpectedUnmodifiableSet, Set.class);

        assertEquals(deserializer.getClass().getSimpleName(), "StdDelegatingDeserializer");
        assertEquals(actualUnmodifiableSet, deserializedUnmodifiableSet);
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForCollection(DeserializationContext, JavaType)}.
     * Test case for singleton List.
     */
    @Test
    public void testFindForCollectionSingletonList() throws Exception {
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(Collections.class, Boolean.TYPE, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeList);

        JsonNode jsonNodeList = new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST.getName())
                .put("value", new ArrayNode(objectMapper.getFactory()).add(new Boolean(true))
                        .add(new Boolean(true)));
        JsonNode jsonNodeExpectedList = new ObjectNode(objectMapper.getFactory()).put("value", new ArrayNode(objectMapper.getFactory())
                .add(new Boolean(true))
                .add(new Boolean(true)));

        Object deserializedList = objectMapper.readValue(jsonNodeList, Object.class);
        ObjectMapper expectedMapper = new ObjectMapper();
        ObjectMapper expectedMapper = new ObjectMapper();
        expectedMapper.registerValueHandlerForTypes(List.class, new StdSerializer<List<Boolean>>(ArrayList.class) {
            @Override
            public List<Boolean> getEmptyValue(SerializerProvider serializers) {
                return new ArrayList<Boolean>();
            }
        });
        Object actualList = expectedMapper.readValue(jsonNodeExpectedList, List.class);

        assertEquals(deserializer.getClass().getSimpleName(), "StdDelegatingDeserializer");
        assertEquals(actualList, deserializedList);
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForCollection(DeserializationContext, JavaType)}.
     * Test case for unmodifiable List.
     */
    @Test
    public void testFindForCollectionUnmodifiableList() throws Exception {
        JavaType javaTypeUnmodifiableList = TypeFactory.defaultInstance().constructType(Collections.class, Boolean.TYPE, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaTypeUnmodifiableList);

        JsonNode jsonNodeUnmodifiableList = new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST.getName())
                .put("value", new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_LIST.getName())
                        .put("value", new ArrayNode(objectMapper.getFactory()).add(new Boolean(true))
                                .add(new Boolean(true))));

        JsonNode jsonNodeExpectedUnmodifiableList = new ObjectNode(objectMapper.getFactory())
                .put("value", new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_LIST.getName())
                        .put("value", new ArrayNode(objectMapper.getFactory())
                                .add(new Boolean(true))
                                .add(new Boolean(true))));

        Object deserializedUnmodifiableList = objectMapper.readValue(jsonNodeUnmodifiableList, Object.class);
        ObjectMapper expectedMapper = new ObjectMapper();
        ObjectMapper expectedMapper = new ObjectMapper();
        expectedMapper.registerValueHandlerForTypes(List.class, new StdSerializer<List<Boolean>>(ArrayList.class) {
            @Override
            public List<Boolean> getEmptyValue(SerializerProvider serializers) {
                return new ArrayList<Boolean>(Collections.singletonList(new Boolean(true)));
            }
        });
        Object actualUnmodifiableList = expectedMapper.readValue(jsonNodeExpectedUnmodifiableList, List.class);

        assertEquals(deserializer.getClass().getSimpleName(), "StdDelegatingDeserializer");
        assertEquals(actualUnmodifiableList, deserializedUnmodifiableList);
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForMap(DeserializationContext, JavaType)}.
     * Test case for singleton Map.
     */
    @Test
    public void testFindForMapSingletonMap() throws Exception {
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Collections.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaTypeMap);

        JsonNode jsonNodeMap = new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP.getName())
                .put("value", new ObjectNode(objectMapper.getFactory()).put("key", "a")
                        .put("value", new ObjectNode(objectMapper.getFactory()).put("value", "b")));
        JsonNode jsonNodeExpectedMap = new ObjectNode(objectMapper.getFactory()).put("value", new ObjectNode(objectMapper.getFactory())
                .put("key", "a")
                .put("value", "b"));

        Object deserializedMap = objectMapper.readValue(jsonNodeMap, Object.class);
        ObjectMapper expectedMapper = new ObjectMapper();
        ObjectMapper expectedMapper = new ObjectMapper();
        expectedMapper.registerValueHandlerForTypes(Map.class, new StdSerializer<Map<String, String>>(HashMap.class) {
            @Override
            public Map<String, String> getEmptyValue(SerializerProvider serializers) {
                return new HashMap<String, String>();
            }
        });
        Object actualMap = expectedMapper.readValue(jsonNodeExpectedMap, Map.class);

        assertEquals(deserializer.getClass().getSimpleName(), "StdDelegatingDeserializer");
        assertEquals(actualMap, deserializedMap);
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#findForMap(DeserializationContext, JavaType)}.
     * Test case for unmodifiable Map.
     */
    @Test
    public void testFindForMapUnmodifiableMap() throws Exception {
        JavaType javaTypeUnmodifiableMap = TypeFactory.defaultInstance().constructType(Collections.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaTypeUnmodifiableMap);

        JsonNode jsonNodeUnmodifiableMap = new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP.getName())
                .put("value", new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_SINGLETON_MAP.getName())
                        .put("value", new ObjectNode(objectMapper.getFactory()).put("key", "a")
                                .put("value", "b")));

        JsonNode jsonNodeExpectedUnmodifiableMap = new ObjectNode(objectMapper.getFactory())
                .put("value", new ObjectNode(objectMapper.getFactory()).put("class", JavaUtilCollectionsDeserializers.CLASS_UNMODIFIABLE_MAP.getName())
                        .put("value", new ObjectNode(objectMapper.getFactory()).put("key", "a")
                                .put("value", "b")));

        Object deserializedUnmodifiableMap = objectMapper.readValue(jsonNodeUnmodifiableMap, Object.class);
        ObjectMapper expectedMapper = new ObjectMapper();
        ObjectMapper expectedMapper = new ObjectMapper();
        expectedMapper.registerValueHandlerForTypes(Map.class, new StdSerializer<Map<String, String>>(HashMap.class) {
            @Override
            public Map<String, String> getEmptyValue(SerializerProvider serializers) {
                return Collections.unmodifiableMap(new HashMap<String, String>() {{
                    put("a", "b");
                }});
            }
        });
        Object actualUnmodifiableMap = expectedMapper.readValue(jsonNodeExpectedUnmodifiableMap, Map.class);

        assertEquals(deserializer.getClass().getSimpleName(), "StdDelegatingDeserializer");
        assertEquals(actualUnmodifiableMap, deserializedUnmodifiableMap);
    }

    /**
     * Test method for {@link JavaUtilCollectionsDeserializers#converter(int, JavaType, Class<>)}.
     */
    @Test
    public void testConverter() throws Exception {
        JavaType javaTypeSet = TypeFactory.defaultInstance().constructType(Collections.class, Boolean.TYPE);
        JavaType javaTypeList = TypeFactory.defaultInstance().constructType(Collections.class, Boolean.TYPE, Boolean.TYPE);
        JavaType javaTypeMap = TypeFactory.defaultInstance().constructType(Collections.class, String.class, String.class);

        JavaUtilCollectionsConverter converterSingletonSet = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, javaTypeSet, Set.class);
        JavaUtilCollectionsConverter converterSingletonList = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, javaTypeList, List.class);
        JavaUtilCollectionsConverter converterSingletonMap = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, javaTypeMap, Map.class);

        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, converterSingletonSet._kind);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, converterSingletonList._kind);
        assertEquals(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, converterSingletonMap._kind);
    }
}