package com.fasterxml.jackson.databind.deser.impl.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // Register conversion 
        SimpleModule module = new SimpleModule();
        module.addConverter(new JavaUtilCollectionsConverterTest());
        MAPPER.registerModule(module);
        MAPPER.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        MAPPER.enable(DeserializationFeature.FAIL_ON_empty_BEANS);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setSerializerProvider(MAPPER.getPolymorphicSerializerProvider().findValueSerializer(Map.class));
    }

    static class JavaUtilCollectionsConverterTest extends DefaultSerializerProvider.BaseSerializerProvider.BaseSerializerProvider {
        @Override
        public JsonSerializer<Object> findValueSerializer(Class<?> type, JsonSerializer<?> existingSerializer) {
            if (type == Map.class || type == List.class || type == Set.class) {
                return MAPPER.getFactory().findValueSerializer(type, JavaUtilCollectionsDeserializers.class);
            }
            return super.findValueSerializer(type, existingSerializer);
        }
    }

    @org.junit.Test
    public void testFindForCollection() throws Exception {
        // Test for singleton collections
        String jsonSingletonSet = "{\"@type\":1,\"class\":\"java.util.Collections$1\",\"value\":[true]}";
        JsonNode nodeSingletonSet = MAPPER.readTree(jsonSingletonSet);
        JavaType javaTypeSingletonSet = MAPPER.getTypeFactory().constructType(Collection.class);
        JsonDeserializer<?> deserializerSingletonSet = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeSingletonSet);
        Object readValueSingletonSet = MAPPER.readValue(jsonSingletonSet, javaTypeSingletonSet);
        assertEquals(deserializerSingletonSet.getClass().getSimpleName(), deserializerSingletonSet.getClass().getSimpleName());
        assertEquals(readValueSingletonSet.getClass().getSimpleName(), Sets.class.getSimpleName());
        assertEquals(readValueSingletonSet, Sets.newHashSet(true));

        String jsonSingletonList = "{\"@type\":2,\"class\":\"java.util.Collections$singletonList\",\"value\":[true]}";
        JsonNode nodeSingletonList = MAPPER.readTree(jsonSingletonList);
        JavaType javaTypeSingletonList = MAPPER.getTypeFactory().constructType(Collection.class);
        JsonDeserializer<?> deserializerSingletonList = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeSingletonList);
        Object readValueSingletonList = MAPPER.readValue(jsonSingletonList, javaTypeSingletonList);
        assertEquals(deserializerSingletonList.getClass().getSimpleName(), deserializerSingletonList.getClass().getSimpleName());
        assertEquals(readValueSingletonList.getClass().getSimpleName(), Lists.class.getSimpleName());
        assertEquals(readValueSingletonList, Lists.newArrayList(true));

        String jsonSingletonMap = "{\"@type\":3,\"class\":\"java.util.Collections$SingletonMap\",\"value\":{{\"a\":\"b\"}}}";
        JsonNode nodeSingletonMap = MAPPER.readTree(jsonSingletonMap);
        JavaType javaTypeSingletonMap = MAPPER.getTypeFactory().constructType(Map.class);
        JsonDeserializer<?> deserializerSingletonMap = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getDeserializationContext(), javaTypeSingletonMap);
        Object readValueSingletonMap = MAPPER.readValue(jsonSingletonMap, javaTypeSingletonMap);
        assertEquals(deserializerSingletonMap.getClass().getSimpleName(), deserializerSingletonMap.getClass().getSimpleName());
        assertEquals(readValueSingletonMap.getClass().getSimpleName(), Maps.class.getSimpleName());
        assertEquals(readValueSingletonMap, Maps.newHashMap());
        assertEquals(readValueSingletonMap.getClass().cast(Maps.class).get("a"), "b");

        // Test for unmodifiable collections
        String jsonUnmodifiableSet = "{\"@type\":4,\"class\":\"java.util.Collections$unmodifiableSet\",\"value\":[true]}";
        JsonNode nodeUnmodifiableSet = MAPPER.readTree(jsonUnmodifiableSet);
        JavaType javaTypeUnmodifiableSet = MAPPER.getTypeFactory().constructType(Collection.class);
        JsonDeserializer<?> deserializerUnmodifiableSet = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeUnmodifiableSet);
        Object readValueUnmodifiableSet = MAPPER.readValue(jsonUnmodifiableSet, javaTypeUnmodifiableSet);
        assertEquals(deserializerUnmodifiableSet.getClass().getSimpleName(), deserializerUnmodifiableSet.getClass().getSimpleName());
        assertEquals(readValueUnmodifiableSet.getClass().getSimpleName(), Collections.class.getName() + "$UnmodifiableSet");
        // Cannot modify the set so we cannot test the result

        String jsonUnmodifiableList = "{\"@type\":5,\"class\":\"java.util.Collections$unmodifiableList\",\"value\":[true]}";
        JsonNode nodeUnmodifiableList = MAPPER.readTree(jsonUnmodifiableList);
        JavaType javaTypeUnmodifiableList = MAPPER.getTypeFactory().constructType(Collection.class);
        JsonDeserializer<?> deserializerUnmodifiableList = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeUnmodifiableList);
        Object readValueUnmodifiableList = MAPPER.readValue(jsonUnmodifiableList, javaTypeUnmodifiableList);
        assertEquals(deserializerUnmodifiableList.getClass().getSimpleName(), deserializerUnmodifiableList.getClass().getSimpleName());
        assertEquals(readValueUnmodifiableList.getClass().getSimpleName(), Collections.class.getName() + "$UnmodifiableList");
        // Cannot modify the list so we cannot test the result

        String jsonUnmodifiableMap = "{\"@type\":6,\"class\":\"java.util.Collections$unmodifiableMap\",\"value\":{{\"a\":\"b\"}}}";
        JsonNode nodeUnmodifiableMap = MAPPER.readTree(jsonUnmodifiableMap);
        JavaType javaTypeUnmodifiableMap = MAPPER.getTypeFactory().constructType(Map.class);
        JsonDeserializer<?> deserializerUnmodifiableMap = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getDeserializationContext(), javaTypeUnmodifiableMap);
        Object readValueUnmodifiableMap = MAPPER.readValue(jsonUnmodifiableMap, javaTypeUnmodifiableMap);
        assertEquals(deserializerUnmodifiableMap.getClass().getSimpleName(), deserializerUnmodifiableMap.getClass().getSimpleName());
        assertEquals(readValueUnmodifiableMap.getClass().getSimpleName(), Collections.class.getName() + "$UnmodifiableMap");
        // Cannot modify the map so we cannot test the result

        // Test for other collections
        String jsonDefaultList = "{\"@type\":7,\"class\":\"java.util.Arrays$asList\",\"value\":[true]}";
        JsonNode nodeDefaultList = MAPPER.readTree(jsonDefaultList);
        JavaType javaTypeDefaultList = MAPPER.getTypeFactory().constructType(List.class);
        JsonDeserializer<?> deserializerDefaultList = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeDefaultList);
        Object readValueDefaultList = MAPPER.readValue(jsonDefaultList, javaTypeDefaultList);
        assertEquals(deserializerDefaultList.getClass().getSimpleName(), deserializerDefaultList.getClass().getSimpleName());
        assertEquals(readValueDefaultList.getClass().getSimpleName(), Lists.class.getSimpleName());
        assertEquals(readValueDefaultList, Lists.newArrayList(true));

        String jsonDefaultSet = "{\"@type\":7,\"class\":\"java.util.Arrays$asList\",\"value\":[true]}";
        JsonNode nodeDefaultSet = MAPPER.readTree(jsonDefaultSet);
        JavaType javaTypeDefaultSet = MAPPER.getTypeFactory().constructType(Set.class);
        JsonDeserializer<?> deserializerDefaultSet = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeDefaultSet);
        Object readValueDefaultSet = MAPPER.readValue(jsonDefaultSet, javaTypeDefaultSet);
        assertEquals(deserializerDefaultSet.getClass().getSimpleName(), deserializerDefaultSet.getClass().getSimpleName());
        assertEquals(readValueDefaultSet.getClass().getSimpleName(), Sets.class.getSimpleName());
        assertEquals(readValueDefaultSet, Sets.newHashSet(true));

        // Test for empty collections
        String jsonEmptySet = "{\"@type\":1,\"class\":\"java.util.Collections$singleton\",\"value\":[]}";
        JsonNode nodeEmptySet = MAPPER.readTree(jsonEmptySet);
        JavaType javaTypeEmptySet = new TypeReference<Set<Object>>(){}.getType();
        JsonDeserializer<?> deserializerEmptySet = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeEmptySet);
        Object readValueEmptySet = MAPPER.readValue(jsonEmptySet, javaTypeEmptySet);
        assertEquals(deserializerEmptySet.getClass().getSimpleName(), deserializerEmptySet.getClass().getSimpleName());
        assertEquals(readValueEmptySet.getClass().getSimpleName(), Sets.class.getSimpleName());
        assertEquals(readValueEmptySet, Collections.emptySet());

        String jsonEmptyList = "{\"@type\":2,\"class\":\"java.util.Collections$emptyList\",\"value\":[]}";
        JsonNode nodeEmptyList = MAPPER.readTree(jsonEmptyList);
        JavaType javaTypeEmptyList = new TypeReference<List<Object>>(){}.getType();
        JsonDeserializer<?> deserializerEmptyList = JavaUtilCollectionsDeserializers.findForCollection(MAPPER.getDeserializationContext(), javaTypeEmptyList);
        Object readValueEmptyList = MAPPER.readValue(jsonEmptyList, javaTypeEmptyList);
        assertEquals(deserializerEmptyList.getClass().getSimpleName(), deserializerEmptyList.getClass().getSimpleName());
        assertEquals(readValueEmptyList.getClass().getSimpleName(), Lists.class.getSimpleName());
        assertEquals(readValueEmptyList, Collections.emptyList());

        String jsonEmptyMap = "{\"@type\":3,\"class\":\"java.util.Collections$emptyMap\",\"value\":{}}";
        JsonNode nodeEmptyMap = MAPPER.readTree(jsonEmptyMap);
        JavaType javaTypeEmptyMap = new TypeReference<Map<String, Object>>(){}.getType();
        JsonDeserializer<?> deserializerEmptyMap = JavaUtilCollectionsDeserializers.findForMap(MAPPER.getDeserializationContext(), javaTypeEmptyMap);
        Object readValueEmptyMap = MAPPER.readValue(jsonEmptyMap, javaTypeEmptyMap);
        assertEquals(deserializerEmptyMap.getClass().getSimpleName(), deserializerEmptyMap.getClass().getSimpleName());
        assertEquals(readValueEmptyMap.getClass().getSimpleName(), Maps.class.getSimpleName());
        assertEquals(readValueEmptyMap, Collections.emptyMap());
    }
}