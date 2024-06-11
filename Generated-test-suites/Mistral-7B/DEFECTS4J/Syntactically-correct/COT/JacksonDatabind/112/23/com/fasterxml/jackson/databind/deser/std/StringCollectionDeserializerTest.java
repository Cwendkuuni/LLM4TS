package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util. Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class StringCollectionDeserializerTest {

    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws JsonProcessingException {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
    }

    // Basic Test Case
    @Test
    public void testDeserializeNormalCase() throws Exception {
        String jsonString = "[\"Hello\",\"World\"]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};
        Collection<String> result = objectMapper.readValue(jsonString, typeRef);
        assertEquals(Arrays.asList("Hello", "World"), result);
    }

    // Test deserialization with null value
    @Test
    public void testDeserializeNullValue() throws Exception {
        String jsonString = "[null]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};
        Collection<String> result = objectMapper.readValue(jsonString, typeRef);
        assertNull(result);
    }

    // Test deserialization with empty array
    @Test
    public void testDeserializeEmptyArray() throws Exception {
        String jsonString = "[]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};
        Collection<String> result = objectMapper.readValue(jsonString, typeRef);
        assertTrue(result.isEmpty());
    }

    // Test deserialization with custom deserializer
    @Test
    public void testDeserializeWithCustomDeserializer() throws Exception {
        String valueDeserializerClassName = "com.example.CustomStringDeserializer";
        String jsonString = "[\"Hello\",{\"class\":\"" + valueDeserializerClassName + "\",\"value\":\"Test\"}]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};

        JsonDeserializer<String> customDeserializer = objectMapper.findValueDeserializer(String.class, null);
        ContextualDeserializer contextualDeserializer = objectMapper.findContextualDeserializer(JsonDeserializer.None.class, JsonDeserializer.None.class, null);
        contextualDeserializer = contextualDeserializer.withValueDeserializer(customDeserializer);
        ObjectMapper customObjectMapper = new ObjectMapper(jsonFactory);
        customObjectMapper.setDefaultUseWrapper(false);
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_UNPARSED_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.READ_UNKNOWN_ENUMS_USING_DEFAULT_VALUE, true);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        customObjectMapper.findAndRegisterDeserializers(customObjectMapper.getClass());
        customObjectMapper.setBeanDescriptionVisibility(JsonAutoDetect.Visibility.ANY);

        Collection<String> result = customObjectMapper.readValue(jsonString, typeRef);
        assertEquals(Arrays.asList("Hello", "Test"), result);
    }

    // Test deserialization with custom deserializer and null value
    @Test
    public void testDeserializeWithCustomDeserializerAndNullValue() throws Exception {
        String valueDeserializerClassName = "com.example.CustomStringDeserializer";
        String jsonString = "[null,{\"class\":\"" + valueDeserializerClassName + "\",\"value\":\"Test\"}]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};

        JsonDeserializer<String> customDeserializer = objectMapper.findValueDeserializer(String.class, null);
        ContextualDeserializer contextualDeserializer = objectMapper.findContextualDeserializer(JsonDeserializer.None.class, JsonDeserializer.None.class, null);
        contextualDeserializer = contextualDeserializer.withValueDeserializer(customDeserializer);
        ObjectMapper customObjectMapper = new ObjectMapper(jsonFactory);
        customObjectMapper.setDefaultUseWrapper(false);
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_UNPARSED_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.READ_UNKNOWN_ENUMS_USING_DEFAULT_VALUE, true);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        customObjectMapper.findAndRegisterDeserializers(customObjectMapper.getClass());
        customObjectMapper.setBeanDescriptionVisibility(JsonAutoDetect.Visibility.ANY);

        Collection<String> result = customObjectMapper.readValue(jsonString, typeRef);
        assertNull(result);
    }

    // Test deserialization with custom instantiator
    @Test
    public void testDeserializeWithCustomInstantiator() throws Exception {
        String instantiatorClassName = "com.example.CustomStringInstantiator";
        String jsonString = "[\"Hello\",\"World\"]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};

        ValueInstantiator valueInstantiator = objectMapper.findValueInstantiator(String[].class);
        ContextualDeserializer contextualDeserializer = objectMapper.findContextualDeserializer(JsonDeserializer.None.class, JsonDeserializer.None.class, null);
        contextualDeserializer = contextualDeserializer.withValueInstantiator(valueInstantiator);
        ObjectMapper customObjectMapper = new ObjectMapper(jsonFactory);
        customObjectMapper.setDefaultUseWrapper(false);
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_UNPARSED_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        customObjectMapper.setBeanDescriptionVisibility(JsonAutoDetect.Visibility.ANY);
        customObjectMapper.setValueInstantiator(new CustomStringListInstantiator(instantiatorClassName));

        Collection<String> result = customObjectMapper.readValue(jsonString, typeRef);
        assertEquals(Arrays.asList("Hello", "World"), result);
    }

    // Test deserialization with delegate deserializer
    @Test
    public void testDeserializeWithDelegateDeserializer() throws Exception {
        String delegateDeserializerClassName = "com.example.DelegateStringDeserializer";
        String jsonString = "[\"Hello\",\"World\"]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};

        JsonDeserializer<String> delegateDeserializer = objectMapper.findDeserializerByContextualization(Object.class, null);
        ContextualDeserializer contextualDeserializer = objectMapper.findContextualDeserializer(JsonDeserializer.None.class, JsonDeserializer.None.class, null);
        contextualDeserializer = contextualDeserializer.withDelegateDeserializer(delegateDeserializer);
        ObjectMapper customObjectMapper = new ObjectMapper(jsonFactory);
        customObjectMapper.setDefaultUseWrapper(false);
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_UNPARSED_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        customObjectMapper.setBeanDescriptionVisibility(JsonAutoDetect.Visibility.ANY);

        JsonDeserializer<Object> objectDeserializer = new ObjectMapper().findDeserializerByClass(DelegateStringDeserializer.class);
        ContextualDeserializer contextualDeserializerObj = objectMapper.findContextualDeserializer(JsonDeserializer.None.class, JsonDeserializer.None.class, null);
        contextualDeserializerObj = contextualDeserializerObj.withDelegateDeserializer(objectDeserializer);
        customObjectMapper.setDefaultUseWrapper(false);
        customObjectMapper.setValueDeserializer(contextualDeserializerObj);

        Collection<String> result = customObjectMapper.readValue(jsonString, typeRef);
        assertEquals(Arrays.asList("Hello", "World"), result);
    }

    // Test deserialization with missing deserializer
    @Test(expected = JsonProcessingException.class)
    public void testDeserializeWithMissingDeserializer() throws Exception {
        String jsonString = "[{\"class\":\"com.example.NonExistentDeserializer\",\"value\":\"Test\"}]";
        TypeReference<Collection<String>> typeRef = new TypeReference<Collection<String>>() {};

        ObjectMapper customObjectMapper = new ObjectMapper(jsonFactory);
        customObjectMapper.setDefaultUseWrapper(false);
        customObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_UNPARSED_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        customObjectMapper.setDeserializationFeature(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        customObjectMapper.setBeanDescriptionVisibility(JsonAutoDetect.Visibility.ANY);

        Collection<String> result = customObjectMapper.readValue(jsonString, typeRef);
        fail("Should throw JsonProcessingException when deserializing with missing deserializer");
    }
}