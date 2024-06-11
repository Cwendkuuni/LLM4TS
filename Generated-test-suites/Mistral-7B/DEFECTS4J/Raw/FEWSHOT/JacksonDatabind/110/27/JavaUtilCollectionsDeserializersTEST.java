package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.DeserializationTest.testDeserializer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();

        JavaType typeSingletonSet = typeFactory.constructType(Set.class, Boolean.TYPE);
        JavaType typeSingletonList = typeFactory.constructType(List.class, Boolean.TYPE);
        JavaType typeSingletonMap = typeFactory.constructType(Map.class, String.class, String.class);
        JavaType typeUnmodifiableSet = typeFactory.constructType(Collections.unmodifiableSet(Collections.singleton(Boolean.TRUE)).getClass());
        JavaType typeUnmodifiableList = typeFactory.constructType(Collections.unmodifiableList(Collections.singletonList(Boolean.TRUE)).getClass());
        JavaType typeUnmodifiableMap = typeFactory.constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());
        JavaType typeAsList = typeFactory.constructType(List.class);

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeSingletonSet);
        assertEquals(Set.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeSingletonList);
        assertEquals(List.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeSingletonMap);
        assertEquals(Map.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeUnmodifiableSet);
        assertEquals(Set.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeUnmodifiableList);
        assertEquals(List.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeUnmodifiableMap);
        assertEquals(Map.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeAsList);
        assertEquals(List.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeUnmodifiableMap.getRawType());
        assertNull(deserializer);

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeFactory.constructType(Map.class, Integer.TYPE, String.class));
        assertNull(deserializer);

        deserializer = JavaUtilCollectionsDeserializers.findForCollection(context, typeFactory.constructType(Set.class, Double.TYPE));
        assertNull(deserializer);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext context = mapper.getDeserializationContext();
        TypeFactory typeFactory = context.getTypeFactory();

        JavaType typeSingletonMap = typeFactory.constructType(Map.class, String.class, String.class);
        JavaType typeUnmodifiableMap = typeFactory.constructType(Collections.unmodifiableMap(Collections.singletonMap("a", "b")).getClass());

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(context, typeSingletonMap);
        assertEquals(Map.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, typeUnmodifiableMap);
        assertEquals(Map.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, typeFactory.constructType(Map.class, Integer.TYPE, String.class));
        assertEquals(Map.class, deserializer.getTargetType(context));

        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, typeFactory.constructType(Map.class, String.class, Double.TYPE));
        assertNull(deserializer);

        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, typeFactory.constructType(Set.class, String.class));
        assertNull(deserializer);

        deserializer = JavaUtilCollectionsDeserializers.findForMap(context, typeFactory.constructType(List.class, String.class));
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$SingletonList\",\"value\":[\"foo\"]}";

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(List.class, String.class));

        List<String> result = mapper.readValue(json, deserializer.getType(mapper.getDeserializationContext()));
        assertEquals("foo", result.get(0));
    }

    @Test
    public void testDeserializeMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$SingletonMap\",\"value\":{\"key\":\"foo\",\"value\":\"bar\"}}";

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(Map.class, String.class, String.class));

        Map<String, String> result = mapper.readValue(json, deserializer.getType(mapper.getDeserializationContext()));
        assertEquals("foo", result.get("key"));
        assertEquals("bar", result.get("value"));
    }

    @Test
    public void testDeserializeUnmodifiableCollection() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$UnmodifiableList\",\"value\":[\"foo\"]}";

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(List.class, String.class));

        List<String> result = mapper.readValue(json, deserializer.getType(mapper.getDeserializationContext()));
        assertEquals("foo", result.get(0));
        assertFalse(result instanceof List);
        assertTrue(result instanceof java.util. Collections.unmodifiableList);
    }

    @Test
    public void testDeserializeUnmodifiableMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"java.util.Collections$UnmodifiableMap\",\"value\":{\"key\":\"foo\",\"value\":\"bar\"}}";

        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(),
                mapper.getTypeFactory().constructType(Map.class, String.class, String.class));

        Map<String, String> result = mapper.readValue(json, deserializer.getType(mapper.getDeserializationContext()));
        assertEquals("foo", result.get("key"));
        assertEquals("bar", result.get("value"));
        assertFalse(result instanceof Map);
        assertTrue(result instanceof java.util. Collections.unmodifiableMap);
    }
}