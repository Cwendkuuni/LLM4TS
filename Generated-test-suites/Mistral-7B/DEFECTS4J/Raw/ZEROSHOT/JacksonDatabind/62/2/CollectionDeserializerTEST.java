package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

/**
 * Comprehensive JUnit 4 test file for CollectionDeserializer class.
 */
@JacksonStdImpl
public class CollectionDeserializerTest {

    // Helper class for test data
    @JsonRootName("root")
    static class TestData {
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        private List<Integer> intList;
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, value = JsonFormat.Value.WRAP_ARRAY_VALUE)
        private Collection<Integer> intCollection;
        @JsonFormat(with = JsonFormat.Feature.FAIL_ON_EMPTY_COLLECTION)
        private Collection<String> stringCollection;

        public List<Integer> getIntList() {
            return intList;
        }

        public void setIntList(List<Integer> intList) {
            this.intList = intList;
        }

        public Collection<Integer> getIntCollection() {
            return intCollection;
        }

        public void setIntCollection(Collection<Integer> intCollection) {
            this.intCollection = intCollection;
        }

        public Collection<String> getStringCollection() {
            return stringCollection;
        }

        public void setStringCollection(Collection<String> stringCollection) {
            this.stringCollection = stringCollection;
        }
    }

    // Helper method to create TestData instance for tests
    private TestData createTestData() {
        TestData data = new TestData();
        data.getIntList() = Arrays.asList(1, 2, 3);
        data.getIntCollection() = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};
        data.getStringCollection() = new ArrayList<String>() {{
            add("one");
            add("two");
        }};
        return data;
    }

    @Test
    public void testDeserializeIntList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(data);
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(data.getIntList(), deserializedData.getIntList());
    }

    @Test
    public void testDeserializeIntCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(data);
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(data.getIntCollection().size(), deserializedData.getIntCollection().size());
        for (int i = 0; i < deserializedData.getIntCollection().size(); i++) {
            assertEquals(data.getIntCollection().get(i), deserializedData.getIntCollection().get(i));
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyCollectionString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = new TestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(data);
        mapper.readValue(json, Collection.class);
    }

    @Test
    public void testDeserializeSingleValueIntList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(data.getIntList().get(0));
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(data.getIntList().size(), deserializedData.getIntList().size());
        assertEquals(data.getIntList().get(0), deserializedData.getIntList().get(0));
    }

    @Test
    public void testDeserializeSingleValueIntCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(data.getIntCollection().get(0));
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(data.getIntCollection().size(), deserializedData.getIntCollection().size());
        assertEquals(data.getIntCollection().get(0), deserializedData.getIntCollection().get(0));
    }

    @Test
    public void testDeserializeEmptyCollectionIntList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(Collections.emptyList());
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(Collections.emptyList(), deserializedData.getIntList());
    }

    @Test
    public void testDeserializeSingleValueNullIntList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(null);
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(Collections.emptyList(), deserializedData.getIntList());
    }

    @Test
    public void testDeserializeSingleValueNullIntCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(null);
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(Collections.emptyList(), deserializedData.getIntCollection());
    }

    @Test
    public void testDeserializeCollectionOfStrings() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestData data = createTestData();
        String json = mapper.writerWithRootValue(data).writeValueAsString(data);
        TestData deserializedData = mapper.readValue(json, TestData.class);
        assertEquals(data.getStringCollection().size(), deserializedData.getStringCollection().size());
        for (int i = 0; i < deserializedData.getStringCollection().size(); i++) {
            assertEquals(data.getStringCollection().get(i), deserializedData.getStringCollection().get(i));
        }
    }
}