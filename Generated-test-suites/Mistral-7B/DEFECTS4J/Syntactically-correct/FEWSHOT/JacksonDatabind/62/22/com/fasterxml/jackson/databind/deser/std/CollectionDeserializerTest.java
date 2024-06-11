package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

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
    // ... (constructor, fields, and methods omitted for brevity)

    // Test for deserializing empty array to empty collection
    @Test
    public void testDeserializeEmptyArrayToEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);
        JsonParser parser = mapper.getFactory().createParser("[]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertTrue(result.isEmpty());
    }

    // Test for deserializing non-empty array to collection with correct number of elements
    @Test
    public void testDeserializeNonEmptyArrayToCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, Integer.class);
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        Collection<Integer> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        List<Integer> expected = Arrays.asList(1, 2, 3);
        assertEquals(expected, result);
    }

    // Test for deserializing non-empty array to collection with type deserializer
    @Test
    public void testDeserializeNonEmptyArrayToCollectionWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<MyClass> valueDeserializer = mapper.deserializerForClass(MyClass.class);
        TypeDeserializer valueTypeDeserializer = mapper.getTypeFactory()
                .constructTypeDeserializer(MyClass.class);
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, MyClass.class);
        deserializer = deserializer.withValueDeserializer(valueDeserializer)
                .withValueTypeDeserializer(valueTypeDeserializer);
        JsonParser parser = mapper.getFactory().createParser("[{\"name\": \"Alice\"}, {\"name\": \"Bob\"}]");
        Collection<MyClass> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        List<MyClass> expected = Arrays.asList(new MyClass("Alice"), new MyClass("Bob"));
        assertEquals(expected, result);
    }

    // Test for handling null array
    @Test
    public void testDeserializeNullArrayToNullCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);
        JsonParser parser = mapper.getFactory().createParser("null");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertNull(result);
    }

    // Test for handling non-array JSON
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArrayJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);
        JsonParser parser = mapper.getFactory().createParser("{\"foo\": \"bar\"}");
        deserializer.deserialize(parser, mapper.getDeserializationContext());
    }

    // Test for handling empty string
    @Test
    public void testDeserializeEmptyStringToEmptyCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);
        JsonParser parser = mapper.getFactory().createParser("\"\"");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertTrue(result.isEmpty());
    }

    // Test for handling JSON with single value that can be unwrapped as collection
    @Test
    public void testDeserializeSingleValueThatCanBeUnwrappedAsCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);
        JsonParser parser = mapper.getFactory().createParser("[\"foo\"]");
        Collection<String> result = deserializer.deserialize(parser, mapper.getDeserializationContext());
        assertEquals(Arrays.asList("foo"), result);
    }

    // Test for handling JSON with single value that cannot be unwrapped as collection
    @Test(expected = JsonMappingException.class)
    public void testDeserializeSingleValueThatCannotBeUnwrappedAsCollection() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, String.class);
        JsonParser parser = mapper.getFactory().createParser("[1]");
        deserializer.deserialize(parser, mapper.getDeserializationContext());
    }
}