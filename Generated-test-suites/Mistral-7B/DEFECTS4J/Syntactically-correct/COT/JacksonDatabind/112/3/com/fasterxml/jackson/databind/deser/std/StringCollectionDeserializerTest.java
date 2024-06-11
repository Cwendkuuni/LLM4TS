package com.example.test; // Replace com.example.test with the package of your test class

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test file for class StringCollectionDeserializer
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    

    @Test
    public void testStringCollectionDeserializerCreation() {
        // Test default constructor
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Assert.assertNotNull(deserializer);

        // Test constructor with provided parameters
        JavaType collectionType = ObjectMapper.ValueType.collector(Collection.class, ElementType.STRING);
        StringCollectionDeserializer deserializerWithParams = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        Assert.assertNotNull(deserializerWithParams);
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Assert.assertFalse(deserializer.isCachable());
    }

    @Test
    public void testCreateContextual() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        when(ctxt.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(ctxt.handleSecondaryContextualization(any(JsonDeserializer.class), eq(property), any(JavaType.class)))
                .thenReturn(deserializer);

        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nuller = mock(NullValueProvider.class);
        Boolean unwrapSingle = Boolean.FALSE;

        JsonDeserializer<String> contextualDeserializer = deserializer.createContextual(ctxt, property);

        verify(ctxt).handleSecondaryContextualization(eq(delegateDeserializer), eq(property), any(JavaType.class));
        Assert.assertEquals(deserializer, contextualDeserializer);
    }

    @Test
    public void testGetContentDeserializer() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<String> contentDeserializer = deserializer.getContentDeserializer();
        Assert.assertNotNull(contentDeserializer);
    }

    @Test
    public void testgetValueInstantiator() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        ValueInstantiator valueInstantiator = deserializer.getValueInstantiator();
        Assert.assertNotNull(valueInstantiator);
    }

    @Test
    public void testDeserialize() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = objectMapper.getFactory().createParser("[\"test\", \"test2\"]");

        Collection<String> result = deserializer.deserialize(parser, new DeserializationContext(), new ArrayList<>());
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("test", result.get(0));
        Assert.assertEquals("test2", result.get(1));
    }

    @Test
    public void testDeserializeWithCustomValueDeserializer() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<String> customValueDeserializer = mock(JsonDeserializer.class);

        JsonParser parser = objectMapper.getFactory().createParser("[\"test\", \"test2\"]");

        when(customValueDeserializer.deserialize(parser, any(DeserializationContext.class)))
                .thenReturn("testCustom");

        deserializer._valueDeserializer = customValueDeserializer;

        Collection<String> result = deserializer.deserialize(parser, new DeserializationContext(), new ArrayList<>());
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("testCustom", result.get(0));
        Assert.assertEquals("test2", result.get(1));
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = objectMapper.getFactory().createParser("[]");

        NullValueProvider nullValueProvider = ObjectMapper.NullValueProvider.INSTANCE;
        when(nullValueProvider.getNullValue(any(DeserializationContext.class)))
                .thenReturn("nullValue");

        deserializer._nullProvider = nullValueProvider;

        Collection<String> result = deserializer.deserialize(parser, new DeserializationContext(), new ArrayList<>());
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("nullValue", result.get(0));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = objectMapper.getFactory().createParser("\"invalidJson\"");

        Collection<String> result = deserializer.deserialize(parser, new DeserializationContext(), new ArrayList<>());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithArrayOfObjects() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = objectMapper.getFactory().createParser("[{\"name\": \"test\"}]");

        Collection<String> result = deserializer.deserialize(parser, new DeserializationContext(), new ArrayList<>());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithEmptyArray() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonParser parser = objectMapper.getFactory().createParser("[]");

        Collection<String> result = deserializer.deserialize(parser, new DeserializationContext(), new ArrayList<>());
    }

    @Test
    public void testDeserializeUsingDelegate() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        Collection<String> expectedResult = Collections.singletonList("test");

        JsonParser parser = objectMapper.getFactory().createParser("[\"test\"]");

        when(delegateDeserializer.deserialize(parser, any(DeserializationContext.class)))
                .thenReturn(Collections.singletonMap("name", "test"));

        deserializer._delegateDeserializer = delegateDeserializer;

        Collection<String> result = deserializer.deserialize(parser, new DeserializationContext(), new ArrayList<>());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException, JsonProcessingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Collection<String> expectedResult = Collections.singletonList("test");

        JsonParser parser = objectMapper.getFactory().createParser("[\"test\"]");

        when(typeDeserializer.deserializeTypedFromArray(parser, any(DeserializationContext.class)))
                .thenReturn("test");

        Collection<String> result = deserializer.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);
        Assert.assertEquals(expectedResult, result);
    }

    
}