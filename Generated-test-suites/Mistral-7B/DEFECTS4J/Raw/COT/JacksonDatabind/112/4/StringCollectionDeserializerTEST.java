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
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@JacksonStdImpl
public final class StringCollectionDeserializerTest {

    private static final long serialVersionUID = 1L;
    private JsonFactory jsonFactory;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext context;
    private StringCollectionDeserializer deserializer;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        objectMapper = new ObjectMapper(jsonFactory);
        context = mock(DeserializationContext.class);
        jsonParser = jsonFactory.createParser("[]");
        deserializer = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, null, false);
    }

    @Test
    public void testStringCollectionDeserializerConstructor() {
        assertNotNull(deserializer);
        assertEquals(String.class, deserializer._valueDeserializer.getType());
    }

    @Test
    public void testIsCachable() {
        assertFalse(deserializer.isCachable());
    }

    @Test
    public void testCreateContextual() throws Exception {
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.getType()).thenReturn(String.class);

        JsonDeserializer<String> result = deserializer.createContextual(context, null);

        assertEquals(deserializer.getClass(), result.getClass());
        verify(context).handleSecondaryContextualization(valueDeserializer, null, String.class);
    }

    @Test
    public void testGetContentDeserializer() {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        when(deserializer._valueDeserializer).thenReturn(valueDeserializer);
        assertEquals(valueDeserializer, deserializer.getContentDeserializer());
    }

    @Test
    public void testGetValueInstantiator() {
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        when(deserializer._valueInstantiator).thenReturn(valueInstantiator);
        assertEquals(valueInstantiator, deserializer.getValueInstantiator());
    }

    @Test
    public void testDeserialize() throws Exception {
        Collection<String> collection = new ArrayList<>();
        when(context.findContentDeserializer(String[].class)).thenReturn(new StdSerializer.StdSerializer(String[].class));

        deserializer.deserialize(jsonParser, context, collection);

        assertEquals(0, collection.size());
        jsonParser.nextToken(); // consume the START_ARRAY token

        String value = "test";
        jsonParser.value(value);
        jsonParser.nextToken(); // consume the STRING token

        deserializer.deserialize(jsonParser, context, collection);

        assertEquals(1, collection.size());
        assertEquals(value, collection.get(0));

        jsonParser.nextToken(); // consume the COMMA token

        jsonParser.value("another test");
        deserializer.deserialize(jsonParser, context, collection);

        assertEquals(2, collection.size());
        assertEquals("another test", collection.get(1));

        jsonParser.nextToken(); // consume the END_ARRAY token
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws Exception {
        deserializer.deserialize(jsonParser, context, new ArrayList<>());

        jsonParser.nextToken(); // consume the INVALID_TOKEN token
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullInput() throws Exception {
        deserializer.deserialize(jsonParser, context, new ArrayList<>());

        jsonParser.nextToken(); // consume the NULL token
    }

    @Test
    public void testDeserializeWithNullValueProvider() throws Exception {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(context.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(context.findNullValueProvider(String.class)).thenReturn(nullValueProvider);

        StringCollectionDeserializer deserializerWithNullValueProvider = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, nullValueProvider, false);

        Collection<String> collection = new ArrayList<>();

        deserializerWithNullValueProvider.deserialize(jsonParser, context, collection);

        assertEquals(0, collection.size());
    }

    @Test
    public void testDeserializeWithNullValueProviderAndValue() throws Exception {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(context.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(context.findNullValueProvider(String.class)).thenReturn(nullValueProvider);

        String value = "test";
        jsonParser.value(value);
        jsonParser.nextToken(); // consume the STRING token

        Collection<String> collection = new ArrayList<>();
        StringCollectionDeserializer deserializerWithNullValueProvider = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, nullValueProvider, false);

        deserializerWithNullValueProvider.deserialize(jsonParser, context, collection);

        assertEquals(1, collection.size());
        assertEquals(value, collection.get(0));
    }

    @Test
    public void testDeserializeWithNullValueProviderAndEmptyJson() throws Exception {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(context.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(context.findNullValueProvider(String.class)).thenReturn(nullValueProvider);

        Collection<String> collection = new ArrayList<>();
        StringCollectionDeserializer deserializerWithNullValueProvider = new StringCollectionDeserializer(
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, String.class),
                null, null, null, nullValueProvider, false);

        deserializerWithNullValueProvider.deserialize(jsonParser, context, collection);

        assertEquals(0, collection.size());
    }
}