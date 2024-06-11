package com.example.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JacksonStdImpl
public class CollectionDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Test
    public void testCollectionDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = mapper.deserializerFactory().findValueDeserializer(String.class, null);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = mapper.valueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        assertNotNull(collectionDeserializer);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("");

        ObjectNode jsonNode = jsonFactory.createObjectNode();
        jsonNode.put("name", "John Doe");
        jsonNode.put("ages", "[25, 28, 30]");

        jsonParser.readValueAs(Collection.class, CollectionDeserializer.class, jsonNode);

        List<Object> result = (List<Object>) jsonParser.getValueAs(Collection.class);

        assertTrue(result.size() > 0);
        assertTrue(result.get(0) instanceof String);
        assertTrue(result.get(1) instanceof Number);

        Object name = result.get(0);
        assertEquals("John Doe", name);

        Object age = result.get(1);
        assertEquals(25, age.getClass().getMethod("intValue").invoke(age));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCollectionDeserializerWithTypeDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = mapper.deserializerFactory().findValueDeserializer(String.class, null);
        TypeDeserializer valueTypeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(Integer.class, null);
        ValueInstantiator valueInstantiator = mapper.valueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = null;
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        assertNotNull(collectionDeserializer);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("");

        ObjectNode jsonNode = jsonFactory.createObjectNode();
        jsonNode.put("name", "John Doe");
        jsonNode.put("ages", "[25, 28, 30]");

        jsonParser.setContext(new StandardDeserializationContext(mapper.getConfiguration(), jsonParser));

        jsonParser.readValueAs(Collection.class, collectionDeserializer.getClass(), jsonNode);

        List<Object> result = (List<Object>) jsonParser.getValueAs(Collection.class);

        assertTrue(result.size() > 0);
        assertTrue(result.get(0) instanceof String);
        assertTrue(result.get(1) instanceof Number);

        Object name = result.get(0);
        assertEquals("John Doe", name);

        Object age = result.get(1);
        assertEquals(25, age.getClass().getMethod("intValue").invoke(age));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        JsonDeserializer<String> valueDeserializer = mapper.deserializerFactory().findValueDeserializer(String.class, null);
        TypeDeserializer valueTypeDeserializer = null;
        ValueInstantiator valueInstantiator = mapper.valueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = mapper.deserializerFactory().findDeserializer(Integer.class, null);
        Boolean unwrapSingle = null;

        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);

        assertNotNull(collectionDeserializer);

        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = jsonFactory.getParser("");

        ObjectNode jsonNode = jsonFactory.createObjectNode();
        jsonNode.put("name", "John Doe");
        jsonNode.put("ages", "[25, 28, 30]");

        JsonDeserializer<Object> deserializer = collectionDeserializer.withResolved(null, valueDeserializer, valueTypeDeserializer);

        jsonParser.setContext(new StandardDeserializationContext(mapper.getConfiguration(), jsonParser));

        jsonParser.readValueAs(Collection.class, deserializer.getClass(), jsonNode);

        List<Object> result = (List<Object>) jsonParser.getValueAs(Collection.class);

        assertTrue(result.size() > 0);
        assertTrue(result.get(0) instanceof String);
        assertTrue(result.get(1) instanceof Number);

        Object name = result.get(0);
        assertEquals("John Doe", name);

        Object age = result.get(1);
        assertEquals(25, age.getClass().getMethod("intValue").invoke(age));
    }
}