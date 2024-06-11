package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase.CollectionReferringAccumulator;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer.CollectionReferring;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CollectionDeserializerTest {
    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private JavaType javaType;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;
    private ValueInstantiator valueInstantiator;
    private JsonDeserializer<Object> delegateDeserializer;
    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        valueDeserializer = mock(JsonDeserializer.class);
        valueTypeDeserializer = mock(TypeDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        delegateDeserializer = mock(JsonDeserializer.class);
        collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);
    }

    @Test
    public void testEmptyArrayDeserialization() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, null, null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testSingleElementDeserialization() throws Exception {
        String element = "element";
        JsonParser jsonParser = jsonFactory.createParser("[\"" + element + "\"]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, null, null);
        assertEquals(Collections.singletonList(element), result);
    }

    @Test
    public void testDeserializationWithValueDeserializer() throws Exception {
        String element = "element";
        when(valueDeserializer.deserialize(eq(jsonFactory), eq(null), any(DeserializationContext.class)))
                .thenReturn(element);

        JsonParser jsonParser = jsonFactory.createParser("[\"" + element + "\"]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, null, null);
        assertEquals(Collections.singletonList(element), result);

        verify(valueDeserializer, times(1)).deserialize(eq(jsonFactory), eq(null), any(DeserializationContext.class));
    }

    @Test
    public void testDeserializationWithTypeDeserializer() throws Exception {
        JsonDeserializer<Integer> integerDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserializeWithType(eq(jsonFactory), eq(null), eq(integerDeserializer)))
                .thenReturn(1);

        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<Integer> result = collectionDeserializer.deserialize(jsonParser, null, integerDeserializer);
        assertEquals(Collections.emptyList(), result);

        jsonParser = jsonFactory.createParser("[]");
        result = collectionDeserializer.deserialize(jsonParser, null, integerDeserializer);
        assertEquals(Collections.emptyList(), result);

        jsonParser = jsonFactory.createParser("[1]");
        result = collectionDeserializer.deserialize(jsonParser, null, integerDeserializer);
        assertEquals(Collections.singletonList(1), result);

        jsonParser = jsonFactory.createParser("[1, 2]");
        result = collectionDeserializer.deserialize(jsonParser, null, integerDeserializer);
        assertEquals(Arrays.asList(1, 2), result);

        verify(valueDeserializer, times(3)).deserializeWithType(eq(jsonFactory), eq(null), eq(integerDeserializer));
    }

    @Test
    public void testDeserializationWithDefaultValue() throws Exception {
        Collection<String> defaultResult = new ArrayList<>();
        defaultResult.add("default");

        JsonParser jsonParser = jsonFactory.createParser("[]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, null, null);
        assertEquals(defaultResult, result);

        jsonParser = jsonFactory.createParser("[]");
        result = collectionDeserializer.deserialize(jsonParser, null, defaultResult);
        assertEquals(defaultResult, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayDeserialization() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("{\"element\": \"value\"}");
        collectionDeserializer.deserialize(jsonParser, null, null);
    }

    @Test
    public void testDeserializationWithDelegateDeserializer() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[1, {\"id\": 2}]");
        when(delegateDeserializer.deserialize(eq(jsonFactory), eq(null), any(DeserializationContext.class)))
                .thenReturn(2);

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, null, delegateDeserializer, null);
        assertEquals(Arrays.asList(1, 2), result);

        verify(delegateDeserializer, times(1)).deserialize(eq(jsonFactory), eq(null), any(DeserializationContext.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithUnresolvedForwardReference() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[{ \"$ref\": \"#id1\" }]");
        when(valueDeserializer.resolveForwardReference(eq("#id1"), anyObject()))
                .thenThrow(new UnresolvedForwardReference("Unresolved forward reference", null));

        collectionDeserializer.deserialize(jsonParser, null, null);
    }

    @Test
    public void testDeserializationWithEmptyString() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[\"\"]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, null, null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testDeserializationWithNullValue() throws Exception {
        JsonParser jsonParser = jsonFactory.createParser("[null]");
        Collection<String> result = collectionDeserializer.deserialize(jsonParser, null, null);
        assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testDeserializationWithJsonMappingException() throws Exception {
        when(valueDeserializer.deserialize(eq(jsonFactory), eq(null), any(DeserializationContext.class)))
                .thenThrow(new JsonMappingException("Error"));

        JsonParser jsonParser = jsonFactory.createParser("[\"" + JsonMappingException.class.getName() + "\"]");
        try {
            collectionDeserializer.deserialize(jsonParser, null, null);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Error", e.getCause().getMessage());
        }
    }
}