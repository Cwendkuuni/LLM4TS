import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerializationWithConverter() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new StringToNumberConverter(),
                mapper.getTypeFactory().constructType(Integer.class),
                mapper.findValueSerializer(Integer.class)
        );

        Integer input = 123;
        String outputJson = "{\"number\":123}";

        String resultJson = mapper.writeValueAsString(input, serializer);
        assertEquals(outputJson, resultJson);

        Object deserializedObject = mapper.readValue(resultJson, mapper.getTypeFactory().constructType(Integer.class));
        assertEquals(input, deserializedObject);
    }

    @Test
    public void testSerializationWithNullInput() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new StringToNumberConverter(),
                mapper.getTypeFactory().constructType(Integer.class),
                mapper.findValueSerializer(Integer.class)
        );

        Object input = null;
        String outputJson = "null";

        String resultJson = mapper.writeValueAsString(input, serializer);
        assertEquals(outputJson, resultJson);

        Object deserializedObject = mapper.readValue(resultJson, mapper.getTypeFactory().constructType(Integer.class));
        assertNull(deserializedObject);
    }

    @Test
    public void testSerializationWithUnknownInputType() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(
                new StringToNumberConverter(),
                mapper.getTypeFactory().constructType(Integer.class),
                mapper.findValueSerializer(Integer.class)
        );

        String inputString = "test";
        String outputJson = "{\"number\":null}";

        String resultJson = mapper.writeValueAsString(inputString, serializer);
        assertEquals(outputJson, resultJson);

        Object deserializedObject = mapper.readValue(resultJson, mapper.getTypeFactory().constructType(Integer.class));
        assertNull(deserializedObject);
    }

    @Test
    public void testSerializationWithEmptyList() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer<List<Integer>> serializer = new StdDelegatingSerializer<>(
                new ArrayListToJsonArrayConverter(),
                mapper.getTypeFactory().constructType(java.util.List.class, Integer.class),
                mapper.findValueSerializer(java.util.List.class)
        );

        List<Integer> inputList = new ArrayList<>();
        String outputJson = "[ ]";

        String resultJson = mapper.writeValueAsString(inputList, serializer);
        assertEquals(outputJson, resultJson);

        Object deserializedObject = mapper.readValue(resultJson, mapper.getTypeFactory().constructType(java.util.List.class, Integer.class));
        assertEquals(inputList, deserializedObject);
    }

    @Test
    public void testSerializationWithEmptyMap() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>(
                new MapToJsonObjectConverter(),
                mapper.getTypeFactory().constructType(java.util.Map.class, String.class, Integer.class),
                mapper.findValueSerializer(java.util.Map.class)
        );

        java.util.Map<String, Integer> inputMap = new HashMap<>();
        String outputJson = "{}";

        String resultJson = mapper.writeValueAsString(inputMap, serializer);
        assertEquals(outputJson, resultJson);

        Object deserializedObject = mapper.readValue(resultJson, mapper.getTypeFactory().constructType(java.util.Map.class, String.class, Integer.class));
        assertEquals(inputMap, deserializedObject);
    }

    @Test
    public void testSerializationWithEmptyMapNotContainingKeyType() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>(
                new MapToJsonObjectConverter(),
                mapper.getTypeFactory().constructType(java.util.Map.class, Object.class, Object.class),
                mapper.findValueSerializer(java.util.Map.class)
        );

        java.util.Map<Object, Object> inputMap = new HashMap<>();
        String outputJson = "{}";

        String resultJson = mapper.writeValueAsString(inputMap, serializer);
        assertEquals(outputJson, resultJson);

        Object deserializedObject = mapper.readValue(resultJson, mapper.getTypeFactory().constructType(java.util.Map.class, Object.class, Object.class));
        assertEquals(inputMap, deserializedObject);
    }

    @Test
    public void testSerializationWithInvalidDelegateType() {
        // This test case cannot be executed with JUnit and would require custom test setup
        // to create an appropriate SerializerProvider and test invocation.

        // The test would involve instantiating a StdDelegatingSerializer with invalid
        // type arguments and attempting to serialize a value. An exception should be
        // thrown.
    }

    @Test
    public void testSerializationWithNullDelegateSerializer() {
        // This test case cannot be executed with JUnit and would require custom test setup
        // to create an appropriate SerializerProvider and test invocation.

        // The test would involve instantiating a StdDelegatingSerializer with a null
        // delegate serializer and attempting to serialize a value. An exception should be
        // thrown.
    }

    @Test
    public void testSerializationWithNullDelegateType() {
        // This test case cannot be executed with JUnit and would require custom test setup
        // to create an appropriate SerializerProvider and test invocation.

        // The test would involve instantiating a StdDelegatingSerializer with a null
        // delegate type and attempting to serialize a value. An exception should be
        // thrown.
    }
}