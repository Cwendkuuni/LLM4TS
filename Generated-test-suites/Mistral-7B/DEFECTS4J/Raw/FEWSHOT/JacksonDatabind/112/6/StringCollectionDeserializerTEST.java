package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Test cases for {@link StringCollectionDeserializer} class.
 */
public class StringCollectionDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(MAPPER.createParser("[]"), MAPPER.getDeserializationContext());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializeSingleValueAsArray() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);
        JsonNode node = MAPPER.readTree("[\"foo\"]");
        Collection<String> result = deserializer.deserialize(MAPPER.createParser(node), MAPPER.getDeserializationContext());
        assertEquals(Arrays.asList("foo"), result);
    }

    @Test
    public void testDeserializeWithCustomValueDeserializer() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class);
        JsonDeserializer<String> customValueDeserializer = mock(JsonDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueInstantiator, null, null, customValueDeserializer, null);
        String[] expectedValues = new String[]{"foo1", "foo2"};
        when(customValueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(expectedValues);
        Collection<String> result = deserializer.deserialize(MAPPER.createParser("[\"" + Arrays.toString(expectedValues) + "\"]"), MAPPER.getDeserializationContext());
        assertEquals(Arrays.asList(expectedValues), result);
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueInstantiator, delegateDeserializer, null, null, null);
        ObjectNode node = MAPPER.createObjectNode();
        node.put("items", MAPPER.createParser(MAPPER.writeValueAsString(new String[]{"foo1", "foo2"})));
        Collection<String> result = deserializer.deserialize(MAPPER.createParser(node), MAPPER.getDeserializationContext());
        assertEquals(Arrays.asList("foo1", "foo2"), result);
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, nullValueProvider);
        Collection<String> result = deserializer.deserialize(MAPPER.createParser("[]"), MAPPER.getDeserializationContext());
        assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testDeserializeWithUnmarshallingFeature() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class);
        Boolean unwrapSingle = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);
        Collection<String> result = deserializer.deserialize(MAPPER.createParser("\"foo\""), MAPPER.getDeserializationContext());
        assertEquals(Collections.singletonList("foo"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidJson() throws IOException {
        JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);
        deserializer.deserialize(MAPPER.createParser("{foo: \"bar\"}"), MAPPER.getDeserializationContext());
    }
}