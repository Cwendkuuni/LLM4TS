package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

import org.junit.Test;

/**
 * Test class for {@link JsonGeneratorImpl}
 */
public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImplConstruction() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testJsonGeneratorImplDefaultFeatures() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        assertTrue(jsonGeneratorImpl.isEnabled(Feature.WRITE_BIGDECIMAL_AS_PLAIN));
        assertFalse(jsonGeneratorImpl.isEnabled(Feature.ESCAPE_FIELD_NAMES));
        assertFalse(jsonGeneratorImpl.isEnabled(Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
        assertNull(jsonGeneratorImpl.getRootValueSeparator());
    }

    @Test
    public void testJsonGeneratorImplCustomFeatures() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.getId() | Feature.ESCAPE_FIELD_NAMES.getId(), null);
        assertTrue(jsonGeneratorImpl.isEnabled(Feature.WRITE_BIGDECIMAL_AS_PLAIN));
        assertTrue(jsonGeneratorImpl.isEnabled(Feature.ESCAPE_FIELD_NAMES));
        assertFalse(jsonGeneratorImpl.isEnabled(Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
        assertNull(jsonGeneratorImpl.getRootValueSeparator());

        JsonGeneratorImpl jsonGeneratorImplQuoteFieldNames = new JsonGeneratorImpl(ioContext, Feature.QUOTE_FIELD_NAMES.getId(), null);
        assertTrue(jsonGeneratorImplQuoteFieldNames.isEnabled(Feature.WRITE_BIGDECIMAL_AS_PLAIN));
        assertFalse(jsonGeneratorImplQuoteFieldNames.isEnabled(Feature.ESCAPE_FIELD_NAMES));
        assertTrue(jsonGeneratorImplQuoteFieldNames.isEnabled(Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGeneratorImplQuoteFieldNames.getHighestEscapedChar());
        assertNull(jsonGeneratorImplQuoteFieldNames.getCharacterEscapes());
        assertNull(jsonGeneratorImplQuoteFieldNames.getRootValueSeparator());
    }

    @Test
    public void testJsonGeneratorImplCustomEscapes() {
        IOContext ioContext = new IOContext();
        CharacterEscapes characterEscapes = new CharacterEscapes() {
            @Override
            public String getEscapeSeq(char c, int featureFlags) {
                return Character.toString(c + 1);
            }
        };
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setCharacterEscapes(characterEscapes);
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        jsonGenerator.setCharacterEscapes(jsonGeneratorImpl.getCharacterEscapes());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("test", "test");
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputFlowWriter().toString();
        assertTrue(result.contains("\\u0002est"));
    }

    @Test
    public void testJsonGeneratorImplCustomRootValueSeparator() {
        IOContext ioContext = new IOContext();
        SerializableString rootValueSeparator = new SerializableString("[");
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setRootValueSeparator(rootValueSeparator);
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        jsonGenerator.setRootValueSeparator(jsonGeneratorImpl.getRootValueSeparator());
        jsonGenerator.writeStartValue();
        jsonGenerator.writeString("test");
        jsonGenerator.writeEnd();
        String result = jsonGenerator.getOutputFlowWriter().toString();
        assertTrue(result.startsWith("[{\"test\":\""));
    }

    @Test
    public void testJsonGeneratorImplCustomMaxNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(128);
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new StringWriter());
        jsonGenerator.setPrettyPrinter(new DefaultPrettyPrinter());
        jsonGenerator.setRootValueSeparator(jsonGeneratorImpl.getRootValueSeparator());
        jsonGenerator.setCharacterEscapes(jsonGeneratorImpl.getCharacterEscapes());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("test", (char) 128);
        jsonGenerator.writeEndObject();
        String result = jsonGenerator.getOutputFlowWriter().toString();
        assertTrue(result.contains("\\u0080"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonGeneratorImplNegativeMaxNonEscapedChar() {
        IOContext ioContext = new IOContext();
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, 0, null);
        jsonGeneratorImpl.setHighestNonEscapedChar(-1);
    }
}