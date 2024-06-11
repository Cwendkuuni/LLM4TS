package com.example; // replace with the actual package name

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

@JacksonStdImpl
public class CollectionDeserializerTest {

    @SuppressWarnings("unchecked")
    private static final CollectionDeserializer createDeserializer(JavaType collectionType,
            JsonDeserializer<Object> valueDeserializer, TypeDeserializer valueTypeDeserializer,
            ValueInstantiator valueInstantiator, JsonDeserializer<Object> delegateDeserializer,
            Boolean unwrapSingle) {
        return new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer,
                valueInstantiator, delegateDeserializer, unwrapSingle);
    }

    @SuppressWarnings("unchecked")
    private static final CollectionDeserializer createDeserializer(JavaType collectionType,
            JsonDeserializer<Object> valueDeserializer, TypeDeserializer valueTypeDeserializer,
            ValueInstantiator valueInstantiator) {
        return new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer,
                valueInstantiator, null, null);
    }

    @Test
    public void testCreateContextualInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);

        CollectionDeserializer deserializer1 = createDeserializer(javaTypeListOfStrings, stringDeserializer, null, null, null, null);
        CollectionDeserializer deserializer2 = deserializer1.withResolved(null, stringDeserializer, null);

        assertNotSame(deserializer1, deserializer2);
    }

    @Test
    public void testIsCachable() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer stringTypeDeserializer = mapper.getTypeDeserializerForProperty("type");

        CollectionDeserializer deserializer1 = createDeserializer(javaTypeListOfStrings, stringDeserializer, stringTypeDeserializer, null, null, null);
        assertTrue(deserializer1.isCachable());

        JsonDeserializer<Integer> intDeserializer = mapper.findValueDeserializer(Integer.class);
        CollectionDeserializer deserializer2 = createDeserializer(javaTypeListOfStrings, intDeserializer, null, null, null, null);
        assertFalse(deserializer2.isCachable());
    }

    @Test
    public void testCreateContextualWithDelegateDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        ValueInstantiator stringValueInstantiator = mapper.getFactory().getValueInstantiator();
        JsonDeserializer<List<String>> listDeserializer = new CollectionDeserializer.ArrayFormJsonDeserializer(
                javaTypeListOfStrings, stringDeserializer, null, stringValueInstantiator, null, null);

        CollectionDeserializer deserializer1 = createDeserializer(javaTypeListOfStrings, null, null, null, null, null);
        CollectionDeserializer deserializer2 = deserializer1.withResolved(listDeserializer, null, null);

        assertNotSame(deserializer1, deserializer2);
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);

        CollectionDeserializer deserializer = createDeserializer(javaTypeListOfStrings, stringDeserializer, null, null, null, null);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[\"one\",\"two\",\"three\"]");

        Collection<Object> result = deserializer.deserialize(jsonParser, null);
        assertEquals(3, result.size());
        assertEquals("one", result.get(0));
        assertEquals("two", result.get(1));
        assertEquals("three", result.get(2));

        jsonParser.close();
    }

    @Test
    public void testDeserializeNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);

        CollectionDeserializer deserializer = createDeserializer(javaTypeListOfStrings, stringDeserializer, null, null, null, null);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("null");

        Collection<Object> result = deserializer.deserialize(jsonParser, null);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        jsonParser.close();
    }

    @Test
    public void testDeserializeForEmptyString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);

        CollectionDeserializer deserializer = createDeserializer(javaTypeListOfStrings, stringDeserializer, null, null, null, null);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[]");

        Collection<Object> result = deserializer.deserialize(jsonParser, null);
        assertTrue(result.isEmpty());

        jsonParser.close();
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        TypeDeserializer stringTypeDeserializer = mapper.getTypeDeserializerForProperty("type");

        CollectionDeserializer deserializer = createDeserializer(javaTypeListOfStrings, stringDeserializer, stringTypeDeserializer, null, null, null);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("[]");

        Collection<Object> result = deserializer.deserialize(jsonParser, null);
        assertTrue(result.isEmpty());

        JsonParser jsonParserWithType = jsonFactory.createParser("{\"type\":\"String\",\"values\":[]}");

        result = deserializer.deserialize(jsonParserWithType, null);
        assertTrue(result.isEmpty());

        jsonParser.close();
        jsonParserWithType.close();
    }

    @Test
    public void testDeserializeWithUnresolvedForwardReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JavaType javaTypeListOfStrings = mapper.getTypeFactory().constructType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        ValueInstantiator stringValueInstantiator = mapper.getFactory().getValueInstantiator();

        CollectionDeserializer deserializer = createDeserializer(javaTypeListOfStrings, stringDeserializer, null, stringValueInstantiator, null, null);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("{" +
                "\"values\":[" +
                "  {" +
                "    \"$ref\": \"/Property1\"" +
                "  }," +
                "  \"two\"" +
                "]" +
                "}");

        Collection<Object> result = deserializer.deserialize(jsonParser, null);

        JsonParser jsonParserOfProperty1 = jsonFactory.createParser("[" +
                "  \"one\"" +
                "]");

        deserializer.resolveForwardReference("/Property1", jsonParserOfProperty1.readValue(Collection.class));

        assertEquals(2, result.size());
        assertEquals("one", result.get(0));
        assertEquals("two", result.get(1));

        jsonParser.close();
        jsonParserOfProperty1.close();
    }
}