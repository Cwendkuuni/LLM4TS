package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsonMappingExceptionTest {

    private static final ObjectMapper mapper = new JsonMapper();

    @Test
    public void testJsonMappingExceptionDeserialization() {
        String json = "{\"name\":\"John Doe\",\"age\":null}";
        try {
            mapper.readValue(json, User.class);
        } catch (JsonMappingException e) {
            assertNotNull(e.getProcessor());
            assertEquals("age", e.getPath().get(0).getFieldName());
            assertEquals(-1, e.getPath().get(0).getIndex());
            assertNull(e.getPath().get(0).getFrom());
            assertEquals("Unexpected value: null is not represented correctly as a JsonValue", e.getMessage());
        }
    }

    @Test
    public void testJsonMappingExceptionSerialization() {
        User user = new User();
        user.setName("John Doe");
        user.setAge(null);

        try {
            mapper.writeValueAsString(user);
        } catch (JsonMappingException e) {
            assertNotNull(e.getProcessor());
            assertEquals("age", e.getPath().get(0).getFieldName());
            assertEquals(-1, e.getPath().get(0).getIndex());
            assertNull(e.getPath().get(0).getFrom());
            assertEquals("Serializing instance of type java.lang.Integer but was not able to read a JsonValue for field 'age' (through reference chain: com.fasterxml.jackson.databind.test.JsonMappingExceptionTest$User['name'='John Doe'])", e.getMessage());
        }
    }

    @Test
    public void testJsonMappingExceptionReferenceChainLimit() {
        User user1 = new User();
        user1.setName("John Doe 1");
        User user2 = new User();
        user2.setName("John Doe 2");
        user1.setUser(user2);
        user2.setUser(user1);

        try {
            mapper.writeValueAsString(user1);
        } catch (JsonMappingException e) {
            assertNotNull(e.getProcessor());
            assertEquals(2, e.getPath().size());
            assertEquals("user", e.getPath().get(0).getFieldName());
            assertEquals(-1, e.getPath().get(0).getIndex());
            assertNull(e.getPath().get(0).getFrom());
            assertEquals("user", e.getPath().get(1).getFieldName());
            assertEquals(-1, e.getPath().get(1).getIndex());
            assertNull(e.getPath().get(1).getFrom());
            assertEquals("Serializing instance of type com.fasterxml.jackson.databind.test.JsonMappingExceptionTest$User but was not able to read a JsonValue for field 'user' (through reference chain: com.fasterxml.jackson.databind.test.JsonMappingExceptionTest$User['name'='John Doe 1']->com.fasterxml.jackson.databind.test.JsonMappingExceptionTest$User['name'='John Doe 2'])", e.getMessage());
        }
    }

    @Test
    public void testJsonMappingExceptionWithMessage() {
        try {
            throw new JsonMappingException(new JsonParser(), "Custom message");
        } catch (JsonMappingException e) {
            assertEquals("Custom message", e.getMessage());
            assertNull(e.getPath());
            assertNull(e.getProcessor());
        }
    }

    static class User {
        private String name;
        private Integer age;
        private User user;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}