package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonMappingExceptionTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testJsonMappingException() {
        try {
            String json = "{\"name\":\"John\", \"age\":[1, 2, 3, \"invalid\"]}";
            mapper.readValue(json, List.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("age[3]", ref.getDescription());
            assertEquals(JsonGenerator.Class, ref.getFrom().getClass());
            assertNull(ref.getFieldName());
            assertNull(ref.getIndex());
            assertEquals(JsonMappingException.class, e.getClass());
        }

        try {
            String json2 = "{\"name\":[\"John\", \"Mike\"]}";
            mapper.readValue(json2, String.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("name[0]", ref.getDescription());
            assertEquals(JsonParser.Class, ref.getFrom().getClass());
            assertNull(ref.getFieldName());
            assertNull(ref.getIndex());
            assertEquals(JsonMappingException.class, e.getClass());
        }

        try {
            String json3 = "{\"name\":\"John\", \"hobbies\":[{\"name\":\"football\"}, 123]}";
            mapper.readValue(json3, Hobbies.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("hobbies[1]", ref.getDescription());
            assertEquals(JsonToken.VALUE_NUMBER_INT, ((JsonParser.Token) ref.getFrom()).getTokenTypeId());
            assertNull(ref.getFieldName());
            assertNull(ref.getIndex());
            assertEquals(JsonMappingException.class, e.getClass());
        }

        try {
            String json4 = "{\"name\":[\"John\", \"Mike\"], \"age\":123}";
            mapper.readValue(json4, Person.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("age", ref.getDescription());
            assertEquals(JsonParser.Class, ref.getFrom().getClass());
            assertEquals("age", ref.getFieldName());
            assertNull(ref.getIndex());
            assertEquals(JsonMappingException.class, e.getClass());
        }

        try {
            String json5 = "{\"name\":{\"first\":\"John\", \"last\":\"Doe\"}, \"age\":123}";
            mapper.readValue(json5, Person.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference ref = e.getPath().get(0);
            assertEquals("name.last", ref.getDescription());
            assertEquals(JsonToken.START_OBJECT, ((JsonParser.Token) ref.getFrom()).getTokenTypeId());
            assertNull(ref.getFieldName());
            assertNull(ref.getIndex());
            assertEquals(JsonMappingException.class, e.getClass());
        }
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

    static class Hobbies {
        private List<String> hobbies;

        public List<String> getHobbies() {
            return hobbies;
        }

        public void setHobbies(List<String> hobbies) {
            this.hobbies = hobbies;
        }
    }
}