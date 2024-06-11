package com.example.test.jackson;

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Test class for StringCollectionDeserializer.
 */
public class StringCollectionDeserializerTest {

    private static final Class<Collection<String>> COLLECTION_STRING_TYPE = new TypeReference<Collection<String>>() {}.getType();
    private static final Class<String> STRING_TYPE = String.class;

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private DeserializationContext context;
    private StringCollectionDeserializer deserializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        context = mock(DeserializationContext.class);
        deserializer = new StringCollectionDeserializer(COLLECTION_STRING_TYPE, null, null, null, null, null);
    }

    /**
     * Test deserialization with null input.
     */
    @Test
    public void testDeserializeNull() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextNull()).thenReturn(true);

        Collection<String> result = deserializer.deserialize(jsonParser, context);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test deserialization with empty array input.
     */
    @Test
    public void testDeserializeEmptyArray() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextNull()).thenReturn(false);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = deserializer.deserialize(jsonParser, context);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test deserialization with single value input.
     */
    @Test
    public void testDeserializeSingleValue() throws Exception {
        String testString = "test";
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextTextValue()).thenReturn(testString);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = deserializer.deserialize(jsonParser, context);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testString, result.iterator().next());
    }

    /**
     * Test deserialization with multiple values input.
     */
    @Test
    public void testDeserializeMultipleValues() throws Exception {
        String testString1 = "test1";
        String testString2 = "test2";

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextTextValue()).thenCallRealMethod();
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextTextValue()).thenReturn(testString1);
        when(jsonParser.nextTextValue()).thenReturn(testString2);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = deserializer.deserialize(jsonParser, context);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testString1, result.iterator().next());
        assertEquals(testString2, result.iterator().next());
    }

    /**
     * Test deserialization with custom value deserializer.
     */
    @Test
    public void testDeserializeWithCustomValueDeserializer() throws Exception {
        String testString = "test";
        JsonDeserializer<String> customValueDeserializer = new StubStringDeserializer();

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextTextValue()).thenCallRealMethod();
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextTextValue()).thenReturn(testString);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = deserializer.deserialize(jsonParser, context, customValueDeserializer);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testString, result.iterator().next());
        verify(customValueDeserializer).deserialize(jsonParser, context);
    }

    /**
     * Test deserialization with custom value deserializer and custom value instantiator.
     */
    @Test
    public void testDeserializeWithCustomValueDeserializerAndInstantiator() throws Exception {
        String testString = "test";
        JsonDeserializer<String> customValueDeserializer = new StubStringDeserializer();
        ValueInstantiator customValueInstantiator = new StubStringValueInstantiator(customValueDeserializer);

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextTextValue()).thenCallRealMethod();
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextTextValue()).thenReturn(testString);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = deserializer.deserialize(jsonParser, context, null, customValueInstantiator);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testString, result.iterator().next());
        verify(customValueDeserializer).deserialize(jsonParser, context);
    }

    /**
     * Test deserialization with delegate deserializer.
     */
    @Test
    public void testDeserializeWithDelegateDeserializer() throws Exception {
        String testString = "test";
        JsonDeserializer<Object> delegateDeserializer = new StubObjectDeserializer();

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextTextValue()).thenCallRealMethod();
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextTextValue()).thenReturn(testString);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = deserializer.deserialize(jsonParser, context, null, null, delegateDeserializer);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testString, result.iterator().next());
        verify(delegateDeserializer).deserialize(jsonParser, context);
    }

    /**
     * Test deserialization with null value provider.
     */
    @Test
    public void testDeserializeWithNullValueProvider() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextNull()).thenReturn(true);

        Collection<String> result = deserializer.deserialize(jsonParser, context, null, null, null, new StubNullValueProvider());
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test deserialization with unwrapped single value.
     */
    @Test
    public void testDeserializeWithUnwrappedSingleValue() throws Exception {
        String testString = "test";

        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.nextTextValue()).thenReturn(testString);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Collection<String> result = deserializer.deserialize(jsonParser, context, null, null, null, true);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testString, result.iterator().next());
    }

    /**
     * Stub class for String value deserializer.
     */
    private static class StubStringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return "stub";
        }
    }

    /**
     * Stub class for String value instantiation.
     */
    private static class StubStringValueInstantiator extends ValueInstantiator {

        private final JsonDeserializer<String> deserializer;

        public StubStringValueInstantiator(JsonDeserializer<String> deserializer) {
            this.deserializer = deserializer;
        }

        @Override
        public Object createValueInstance(DeserializationContext ctxt)
                throws IOException {
            return deserializer;
        }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt)
                throws IOException {
            return deserializer.deserialize(jsonFactory.getParser(new ByteArrayInputStream("".getBytes())), ctxt);
        }
    }

    /**
     * Stub class for Object deserializer.
     */
    private static class StubObjectDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return "stub";
        }
    }

    /**
     * Stub class for null value provider.
     */
    private static class StubNullValueProvider implements NullValueProvider {

        @Override
        public Object getNullValue(DeserializationContext ctxt) throws IOException {
            return "stub";
        }
    }
}