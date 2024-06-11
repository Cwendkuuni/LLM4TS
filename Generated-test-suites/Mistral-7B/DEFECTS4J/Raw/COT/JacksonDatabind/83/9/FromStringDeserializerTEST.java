package com.example.junit; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    // Test methods for abstract class FromStringDeserializer

    @Test(expected = UnsupportedOperationException.class)
    public void testTypesReturnsSupportedTypes() {
        FromStringDeserializer<?> deserializer = new Std(String.class, 0);
        assertArrayEquals(new Class<?>[] {
                File.class,
                URL.class,
                URI.class,
                Class.class,
                JavaType.class,
                Currency.class,
                Pattern.class,
                Locale.class,
                Charset.class,
                TimeZone.class,
                InetAddress.class,
                InetSocketAddress.class,
                StringBuilder.class
        }, FromStringDeserializer.types());
    }

    @Test
    public void testFindDeserializerReturnsDeserializerForSupportedTypes() {
        Class<?>[] types = new Class<?>[] {
                File.class,
                URL.class,
                URI.class,
                Class.class,
                JavaType.class,
                Currency.class,
                Pattern.class,
                Locale.class,
                Charset.class,
                TimeZone.class,
                InetAddress.class,
                InetSocketAddress.class,
                StringBuilder.class
        };

        for (Class<?> type : types) {
            Std deserializer = (Std) FromStringDeserializer.findDeserializer(type);
            assertEquals(type, deserializer.getValueClass());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDeserializeNullJsonParser() throws IOException {
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        deserializer.deserialize(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidJsonString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("invalidJsonString");
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("");
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        Object result = deserializer.deserialize(parser, null);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStringForFileDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("invalidStringForFileDeserializer");
        FromStringDeserializer<File> deserializer = new Std(File.class, Std.STD_FILE);
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserializeValidStringForFileDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"path/to/file.txt\"");
        FromStringDeserializer<File> deserializer = new Std(File.class, Std.STD_FILE);
        File result = deserializer.deserialize(parser, null);
        assertEquals("path/to/file.txt", result.getAbsolutePath());
    }

    // Test methods for abstract method _deserialize(String value, DeserializationContext ctxt)

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethod_deserialize() {
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        assertThrows(UnsupportedOperationException.class, () -> deserializer._deserialize("value", null));
    }

    @Test
    public void test_deserializeForStringBuilder() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("new StringBuilder()");
        FromStringDeserializer<StringBuilder> deserializer = new Std(StringBuilder.class, Std.STD_STRING_BUILDER);
        StringBuilder result = deserializer._deserialize("value", null);
        assertEquals("value", result.toString());
    }

    // Test methods for protected method _deserializeEmbedded(Object ob, DeserializationContext ctxt)

    @Test
    public void test_deserializeEmbeddedForObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"value\": 42 }");
        ObjectNode node = (ObjectNode) mapper.readTree(parser);
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        Object result = deserializer._deserializeEmbedded(node, null);
        assertEquals(42, result.getClass().getMethods()[0].invoke(result));
    }

    @Test
    public void test_deserializeEmbeddedForNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("null");
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        Object result = deserializer._deserializeEmbedded(null, null);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void test_deserializeEmbeddedForUnsupportedType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{ \"value\": 42.5 }");
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        deserializer._deserializeEmbedded(parser.getEmbeddedObject(), null);
    }

    // Test methods for protected method _deserializeFromEmptyString()

    @Test
    public void test_deserializeFromEmptyStringForStringBuilder() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("");
        FromStringDeserializer<StringBuilder> deserializer = new Std(StringBuilder.class, Std.STD_STRING_BUILDER);
        StringBuilder result = deserializer._deserializeFromEmptyString();
        assertEquals("", result.toString());
    }

    @Test
    public void test_deserializeFromEmptyStringForNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("");
        FromStringDeserializer<Object> deserializer = new Std(String.class, 0);
        Object result = deserializer._deserializeFromEmptyString();
        assertNull(result);
    }

    // Test methods for protected method _deserializeFromArray(JsonParser p, DeserializationContext ctxt)

    @Test
    public void test_deserializeFromArrayForEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[ ]");
        FromStringDeserializer<Object[]> deserializer = new Std(Object[].class, Std.STD_ARRAY);
        Object[] result = deserializer._deserializeFromArray(parser, null);
        assertArrayEquals(new Object[0], result);
    }

    @Test
    public void test_deserializeFromArrayForSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[ 42 ]");
        FromStringDeserializer<Object[]> deserializer = new Std(Object[].class, Std.STD_ARRAY);
        Object[] result = deserializer._deserializeFromArray(parser, null);
        assertEquals(new Object[] { 42 }, result);
    }

    @Test
    public void test_deserializeFromArrayForMultiElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[ 42, 43, 44 ]");
        FromStringDeserializer<Object[]> deserializer = new Std(Object[].class, Std.STD_ARRAY);
        Object[] result = deserializer._deserializeFromArray(parser, null);
        assertArrayEquals(new Object[] { 42, 43, 44 }, result);
    }

    // Test methods for protected method _deserializeFromJsonNode(JsonNode node, DeserializationContext ctxt)

    @Test
    public void test_deserializeFromJsonNodeForStringBuilder() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("new StringBuilder()");
        JsonNode node = mapper.readTree(parser);
        FromStringDeserializer<StringBuilder> deserializer = new Std(StringBuilder.class, Std.STD_STRING_BUILDER);
        StringBuilder result = deserializer._deserializeFromJsonNode(node, null);
        assertEquals("", result.toString());
    }

    @Test
    public void test_deserializeFromJsonNodeForString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("\"Hello, World!\"");
        JsonNode node = mapper.readTree(parser);
        FromStringDeserializer<String> deserializer = new Std(String.class, Std.STD_STRING);
        String result = deserializer._deserializeFromJsonNode(node, null);
        assertEquals("Hello, World!", result);
    }

    // Test methods for protected method _firstHyphenOrUnderscore(String str)

    @Test
    public void test_firstHyphenOrUnderscoreForEmptyString() {
        FromStringDeserializer deserializer = new Std(String.class, 0);
        int result = deserializer._firstHyphenOrUnderscore("");
        assertEquals(-1, result);
    }

    @Test
    public void test_firstHyphenOrUnderscoreForSingleHyphen() {
        FromStringDeserializer deserializer = new Std(String.class, 0);
        int result = deserializer._firstHyphenOrUnderscore("_value");
        assertEquals(0, result);
    }

    @Test
    public void test_firstHyphenOrUnderscoreForSingleUnderscore() {
        FromStringDeserializer deserializer = new Std(String.class, 0);
        int result = deserializer._firstHyphenOrUnderscore("value_");
        assertEquals(4, result);
    }

    @Test
    public void test_firstHyphenOrUnderscoreForMultipleHyphens() {
        FromStringDeserializer deserializer = new Std(String.class, 0);
        int result = deserializer._firstHyphenOrUnderscore("value____");
        assertEquals(0, result);
    }

    @Test
    public void test_firstHyphenOrUnderscoreForMultipleUnderscores() {
        FromStringDeserializer deserializer = new Std(String.class, 0);
        int result = deserializer._firstHyphenOrUnderscore("value_____");
        assertEquals(5, result);
    }

    @Test
    public void test_firstHyphenOrUnderscoreForMultipleHyphensAndUnderscores() {
        FromStringDeserializer deserializer = new Std(String.class, 0);
        int result = deserializer._firstHyphenOrUnderscore("value__value_____");
        assertEquals(0, result);
    }
}