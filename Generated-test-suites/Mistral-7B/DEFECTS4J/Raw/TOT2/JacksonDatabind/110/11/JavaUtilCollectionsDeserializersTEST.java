import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        // Test with valid input
        List list = new ArrayList();
        list.add("Valid Input");
        ObjectNode input = mapper.valueToTree(list);
        ObjectNode output = mapper.treeToValue(input, List.class);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        // Test with valid input
        Map map = new HashMap();
        map.put("Key", "Value");
        ObjectNode input = mapper.valueToTree(map);
        ObjectNode output = mapper.treeToValue(input, Map.class);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));
    }

    @Test
    public void testConverver() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType inputType = mapper.getTypeFactory().constructType(List.class);
        Class<?> rawSuper = List.class;
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, inputType, rawSuper);
        // Test with valid input
        List list = new ArrayList();
        list.add("Valid Input");
        ObjectNode inputObject = mapper.valueToTree(list);
        ObjectNode outputObject = mapper.treeToValue(inputObject, Object.class);
        Object output = converter.convert(outputObject);
        assertEquals(list, output);
    }

    @Test
    public void testEmptyCollectionsAndLists() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        ObjectNode input = mapper.createObjectNode();
        // Test for empty List
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));

        // Test for empty Set
        JavaType setType = mapper.getTypeFactory().constructType(HashSet.class);
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, setType);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));

        // Test for empty Map
        JavaType mapType = mapper.getTypeFactory().constructType(HashMap.class);
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, mapType);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));
    }

    @Test
    public void testNullInputs() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(List.class);
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            fail("Expected JsonMappingException for null input");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test for null Set
        type = mapper.getTypeFactory().constructType(HashSet.class);
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            fail("Expected JsonMappingException for null input");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test for null Map
        type = mapper.getTypeFactory().constructType(HashMap.class);
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            fail("Expected JsonMappingException for null input");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidInputTypes() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(Integer.class);
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            fail("Expected JsonMappingException for invalid input type");
        } catch (JsonMappingException e) {
            // Expected
        }

        type = mapper.getTypeFactory().constructType(Map.class);
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            fail("Expected JsonMappingException for invalid input type");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testUnmodifiableCollectionsAndMaps() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType setType = mapper.getTypeFactory().constructType(Collections.unmodifiableSet(Arrays.asList(1, 2, 3)).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, setType);
        ObjectNode input = mapper.createObjectNode();
        ObjectNode output = mapper.treeToValue(input, Object.class);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));

        JavaType listType = mapper.getTypeFactory().constructType(Collections.unmodifiableList(Arrays.asList(1, 2, 3)).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, listType);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));

        JavaType mapType = mapper.getTypeFactory().constructType(Collections.unmodifiableMap(Arrays.asList(1, 2, 3)).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, mapType);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));
    }

    @Test
    public void testCollectionsFromRawClasses() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType type = mapper.getTypeFactory().constructType(ArrayList.class);
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        ObjectNode input = mapper.valueToTree(Arrays.asList(1, 2, 3));
        ObjectNode output = mapper.treeToValue(input, Object.class);
        assertEquals(deserializer, mapper.readValue(mapper.writeValueAsString(input), JsonDeserializer.class));
    }

    @Test
    public void testSingletonCollections() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType setType = mapper.getTypeFactory().constructType(HashSet.class);
        setType = mapper.getTypeFactory().constructType(Collections.singleton(1).getClass());
        JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, setType);
        ObjectNode input = mapper.valueToTree(Collections.singleton(1));
        ObjectNode output = mapper.treeToValue(input, Object.class);
        Object expectedOutput = new HashSet<>();
        expectedOutput.add(1);
        assertEquals(expectedOutput, deserializer.deserialize(input, ctxt));

        JavaType listType = mapper.getTypeFactory().constructType(ArrayList.class);
        listType = mapper.getTypeFactory().constructType(Collections.singletonList(1).getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, listType);
        input = mapper.valueToTree(Collections.singletonList(1));
        output = mapper.treeToValue(input, Object.class);
        expectedOutput = new ArrayList<>();
        expectedOutput.add(1);
        assertEquals(expectedOutput, deserializer.deserialize(input, ctxt));

        JavaType mapType = mapper.getTypeFactory().constructType(HashMap.class);
        mapType = mapper.getTypeFactory().constructType(Collections.singletonMap("Key", "Value").getClass());
        deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, mapType);
        input = mapper.valueToTree(Collections.singletonMap("Key", "Value"));
        output = mapper.treeToValue(input, Object.class);
        expectedOutput = new HashMap<>();
        expectedOutput.put("Key", "Value");
        assertEquals(expectedOutput, deserializer.deserialize(input, ctxt));
    }
}