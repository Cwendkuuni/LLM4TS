package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws IOException {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        ValueDeserializer valueDeserializer = new StringDeserializer();
        JsonDeserializer<Object> delegateDeserializer = null;
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = null;
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        // Test createContextual
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        CollectionDeserializer contextualDeserializer = deserializer.createContextual(context, property);
        assertEquals(deserializer, contextualDeserializer);

        // Test getContentDeserializer
        JsonDeserializer<Object> contentDeserializer = contextualDeserializer.getContentDeserializer();
        assertEquals(valueDeserializer, contentDeserializer);

        // Test deserialize
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Collection<String> result = new ArrayList<>();
        Collection<String> deserializedCollection = contextualDeserializer.deserialize(parser, deserializationContext);
        assertEquals(result, deserializedCollection);

        // Test deserialize with non-empty string
        String nonEmptyString = "test";
        parser = mock(JsonParser.class, new Expectations() {{
            allowUsing(parser).getText(); willReturn(nonEmptyString);
            allowUsing(parser).hasToken(JsonToken.VALUE_STRING); willReturn(true);
        }});
        deserializedCollection = contextualDeserializer.deserialize(parser, deserializationContext);
        List<String> expectedList = Arrays.asList(nonEmptyString);
        assertEquals(expectedList, deserializedCollection);

        // Test deserialize with empty string
        parser = mock(JsonParser.class, new Expectations() {{
            allowUsing(parser).getText(); willReturn("");
            allowUsing(parser).hasToken(JsonToken.VALUE_STRING); willReturn(true);
        }});
        deserializedCollection = contextualDeserializer.deserialize(parser, deserializationContext);
        assertNotNull(deserializedCollection);
        assertTrue(deserializedCollection.isEmpty());

        // Test deserialize with null
        parser = mock(JsonParser.class, new Expectations() {{
            allowUsing(parser).nextToken(); willReturn(JsonToken.VALUE_NULL);
        }});
        deserializedCollection = contextualDeserializer.deserialize(parser, deserializationContext);
        assertNotNull(deserializedCollection);
        assertTrue(deserializedCollection.isEmpty());

        // Test deserialize with array
        String jsonArray = "[\"test1\", \"test2\", \"test3\"]";
        parser = mock(JsonParser.class, new Expectations() {{
            allowUsing(parser).getText(); willReturn(jsonArray);
            allowUsing(parser).isExpectedStartArrayToken(); willReturn(true);
        }});
        expectedList = Arrays.asList("test1", "test2", "test3");
        deserializedCollection = contextualDeserializer.deserialize(parser, deserializationContext);
        assertEquals(expectedList, deserializedCollection);

        // Test deserialize with type deserializer
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        contextualDeserializer = new CollectionDeserializer(collectionType, valueDeserializer, typeDeserializer, valueInstantiator, delegateDeserializer, null);
        parser = mock(JsonParser.class, new Expectations() {{
            allowUsing(parser).getText(); willReturn(jsonArray);
            allowUsing(parser).isExpectedStartArrayToken(); willReturn(true);
        }});
        deserializedCollection = contextualDeserializer.deserialize(parser, deserializationContext);
        TypeDeserializer deserializedTypeDeserializer = contextualDeserializer.getContentDeserializer();
        TypeDeserializer expectedTypeDeserializer = typeDeserializer;
        assertEquals(expectedTypeDeserializer, deserializedTypeDeserializer);
        assertEquals(expectedList, deserializedCollection);

        // Test deserialize with delegate deserializer
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        contextualDeserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);
        parser = mock(JsonParser.class, new Expectations() {{
            allowUsing(parser).getText(); willReturn(jsonArray);
            allowUsing(parser).isExpectedStartArrayToken(); willReturn(true);
        }});
        Collection<Object> deserializedDelegateCollection = contextualDeserializer.deserialize(parser, deserializationContext);
        JsonDeserializer<Object> deserializedDelegateDeserializer = contextualDeserializer.getContentDeserializer();
        JsonDeserializer<Object> expectedDelegateDeserializer = delegateDeserializer;
        assertEquals(expectedDelegateDeserializer, deserializedDelegateDeserializer);
        assertEquals(expectedList, deserializedDelegateCollection);

        // Test deserialize with unwrapSingle
        Boolean unwrapSingle = true;
        contextualDeserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
        parser = mock(JsonParser.class, new Expectations() {{
            allowUsing(parser).getText(); willReturn("test");
            allowUsing(parser).isExpectedStartArrayToken(); willReturn(false);
        }});
        String deserializedSingleString = contextualDeserializer.deserialize(parser, deserializationContext).get(0).toString();
        assertEquals("test", deserializedSingleString);

        // Test isCachable
        assertTrue(contextualDeserializer.isCachable());
    }
}