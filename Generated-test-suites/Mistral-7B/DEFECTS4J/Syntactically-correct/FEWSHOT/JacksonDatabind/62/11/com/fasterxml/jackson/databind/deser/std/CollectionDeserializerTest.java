package org.apache.commons.cli;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.*;

/**
 * Test cases for {@link CollectionDeserializer} class.
 */
@JacksonStdImpl
public class CollectionDeserializerTest extends TestCase {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmptyCollection() throws IOException {
        String json = "[]";
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        List<Integer> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCollectionWithSingleElement() throws IOException {
        String json = "[1]";
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        List<Integer> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Collections.singletonList(1), result);
    }

    @Test
    public void testCollectionWithMultipleElements() throws IOException {
        String json = "[1, 2, 3]";
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        List<Integer> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testCollectionWithNullElement() throws IOException {
        String json = "[null]";
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Object.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        List<Object> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Collections.singletonList(null), result);
    }

    @Test
    public void testCollectionWithCustomTypeIdHandler() throws IOException {
        String json = "[{\"type\":\"MyCustomType\",\"value\":1}]";
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, MyCustomType.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        List<MyCustomType> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Collections.singletonList(new MyCustomType(1)), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidCollection() throws IOException {
        String json = "1";
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, null, null, null, null);
        deserializer.deserialize(mapper.readTree(json), null);
    }

    @Test
    public void testCollectionWithPolymorphicElements() throws IOException {
        String json = "[{\"type\":\"StringType\",\"value\":\"foo\"},{\"type\":\"NumberType\",\"value\":42}]";
        JavaType collectionType = mapper.getTypeFactory()
                .constructCollectionType(List.class, Map.class);

        JavaType stringType = mapper.getTypeFactory()
                .constructType(String.class, Map.class);
        JavaType numberType = mapper.getTypeFactory()
                .constructType(Number.class, Map.class);

        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                Object id = null;
                JsonToken t = p.nextToken();
                if (t == JsonToken.FIELD_NAME) {
                    id = p.getCurrentName();
                    p.nextToken(); // consume ':'
                }
                Object value = null;
                if (t == JsonToken.VALUE_STRING) {
                    value = ctxt.findValueDeserializer(String.class);
                    value = value.deserialize(p, ctxt);
                } else if (t == JsonToken.VALUE_NUMBER_INT) {
                    value = ctxt.findValueDeserializer(Int.class);
                    value = value.deserialize(p, ctxt);
                } else {
                    throw new IOException("Unexpected token: " + t);
                }
                Map map = new HashMap();
                map.put("type", id);
                map.put("value", value);
                return map;
            }
        };

        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, null, typeDeserializer,
                null, null, null);
        List<Map<String, Object>> result = deserializer.deserialize(mapper.readTree(json), null);
        assertEquals(Arrays.asList(
                new HashMap<String, Object>() {{
                    put("type", "StringType");
                    put("value", "foo");
                }},
                new HashMap<String, Object>() {{
                    put("type", "NumberType");
                    put("value", 42);
                }}
        ), result);
    }

    static class MyCustomType {
        private final int _value;

        MyCustomType(int value) {
            _value = value;
        }

        @JsonFormat(serializable = false)
        public int getValue() {
            return _value;
        }
    }
}