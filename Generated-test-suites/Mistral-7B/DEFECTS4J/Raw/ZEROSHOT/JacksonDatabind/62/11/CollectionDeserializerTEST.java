package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

@SuppressWarnings("unchecked")
public class CollectionDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonFormat(features = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private static class TestBean {
        private List<Integer> list;

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }
    }

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = MAPPER.deserializerFactory().findValueDeserializer(Integer.class, null);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = MAPPER.config.getValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        String json = "[1, 2, 3]";
        TestBean testBean = new TestBean();

        // Test serialization
        String serializedJson = MAPPER.writeValueAsString(testBean);
        assertEquals(json, serializedJson);

        // Test deserialization
        TestBean deserializedTestBean = MAPPER.readValue(json, TestBean.class);
        List<Integer> deserializedList = deserializedTestBean.getList();
        assertEquals(3, deserializedList.size());
        assertEquals(1, deserializedList.get(0));
        assertEquals(2, deserializedList.get(1));
        assertEquals(3, deserializedList.get(2));

        // Test deserialization using CollectionDeserializer
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        Collection<Object> deserializedCollection = collectionDeserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
        List<Integer> deserializedList2 = new ArrayList<>(deserializedCollection);
        assertEquals(3, deserializedList2.size());
        assertEquals(1, deserializedList2.get(0));
        assertEquals(2, deserializedList2.get(1));
        assertEquals(3, deserializedList2.get(2));
    }

    @Test
    public void testDeserializationWithDelegateDeserializer() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = MAPPER.deserializerFactory().findValueDeserializer(String.class, null);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = MAPPER.config.getValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = MAPPER.deserializerFactory().findValueDeserializer(String.class, null);
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        String json = "[\"hello\", \"world\"]";
        TestBean testBean = new TestBean();

        // Test deserialization
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        Collection<Object> deserializedCollection = collectionDeserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
        List<String> deserializedList = new ArrayList<>(deserializedCollection);
        assertEquals(2, deserializedList.size());
        assertEquals("hello", deserializedList.get(0));
        assertEquals("world", deserializedList.get(1));

        // Test deserialization using delegate deserializer
        collectionDeserializer = collectionDeserializer.withResolved(delegateDeserializer, valueDeserializer, valueTypeDeserializer, null);
        deserializedCollection = collectionDeserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
        deserializedList = new ArrayList<>(deserializedCollection);
        assertEquals(2, deserializedList.size());
        assertEquals("hello", deserializedList.get(0));
        assertEquals("world", deserializedList.get(1));
    }

    @Test
    public void testDeserializationWithValueTypeDeserializer() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, BigInteger.class);
        JsonDeserializer<Object> valueDeserializer = MAPPER.deserializerFactory().findValueDeserializer(BigInteger.class, null);
        TypeDeserializer valueTypeDeserializer = MAPPER.deserializerFactory().findTypeDeserializer(BigInteger.class, null);
        ValueInstantiator valueInstantiator = MAPPER.config.getValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        String json = "[123, 456]";
        TestBean testBean = new TestBean();

        // Test deserialization
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        Collection<Object> deserializedCollection = collectionDeserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
        List<BigInteger> deserializedList = new ArrayList<>(deserializedCollection);
        assertEquals(2, deserializedList.size());
        assertEquals(new BigInteger("123"), deserializedList.get(0));
        assertEquals(new BigInteger("456"), deserializedList.get(1));

        // Test deserialization using value type deserializer
        collectionDeserializer = collectionDeserializer.withResolved(null, valueDeserializer, valueTypeDeserializer, null);
        deserializedCollection = collectionDeserializer.deserialize(jsonParser, MAPPER.getDeserializationContext());
        deserializedList = new ArrayList<>(deserializedCollection);
        assertEquals(2, deserializedList.size());
        assertEquals(new BigInteger("123"), deserializedList.get(0));
        assertEquals(new BigInteger("456"), deserializedList.get(1));
    }
}