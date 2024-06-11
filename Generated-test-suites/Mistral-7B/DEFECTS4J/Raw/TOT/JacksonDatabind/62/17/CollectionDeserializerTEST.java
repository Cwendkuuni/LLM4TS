package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyEmptyArray() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[]"), Collection.class);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testEmptyArrayWithNullValue() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[]"), Collection.class);
        Assert.assertNull(mapper.readValue(new String("null"), Object.class, result.getClass()));
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testEmptyArrayWithSingleValue() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[]"), Collection.class);
        Object singleValue = new Object();
        result.add(singleValue);
        Assert.assertEquals(Arrays.asList(singleValue), result);
    }

    @Test
    public void testEmptyArrayWithSingleValueString() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[]"), Collection.class);
        String singleValue = "stringValue";
        result.add(singleValue);
        Assert.assertEquals(Arrays.asList(singleValue), result);
    }

    @Test
    public void testArrayWithNullValues() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[null, null]"), Collection.class);
        Assert.assertNull(mapper.readValue(new String("null"), Object.class, result.getClass()));
        Assert.assertNull(mapper.readValue(new String("null"), Object.class, result.getClass()));
        Assert.assertEquals(Arrays.asList(null, null), result);
    }

    @Test
    public void testArrayWithSingleObjectValue() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[1]"), Collection.class);
        Assert.assertEquals(Arrays.asList(1), result);
    }

    @Test
    public void testArrayWithSingleStringValue() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[\"stringValue\"]"), Collection.class);
        Assert.assertEquals(Arrays.asList("stringValue"), result);
    }

    @Test
    public void testArrayWithMultipleObjectValues() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[1, 2, 3]"), Collection.class);
        Assert.assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testArrayWithMultipleStringValues() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[\"string1\", \"string2\", \"string3\"]"), Collection.class);
        Assert.assertEquals(Arrays.asList("string1", "string2", "string3"), result);
    }

    @Test
    public void testArrayWithEmptyObjectValue() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[{}, {}]"), Collection.class);
        Assert.assertEquals(Arrays.asList(new Object(), new Object()), result);
    }

    @Test
    public void testArrayWithEmptyStringValue() throws IOException {
        Collection<Object> result = mapper.readValue(new String("[\"\", \"\"]"), Collection.class);
        Assert.assertEquals(Arrays.asList("", ""), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testArrayWithMixedObjectAndStringValues() throws IOException {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        Collection<Object> result = mapper.readValue(new String("[1, \"stringValue\"]"), Collection.class);
    }

    @Test
    public void testArrayWithCustomObjectType() throws IOException {
        Collection<MyClass> result = mapper.readValue(new String("[{\"field\": 1}, {\"field\": 2}]"), Collection.class, MyClass.class);
        Assert.assertEquals(Arrays.asList(new MyClass(1), new MyClass(2)), result);
    }

    @Test
    public void testArrayWithNestedObjects() throws IOException {
        Collection<NestedObject> result = mapper.readValue(new String("[{\"nestedField\": \"value1\"}, {\"nestedField\": \"value2\"}]"), Collection.class, NestedObject.class);
        Assert.assertEquals(Arrays.asList(new NestedObject("value1"), new NestedObject("value2")), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testArrayWithUnresolvedForwardReference() throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_ALLOW_EMPTY_AND_UNRESOLVED_ARRAYS, false);
        Collection<Object> result = mapper.readValue(new String("[{@ref:unresolvedRef}]"), Collection.class);
    }

    @Test
    public void testArrayWithUnresolvedForwardReferenceResolved() throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_ALLOW_EMPTY_AND_UNRESOLVED_ARRAYS, false);
        ObjectMapper mapperWithObjectIdReader = new ObjectMapper(mapper);
        mapperWithObjectIdReader.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializerFactory(Object[].class, new UnresolvedForwardReferenceDeserializerFactory(mapperWithObjectIdReader));
            }
        });
        Collection<Object> result = mapperWithObjectIdReader.readValue(new String("[{@ref:resolvedRef}]"), Collection.class);
        Object resolvedValue = new Object();
        mapperWithObjectIdReader.getFactory().getObjectIdReader().resolveForwardReference("resolvedRef", resolvedValue);
        mapperWithObjectIdReader.readValue(new String("null"), Object.class, result.getClass());
        mapperWithObjectIdReader.readValue(new String("null"), Object.class, result.getClass());
        Assert.assertEquals(Arrays.asList(resolvedValue, resolvedValue), result);
    }

    @Test
    public void testArrayWithUnresolvedForwardReferenceWithCustomType() throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_ALLOW_EMPTY_AND_UNRESOLVED_ARRAYS, false);
        ObjectMapper mapperWithObjectIdReader = new ObjectMapper(mapper);
        mapperWithObjectIdReader.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addDeserializerFactory(MyClass[].class, new UnresolvedForwardReferenceDeserializerFactory(mapperWithObjectIdReader));
            }
        });
        Collection<MyClass> result = mapperWithObjectIdReader.readValue(new String("[{@ref:resolvedRef}]"), Collection.class, MyClass.class);
        Object resolvedValue = new MyClass(1);
        mapperWithObjectIdReader.getFactory().getObjectIdReader().resolveForwardReference("resolvedRef", resolvedValue);
        Assert.assertEquals(Collections.singletonList(resolvedValue), result);
    }

    static class MyClass {
        private final int field;

        public MyClass(int field) {
            this.field = field;
        }
    }

    static class NestedObject {
        private final String nestedField;

        public NestedObject(String nestedField) {
            this.nestedField = nestedField;
        }
    }

    static class UnresolvedForwardReferenceDeserializerFactory extends KeyDeserializer.None {
        private final ObjectMapper delegate;

        public UnresolvedForwardReferenceDeserializerFactory(ObjectMapper delegate) {
            this.delegate = delegate;
        }

        @Override
        public ObjectDeserializer createCachable(DeserializationContext ctxt) throws JsonMappingException {
            return new ObjectDeserializer() {
                @Override
                public Object deserialize(JsonParser p, DeserializationContext ctxt)
                        throws IOException {
                    return delegate.getFactory().getObjectIdReader().resolveForwardReference(ctxt.getCurrentName(), null);
                }
            };
        }
    }
}