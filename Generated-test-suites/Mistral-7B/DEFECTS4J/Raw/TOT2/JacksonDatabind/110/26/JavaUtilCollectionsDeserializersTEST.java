import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testFindForCollection() throws IOException {
        Map<String, ObjectMapper.SerializedForm> tests = new HashMap<>();

        tests.put("singletonList", new ObjectMapper().readValue(
                mapper.writeValueAsString(Arrays.asList(1)),
                new TypeReference<List<Integer>>() {}.getType()));

        tests.put("singletonSet", new ObjectMapper().readValue(
                mapper.writeValueAsString(Collections.singleton(1)),
                new TypeReference<Set<Integer>>() {}.getType()));

        tests.put("singletonMap", new ObjectMapper().readValue(
                mapper.writeValueAsString(Collections.singletonMap("key", 1)),
                new TypeReference<Map<String, Integer>>() {}.getType()));

        tests.put("unmodifiableList", new ObjectMapper().readValue(
                mapper.writeValueAsString(Collections.unmodifiableList(Arrays.asList(1))),
                new TypeReference<List<Integer>>() {}.getType()));

        tests.put("unmodifiableSet", new ObjectMapper().readValue(
                mapper.writeValueAsString(Collections.unmodifiableSet(Arrays.asList(1))),
                new TypeReference<Set<Integer>>() {}.getType()));

        tests.put("unmodifiableMap", new ObjectMapper().readValue(
                mapper.writeValueAsString(Collections.unmodifiableMap(Collections.singletonMap("key", 1))),
                new TypeReference<Map<String, Integer>>() {}.getType()));

        tests.put("asList", Arrays.asList(1));

        for (Map.Entry<String, Object> entry : tests.entrySet()) {
            TypeDeserializer deserializer = JavaUtilCollectionsDeserializers.findForCollection(
                    mapper.getDeserializationContext(),
                    mapper.getTypeFactory().constructType(entry.getValue().getClass()));
            TypeSerializer serializer = mapper.schema().getSerializers().findValueSerializer(entry.getValue().getClass());
            assertNotNull(deserializer);
            assertEquals(serializer.typeSerializers().get(0).getEmptyValueSerializer().getClass(), deserializer.getTypeSerializer().getEmptyValueSerializer().getClass());
        }
    }

    @org.junit.Test
    public void testFindForMap() throws IOException {
        Map<String, ObjectMapper.SerializedForm> tests = new HashMap<>();

        tests.put("singletonMap", new ObjectMapper().readValue(
                mapper.writeValueAsString(Collections.singletonMap("key", 1)),
                new TypeReference<Map<String, Integer>>() {}.getType()));

        tests.put("unmodifiableMap", new ObjectMapper().readValue(
                mapper.writeValueAsString(Collections.unmodifiableMap(Collections.singletonMap("key", 1))),
                new TypeReference<Map<String, Integer>>() {}.getType()));

        for (Map.Entry<String, Object> entry : tests.entrySet()) {
            TypeDeserializer deserializer = JavaUtilCollectionsDeserializers.findForMap(
                    mapper.getDeserializationContext(),
                    mapper.getTypeFactory().constructType(entry.getValue().getClass()));
            TypeSerializer serializer = mapper.schema().getSerializers().findValueSerializer(entry.getValue().getClass());
            assertNotNull(deserializer);
            assertEquals(serializer.typeSerializers().get(0).getEmptyValueSerializer().getClass(), deserializer.getTypeSerializer().getEmptyValueSerializer().getClass());
        }
    }

    // TODO: Write test case for converter(int kind, JavaType concreteType, Class<?> rawSuper)
}