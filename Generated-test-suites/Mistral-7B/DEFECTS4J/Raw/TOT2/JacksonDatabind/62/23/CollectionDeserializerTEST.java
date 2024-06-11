import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class CollectionDeserializerTest {

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSingleElementArrayDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1]";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
    }

    @Test
    public void testDeserializationWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\":\"John\",\"age\":30}]";
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, null, null);
        Collection<Object> result = mapper.readValue(json, deserializer);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0));
    }

    @Test
    public void testDeserializationWithValueTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\":\"John\",\"age\":30}]";
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Person.class);
        TypeDeserializer valueTypeDeserializer = mapper.findValueTypeDeserializer(Person.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, valueTypeDeserializer, null);
        Collection<Object> result = mapper.readValue(json, deserializer);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Person);
    }

    @Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"name\":\"John\",\"age\":30}]";
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Person.class);
        JsonDeserializer<List<Person>> delegateDeserializer = mapper.findValueDeserializer(List.class, Person.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, delegateDeserializer);
        Collection<Object> result = mapper.readValue(json, deserializer);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof List);
        List<Person> listResult = (List<Person>) result.get(0);
        assertEquals(1, listResult.size());
        assertTrue(listResult.get(0) instanceof Person);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithNonArrayJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\"}";
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        Collection<Object> result = mapper.readValue(json, deserializer);
        fail("Expected JsonMappingException, but got " + result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithNullJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);
        Collection<Object> result = mapper.readValue(json, deserializer);
        fail("Expected JsonMappingException, but got " + result);
    }

    @Test
    public void testDeserializationWithEmptyStringJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        Collection<Object> result = mapper.readValue(json, CollectionDeserializer.class);
        assertTrue(result.isEmpty());
    }

    static class Person {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}