import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    // Test for basic functionality of deserializing empty Collection
    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        String json = "[]";
        Collection<Object> expectedResult = new ArrayList<>();
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for basic functionality of deserializing Collection with single element
    @Test
    public void testSingleElementCollectionDeserialization() throws IOException {
        String json = "[1]";
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for basic functionality of deserializing Collection with multiple elements
    @Test
    public void testMultipleElementsCollectionDeserialization() throws IOException {
        String json = "[1, 2, 3]";
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(3);
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for deserializing Collection with null elements
    @Test
    public void testNullElementsCollectionDeserialization() throws IOException {
        String json = "[null]";
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(null);
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for deserializing Collection with duplicate elements
    @Test
    public void testDuplicateElementsCollectionDeserialization() throws IOException {
        String json = "[1, 1, 2, 2, 3, 3]";
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(2);
        expectedResult.add(3);
        expectedResult.add(3);
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for deserializing Collection with empty String as a single element
    @Test
    public void testEmptyStringAsSingleElementCollectionDeserialization() throws IOException {
        String json = "[\"\"]";
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add("");
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for deserializing Collection with primitive types
    @Test
    public void testPrimitiveTypeCollectionDeserialization() throws IOException {
        String json = "[1, 2, 3.0, true, false]";
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(3.0);
        expectedResult.add(true);
        expectedResult.add(false);
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for deserializing Collection with custom objects
    @Test
    public void testCustomObjectCollectionDeserialization() throws IOException {
        String json = "[{\"name\":\"Alice\", \"age\":30}, {\"name\":\"Bob\", \"age\":25}]";
        Collection<Object> expectedResult = new ArrayList<>();
        expectedResult.add(new Person("Alice", 30));
        expectedResult.add(new Person("Bob", 25));
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(expectedResult, result);
    }

    // Test for handling empty String as an empty Collection
    @Test
    public void testEmptyStringAsEmptyCollectionDeserialization() throws IOException {
        String json = "";
        Collection<Object> expectedResult = new ArrayList<>();
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertNull(result);
    }

    // Test for handling non-Array JSON
    @Test(expected = JsonMappingException.class)
    public void testNonArrayJsonDeserialization() throws IOException {
        String json = "{\"name\":\"Alice\", \"age\":30}";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    // Test for handling missing value in Collection
    @Test(expected = JsonMappingException.class)
    public void testMissingValueInCollectionDeserialization() throws IOException {
        String json = "[1]";
        MapperFactory.builder()
                .registerModule(new SimpleModule()
                        .setSerializerProvider(mapper.getSerializerProvider()))
                .build()
                .readValue(json, CollectionDeserializer.class);
    }

    // Test for handling extra elements in Collection
    @Test(expected = JsonMappingException.class)
    public void testExtraElementsInCollectionDeserialization() throws IOException {
        String json = "[1, 2, 3, 4]";
        MapperFactory.builder()
                .registerModule(new SimpleModule()
                        .setSerializerProvider(mapper.getSerializerProvider()))
                .build()
                .readValue(json, CollectionDeserializer.class);
    }

    // Test for handling non-Serializable elements in Collection
    @Test(expected = IOException.class)
    public void testNonSerializableElementsInCollectionDeserialization() throws IOException {
        String json = "[new NonSerializableClass()]";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    // Test for handling circular references in Collection
    @Test(expected = JsonMappingException.class)
    public void testCircularReferencesInCollectionDeserialization() throws IOException {
        String json = "[{ \"ref\": 1 }, { \"ref\": 0 }]";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    // Test for handling invalid JSON in Collection
    @Test(expected = JsonMappingException.class)
    public void testInvalidJsonInCollectionDeserialization() throws IOException {
        String json = "{\"name\":\"Alice\", [\"age\":30}";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    // Test for handling JSON with duplicate keys in Collection
    @Test(expected = JsonMappingException.class)
    public void testDuplicateKeysInCollectionDeserialization() throws IOException {
        String json = "[{\"name\":\"Alice\", \"age\":30}, {\"name\":\"Alice\", \"age\":25}]";
        mapper.readValue(json, CollectionDeserializer.class);
    }

    private static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    private static class NonSerializableClass {

    }
}