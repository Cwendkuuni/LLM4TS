package com.example.test; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test class for StringCollectionDeserializer
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private JsonDeserializer<String> stringDeserializer;
    private ContextualDeserializer stringCollectionDeserializer;
    private ValueInstantiator valueInstantiator;
    private NullValueProvider nullValueProvider;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        stringDeserializer = new StringDeserializer();
        valueInstantiator = mock(ValueInstantiator.class);
        nullValueProvider = mock(NullValueProvider.class);
        when(valueInstantiator.createValueInstance(JsonTypeFactory.valueToValueType(Collection.class)))
            .thenReturn(new ArrayList<>());
        when(nullValueProvider.getNullValue(Mockito.any(DeserializationContext.class)))
            .thenReturn(null);
        stringCollectionDeserializer = new StringCollectionDeserializer(
                JsonTypeFactory.constructType(Collection.class, String.class),
                stringDeserializer, valueInstantiator, null, nullValueProvider, null);
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        String inputJson = "[]";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonFactory.createParser(jsonNode), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeSingleElementArray() throws Exception {
        String inputJson = "[\"element\"]";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonFactory.createParser(jsonNode), null);
        assertEquals(Arrays.asList("element"), result);
    }

    @Test
    public void testDeserializeMultipleElementsArray() throws Exception {
        String inputJson = "[\"element1\", \"element2\", \"element3\"]";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonFactory.createParser(jsonNode), null);
        assertEquals(Arrays.asList("element1", "element2", "element3"), result);
    }

    @Test
    public void testDeserializeWithNullValue() throws Exception {
        String inputJson = "[null]";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonFactory.createParser(jsonNode), null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithEmptyString() throws Exception {
        String inputJson = "[\"\"]";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonFactory.createParser(jsonNode), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidJson() throws Exception {
        String inputJson = "invalidJson";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        stringCollectionDeserializer.deserialize(jsonFactory.createParser(jsonNode), null);
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws Exception {
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        when(delegateDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
            .thenReturn(new ObjectNode(jsonFactory));
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JsonTypeFactory.constructType(Collection.class, String.class),
                null, null, delegateDeserializer, null, null);
        String inputJson = "[\"element1\", \"element2\", \"element3\"]";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        Collection<String> result = deserializer.deserialize(jsonFactory.createParser(jsonNode), null);
        assertEquals(Arrays.asList("element1", "element2", "element3"), result);
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        when(customDeserializer.deserialize(Mockito.any(JsonParser.class), Mockito.any(DeserializationContext.class)))
            .thenReturn("customValue");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                JsonTypeFactory.constructType(Collection.class, String.class),
                null, null, null, customDeserializer, null);
        String inputJson = "[\"element1\", \"element2\", \"element3\"]";
        ObjectNode jsonNode = objectMapper.readTree(inputJson);
        Collection<String> result = deserializer.deserialize(jsonFactory.createParser(jsonNode), null);
        List<String> expectedResult = Arrays.asList("customValue", "customValue", "customValue");
        assertEquals(expectedResult, result);
    }
}