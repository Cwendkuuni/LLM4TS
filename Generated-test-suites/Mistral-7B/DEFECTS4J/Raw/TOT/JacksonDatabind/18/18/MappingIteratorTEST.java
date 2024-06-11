package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.JsonDeserializer.Contextualizer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    private ObjectMapper mapper;
    private ObjectReader reader;
    private JsonFactory factory;

    @Before
    public void setup() {
        factory = new JsonFactory();
        mapper = new ObjectMapper();
        reader = mapper.readerForListOf(String.class);
    }

    @After
    public void teardown() throws Exception {
        reader.close();
        mapper.close();
        factory.close();
    }

    // Test for empty iterator
    @Test
    public void testEmptyIterator() throws Exception {
        MappingIterator<String> emptyIterator = MappingIterator.emptyIterator();
        assertFalse(emptyIterator.hasNext());
    }

    // Test for iterator with null Type
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullType() throws Exception {
        JavaType nullType = null;
        MappingIterator<String> iterator = new MappingIterator<>(nullType, null, null, null, false, null);
    }

    // Test for iterator initialization with managed parser
    @Test
    public void testIteratorWithManagedParser() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new String[]{"value1", "value2"}));
        ObjectReader managedReader = mapper.readerForTree(node);
        MappingIterator<String> iterator = new MappingIterator<>(stringType, managedReader.getParser(), null, null, true, null);
        assertTrue(iterator.hasNext());
        assertEquals("value1", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("value2", iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
        managedReader.close();
    }

    // Test for iterator initialization without managed parser
    @Test
    public void testIteratorWithoutManagedParser() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new String[]{"value1", "value2"}));
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        iterator.next();
        iterator.next();
        iterator.close();
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    // Test for iterator with null Context
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullContext() {
        DeserializationContext context = null;
        JsonDeserializer<String> deserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        MappingIterator<String> iterator = new MappingIterator<>(null, null, context, deserializer, false, null);
    }

    // Test for iterator with null Deserializer
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullDeserializer() {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        MappingIterator<String> iterator = new MappingIterator<>(stringType, null, null, null, false, null);
    }

    // Test for iterator parsing String values
    @Test
    public void testIteratorParsingStringValues() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new String[]{"value1", "value2", "value3"}));
        ObjectReader reader = mapper.readerForTree(node);
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());
        assertEquals("value3", iterator.next());
        assertFalse(iterator.hasNext());
        iterator.close();
        reader.close();
    }

    // Test for iterator parsing Int values
    @Test
    public void testIteratorParsingIntValues() throws Exception {
        JavaType intType = mapper.getTypeFactory().constructType(List.class, Integer.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new Object[] {1, 2, 3}));
        ObjectReader reader = mapper.readerForTree(node);
        MappingIterator<Integer> iterator = reader.readValuesAs(Integer.class);
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertFalse(iterator.hasNext());
        iterator.close();
        reader.close();
    }

    // Test for iterator parsing Custom class values
    @Test
    public void testIteratorParsingCustomClassValues() throws Exception {
        JavaType customType = mapper.getTypeFactory().constructType(List.class, MyClass.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new Object[] {new MyClass("value1"), new MyClass("value2"), new MyClass("value3")}));
        ObjectReader reader = mapper.readerForTree(node);
        MappingIterator<MyClass> iterator = reader.readValuesAs(MyClass.class);
        assertEquals("value1", iterator.next().getValue());
        assertEquals("value2", iterator.next().getValue());
        assertEquals("value3", iterator.next().getValue());
        assertFalse(iterator.hasNext());
        iterator.close();
        reader.close();
    }

    // Test for iterator handling "hasNextValue" failure
    @Test(expected = JsonMappingException.class)
    public void testIteratorHandlingHasNextValueFailure() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new Object[] {"value1", "value2"}));
        ObjectReader reader = mapper.readerForTree(node);
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        iterator.next();
        iterator.next();
        iterator.next(); // should throw exception
        assertFalse(iterator.hasNext());
    }

    // Test for iterator handling "nextValue" failure
    @Test(expected = JsonMappingException.class)
    public void testIteratorHandlingNextValueFailure() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new Object[] {"value1"}));
        ObjectReader reader = mapper.readerForTree(node);
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        iterator.next();
        iterator.next(); // should throw exception
        assertFalse(iterator.hasNext());
    }

    // Test for iterator handling "readAll" failure
    @Test(expected = JsonMappingException.class)
    public void testIteratorHandlingReadAllFailure() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectNode node = factory.createObjectNode();
        node.put("values", factory.createValueFactory().valueFromObject(new Object[] {"value1"}));
        ObjectReader reader = mapper.readerForTree(node);
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        List<String> list = new ArrayList<>();
        iterator.readAll(list); // should throw exception
        assertFalse(iterator.hasNext());
        assertEquals(0, list.size());
    }

    // Test for iterator handling "readAll" with null list
    @Test(expected = IllegalArgumentException.class)
    public void testIteratorHandlingReadAllWithNullList() {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        iterator.readAll((List<String>) null);
    }

    // Test for iterator handling "readAll" with pre-initialized list
    @Test
    public void testIteratorHandlingReadAllWithPreInitializedList() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectReader reader = mapper.readerForTree(factory.createObjectNode());
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        List<String> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(0, list.size());
    }

    // Test for iterator handling "readAll" with pre-filled list
    @Test
    public void testIteratorHandlingReadAllWithPreFilledList() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectReader reader = mapper.readerForTree(factory.createObjectNode());
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        List<String> list = new ArrayList<>();
        list.add("value1");
        iterator.readAll(list);
        assertEquals(1, list.size());
    }

    // Test for iterator handling "readAll" with ArrayList as result
    @Test
    public void testIteratorHandlingReadAllWithArrayListAsResult() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectReader reader = mapper.readerForTree(factory.createObjectNode());
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        List<String> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(0, list.size());
        iterator = reader.readValuesAs(ArrayList.class);
        list = (ArrayList<String>) iterator.next();
        assertEquals(0, list.size());
    }

    // Test for iterator handling "readAll" with LinkedList as result
    @Test
    public void testIteratorHandlingReadAllWithLinkedListAsResult() throws Exception {
        JavaType stringType = mapper.getTypeFactory().constructType(List.class, String.class);
        ObjectReader reader = mapper.readerForTree(factory.createObjectNode());
        MappingIterator<String> iterator = reader.readValuesAs(String.class);
        List<String> list = new ArrayList<>();
        iterator.readAll(list);
        assertEquals(0, list.size());
        iterator = reader.readerForListOf(String.class);
        list = (LinkedList<String>) iterator.next();
        assertEquals(0, list.size());
    }

    // Custom class used for testing
    public static class MyClass {
        private final String value;

        public MyClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static class MyClassDeserializer extends StdDeserializer<MyClass> {
            public MyClassDeserializer() {
                this(null);
            }

            public MyClassDeserializer(Class<MyClass> t) {
                this(t, null);
            }

            public MyClassDeserializer(Class<MyClass> t, Contextualizer contextualizer) {
                super(t);
            }

            @Override
            public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                String value = p.getValueAsString();
                return new MyClass(value);
            }
        }
    }
}