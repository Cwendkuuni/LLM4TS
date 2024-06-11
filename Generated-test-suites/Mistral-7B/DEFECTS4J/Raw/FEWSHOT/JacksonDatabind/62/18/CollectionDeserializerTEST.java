package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "[\"foo\",\"bar\"]";
        List<String> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, null, null);

        String json = "{foo: \"bar\"}";
        deserializer.deserialize(mapper.readTree(json), null);
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> delegateDeserializer = mapper.findValueDeserializer(String.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, null, delegateDeserializer, null);

        String json = "[\"foo\",\"bar\"]";
        List<String> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        String valueType = "com.example.MyString";
        JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.findValueDeserializer(valueType);
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null, null, null);

        String json = "[{\"value\":\"foo\"},{\"value\":\"bar\"}]";
        List<String> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testValueInstantiator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, null, null, valueInstantiator, null, null);

        String json = "[\"foo\",\"bar\"]";
        List<String> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
    }

    @Test
    public void testUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.findValueDeserializer("com.example.MyString");
        CollectionDeserializer deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null, null, null);

        String json = "[{@MyRef}:[],[]]";
        List<List<String>> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(2, result.size());
        assertEquals(0, result.get(0).size());
        assertEquals(0, result.get(1).size());

        String json2 = "[{\"MyRef\":[\"foo\",\"bar\"]},[]]";
        deserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, null, null, null);
        result = deserializer.deserialize(mapper.readTree(json2), null);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).size());
        assertEquals("foo", result.get(0).get(0));
        assertEquals("bar", result.get(0).get(1));
        assertEquals(0, result.get(1).size());
    }
}