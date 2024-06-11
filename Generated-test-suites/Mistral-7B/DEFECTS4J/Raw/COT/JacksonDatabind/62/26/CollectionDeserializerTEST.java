package com.example.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.TypeDeserializerForElement;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class CollectionDeserializerTest {

    private static final JavaType LIST_TYPE = ObjectMapper.getDefaultInstance().getTypeFactory()
            .constructParametricType(List.class, String.class);
    private static final JavaType COLLECTION_TYPE = ObjectMapper.getDefaultInstance().getTypeFactory()
            .constructCollectionType(List.class, Object.class);
    private static final ObjectMapper MAPPER = ObjectMapper.getDefaultInstance();
    private CollectionDeserializer collectionDeserializer;
    private JsonDeserializer<String> stringDeserializer;
    private TypeDeserializer stringTypeDeserializer;
    private ValueInstantiator valueInstantiator;

    @Before
    public void setup() {
        stringDeserializer = mock(JsonDeserializer.class);
        stringTypeDeserializer = mock(TypeDeserializer.class);
        when(stringDeserializer.getType()).thenReturn(Object.class);
        when(stringTypeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any()))
                .thenAnswer(invocation -> invocation.getArgument(0).readValueAs(List.class));
        valueInstantiator = mock(ValueInstantiator.class);
        when(valueInstantiator.createUsingDefault(any(DeserializationContext.class)))
                .thenReturn(new ArrayList<>());

        collectionDeserializer = new CollectionDeserializer(
                COLLECTION_TYPE, stringDeserializer, stringTypeDeserializer, valueInstantiator, null, null);
    }

    @Test
    public void testCreateContextual() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        Boolean unwrapSingle = false;

        CollectionDeserializer deserializer = collectionDeserializer.withResolved(delegateDeserializer, null, null, unwrapSingle);
        assertEquals(delegateDeserializer, deserializer.getDelegateDeserializer());
        assertNull(deserializer.getValueDeserializer());
        assertNull(deserializer.getValueTypeDeserializer());
        assertNull(deserializer.getValueInstantiator());

        verifyZeroInteractions(context, property, stringDeserializer, stringTypeDeserializer, valueInstantiator);
    }

    @Test
    public void testDeserializeFromArray() throws IOException {
        JsonParser parser = MAPPER.getFactory()
                .createParser("[\"apple\", \"banana\", \"orange\"]");
        Collection<Object> result = collectionDeserializer.deserialize(parser, null);

        List<String> expectedList = Arrays.asList("apple", "banana", "orange");
        assertEquals(expectedList, result);
    }

    @Test
    public void testDeserializeFromArrayWithTypeDeserializer() throws IOException {
        JsonParser parser = MAPPER.getFactory()
                .createParser("[1, 2, 3]");
        TypeDeserializerForElement typeDeserializer = mock(TypeDeserializerForElement.class);

        JsonDeserializer<Object> deserializer = collectionDeserializer.withResolved(null, null, typeDeserializer, null);
        Collection<Object> result = deserializer.deserialize(parser, null);

        List<Integer> expectedList = Arrays.asList(1, 2, 3);
        assertEquals(expectedList, result);
        verify(typeDeserializer).deserializeTypedFromArray(parser, null);
    }

    @Test
    public void testDeserializeFromObject() throws IOException {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("value", "[\"apple\", \"banana\", \"orange\"]");

        Collection<Object> result = collectionDeserializer.deserialize(MAPPER.readTree(node), null);

        List<String> expectedList = Arrays.asList("apple", "banana", "orange");
        assertEquals(expectedList, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFromNull() throws IOException {
        JsonParser parser = null;
        collectionDeserializer.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFromEmptyString() throws IOException {
        JsonParser parser = MAPPER.getFactory()
                .createParser("\"\"");
        collectionDeserializer.deserialize(parser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeFromNonArray() throws IOException {
        JsonParser parser = MAPPER.getFactory()
                .createParser("{\"value\":\"apple\"}");
        collectionDeserializer.deserialize(parser, null);
    }

    @Test
    public void testIsCachable() {
        assertTrue(collectionDeserializer.isCachable());
    }

    @Test
    public void testCreateContextualWithDelegateDeserializer() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        Boolean unwrapSingle = false;

        CollectionDeserializer deserializer = collectionDeserializer.withResolved(delegateDeserializer, null, null, unwrapSingle);

        assertEquals(delegateDeserializer, deserializer.getDelegateDeserializer());
        assertEquals(stringDeserializer, deserializer.getValueDeserializer());
        assertNull(deserializer.getValueTypeDeserializer());
        assertNull(deserializer.getValueInstantiator());

        verifyZeroInteractions(context, property, stringDeserializer, stringTypeDeserializer, valueInstantiator);
    }
}