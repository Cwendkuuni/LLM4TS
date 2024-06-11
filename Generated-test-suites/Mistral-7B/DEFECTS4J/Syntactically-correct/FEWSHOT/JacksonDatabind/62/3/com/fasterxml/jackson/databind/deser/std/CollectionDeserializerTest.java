package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.*;

/**
 * Basic serializer that can take JSON "Array" structure and
 * construct a {@link java.util.Collection} instance, with typed contents.
 *<p>
 * Note: for untyped content (one indicated by passing Object.class
 * as the type), {@link UntypedObjectDeserializer} is used instead.
 * It can also construct {@link java.util.List}s, but not with specific
 * POJO types, only other containers and primitives/wrappers.
 */
@JacksonStdImpl
public class CollectionDeserializer
        extends ContainerDeserializerBase<Collection<Object>>
        implements ContextualDeserializer
{
    // Test methods for all constructors go here

    // Test methods for isCachable() go here

    @Test
    public void testCreateContextual() throws Exception {
        // Test creating contextual instance with various settings

        // Test using default values
        CollectionDeserializer deserializer1 = new CollectionDeserializer(
                JsonTypeFactory.instance.constructSimpleType(Collection.class, Object.class),
                null,
                null,
                null,
                null,
                null);
        Collection<Object> result1 = deserializer1.createContextual(mockContext(), mockProperty());

        // Test passing specific values for each setting
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        Boolean unwrapSingle = Boolean.TRUE;

        CollectionDeserializer deserializer2 = new CollectionDeserializer(
                JsonTypeFactory.instance.constructSimpleType(Collection.class, Object.class),
                valueDeserializer,
                valueTypeDeserializer,
                valueInstantiator,
                delegateDeserializer,
                unwrapSingle);

        Collection<Object> result2 = deserializer2.createContextual(mockContext(), mockProperty());

        // Test copy constructor
        CollectionDeserializer deserializer3 = new CollectionDeserializer(deserializer2);
        Collection<Object> result3 = deserializer3.createContextual(mockContext(), mockProperty());

        // Test fluent-factory method
        CollectionDeserializer deserializer4 = deserializer2.withResolved(valueDeserializer, valueDeserializer, valueTypeDeserializer, unwrapSingle);
        Collection<Object> result4 = deserializer4.createContextual(mockContext(), mockProperty());

        // Test deprecated method
        CollectionDeserializer deserializer5 = deserializer2.withResolved(valueDeserializer, valueDeserializer, valueTypeDeserializer);
        Collection<Object> result5 = deserializer5.createContextual(mockContext(), mockProperty());

        // Compare results
        assertEquals(result1, result2);
        assertEquals(result1, result3);
        assertEquals(result1, result4);
        assertEquals(result1, result5);
    }

    @Test
    public void testDeserialize() throws Exception {
        // Test deserializing with null input
        CollectionDeserializer deserializer = new CollectionDeserializer(
                JsonTypeFactory.instance.constructSimpleType(Collection.class, Object.class),
                null,
                null,
                null,
                null,
                null);
        Collection<Object> result = deserializer.deserialize(mockParser(), mockContext());
        assertNull(result);

        // Test deserializing from empty JSON Array
        String emptyJsonArray = "[]";
        JsonParser parser = mockParser().readValueAsString(emptyJsonArray);
        result = deserializer.deserialize(parser, mockContext());
        assertTrue(result.isEmpty());

        // Test deserializing from JSON Array with single value
        String jsonArrayWithSingleValue = "[\"foo\"]";
        parser = mockParser().readValueAsString(jsonArrayWithSingleValue);
        result = deserializer.deserialize(parser, mockContext());
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());

        // Test deserializing from JSON Array with multiple values
        String jsonArrayWithMultipleValues = "[1, 2, 3]";
        parser = mockParser().readValueAsString(jsonArrayWithMultipleValues);
        result = deserializer.deserialize(parser, mockContext());
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));

        // Test deserializing with delegate deserializer
        Object delegateObject = new Object();
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        when(delegateDeserializer.deserialize(mockParser(), mockContext())).thenReturn(delegateObject);

        deserializer = new CollectionDeserializer(
                JsonTypeFactory.instance.constructSimpleType(Collection.class, Object.class),
                null,
                null,
                null,
                delegateDeserializer,
                null);

        result = deserializer.deserialize(mockParser(), mockContext());
        assertTrue(result.contains(delegateObject));

        // Test deserializing with content converter
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        when(stringDeserializer.deserialize(mockParser(), mockContext())).thenReturn("foo");

        deserializer = new CollectionDeserializer(
                JsonTypeFactory.instance.constructSimpleType(Collection.class, String.class),
                stringDeserializer,
                null,
                null,
                null,
                null);

        result = deserializer.deserialize(mockParser(), mockContext());
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());

        // Test deserializing with type deserializer
        JavaType javaType = JsonTypeFactory.instance.constructSimpleType(String[].class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(mockParser(), mockContext())).thenReturn(new String[]{"foo"});

        deserializer = new CollectionDeserializer(
                JsonTypeFactory.instance.constructSimpleType(Collection.class, String.class),
                null,
                typeDeserializer,
                null,
                null,
                null);

        result = deserializer.deserialize(mockParser(), mockContext());
        assertEquals(1, result.size());
        assertEquals("foo", result.iterator().next());

        // Test handling null values
        JsonParser parserWithNullValue = mockParser().nextNullValue();
        result = deserializer.deserialize(parserWithNullValue, mockContext());
        assertNull(result);

        // Test handling non-array input
        JsonParser parserWithNonArrayInput = mockParser().nextValue(JsonToken.VALUE_STRING, "non-array");
        result = deserializer.deserialize(parserWithNonArrayInput, mockContext());
        assertNull(result);

        // Test handling unresolved forward references
        JsonParser parserWithUnresolvedForwardReference = mockParser();
        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference();
        unresolvedForwardReference.setRoid(mock(ObjectIdReader.class));
        Collections.addAll(unresolvedForwardReference.getUnresolvedIDs(), "id1", "id2");

        when(parserWithUnresolvedForwardReference.getCurrentName()).thenReturn("propertyName");
        when(parserWithUnresolvedForwardReference.getCurrentValueAsString()).thenReturn("{\"id1\":null,\"id2\":null}");

        CollectionDeserializer deserializerWithForwardReference = new CollectionDeserializer(
                JsonTypeFactory.instance.constructSimpleType(Collection.class, Object.class),
                null,
                null,
                null,
                null,
                null);

        try {
            deserializerWithForwardReference.deserialize(parserWithUnresolvedForwardReference, mockContext());
            fail();
        } catch (JsonMappingException e) {
            // expected
            assertEquals("Unresolved forward reference(s): [id1, id2]", e.getMessage());
        }

        // Test handling runtime exceptions
        doThrow(new RuntimeException("TestException")).when(mockParser()).nextValue(JsonToken.START_ARRAY);

        Collection<Object> resultWithRuntimeException = deserializer.deserialize(mockParser(), mockContext());
        fail();
    }
}