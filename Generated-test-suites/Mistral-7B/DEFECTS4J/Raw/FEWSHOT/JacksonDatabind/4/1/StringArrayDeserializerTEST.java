package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Separate implementation for serializing String arrays (instead of
 * using {@link ObjectArrayDeserializer}).
 * Used if (and only if) no custom value deserializers are used.
 */
@JacksonStdImpl
public final class StringArrayDeserializer
        extends StdDeserializer<String[]>
        implements ContextualDeserializer
{
    // class body here
}

public class StringArrayDeserializerTest {

    @Test
    public void testEmptyArray() throws IOException {
        String[] expected = new String[0];
        JsonParser mockParser = createMockJsonParser();
        DeserializationContext mockContext = createMockContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(mockParser, mockContext);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithSingleValue() throws IOException {
        String value = "foo";
        JsonParser mockParser = createMockJsonParser(value);
        DeserializationContext mockContext = createMockContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(mockParser, mockContext);
        assertArrayEquals(new String[] { value }, result);
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        JsonParser mockParser = createMockJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext mockContext = createMockContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(mockParser, mockContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        String[] expected = new String[] { "foo", "bar" };
        JsonParser mockParser = createMockJsonParser();
        DeserializationContext mockContext = createMockContext();
        JsonDeserializer<String> mockElementDeserializer = createMockStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mockElementDeserializer);
        String[] result = deserializer.deserialize(mockParser, mockContext);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        String[] expected = new String[] { "foo", "bar" };
        JsonParser mockParser = createMockJsonParser();
        DeserializationContext mockContext = createMockContext();
        TypeDeserializer mockTypeDeserializer = createMockTypeDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String value = "foo";
        JsonParser mockParser = createMockJsonParser(value);
        DeserializationContext mockContext = createMockContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(mockParser, mockContext);
        assertArrayEquals(new String[] { value }, result);
    }

    @Test
    public void testCreateContextual() {
        // This test case is not comprehensive as it depends on mock implementations of JsonDeserializer and other classes.
        // However, it checks the basic functionality of createContextual method.
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertThat(deserializer.createContextual(mockContext, mockProperty).getClass(), is(StringArrayDeserializer.class));
    }

    private JsonParser createMockJsonParser() {
        return createMockJsonParser(JsonToken.START_ARRAY);
    }

    private JsonParser createMockJsonParser(JsonToken token) {
        JsonParser mockParser = createMock(JsonParser.class);
        expect(mockParser.nextToken()).andReturn(token);
        return mockParser;
    }

    private DeserializationContext createMockContext() {
        return createMock(DeserializationContext.class);
    }

    private JsonDeserializer<String> createMockStringDeserializer() {
        return createMock(JsonDeserializer.class);
    }

    private TypeDeserializer createMockTypeDeserializer() {
        return createMock(TypeDeserializer.class);
    }
}