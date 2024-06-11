package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultDeserializer() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null);
        List<String> result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCustomValueDeserializer() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, valueDeserializer, null);
        List<String> result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertTrue(result.isEmpty());

        JsonParser parser = mapper.createParser("[\"foo\",\"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testValueInstantiator() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        ValueInstantiator valueInstantiator = mapper.getValueInstantiator();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, valueInstantiator, null, null, null, null);
        List<String> result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.emptyList(), result);

        JsonParser parser = mapper.createParser("[\"foo\",\"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<Object>() {};
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, null, null, null);
        List<String> result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.emptyList(), result);

        JsonParser parser = mapper.createParser("[1, 2, 3]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Collections.emptyList(), result);

        parser = mapper.createParser("[\"foo\",\"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar"), result);
    }

    @Test
    public void testNullValueProvider() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        NullValueProvider nullValueProvider = mapper.getConfig().getNullValueProvider();
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, nullValueProvider);
        List<String> result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.singletonList(null), result);

        JsonParser parser = mapper.createParser("[\"foo\", null, \"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", null, "bar"), result);
    }

    @Test
    public void testFormatFeature() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Boolean unwrapSingle = findFormatFeature(ctxt, null, collectionType, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        assertTrue(unwrapSingle == null || unwrapSingle == Boolean.TRUE);

        ctxt = mapper.getDeserializationContext().withFeature(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, false);
        unwrapSingle = findFormatFeature(ctxt, null, collectionType, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        assertFalse(unwrapSingle != null);
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null);

        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<Object>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, null, null, null);
        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, valueDeserializer, null);
        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, valueDeserializer);
        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, valueDeserializer, null);
        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, valueDeserializer, null, null);

        JsonDeserializer<String> finalValueDeserializer = new StdDeserializer<String>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, finalValueDeserializer, null, null);

        JsonDeserializer<Object> finalDelegateDeserializer = new StdDeserializer<Object>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, finalDelegateDeserializer, null, null, null);

        NullValueProvider finalNullValueProvider = mapper.getConfig().getNullValueProvider();
        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, null, finalNullValueProvider, null);

        assertEquals(deserializer, deserializer.withResolved(null, null, null, null));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, null, null));
        assertEquals(deserializer, deserializer.withResolved(null, valueDeserializer, null, null));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, valueDeserializer, null, null));
        assertEquals(deserializer, deserializer.withResolved(null, null, null, Boolean.TRUE));
        assertEquals(deserializer, deserializer.withResolved(null, null, null, Boolean.FALSE));
        assertEquals(deserializer, deserializer.withResolved(null, null, finalNullValueProvider, null));
        assertEquals(deserializer, deserializer.withResolved(null, null, null, Boolean.TRUE, finalNullValueProvider, null));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, null, Boolean.TRUE, finalNullValueProvider, null));
        assertEquals(deserializer, deserializer.withResolved(null, null, null, Boolean.TRUE, null, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, null, Boolean.TRUE, null, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(null, null, null, Boolean.TRUE, finalNullValueProvider, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, null, Boolean.TRUE, finalNullValueProvider, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(null, null, finalNullValueProvider, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, finalNullValueProvider, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(null, null, null, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, null, finalValueDeserializer));
        assertEquals(deserializer, deserializer.withResolved(null, null, finalNullValueProvider, finalValueDeserializer, null, null, null, null));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, finalNullValueProvider, finalValueDeserializer, null, null, null, null));
        assertEquals(deserializer, deserializer.withResolved(null, null, null, finalValueDeserializer, null, null, null, null));
        assertEquals(deserializer, deserializer.withResolved(delegateDeserializer, null, null, finalValueDeserializer, null, null, null, null));
    }

    @Test
    public void testIsCachable() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null);
        assertTrue(deserializer.isCachable());

        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, null, valueDeserializer, null, null);
        assertFalse(deserializer.isCachable());

        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<Object>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, null, null, null);
        assertFalse(deserializer.isCachable());
    }

    @Test
    public void testDeserialize() throws IOException {
        JavaType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, null, null);

        List<String> result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.emptyList(), result);

        JsonParser parser = mapper.createParser("[\"foo\",\"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", "bar"), result);

        parser = mapper.createParser("[\n1,\n2,\n3\n]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("1", "2", "3"), result);

        parser = mapper.createParser("[null]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Collections.singletonList(null), result);

        parser = mapper.createParser("[\"foo\",null,\"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList("foo", null, "bar"), result);

        JsonDeserializer<String> valueDeserializer = new StdDeserializer<String>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, null, valueDeserializer, null, null);

        result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.emptyList(), result);

        parser = mapper.createParser("[\"foo\",\"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(valueDeserializer.deserialize(parser, null), valueDeserializer.deserialize(parser, null)), result);

        JsonDeserializer<Object> delegateDeserializer = new StdDeserializer<Object>() {};
        deserializer = new StringCollectionDeserializer(collectionType, null, delegateDeserializer, null, null, null);

        Object delegateResult = new Object();
        result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.emptyList(), result);

        parser = mapper.createParser("[1, 2, 3]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(delegateDeserializer.deserialize(parser, null), delegateDeserializer.deserialize(parser, null), delegateDeserializer.deserialize(parser, null)), result);

        result = deserializer.deserialize(parser, null, new ArrayList<String>());
        assertEquals(Arrays.asList(delegateDeserializer.deserialize(parser, null), delegateDeserializer.deserialize(parser, null), delegateDeserializer.deserialize(parser, null)), result);

        deserializer = new StringCollectionDeserializer(collectionType, null, null, null, null, new StringSerializer());

        result = deserializer.deserialize(mapper.createParser("[]"), null);
        assertEquals(Collections.emptyList(), result);

        parser = mapper.createParser("[\"foo\",\"bar\"]");
        result = deserializer.deserialize(parser, null);
        assertEquals(Arrays.asList(parser.readValueAs(String.class), parser.readValueAs(String.class)), result);
    }

    private static Boolean findFormatFeature(DeserializationContext ctxt, AnnotatedWithParams p, JavaType type, JsonFormat.Feature feature) {
        return ctxt.findFeatureValue(p, feature, type);
    }

    private static JsonDeserializer<?> findDeserializer(DeserializationContext ctxt, JavaType type, AnnotatedWithParams p) throws JsonMappingException {
        return ctxt.findDeserializer(type, p);
    }

    private static JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, AnnotatedWithParams p, JsonDeserializer<?> deser) throws JsonMappingException {
        return ctxt.findValueDeserializer(deser.getType(), p, deser);
    }

    private static NullValueProvider findContentNullProvider(DeserializationContext ctxt, AnnotatedWithParams p, JsonDeserializer<?> deser) throws JsonMappingException {
        return ctxt.findNullValueProvider(deser.getType(), p);
    }

    private static boolean isDefaultDeserializer(JsonDeserializer<?> deser) {
        return deser == null || (deser instanceof StdDeserializer.None);
    }
}