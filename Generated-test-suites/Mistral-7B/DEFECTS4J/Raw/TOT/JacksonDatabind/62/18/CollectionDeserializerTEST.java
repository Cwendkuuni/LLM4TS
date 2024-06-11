package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

@org.junit.runner.RunWith(org.junit.platform.runner.JUnitPlatform.class)
public class CollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testEmptyArrayDeserialization() throws Exception {
        String json = "[]";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Integer.class),
                null, null, null, null);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
        assertEquals(Collections.emptyList(), result);
    }

    @org.junit.Test
    public void testSingleValueDeserialization() throws Exception {
        String json = "[1]";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Integer.class),
                null, null, null, null);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
        assertEquals(Collections.singletonList(1), result);
    }

    @org.junit.Test
    public void testArrayDeserialization() throws Exception {
        String json = "[1, 2, 3]";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Integer.class),
                null, null, null, null);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @org.junit.Test
    public void testNullDeserialization() throws Exception {
        String json = "[null]";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Integer.class),
                null, null, null, null);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
        assertEquals(Collections.singletonList(null), result);
    }

    @org.junit.Test
    public void testPolymorphicDeserialization() throws Exception {
        String json = "[1, 'two']";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Number.class),
                new TypeDeserializer() {
                    @Override
                    public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                        return p.getValueAsString();
                    }
                }, null, null, null);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
        assertEquals(Arrays.asList(1, "two"), result);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testInvalidDeserialization() throws Exception {
        String json = "invalidJson";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Integer.class),
                null, null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
    }

    @org.junit.Test
    public void testSingleValueUnwrapping() throws Exception {
        String json = "[1]";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Integer.class),
                null, null, null, Boolean.TRUE);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
        assertEquals(1, result.size());
        assertTrue(result.iterator().next() instanceof Number);
        assertEquals(1, ((Number) result.iterator().next()).intValue());
    }

    @org.junit.Test
    public void testListDeserialization() throws Exception {
        String json = "[1, ['a', 'b'], 3.0]";
        JsonNode node = mapper.readTree(json);
        CollectionDeserializer deserializer = new CollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(ArrayList.class),
                mapper.getFactory().findValueDeserializer(Object.class),
                null, null, null, null);
        Collection<Object> result = deserializer.deserialize(mapper.getFactory().createParser(node), deserializer.getDefaultContext());
        List<Object> expected = new ArrayList<>();
        expected.add(1);
        expected.add(Arrays.asList("a", "b"));
        expected.add(3.0);
        assertEquals(expected, result);
    }
}