package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

/**
 * Test class for {@link NullifyingDeserializer}.
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserializeWithNullifyingDeserializer() throws Exception {
        String json = "{\"name\":\"John\", \"age\":30}";
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        Object readValue = MAPper.readValue(json, Object.class, nullifyingDeserializer);
        assertNull(readValue);
    }

    @Test
    public void testDeserializeWithTypeDeserializerNull() throws Exception {
        String json = "[1, 2, 3]";
        ObjectNode node = MAPper.readTree(json);
        TypeDeserializer typeDeserializer = null;
        Object readValue = NullifyingDeserializer.instance.deserializeWithType(MAPper.createParser(node), new DeserializationContext(), typeDeserializer);
        assertNull(readValue);
    }

    @Test
    public void testDeserializeWithTypeDeserializerArray() throws Exception {
        String json = "[1, 2, 3]";
        ObjectNode node = MAPper.readTree(json);
        TypeDeserializer typeDeserializer = MAPper.getTypeFactory().constructArrayType(Integer.class);
        Object readValue = NullifyingDeserializer.instance.deserializeWithType(MAPper.createParser(node), new DeserializationContext(), typeDeserializer);
        assertNull(readValue);
    }

    @Test
    public void testDeserializeWithTypeDeserializerObject() throws Exception {
        String json = "{\"name\":\"John\", \"age\":30}";
        ObjectNode node = MAPper.readTree(json);
        TypeDeserializer typeDeserializer = new MapTypeDeserializer(MAPper.getTypeFactory().constructType(Person.class));
        Object readValue = NullifyingDeserializer.instance.deserializeWithType(MAPper.createParser(node), new DeserializationContext(), typeDeserializer);
        assertNull(readValue);
    }

    @Test
    public void testDeserializeWithTypeDeserializerComplexObject() throws Exception {
        String json = "{\"name\":{\"firstName\":\"John\", \"lastName\":\"Doe\"}, \"age\":30}";
        ObjectNode node = MAPper.readTree(json);
        TypeDeserializer typeDeserializer = new MapTypeDeserializer(MAPper.getTypeFactory().constructType(Person.class));
        Object readValue = NullifyingDeserializer.instance.deserializeWithType(MAPper.createParser(node), new DeserializationContext(), typeDeserializer);
        assertNull(readValue);
    }

    @Test
    public void testDeserializeWithTypeDeserializerStartArray() throws Exception {
        String json = "[1, 2, 3]";
        ObjectNode node = MAPper.readTree(json);
        TypeDeserializer typeDeserializer = MAPper.getTypeFactory().constructArrayType(Integer[].class);
        Object readValue = NullifyingDeserializer.instance.deserializeWithType(MAPper.createParser(node), new DeserializationContext(), typeDeserializer);
        assertNull(readValue);
    }

    @Test
    public void testDeserializeWithTypeDeserializerStartObject() throws Exception {
        String json = "{\"name\":\"John\", \"age\":30}";
        ObjectNode node = MAPper.readTree(json);
        TypeDeserializer typeDeserializer = new MapTypeDeserializer(MAPper.getTypeFactory().constructType(Person.class));
        Object readValue = NullifyingDeserializer.instance.deserializeWithType(MAPper.createParser(node), new DeserializationContext(), typeDeserializer);
        assertNull(readValue);
    }

    static class Person {
        private String firstName;
        private String lastName;
        private int age;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}