package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Test
public class StringCollectionDeserializerTest extends BaseTest {

    @Test
    public void testStringCollectionDeserializer() throws IOException {
        // Test default deserialization
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, null);
        Collection<String> collection = deserializer.deserialize(createParser("['foo', 'bar']"), null);
        Assert.assertEquals(collection.size(), 2);
        Assert.assertEquals(collection.toString(), Arrays.asList("foo", "bar").toString());

        // Test deserialization with custom value deserializer
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("customValue");
        StringCollectionDeserializer deserializerWithValueDeserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, valueDeserializer, null, null);
        collection = deserializerWithValueDeserializer.deserialize(createParser("['foo', 'bar']"), null);
        Assert.assertEquals(collection.size(), 2);
        Assert.assertEquals(collection.get(0), "customValue");
        Assert.assertEquals(collection.get(1), "customValue");

        // Test deserialization with delegate deserializer
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        when(delegateDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(Arrays.asList("delegateValue1", "delegateValue2"));
        StringCollectionDeserializer deserializerWithDelegateDeserializer = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, delegateDeserializer, null, null, null);
        collection = deserializerWithDelegateDeserializer.deserialize(createParser("[]"), null);
        Assert.assertEquals(collection.size(), 2);
        Assert.assertEquals(collection.get(0), "delegateValue1");
        Assert.assertEquals(collection.get(1), "delegateValue2");

        // Test deserialization with value instantiator
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        when(valueInstantiator.createValueInstance(any(DeserializationContext.class)))
                .thenReturn(new ArrayList<>());
        StringCollectionDeserializer deserializerWithValueInstantiator = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                valueInstantiator, null, null, null, null);
        collection = deserializerWithValueInstantiator.deserialize(createParser("[]"), null);
        Assert.assertTrue(collection instanceof ArrayList);
        collection.add("testValue");
        Assert.assertEquals(collection.toString(), Arrays.asList("testValue").toString());

        // Test deserialization with null value provider
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(any(DeserializationContext.class)))
                .thenReturn("nullValue");
        StringCollectionDeserializer deserializerWithNullValueProvider = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, nullValueProvider, null);
        collection = deserializerWithNullValueProvider.deserialize(createParser("[]"), null);
        Assert.assertEquals(collection.size(), 1);
        Assert.assertEquals(collection.get(0), "nullValue");

        // Test deserialization with unwrapSingle feature
        StringCollectionDeserializer deserializerWithUnwrapSingle = new StringCollectionDeserializer(
                ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, Boolean.TRUE);
        collection = deserializerWithUnwrapSingle.deserialize(createParser("'singleValue'"), null);
        Assert.assertEquals(collection.size(), 1);
        Assert.assertEquals(collection.get(0), "singleValue");
    }

    private JsonParser createParser(String json) throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        return mapper.getFactory().createParser(json);
    }
}