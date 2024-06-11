package com.example.test; // replace with your test package name

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
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Comprehensive JUnit test cases for StringCollectionDeserializer class.
 */
public class StringCollectionDeserializerTest {

    

    // 1. Test constructor
    @Test
    public void testConstructor() {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        assertNotNull(deserializer);

        JavaType collectionTypeWithDelegate = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializerWithDelegate = new StringCollectionDeserializer(collectionTypeWithDelegate, null, null, null, new StdScalarDeserializer(String.class), null);
        assertNotNull(deserializerWithDelegate);
    }

    // 2. Test isCachable method
    @Test
    public void testIsCachable() {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        assertTrue(deserializer.isCachable());

        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<>();
        JsonDeserializer<String> valueDeserializer = new StdScalarDeserializer(String.class);
        NullValueProvider nullValueProvider = null;
        StringCollectionDeserializer deserializerWithDelegate = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, valueDeserializer, nullValueProvider, false);
        assertFalse(deserializerWithDelegate.isCachable());
    }

    // 3. Test createContextual method with null context
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullContext() {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        deserializer.createContextual(null, null);
    }

    // 4. Test createContextual method with empty property
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithEmptyProperty() {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        deserializer.createContextual(new DeserializationContext(null), new BeanProperty(""));
    }

    // 5. Test deserialize method with null input
    @Test(expected = IOException.class)
    public void testDeserializeWithNullInput() throws IOException {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        deserializer.deserialize(null, new DeserializationContext(null), null);
    }

    // 6. Test deserialize method with empty array input
    @Test
    public void testDeserializeWithEmptyArrayInput() throws IOException {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        Collection<String> result = deserializer.deserialize(new JsonFactory().createParser(new ByteArrayInputStream(new byte[0])), new DeserializationContext(null), null);
        assertEquals(0, result.size());
    }

    // 7. Test deserialize method with single value input
    @Test
    public void testDeserializeWithSingleValueInput() throws IOException {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        ArrayNode input = MapperFactory.getMapper().createArrayNode();
        input.add("test");
        Collection<String> result = deserializer.deserialize(new JsonFactory().createParser(input), new DeserializationContext(null), null);
        assertEquals(1, result.size());
        assertEquals("test", result.iterator().next());
    }

    // 8. Test deserialize method with multiple values input
    @Test
    public void testDeserializeWithMultipleValuesInput() throws IOException {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        ArrayNode input = MapperFactory.getMapper().createArrayNode();
        input.add("test1");
        input.add("test2");
        Collection<String> result = deserializer.deserialize(new JsonFactory().createParser(input), new DeserializationContext(null), null);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("test1", "test2")));
    }

    // 9. Test deserialize method with null values
    @Test
    public void testDeserializeWithNullValues() throws IOException {
        JsonFormat.Feature feature = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        NullValueProvider nullValueProvider = new StandardNullValueProvider();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, nullValueProvider, Boolean.TRUE);
        ArrayNode input = MapperFactory.getMapper().createArrayNode();
        input.addNull();
        Collection<String> result = deserializer.deserialize(new JsonFactory().createParser(input), new DeserializationContext(null), null);
        assertEquals(1, result.size());
        assertTrue(result.contains(null));
    }

    // 10. Test deserialize method with custom deserializer
    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StdScalarDeserializer<String> customDeserializer = new StdScalarDeserializer<>(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, customDeserializer, null);
        ArrayNode input = MapperFactory.getMapper().createArrayNode();
        input.add("test1");
        input.add("test2");
        Collection<String> result = deserializer.deserialize(new JsonFactory().createParser(input), new DeserializationContext(null), null);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("test1", "test2")));

        // test custom deserializer handling null values
        input.add(null);
        result = deserializer.deserialize(new JsonFactory().createParser(input), new DeserializationContext(null), null);
        assertEquals(3, result.size());
        assertTrue(result.contains(null));
    }

    // 11. Test deserialize method with delegate deserializer
    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(Collection.class, String.class);
        StdDeserializer<Object> delegateDeserializer = new StdDeserializer<>();
        JsonDeserializer<String> valueDeserializer = new StdScalarDeserializer(String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, valueDeserializer, null, false);
        ArrayNode input = MapperFactory.getMapper().createArrayNode();
        input.add("test1");
        input.add("test2");
        Collection<String> result = deserializer.deserialize(new JsonFactory().createParser(input), new DeserializationContext(null), null);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("test1", "test2")));

        // test delegate deserializer handling null values
        JsonDeserializer<String> nullValueDeserializer = new StdScalarDeserializer(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                if (p.getValueAsInt() == 0) {
                    return null;
                }
                return super.deserialize(p, ctxt);
            }
        };
        input.setAll(Arrays.asList(0, "test1", null, "test2"));
        result = deserializer.deserialize(new JsonFactory().createParser(input), new DeserializationContext(null), null);
        assertEquals(4, result.size());
        assertTrue(result.containsAll(Arrays.asList("test1", null, "test2", null)));
    }

    // 12. Test deserializeWithType method
    @Test
    public void testDeserializeWithType() throws IOException {
        JavaType collectionType = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        TypeDeserializer typeDeserializer = new CollectionTypeDeserializer(collectionType);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, false);
        ArrayNode input = MapperFactory.getMapper().createArrayNode();
        input.add("test1");
        input.add("test2");
        Collection<String> result = deserializer.deserializeWithType(new JsonFactory().createParser(input), new DeserializationContext(null), typeDeserializer);
        assertTrue(result instanceof List);
        assertEquals(2, ((List<String>) result).size());
        assertTrue(result.containsAll(Arrays.asList("test1", "test2")));
    }

    
}