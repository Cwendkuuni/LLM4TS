import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper mapper = new JsonMapper();

    @Test
    public void testFindForCollectionSingleTON_List() throws Exception {
        String json = "{\"class\":\"java.util.ArrayList\",\"type\":7,\"value\":[true]}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        List list = mapper.readValue(json, List.class);
        assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
        assertEquals(deserializer.getDeserializationContext().getFactory().constructType(List.class), list.getClass());
        assertEquals(list, Collections.singletonList(true));
    }

    @Test
    public void testFindForCollectionUnMODIFIABLE_List() throws Exception {
        String json = "{\"class\":\"java.util.Collections\",\"type\":5,\"value\":[true]}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        List list = mapper.readValue(json, List.class);
        assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
        assertEquals(deserializer.getDeserializationContext().getFactory().constructType(List.class), list.getClass());
        assertTrue(list instanceof Collections);
        assertTrue(list.getClass().isAssignableFrom(UnmodifiableList.class));
        assertEquals(list, Collections.unmodifiableList(Collections.singletonList(true)));
    }

    @Test
    public void testFindForCollectionInvalidType() throws Exception {
        String json = "{\"class\":\"java.util.ArrayList\",\"type\":10,\"value\":[true]}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        assertNull(deserializer);
    }

    @Test
    public void testFindForMapSingleTON_Map() throws Exception {
        String json = "{\"class\":\"java.util.HashMap\",\"type\":3,\"value\":{\"key\":\"a\",\"value\":\"b\"}}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        Map map = mapper.readValue(json, Map.class);
        assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
        assertEquals(deserializer.getDeserializationContext().getFactory().constructType(Map.class), map.getClass());
        assertEquals(map, Collections.singletonMap("a", "b"));
    }

    @Test
    public void testFindForMapUnMODIFIABLE_Map() throws Exception {
        String json = "{\"class\":\"java.util.Collections\",\"type\":6,\"value\":{\"key\":\"a\",\"value\":\"b\"}}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        Map map = mapper.readValue(json, Map.class);
        assertEquals(deserializer.getClass(), StdDelegatingDeserializer.class);
        assertEquals(deserializer.getDeserializationContext().getFactory().constructType(Map.class), map.getClass());
        assertTrue(map instanceof Collections);
        assertTrue(map.getClass().isAssignableFrom(UnmodifiableMap.class));
        assertEquals(map, Collections.unmodifiableMap(Collections.singletonMap("a", "b")));
    }

    @Test
    public void testFindForMapInvalidType() throws Exception {
        String json = "{\"class\":\"java.util.HashMap\",\"type\":10,\"value\":{\"key\":\"a\",\"value\":\"b\"}}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(mapper.getDeserializationContext(), javaType);
        assertNull(deserializer);
    }

    @Test
    public void testJsonMappingException() throws Exception {
        String json = "{\"class\":\"java.util.ArrayList\",\"type\":-1,\"value\":[true]}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        try {
            JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        } catch (JsonMappingException e) {
            assertEquals("Can not deserialize Singleton container from -1 entries", e.getMessage());
        }
    }

    @Test
    public void testJsonMappingException2() throws Exception {
        String json = "{\"class\":\"java.util.ArrayList\",\"type\":7,\"value\":[null]}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        try {
            JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        } catch (JsonMappingException e) {
            assertEquals("Can not deserialize Singleton container from non-Boolean value", e.getMessage());
        }
    }

    @Test
    public void testJsonMappingException3() throws Exception {
        String json = "{\"class\":\"java.util.ArrayList\",\"type\":7,\"value\":[1]}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        try {
            JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        } catch (JsonMappingException e) {
            assertEquals("Can not deserialize Singleton container from non-Boolean value", e.getMessage());
        }
    }

    @Test
    public void testJsonMappingException4() throws Exception {
        String json = "{\"class\":\"java.util.ArrayList\",\"type\":7,\"value\":[\"string\"]}";
        JavaType javaType = mapper.readValue(json, new TypeReference<JavaType>() {});
        try {
            JavaUtilCollectionsDeserializers.findForCollection(mapper.getDeserializationContext(), javaType);
        } catch (JsonMappingException e) {
            assertEquals("Can not deserialize Singleton container from non-Boolean value", e.getMessage());
        }
    }
}