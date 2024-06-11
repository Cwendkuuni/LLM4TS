package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

@JacksonStdImpl
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        List<String> deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [

        testParser.valueString("foo");
        testParser.valueNumberInt(1);
        testParser.valueString("bar");

        deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(3, deserializedList.size());
        Assert.assertEquals("foo", deserializedList.get(0));
        Assert.assertEquals(1, deserializedList.get(1).intValue());
        Assert.assertEquals("bar", deserializedList.get(2));
    }

    @Test
    public void testCollectionDeserializer_delegateDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = new StdClassDeserializer();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        List<String> deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [

        testParser.valueString("foo");
        JsonNode node = testParser.readValueAsTree();
        JsonDeserializer<Object> deserializer = objectMapper.findValueDeserializer(node.getClass(), null);
        testParser.setContextualHandler(deserializer);
        testParser.nextToken(); // consume the :
        testParser.valueString("bar");

        deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(1, deserializedList.size());
        Assert.assertEquals("foo:bar", deserializedList.get(0));
    }

    @Test
    public void testCollectionDeserializer_unwrapSingle() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = Boolean.TRUE;
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        List<String> deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [
        testParser.valueString("foo");

        deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(1, deserializedList.size());
        Assert.assertEquals("foo", deserializedList.get(0));
    }

    @Test
    public void testCollectionDeserializer_valueTypeDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, Number.class);
        JsonDeserializer<Object> valueDeserializer = new NumberDeserializer();
        TypeDeserializer valueTypeDeserializer = new EnumDeserializer(MyEnum.class);
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        List<Number> deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [
        testParser.valueNumberInt(1);
        testParser.valueNumberInt(2);

        deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(2, deserializedList.size());
        Assert.assertEquals(1, deserializedList.get(0).intValue());
        Assert.assertEquals(2, deserializedList.get(1).intValue());
    }

    @Test
    public void testCollectionDeserializer_valueDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, null, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        List<String> deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [
        testParser.valueString("foo");

        deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(1, deserializedList.size());
        Assert.assertEquals("foo", deserializedList.get(0));
    }

    @Test
    public void testCollectionDeserializer_valueInstantiator() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = new StdValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws IOException {
                return new MyClass();
            }
        };
        JsonDeserializer<Object> delegateDeserializer = null;
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        List<MyClass> deserializedList = (List<MyClass>) collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [
        testParser.valueString("foo");

        deserializedList = (List<MyClass>) collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(1, deserializedList.size());
        Assert.assertNotNull(deserializedList.get(0));
        Assert.assertEquals("foo", deserializedList.get(0).toString());
    }

    @Test
    public void testCollectionDeserializer_valueDeserializerAndValueTypeDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, MyComplexClass.class);
        JsonDeserializer<Object> valueDeserializer = new MyComplexClassDeserializer();
        TypeDeserializer valueTypeDeserializer = new MyComplexClassTypeDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        List<MyComplexClass> deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [
        testParser.valueNumberInt(1);
        testParser.valueNumberInt(2);

        deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(2, deserializedList.size());
        for (int i = 0; i < deserializedList.size(); i++) {
            MyComplexClass deserializedComplexClass = deserializedList.get(i);
            Assert.assertNotNull(deserializedComplexClass);
            Assert.assertEquals(i + 1, deserializedComplexClass.getValue1());
            Assert.assertEquals(i + 2, deserializedComplexClass.getValue2());
        }
    }

    @Test
    public void testCollectionDeserializer_valueDeserializerAndValueTypeDeserializerWithDelegate() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, MyComplexClass.class);
        JsonDeserializer<Object> valueDeserializer = new MyComplexClassDeserializer();
        TypeDeserializer valueTypeDeserializer = new MyComplexClassTypeDeserializer();
        ValueInstantiator valueInstantiator = new StdValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = new MyComplexClassDeserializer();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        List<MyComplexClass> deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(0, deserializedList.size());

        testParser.nextToken(); // consume the [
        testParser.valueNumberInt(1);
        JsonNode node = testParser.readValueAsTree();
        JsonDeserializer<Object> deserializer = objectMapper.findValueDeserializer(node.getClass(), null);
        testParser.setContextualHandler(deserializer);
        testParser.nextToken(); // consume the :
        testParser.valueNumberInt(2);

        deserializedList = collectionDeserializer.deserialize(testParser, objectMapper.getDeserializationContext());

        Assert.assertNotNull(deserializedList);
        Assert.assertEquals(1, deserializedList.size());
        MyComplexClass deserializedComplexClass = deserializedList.get(0);
        Assert.assertNotNull(deserializedComplexClass);
        Assert.assertEquals(1, deserializedComplexClass.getValue1());
        Assert.assertEquals(2, deserializedComplexClass.getValue2());
    }

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    static class MyClass {
        @Override
        public String toString() {
            return "MyClass";
        }
    }

    static class MyComplexClass {
        private int value1;
        private int value2;

        public MyComplexClass() {
        }

        public MyComplexClass(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        public int getValue1() {
            return value1;
        }

        public int getValue2() {
            return value2;
        }
    }

    static class MyComplexClassDeserializer extends StdDeserializer<MyComplexClass> {
        public MyComplexClassDeserializer() {
            this(null);
        }

        public MyComplexClassDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public MyComplexClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            int value1 = p.nextIntValue();
            int value2 = p.nextIntValue();
            return new MyComplexClass(value1, value2);
        }
    }

    static class MyComplexClassTypeDeserializer extends TypeDeserializer {
        @Override
        public MyComplexClass deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
            int value1 = p.nextIntValue();
            int value2 = p.nextIntValue();
            return new MyComplexClass(value1, value2);
        }
    }

    static class StdValueInstantiator extends ValueInstantiator {
        @Override
        public Object createValueInstance(DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    static JsonParser testParser;
    static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() throws IOException {
        testParser = new ObjectMapper().getFactory().createParser(new StringReader("["));
        objectMapper = new ObjectMapper();
    }
}