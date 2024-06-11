package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ValueDeserializer;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class StringCollectionDeserializerTest {

    @Mock
    JsonParser parser;

    @Mock
    DeserializationContext ctxt;

    @Mock
    ValueInstantiator valueInstantiator;

    @Mock
    JsonDeserializer<Object> delegateDeserializer;

    @Mock
    JsonDeserializer<String> valueDeserializer;

    private StringCollectionDeserializer stringCollectionDeserializer;

    @Before
    public void setup() {
        JavaType collectionType = null;
        NullValueProvider nullValueProvider = null;
        Boolean unwrapSingle = null;
        stringCollectionDeserializer = new StringCollectionDeserializer(collectionType, valueDeserializer,
                valueInstantiator);
    }

    @Test
    public void testDeserialize_withValidString_shouldReturnCollectionOfString() throws IOException {
        Collection<String> expected = new ArrayList<>();
        expected.add("testString1");
        expected.add("testString2");

        parser.nextToken();
        parser.nextToken(); // move to first text value
        parser.getText(); // get first text value
        parser.nextToken(); // move to second text value
        parser.getText(); // get second text value
        parser.nextToken(); // move to end array

        Collection<String> result = stringCollectionDeserializer.deserialize(parser, ctxt);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_withInvalidValue_shouldThrowJsonParseException() throws IOException {
        parser.nextToken();
        parser.nextToken(); // move to invalid value

        stringCollectionDeserializer.deserialize(parser, ctxt);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_withInvalidToken_shouldThrowJsonParseException() throws IOException {
        parser.nextToken(); // move to invalid token

        stringCollectionDeserializer.deserialize(parser, ctxt);
    }

    @Test
    public void testDeserialize_withNullValueAndSkipNullValuesEnabled_shouldReturnCollectionOfStringWithoutNullValue()
            throws IOException {
        Collection<String> expected = new ArrayList<>();
        expected.add("testString");

        parser.nextToken();
        parser.nextToken(); // move to null value
        parser.nextToken(); // move to string value
        parser.getText(); // get string value
        parser.nextToken(); // move to end array

        stringCollectionDeserializer = new StringCollectionDeserializer(JavaType.nullType(), valueDeserializer,
                valueInstantiator, delegateDeserializer, NullValueProvider.skipper(), Boolean.FALSE);

        Collection<String> result = stringCollectionDeserializer.deserialize(parser, ctxt);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_withNullValueAndSkipNullValuesDisabled_shouldReturnCollectionOfStringWithNullValue()
            throws IOException {
        Collection<String> expected = new ArrayList<>();
        expected.add(null);

        parser.nextToken();
        parser.nextToken(); // move to null value
        parser.nextToken(); // move to end array

        stringCollectionDeserializer = new StringCollectionDeserializer(JavaType.nullType(), valueDeserializer,
                valueInstantiator, delegateDeserializer, NullValueProvider.forValue(null), Boolean.FALSE);

        Collection<String> result = stringCollectionDeserializer.deserialize(parser, ctxt);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithType_shouldDeserizalizeWithType() throws IOException {
        Object result = stringCollectionDeserializer.deserializeWithType(parser, ctxt, null);

        Assert.assertNull(result);
    }

    @Test
    public void testGetPropertyDeserializer_shouldReturnNull() {
        ValueDeserializer<?> result = stringCollectionDeserializer.getPropertyDeserializer();

        Assert.assertNull(result);
    }

    @Test
    public void testGetValueInstantiator_shouldReturnValueInstantiator() {
        ValueInstantiator result = stringCollectionDeserializer.getValueInstantiator();

        Assert.assertEquals(valueInstantiator, result);
    }

    @Test
    public void testIsCachable_withCustomDeserializers_shouldReturnFalse() {
        boolean result = stringCollectionDeserializer.isCachable();

        Assert.assertFalse(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_withInvalidDelegateCreator_shouldThrowJsonMappingException()
            throws JsonMappingException {
        BeanProperty property = null;

        stringCollectionDeserializer.createContextual(ctxt, property);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_withNonArray_shouldThrowJsonMappingException() throws IOException {
        Collection<String> result = new ArrayList<>();

        parser.nextToken(); // move to non-array value

        stringCollectionDeserializer.handleNonArray(parser, ctxt, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray_withNonWrappingValue_shouldThrowJsonMappingException() throws IOException {
        Collection<String> result = new ArrayList<>();

        parser.nextToken(); // move to non-wrapping value

        stringCollectionDeserializer.handleNonArray(parser, ctxt, result);
    }

    @Test
    public void testDeserializeUsingCustom_withValidValues_shouldReturnCollectionOfString() throws IOException {
        Collection<String> expected = new ArrayList<>();
        expected.add("testString1");
        expected.add("testString2");

        parser.nextTextValue(); // move to first text value
        parser.getText(); // get first text value
        parser.nextToken(); // move to second text value
        parser.getText(); // get second text value
        parser.nextToken(); // move to end array

        Collection<String> result = stringCollectionDeserializer.deserializeUsingCustom(parser, ctxt,
                new ArrayList<String>(), valueDeserializer);

        Assert.assertEquals(expected, result);
    }

}