package com.example.generatorbase;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonWriteContext;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeneratorBaseTest {
    private static final String WRITE_BINARY = "write a binary value";
    private static final String WRITE_BOOLEAN = "write a boolean value";
    private static final String WRITE_NULL = "write a null";
    private static final String WRITE_NUMBER = "write a number";
    private static final String WRITE_RAW = "write a raw (unencoded) value";
    private static final String WRITE_STRING = "write a string";

    private ObjectCodec _objectCodec;
    private JsonFactory _jsonFactory;
    private JsonWriter _jsonWriter;
    private JsonWriteContext _writeContext;

    @Before
    public void setup() throws Exception {
        _jsonFactory = new JsonFactory();
        _objectCodec = _jsonFactory.getCodec();
        _writeContext = JsonWriteContext.createRootContext();
        _jsonWriter = _jsonFactory.createWriter(new ByteArrayOutputStream(), _objectCodec);
        _jsonWriter.setWriteContext(_writeContext);
    }

    @After
    public void teardown() throws Exception {
        _jsonWriter.close();
    }

    @Test
    public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), _objectCodec);
        assertTrue(generatorBase.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
    }

    @Test
    public void testIsEnabled_ESCAPE_NON_ASCII() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask(), _objectCodec);
        assertTrue(generatorBase.isEnabled(Feature.ESCAPE_NON_ASCII));
    }

    @Test
    public void testIsEnabled_STRICT_DUPLICATE_DETECTION() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | Feature.ESCAPE_NON_ASCII.getMask() | Feature.STRICT_DUPLICATE_DETECTION.getMask(), _objectCodec);
        assertTrue(generatorBase.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testVersion() throws Exception {
        GeneratorBase generatorBase = new GeneratorBase(0, _objectCodec);
        assertEquals("2.12.0", generatorBase.version().toString());
    }

    @Test
    public void testWriteFieldName() throws Exception {
        _jsonWriter.writeFieldName("test");
        TreeNode tree = _jsonFactory.readTree(_jsonWriter.getOutputBuffer().toByteArray());
        assertEquals("test", tree.get("name").asText());
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteRawValue_UnsupportedOperation() throws Exception {
        _jsonWriter.writeRawValue("test");
    }

    @Test
    public void testWriteNull() throws Exception {
        _jsonWriter.writeNull();
        TreeNode tree = _jsonFactory.readTree(_jsonWriter.getOutputBuffer().toByteArray());
        assertEquals("null", tree.asText());
    }

    @Test
    public void testWriteBoolean() throws Exception {
        _jsonWriter.writeBoolean(true);
        TreeNode tree = _jsonFactory.readTree(_jsonWriter.getOutputBuffer().toByteArray());
        assertEquals("true", tree.asText());
    }

    @Test
    public void testWriteNumber() throws Exception {
        _jsonWriter.writeNumber(123);
        TreeNode tree = _jsonFactory.readTree(_jsonWriter.getOutputBuffer().toByteArray());
        assertEquals("123", tree.asText());
    }

    @Test
    public void testWriteString() throws Exception {
        _jsonWriter.writeString("test");
        TreeNode tree = _jsonFactory.readTree(_jsonWriter.getOutputBuffer().toByteArray());
        assertEquals("test", tree.asText());
    }

    @Test
    public void testWriteRawValueString() throws Exception {
        _jsonWriter.writeRawValue("test");
        byte[] bytes = _jsonWriter.getOutputBuffer().toByteArray();
        assertEquals("test", new String(bytes));
    }

    @Test
    public void testWriteRawValueStringWithOffset() throws Exception {
        String test = "test";
        _jsonWriter.writeRawValue(test, 0, test.length());
        byte[] bytes = _jsonWriter.getOutputBuffer().toByteArray();
        assertEquals(test, new String(bytes, 0, test.length()));
    }

    @Test
    public void testWriteRawValueCharArray() throws Exception {
        char[] test = {'t', 'e', 's', 't'};
        _jsonWriter.writeRawValue(test);
        byte[] bytes = _jsonWriter.getOutputBuffer().toByteArray();
        assertEquals(new String(test), new String(bytes));
    }

    @Test
    public void testWriteRawValueCharArrayWithOffset() throws Exception {
        char[] test = {'t', 'e', 's', 't'};
        _jsonWriter.writeRawValue(test, 0, test.length);
        byte[] bytes = _jsonWriter.getOutputBuffer().toByteArray();
        assertEquals(new String(test), new String(bytes, 0, test.length));
    }

    @Test
    public void testWriteObject() throws Exception {
        ObjectMapper mapper = _objectCodec.treeToValue(new ObjectMapper(_jsonFactory).readTree("{\"test\": \"hello\"}"), String.class);
        _jsonWriter.writeObject(mapper);
        TreeNode tree = _jsonFactory.readTree(_jsonWriter.getOutputBuffer().toByteArray());
        assertEquals("hello", tree.asText());
    }

    @Test
    public void testWriteTree() throws Exception {
        ObjectMapper mapper = new ObjectMapper(_jsonFactory);
        TreeNode tree = mapper.readTree("{ \"test\": \"hello\" }");
        _jsonWriter.writeTree(tree);
        TreeNode treeOut = _jsonFactory.readTree(_jsonWriter.getOutputBuffer().toByteArray());
        assertEquals("hello", treeOut.get("test").asText());
    }
}