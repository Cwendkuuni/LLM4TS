package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * StringCollectionDeserializerTest class to test all methods in StringCollectionDeserializer class.
 */
public class StringCollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @JsonDeserialize(using = StringCollectionDeserializer.class)
    static class TestCollection {
        private List<String> strings;

        public List<String> getStrings() {
            return strings;
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }
    }

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    static class TestBean {
        private TestCollection testCollection;

        public TestCollection getTestCollection() {
            return testCollection;
        }

        public void setTestCollection(TestCollection testCollection) {
            this.testCollection = testCollection;
        }
    }

    @org.junit.Test
    public void testStringCollectionDeserializer() throws IOException {
        // Test null input
        String jsonNull = "null";
        TestCollection testCollectionNull = new TestCollection();
        JsonParser parserNull = mapper.getFactory().createParser(jsonNull);
        StringCollectionDeserializer stringCollectionDeserializerNull = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                null, null);
        List<String> deserializedCollectionNull = stringCollectionDeserializerNull.deserialize(parserNull, null);
        assertEquals(Collections.emptyList(), deserializedCollectionNull);

        // Test empty JSON array input
        String jsonEmptyArray = "[ ]";
        TestCollection testCollectionEmptyArray = new TestCollection();
        JsonParser parserEmptyArray = mapper.getFactory().createParser(jsonEmptyArray);
        StringCollectionDeserializer stringCollectionDeserializerEmptyArray = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                null, null);
        List<String> deserializedCollectionEmptyArray = stringCollectionDeserializerEmptyArray.deserialize(parserEmptyArray, null);
        assertEquals(Collections.emptyList(), deserializedCollectionEmptyArray);

        // Test JSON array with single value input
        String jsonSingleValue = "[\"test\"]";
        TestCollection testCollectionSingleValue = new TestCollection();
        JsonParser parserSingleValue = mapper.getFactory().createParser(jsonSingleValue);
        StringCollectionDeserializer stringCollectionDeserializerSingleValue = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                null, null);
        List<String> deserializedCollectionSingleValue = stringCollectionDeserializerSingleValue.deserialize(parserSingleValue, null);
        List<String> expectedCollectionSingleValue = Arrays.asList("test");
        assertEquals(expectedCollectionSingleValue, deserializedCollectionSingleValue);

        // Test JSON array with multiple values input
        String jsonMultipleValues = "[\"test1\",\"test2\",\"test3\"]";
        TestCollection testCollectionMultipleValues = new TestCollection();
        JsonParser parserMultipleValues = mapper.getFactory().createParser(jsonMultipleValues);
        StringCollectionDeserializer stringCollectionDeserializerMultipleValues = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                null, null);
        List<String> deserializedCollectionMultipleValues = stringCollectionDeserializerMultipleValues.deserialize(parserMultipleValues, null);
        List<String> expectedCollectionMultipleValues = Arrays.asList("test1", "test2", "test3");
        assertEquals(expectedCollectionMultipleValues, deserializedCollectionMultipleValues);

        // Test JSON array with null value input
        String jsonArrayWithNullValue = "[\"test1\", null, \"test3\"]";
        TestCollection testCollectionArrayWithNullValue = new TestCollection();
        JsonParser parserArrayWithNullValue = mapper.getFactory().createParser(jsonArrayWithNullValue);
        StringCollectionDeserializer stringCollectionDeserializerArrayWithNullValue = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                null, null);
        List<String> deserializedCollectionArrayWithNullValue = stringCollectionDeserializerArrayWithNullValue.deserialize(parserArrayWithNullValue, null);
        List<String> expectedCollectionArrayWithNullValue = Arrays.asList("test1", null, "test3");
        assertEquals(expectedCollectionArrayWithNullValue, deserializedCollectionArrayWithNullValue);

        // Test JSON array with custom value deserializer input
        String jsonArrayWithCustomDeserializer = "[{\"name\": \"test1\"}, {\"name\": \"test2\"}]";
        TestCollection testCollectionArrayWithCustomDeserializer = new TestCollection();
        JsonParser parserArrayWithCustomDeserializer = mapper.getFactory().createParser(jsonArrayWithCustomDeserializer);
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                String value = p.nextTextValue();
                return value.toUpperCase();
            }
        };
        StringCollectionDeserializer stringCollectionDeserializerArrayWithCustomDeserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                stringDeserializer, null);
        List<String> deserializedCollectionArrayWithCustomDeserializer = stringCollectionDeserializerArrayWithCustomDeserializer.deserialize(parserArrayWithCustomDeserializer, null);
        List<String> expectedCollectionArrayWithCustomDeserializer = Arrays.asList("TEST1", "TEST2");
        assertEquals(expectedCollectionArrayWithCustomDeserializer, deserializedCollectionArrayWithCustomDeserializer);

        // Test JSON object with single String value input
        String jsonObjectWithSingleStringValue = "{\"testCollection\": [\"test\"]}";
        TestBean testBeanObjectWithSingleStringValue = new TestBean();
        JsonParser parserObjectWithSingleStringValue = mapper.getFactory().createParser(jsonObjectWithSingleStringValue);
        StringCollectionDeserializer stringCollectionDeserializerObjectWithSingleStringValue = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                null, null);
        JsonDeserializer<TestCollection> testCollectionDeserializer = new StandardDeserializer<TestCollection>(TestCollection.class) {
            @Override
            public TestCollection deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                TestCollection testCollection = new TestCollection();
                TestCollection deserializedTestCollection = stringCollectionDeserializerObjectWithSingleStringValue.deserialize(p, null);
                testCollection.setStrings(deserializedTestCollection);
                return testCollection;
            }
        };
        JsonDeserializer<TestBean> testBeanDeserializer = new StandardDeserializer<TestBean>(TestBean.class) {
            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                TestBean testBean = new TestBean();
                testBean.setTestCollection(testCollectionDeserializer.deserialize(p, ctxt));
                return testBean;
            }
        };
        TestBean deserializedTestBeanObjectWithSingleStringValue = testBeanDeserializer.deserialize(parserObjectWithSingleStringValue, null);
        List<String> expectedCollectionObjectWithSingleStringValue = Arrays.asList("test");
        assertEquals(expectedCollectionObjectWithSingleStringValue, deserializedTestBeanObjectWithSingleStringValue.getTestCollection().getStrings());

        // Test JSON object with multiple String values input
        String jsonObjectWithMultipleStringValues = "{\"testCollection\":[\"test1\", \"test2\", \"test3\"]}";
        TestBean testBeanObjectWithMultipleStringValues = new TestBean();
        JsonParser parserObjectWithMultipleStringValues = mapper.getFactory().createParser(jsonObjectWithMultipleStringValues);
        StringCollectionDeserializer stringCollectionDeserializerObjectWithMultipleStringValues = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class), null, null, null,
                null, null);
        JsonDeserializer<TestCollection> testCollectionDeserializerObjectWithMultipleStringValues = new StandardDeserializer<TestCollection>(TestCollection.class) {
            @Override
            public TestCollection deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                TestCollection testCollection = new TestCollection();
                TestCollection deserializedTestCollection = stringCollectionDeserializerObjectWithMultipleStringValues.deserialize(p, null);
                testCollection.setStrings(deserializedTestCollection);
                return testCollection;
            }
        };
        JsonDeserializer<TestBean> testBeanDeserializerObjectWithMultipleStringValues = new StandardDeserializer<TestBean>(TestBean.class) {
            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                TestBean testBean = new TestBean();
                testBean.setTestCollection(testCollectionDeserializerObjectWithMultipleStringValues.deserialize(p, null));
                return testBean;
            }
        };
        TestBean deserializedTestBeanObjectWithMultipleStringValues = testBeanDeserializerObjectWithMultipleStringValues.deserialize(parserObjectWithMultipleStringValues, null);
        List<String> expectedCollectionObjectWithMultipleStringValues = Arrays.asList("test1", "test2", "test3");
        assertEquals(expectedCollectionObjectWithMultipleStringValues, deserializedTestBeanObjectWithMultipleStringValues.getTestCollection().getStrings());
    }
}