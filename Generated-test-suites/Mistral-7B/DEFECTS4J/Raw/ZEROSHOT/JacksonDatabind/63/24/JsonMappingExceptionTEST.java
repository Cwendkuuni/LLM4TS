package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() throws IOException {
        String json = "{\"name\": \"John Doe\", \"age\": 30, \"cities\": [\"New York\", \"Los Angeles\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonMappingException exception = null;

        try {
            mapper.readValue(new StringReader(json), String.class);
        } catch (JsonMappingException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("Expected String but was BEAN_DESCRIPTOR, class [simple type, class com.fasterxml.jackson.databind.JsonMappingException$Reference]",
                exception.getMessage());
        List<Reference> path = exception.getPath();
        assertEquals(3, path.size());

        Reference reference1 = path.get(0);
        assertEquals("cities", reference1.getFieldName());
        assertEquals("[Ljava.lang.String;", reference1.getDescription());

        Reference reference2 = path.get(1);
        assertEquals("0", reference2.getIndex());
        assertEquals("[Ljava.lang.String;", reference2.getDescription());

        Reference reference3 = path.get(2);
        assertEquals("name", reference3.getFieldName());
        assertEquals("{\"name\": \"John Doe\", \"age\": 30, \"cities\": [\"New York\", \"Los Angeles\"]}", reference3.getDescription());

        // Test toString method
        String message = exception.toString();
        assertTrue(message.contains("JsonMappingException: Expected String but was BEAN_DESCRIPTOR, class [simple type, class com.fasterxml.jackson.databind.JsonMappingException$Reference]"));
        assertTrue(message.contains("(through reference chain: cities[0]->name)"));

        // Test getFrom() method
        Object from1 = reference1.getFrom();
        assertNotNull(from1);
        assertTrue(from1 instanceof Array);

        Object from2 = reference2.getFrom();
        assertNotNull(from2);
        assertTrue(from2 instanceof String);

        Object from3 = reference3.getFrom();
        assertNotNull(from3);
        assertTrue(from3 instanceof String);

        // Test getLocation() method
        JsonParser parser = mapper.getFactory().createParser(new StringReader(json));
        JsonMappingException locationException = null;
        try {
            mapper.readValue(parser, String.class);
        } catch (JsonMappingException e) {
            locationException = e;
        }
        assertNotNull(locationException);
        JsonLocation location = locationException.getLocation();
        assertNotNull(location);
        assertEquals(location.getColumnNumber(), 4);
        assertEquals(location.getLineNumber(), 1);

        // Test getProcessor() method
        Closeable processor = exception.getProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof JsonParser);

        // Test from() method
        JsonMappingException fromParserException = JsonMappingException.from(parser, "Expected String but was BEAN_DESCRIPTOR, class [simple type, class com.fasterxml.jackson.databind.JsonMappingException$Reference]");
        assertNotNull(fromParserException);
        assertEquals(exception.getMessage(), fromParserException.getMessage());
        List<Reference> fromParserPath = fromParserException.getPath();
        assertEquals(exception.getPath().size(), fromParserPath.size());

        // Test wrapWithPath() method
        Throwable src = new IOException("Unexpected IOE");
        JsonMappingException wrappedException1 = JsonMappingException.wrapWithPath(src, new String[]{"New York"}, "city");
        assertEquals("Unexpected IOE (through reference chain: cities[0]->city)", wrappedException1.getMessage());
        assertEquals(1, wrappedException1.getPath().size());

        Throwable src2 = new IOException("Unexpected IOE");
        JsonMappingException wrappedException2 = JsonMappingException.wrapWithPath(src2, new int[]{0}, null);
        assertEquals("Unexpected IOE (through reference chain: cities[0])", wrappedException2.getMessage());
        assertEquals(1, wrappedException2.getPath().size());

        // Test fromUnexpectedIOE() factory method
        JsonMappingException unexpectedIOE = JsonMappingException.fromUnexpectedIOE(new IOException("Unexpected IOE"));
        assertEquals("Unexpected IOException (of type java.io.IOException): Unexpected IOE", unexpectedIOE.getMessage());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingExceptionWithLocation() throws IOException {
        String json = "{\"name\": null, \"age\": 30, \"cities\": [\"New York\", \"Los Angeles\"]}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.FAIL_ON_EMPTY_BEANS, true);

        try {
            mapper.readValue(new StringReader(json), City.class);
        } catch (JsonProcessingException e) {
            throw new JsonMappingException(e, "Expected String but was START_OBJECT");
        }
    }

    private static class City {
        private String name;
        private List<String> cities;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getCities() {
            return cities;
        }

        public void setCities(List<String> cities) {
            this.cities = cities;
        }
    }

    static class JsonFactoryTest extends JsonFactory {
        @Override
        public JsonParser createParser(CharSequence cs) {
            return null;
        }

        @Override
        public JsonParser createParser(File f) {
            return null;
        }

        @Override
        public JsonParser createParser(InputStream ios) {
            return null;
        }

        @Override
        public JsonParser createParser(byte[] bytes) {
            return null;
        }

        @Override
        public JsonParser createParser(byte[] bytes, int offset, int length) {
            return null;
        }

        @Override
        public JsonParser createParser(StreamSource src) {
            return null;
        }

        @Override
        public JsonParser createParser(CharSource charSrc) {
            return null;
        }

        @Override
        public JsonParser createParser(String content) {
            return new StringParser(content);
        }

        @Override
        public JsonParser createParser(URL url) {
            return null;
        }
    }

    static class StringParser implements JsonParser {
        private final String content;

        public StringParser(String content) {
            this.content = content;
        }

        @Override
        public int nextToken() {
            return 0;
        }

        @Override
        public int nextVal() {
            return 0;
        }

        @Override
        public boolean nextBoolean() {
            return false;
        }

        @Override
        public boolean nextValueIsNull() {
            return false;
        }

        @Override
        public boolean nextValueIsNumber() {
            return false;
        }

        @Override
        public boolean nextValueIsBoolean() {
            return false;
        }

        @Override
        public boolean nextValueIsString() {
            return false;
        }

        @Override
        public boolean nextValueIsArray() {
            return false;
        }

        @Override
        public boolean nextValueIsObject() {
            return false;
        }

        @Override
        public boolean nextValueIsNull() {
            return false;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public JsonFactory getFactory() {
            return null;
        }

        @Override
        public JsonParser nextValue() throws IOException, JsonProcessingException {
            return null;
        }

        @Override
        public void close() {

        }

        @Override
        public JsonTokenCurrent location() {
            return null;
        }

        @Override
        public String getText() {
            return content;
        }

        @Override
        public int getBinaryValue(Base64Variant variant) throws IOException, JsonProcessingException {
            return 0;
        }

        @Override
        public char[] getText(char[] buffer, int offset, int length) throws IOException, JsonProcessingException {
            return new char[0];
        }

        @Override
        public int readBytes(byte[] bytes, int offset, int length) throws IOException, JsonProcessingException {
            return 0;
        }

        @Override
        public void setCodec(JsonDecoder decoder) {

        }

        @Override
        public void setCodec(JsonEncoder encoder) {

        }
    }

    @Test
    public void testSerializeReferenceWithJdkSerialization() throws IOException {
        JsonMappingException exception = new JsonMappingException(new StringReader(""), "Exception message");
        exception.prependPath(new Object() {});
        String written = new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(exception).toString();
        JsonMappingException deserialized = (JsonMappingException) new ObjectInputStream(new ByteArrayInputStream(written.getBytes())).readObject();
        assertEquals(exception.getMessage(), deserialized.getMessage());
        assertNotNull(deserialized.getPath());
        assertEquals(exception.getPath().size(), deserialized.getPath().size());
    }
}