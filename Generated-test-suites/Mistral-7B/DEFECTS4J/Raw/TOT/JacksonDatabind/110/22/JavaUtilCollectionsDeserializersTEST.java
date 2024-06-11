import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFindForCollection_SingletonSet() throws IOException {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        Set<Boolean> expectedResult = Collections.singleton(true);
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":1,\"concreteType\":{\"rawClass\":\"java.util.Set\",\"typeArguments\":[\"java.lang.Boolean\"]}}";
        Set<Boolean> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindForCollection_SingletonList() throws IOException {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        List<Boolean> expectedResult = Collections.singletonList(true);
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":2,\"concreteType\":{\"rawClass\":\"java.util.List\",\"typeArguments\":[\"java.lang.Boolean\"]}}";
        List<Boolean> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindForCollection_SingletonMap() throws IOException {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, String.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        Map<String, String> expectedResult = Collections.singletonMap("a", "b");
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":3,\"concreteType\":{\"rawClass\":\"java.util.Map\",\"typeArguments\":[\"java.lang.String\",\"java.lang.String\"]}}";
        Map<String, String> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindForCollection_UnmodifiableSet() throws IOException {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(Set.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        Set<Boolean> set = Collections.unmodifiableSet(Collections.singleton(true));
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":4,\"concreteType\":{\"rawClass\":\"java.util.Set\",\"typeArguments\":[\"java.lang.Boolean\"]}}";
        Set<Boolean> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(set, result);
    }

    @Test
    public void testFindForCollection_UnmodifiableList() throws IOException {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Boolean.TYPE);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        List<Boolean> list = Collections.unmodifiableList(Arrays.asList(true));
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":5,\"concreteType\":{\"rawClass\":\"java.util.List\",\"typeArguments\":[\"java.lang.Boolean\"]}}";
        List<Boolean> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(list, result);
    }

    @Test
    public void testFindForCollection_UnmodifiableMap() throws IOException {
        // Given
        JavaType javaType = new TypeReference<Map<String, String>>() {}.getType();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":6,\"concreteType\":{\"rawClass\":\"java.util.Map\",\"typeArguments\":[\"java.lang.String\",\"java.lang.String\"]}}";
        Map<String, String> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(map, result);
    }

    @Test
    public void testFindForCollection_EmptyList() throws IOException {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":7,\"concreteType\":{\"rawClass\":\"java.util.List\"}}";
        List<?> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindForCollection_InvalidType() throws IOException {
        // Given
        JavaType javaType = TypeFactory.defaultInstance().constructType(Integer.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(objectMapper.getDeserializationContext(), javaType);

        // When
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":1,\"concreteType\":{\"rawClass\":\"java.util.Set\",\"typeArguments\":[\"java.lang.Boolean\"]}}";
        objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));
    }

    @Test
    public void testFindForMap_SingletonMap() throws IOException {
        // Given
        JavaType javaType = new TypeReference<Map<String, String>>() {}.getType();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);

        // When
        Map<String, String> expectedResult = Collections.singletonMap("a", "b");
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":3,\"concreteType\":{\"rawClass\":\"java.util.Map\",\"typeArguments\":[\"java.lang.String\",\"java.lang.String\"]}}";
        Map<String, String> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindForMap_UnmodifiableMap() throws IOException {
        // Given
        JavaType javaType = new TypeReference<Map<String, String>>() {}.getType();
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(objectMapper.getDeserializationContext(), javaType);

        // When
        Map<String, String> map = Collections.unmodifiableMap(Collections.singletonMap("a", "b"));
        String jsonInput = "{\"class\":\"com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter\",\"kind\":6,\"concreteType\":{\"rawClass\":\"java.util.Map\",\"typeArguments\":[\"java.lang.String\",\"java.lang.String\"]}}";
        Map<String, String> result = objectMapper.readValue(jsonInput, deserializer.getType(objectMapper.getDeserializationContext()));

        // Then
        Assert.assertEquals(map, result);
    }
}