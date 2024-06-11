package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.util.*;

/**
 * Unit tests for {@link CollectionDeserializer}.
 */
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializer() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        Collection<Object> actual = deserializer.deserialize(createParser("[\"foo\", \"bar\", \"baz\"]"), null);
        List<String> expected = Arrays.asList("foo", "bar", "baz");
        assertEquals(expected, actual);
    }

    @Test
    public void testCollectionDeserializerWithValueTypeDeserializer() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>(Integer.class);
        TypeDeserializer valueTypeDeserializer = new IntDeserializer();
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        Collection<Object> actual = deserializer.deserialize(createParser("[-1, 0, 1]"), null);
        List<Integer> expected = Arrays.asList(-1, 0, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = new ArrayNodeDeserializer();
        Boolean unwrapSingle = null;

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = createParser("[]");
        JsonToken currentToken = jsonParser.nextToken();
        assertEquals(JsonToken.START_ARRAY, currentToken);
        JsonDeserializer<?> deserializerForDelegate = deserializer.delegateDeserializer();
        JsonNode actual = deserializerForDelegate.deserialize(jsonParser, null);
        JsonNode expected = createArrayNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testCollectionDeserializerWithUnwrapSingle() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>(Integer.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = Boolean.TRUE;

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        Collection<Object> actual = deserializer.deserialize(createParser("[-1]"), null);
        List<Integer> expected = Arrays.asList(-1);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithUnsupportedToken() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        JsonParser jsonParser = createParser("\"foo\"");
        deserializer.deserialize(jsonParser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNullValueDeserializer() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        try {
            new CollectionDeserializer(collectionType, null, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNullValueTypeDeserializer() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class);
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>(Integer.class);
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        try {
            new CollectionDeserializer(collectionType, valueDeserializer, null, valueInstantiator, delegateDeserializer, unwrapSingle);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNullValueInstantiator() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        try {
            new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, null, delegateDeserializer, unwrapSingle);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNullDelegateDeserializer() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        Boolean unwrapSingle = null;

        try {
            new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, unwrapSingle);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCollectionDeserializerWithNullunwrapSingle() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;

        try {
            new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testCollectionDeserializerWithCustomIdReader() throws Exception {
        JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StringDeserializer();
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = ValueInstantiator.noOpValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;
        CollectionDeserializer.CollectionReferringAccumulator accumulator = new CollectionDeserializer.CollectionReferringAccumulator(String.class, new ArrayList<>());

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null) {
            @Override
            CollectionReferringAccumulator createAccumulator() {
                return accumulator;
            }
        };

        JsonParser jsonParser = createParser("[\"foo\", null, \"bar\"]");
        Collection<Object> result = deserializer.deserialize(jsonParser, null);

        assertEquals(Arrays.asList("foo", null, "bar"), result);
    }

    private JsonParser createParser(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader(content));
    }

    private ArrayNode createArrayNode() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.createArrayNode();
    }
}