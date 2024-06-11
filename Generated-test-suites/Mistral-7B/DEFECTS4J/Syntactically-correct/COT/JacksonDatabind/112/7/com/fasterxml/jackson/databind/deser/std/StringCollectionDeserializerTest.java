package com.example.test;

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
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import static com.fasterxml.jackson.core.JsonToken.*;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
    }

    @Test
    public void testDefaultConstructor() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testStringCollectionDeserializerConstructor() {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);
        Assert.assertNotNull(deserializer);
    }

    @Test
    public void testWithResolved() {
        StringCollectionDeserializer deserializer1 = new StringCollectionDeserializer(null, null, null, null, null, null);
        StringCollectionDeserializer deserializer2 = deserializer1.withResolved(null, null, null, null);
        Assert.assertNotSame(deserializer1, deserializer2);

        deserializer1 = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer2 = deserializer1.withResolved(null, null, null, null);
        deserializer2 = deserializer2.withResolved(null, null, null, null);
        Assert.assertEquals(deserializer1, deserializer2);

        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        NullValueProvider nullValueProvider = new NullValueProviderDefaultImpl();
        StringCollectionDeserializer deserializer3 = new StringCollectionDeserializer(null, null, delegateDeserializer, valueDeserializer, nullValueProvider, null);
        StringCollectionDeserializer deserializer4 = deserializer3.withResolved(null, valueDeserializer, nullValueProvider, null);
        Assert.assertNotSame(deserializer3, deserializer4);

        Assert.assertEquals(deserializer3.getClass(), deserializer4.getClass());
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        Assert.assertFalse(deserializer.isCachable());

        deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer._valueDeserializer = null;
        Assert.assertTrue(deserializer.isCachable());

        deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer._delegateDeserializer = null;
        Assert.assertTrue(deserializer.isCachable());
    }

    @Test
    public void testCreateContextual() {
        JavaType collectionType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer();
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        NullValueProvider nullValueProvider = new NullValueProviderDefaultImpl();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, valueDeserializer, nullValueProvider, null);

        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        BeanProperty property = new BeanProperty("test", Collection.class, null);
        JsonDeserializer<Object> result = deserializer.createContextual(ctxt, property);

        Assert.assertEquals(StringCollectionDeserializer.class, result.getClass());

        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, nullValueProvider, null);
        result = deserializer.createContextual(ctxt, property);
        Assert.assertEquals(StringCollectionDeserializer.class, result.getClass());

        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);
        result = deserializer.createContextual(ctxt, property);
        Assert.assertEquals(StringCollectionDeserializer.class, result.getClass());

        deserializer = new StringCollectionDeserializer(null, null, delegateDeserializer, null, nullValueProvider, null);
        result = deserializer.createContextual(ctxt, property);
        Assert.assertEquals(JsonDeserializer.class, result.getClass());

        deserializer = new StringCollectionDeserializer(null, null, null, valueDeserializer, nullValueProvider, null);
        result = deserializer.createContextual(ctxt, property);
        Assert.assertEquals(JsonDeserializer.class, result.getClass());

        deserializer = new StringCollectionDeserializer(null, null, null, null, nullValueProvider, null);
        ObjectNode node = jsonFactory.createObjectNode();
        node.put("delegateDeserializer", delegateDeserializer);
        result = deserializer.createContextual(ctxt, property, node);
        Assert.assertEquals(JsonDeserializer.class, result.getClass());

        deserializer = new StringCollectionDeserializer(null, null, null, null, nullValueProvider, true);
        result = deserializer.createContextual(ctxt, property);
        Assert.assertEquals(JsonDeserializer.class, result.getClass());
    }

    @Test
    public void testGetContentDeserializer() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        JsonDeserializer<Object> result = deserializer.getContentDeserializer();
        Assert.assertNull(result);

        deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        deserializer._valueDeserializer = new StringDeserializer();
        result = deserializer.getContentDeserializer();
        Assert.assertEquals(StringDeserializer.class, result.getClass());
    }

    @Test
    public void testGetValueInstantiator() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        ValueInstantiator result = deserializer.getValueInstantiator();
        Assert.assertNull(result);

        deserializer = new StringCollectionDeserializer(null, new StdValueInstantiator(), null, null, null, null);
        result = deserializer.getValueInstantiator();
        Assert.assertEquals(StdValueInstantiator.class, result.getClass());
    }

    @Test
    public void testDeserialize() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);

        JsonParser p = jsonFactory.createParser("[\"Test1\", \"Test2\"]");
        Collection<String> result = deserializer.deserialize(p, null);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("Test1", result.iterator().next());
        Assert.assertEquals("Test2", result.iterator().next());

        p = jsonFactory.createParser("[null]");
        result = deserializer.deserialize(p, null);
        Assert.assertEquals(1, result.size());
        Assert.assertNull(result.iterator().next());

        p = jsonFactory.createParser("[\"Test1\", null, \"Test3\"]");
        result = deserializer.deserialize(p, null);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals("Test1", result.iterator().next());
        Assert.assertNull(result.iterator().next());
        Assert.assertEquals("Test3", result.iterator().next());

        p = jsonFactory.createParser("[]");
        result = deserializer.deserialize(p, null);
        Assert.assertEquals(0, result.size());

        p = jsonFactory.createParser("{ \"name\": [\"Test1\", \"Test2\"] }");
        result = deserializer.deserialize(p, null);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("Test1", result.iterator().next());
        Assert.assertEquals("Test2", result.iterator().next());

        p = jsonFactory.createParser("{ \"name\": null }");
        result = deserializer.deserialize(p, null);
        Assert.assertEquals(0, result.size());

        p = jsonFactory.createParser("{ \"name\": [null] }");
        result = deserializer.deserialize(p, null);
        Assert.assertEquals(1, result.size());
        Assert.assertNull(result.iterator().next());

        p = jsonFactory.createParser("{ \"name\": [] }");
        result = deserializer.deserialize(p, null);
        Assert.assertEquals(0, result.size());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);

        JsonParser p = jsonFactory.createParser("[\"Test1\", \"Test2\"]");
        deserializer._valueDeserializer = null;
        Collection<String> result = deserializer.deserialize(p, null);
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullValueDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);

        JsonParser p = jsonFactory.createParser("[\"Test1\", \"Test2\"]");
        deserializer._valueDeserializer = new StringDeserializer();
        deserializer._nullProvider = null;
        Collection<String> result = deserializer.deserialize(p, null);
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullValueInstantiator() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);

        JsonParser p = jsonFactory.createParser("[\"Test1\", \"Test2\"]");
        deserializer._valueDeserializer = new StringDeserializer();
        deserializer._valueInstantiator = null;
        Collection<String> result = deserializer.deserialize(p, null);
        Assert.fail();
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullDelegateDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);

        JsonParser p = jsonFactory.createParser("[\"Test1\", \"Test2\"]");
        deserializer._delegateDeserializer = null;
        Collection<String> result = deserializer.deserialize(p, null);
        Assert.fail();
    }

    @Test
    public void testDeserializeWithCustomDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, null, null, null, null);
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        deserializer._valueDeserializer = stringDeserializer;

        JsonParser p = jsonFactory.createParser("[\"Test1\", \"Test2\"]");
        Collection<String> result = deserializer.deserialize(p, null);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("Test1", result.iterator().next());
        Assert.assertEquals("Test2", result.iterator().next());
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(ArrayNode.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        TypeDeserializer typeDeserializer = objectMapper.getTypeFactory().constructValueTypeDeserializer(javaType);

        JsonParser p = jsonFactory.createParser("[\"Test1\", \"Test2\"]");
        Object result = deserializer.deserializeWithType(p, null, typeDeserializer);
        Assert.assertEquals(javaType.getClass(), result.getClass());
        ArrayNode arrayNode = (ArrayNode) result;
        Collection<String> resultCollection = arrayNode.asCollection(String.class);
        Assert.assertEquals(2, resultCollection.size());
        Assert.assertEquals("Test1", resultCollection.iterator().next());
        Assert.assertEquals("Test2", resultCollection.iterator().next());
    }

    private static class StdDeserializer extends JsonDeserializer<Object> {
    }

    private static class StringDeserializer extends JsonDeserializer<String> {
    }

    private static class StdValueInstantiator implements ValueInstantiator {
        @Override
        public Object createValueInstance(DeserializationContext ctxt, TypeType type) throws IOException {
            return new ArrayList<>();
        }
    }

    private static class NullValueProviderDefaultImpl implements NullValueProvider {
        @Override
        public Object getNullValue(DeserializationContext ctxt) {
            return null;
        }
    }
}