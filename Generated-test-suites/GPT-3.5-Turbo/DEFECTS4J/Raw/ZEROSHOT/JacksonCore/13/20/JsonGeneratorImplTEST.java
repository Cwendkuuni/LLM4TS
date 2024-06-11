package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    private JsonGeneratorImpl jsonGeneratorImpl;

    @Before
    public void setUp() throws Exception {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = null;
        jsonGeneratorImpl = new JsonGeneratorImpl(ctxt, features, codec) {
            @Override
            public void flush() throws IOException {

            }
            @Override
            public void close() throws IOException {

            }
            @Override
            public void writeFieldName(String name) throws IOException {
                
            }
            @Override
            public void writeStartObject() throws IOException {
                
            }
            @Override
            public void writeEndObject() throws IOException {
                
            }
            @Override
            public void writeFieldName(SerializableString name) throws IOException {
                
            }
            @Override
            public void writeStartArray() throws IOException {
                
            }
            @Override
            public void writeEndArray() throws IOException {
                
            }
            @Override
            public void writeString(String text) throws IOException {
                
            }
            @Override
            public void writeString(char[] text, int offset, int len) throws IOException {
                
            }
            @Override
            public void writeString(SerializableString text) throws IOException {
                
            }
            
        };
    }

    // Test for JsonGeneratorImpl constructor
    @Test
    public void testJsonGeneratorImpl() {
        IOContext ctxt = new IOContext();
        int features = 0;
        ObjectCodec codec = null;
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(ctxt, features, codec);
        assertNotNull(jsonGenerator);
    }

    // Test for enable method
    @Test
    public void testEnable() {
        assertEquals(jsonGeneratorImpl.enable(Feature.AUTO_CLOSE_JSON_CONTENT), jsonGeneratorImpl);
    }

    // Test for _checkStdFeatureChanges method
    @Test
    public void test_checkStdFeatureChanges() {
        jsonGeneratorImpl._checkStdFeatureChanges(1, 0);
        assertTrue(jsonGeneratorImpl._cfgUnqNames);
    }

    // Test for setHighestNonEscapedChar method
    @Test
    public void testSetHighestNonEscapedChar() {
        assertEquals(jsonGeneratorImpl.setHighestNonEscapedChar(127), jsonGeneratorImpl);
    }

    // Test for getHighestEscapedChar method
    @Test
    public void testGetHighestEscapedChar() {
        jsonGeneratorImpl.setHighestNonEscapedChar(127);
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
    }

    // Test for setCharacterEscapes method
    @Test
    public void testSetCharacterEscapes() {
        CharacterEscapes esc = null;
        assertEquals(jsonGeneratorImpl.setCharacterEscapes(esc), jsonGeneratorImpl);
    }

    // Test for getCharacterEscapes method
    @Test
    public void testGetCharacterEscapes() {
        CharacterEscapes esc = null;
        jsonGeneratorImpl.setCharacterEscapes(esc);
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
    }

    // Test for setRootValueSeparator method
    @Test
    public void testSetRootValueSeparator() {
        SerializableString sep = new DefaultPrettyPrinter().DEFAULT_ROOT_VALUE_SEPARATOR;
        assertEquals(jsonGeneratorImpl.setRootValueSeparator(sep), jsonGeneratorImpl);
    }

    // Test for version method
    @Test
    public void testVersion() {
        Version version = VersionUtil.versionFor(jsonGeneratorImpl.getClass());
        assertEquals(version, jsonGeneratorImpl.version());
    }

    // Test for writeStringField method
    @Test
    public void testWriteStringField() {
        String fieldName = "test";
        String value = "test";
        try {
            jsonGeneratorImpl.writeStringField(fieldName, value);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}