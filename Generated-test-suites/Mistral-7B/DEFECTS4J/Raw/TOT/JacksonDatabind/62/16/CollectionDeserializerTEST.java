import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(0, collection.size());
    }

    @Test
    public void testSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(1, collection.size());
        Assert.assertEquals(1, collection.get(0));
    }

    @Test
    public void testNullElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[null]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(1, collection.size());
        Assert.assertNull(collection.get(0));
    }

    @Test
    public void testEmptyStringAsEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        JsonNode jsonNodeWithEmptyString = mapper.readTree("\"\"");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        JsonParser jsonParserWithEmptyString = mapper.getFactory().createParser(jsonNodeWithEmptyString);
        deserializer.deserialize(jsonParserWithEmptyString, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(0, collection.size());
    }

    @Test
    public void testEmptyStringAsSingleNullElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("\"\"");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(1, collection.size());
        Assert.assertNull(collection.get(0));
    }

    @Test
    public void testEmptyStringAsSingleNullElementArrayWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("\"\"");
        ObjectMapper delegateMapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = delegateMapper.findValueDeserializer(String.class, null);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                stringDeserializer, null, null, null, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(1, collection.size());
        Assert.assertNull(collection.get(0));
    }

    @Test
    public void testArrayWithSingleElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(1, collection.size());
        Assert.assertEquals(1, collection.get(0));
    }

    @Test
    public void testArrayWithMultipleElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(3, collection.size());
        Assert.assertEquals(1, collection.get(0));
        Assert.assertEquals(2, collection.get(1));
        Assert.assertEquals(3, collection.get(2));
    }

    @Test
    public void testArrayWithNullElements() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, null, 3]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(3, collection.size());
        Assert.assertEquals(1, collection.get(0));
        Assert.assertNull(collection.get(1));
        Assert.assertEquals(3, collection.get(2));
    }

    @Test
    public void testArrayWithEmptyStringAsNullElement() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, \"\", 3]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(3, collection.size());
        Assert.assertEquals(1, collection.get(0));
        Assert.assertNull(collection.get(1));
        Assert.assertEquals(3, collection.get(2));
    }

    @Test
    public void testArrayWithOneEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[\"\"]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        Assert.assertEquals(1, collection.size());
        Assert.assertNull(collection.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testArrayWithInvalidElementType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, true]");
        CollectionDeserializer deserializer = mapper.findContextualValueDeserializer(Collection.class, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        // The test should fail with JsonMappingException due to the invalid element type
    }

    @Test
    public void testArrayWithCustomValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3]");
        JsonDeserializer<Integer> integerDeserializer = mapper.getFactory().valueDeserializerOfType(Integer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, Integer.class),
                integerDeserializer, null, null, null, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        List<Integer> list = (List<Integer>) collection;
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));
    }

    @Test
    public void testArrayWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[\"apple\", \"banana\", \"orange\"]");
        TypeDeserializer stringTypeDeserializer = mapper.getFactory()
                .constructTypeDeserializer(String.class, new SimpleType(String.class));
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, stringTypeDeserializer, null, null, null);
        Collection<Object> collection = deserializer.createContextual(mapper.getDeserializationContext(), null);
        JsonParser jsonParser = mapper.getFactory().createParser(jsonNode);
        deserializer.deserialize(jsonParser, mapper.getDeserializationContext(), collection);
        List<String> list = (List<String>) collection;
        Assert.assertEquals(3, list.size());
        Assert.assertEquals("apple", list.get(0));
        Assert.assertEquals("banana", list.get(1));
        Assert.assertEquals("orange", list.get(2));
    }
}