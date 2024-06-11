package com.fasterxml.jackson.core.json;

import static com.fasterxml.jackson.core.api.TestAttributes.Builder.aAttribute;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonGeneratorImplTest {

    @Test
    public void testJsonGeneratorImpl() {
        IOContext ioContext = new IOContext();
        int features = JsonFactory.getDefaultInstance().getDefaultSerializationFeatures();
        ObjectCodec codec = null; // not used here
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, features, codec);

        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.getName(), jsonGeneratorImpl.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertFalse(jsonGeneratorImpl.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
        assertNull(jsonGeneratorImpl.getRootValueSeparator());

        JsonGeneratorImpl jsonGeneratorImplWithQuoteFieldNames = new JsonGeneratorImpl(ioContext, features, codec).enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImplWithQuoteFieldNames.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));

        JsonGeneratorImpl jsonGeneratorImplWithEscapeNonAscii = new JsonGeneratorImpl(ioContext, features | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask(), codec);
        assertFalse(jsonGeneratorImplWithEscapeNonAscii.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGeneratorImplWithEscapeNonAscii.getHighestEscapedChar());

        JsonGeneratorImpl jsonGeneratorImplWithCustomEscapes = new JsonGeneratorImpl(ioContext, features, codec).setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{-1, -2, -3};
            }
        });
        assertArrayEquals(new int[]{-1, -2, -3}, jsonGeneratorImplWithCustomEscapes.getCharacterEscapes().getEscapeCodesForAscii());

        JsonGeneratorImpl jsonGeneratorImplWithCustomMaxNonEscapedChar = new JsonGeneratorImpl(ioContext, features, codec).setHighestNonEscapedChar(123);
        assertEquals(123, jsonGeneratorImplWithCustomMaxNonEscapedChar.getHighestEscapedChar());

        JsonGeneratorImpl jsonGeneratorImplWithRootValueSeparator = new JsonGeneratorImpl(ioContext, features, codec).setRootValueSeparator(new DefaultPrettyPrinter.DefaultRootValueSeparator("_"));
        assertEquals("_", jsonGeneratorImplWithRootValueSeparator.getRootValueSeparator().getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String jsonStringWithDefaults = "";
        try {
            jsonGeneratorImpl.writeValueAsString(new Object());
            jsonStringWithDefaults = jsonGeneratorImpl.getOutputBuffer().toString();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            jsonGeneratorImpl.close();
        }
        assertTrue(jsonStringWithDefaults.isEmpty());

        String jsonStringWithQuoteFieldNames = "";
        try {
            jsonGeneratorImplWithQuoteFieldNames.writeValueAsString(new Object());
            jsonStringWithQuoteFieldNames = jsonGeneratorImplWithQuoteFieldNames.getOutputBuffer().toString();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            jsonGeneratorImplWithQuoteFieldNames.close();
        }
        assertTrue(jsonStringWithQuoteFieldNames.startsWith("{\""));

        String jsonStringWithEscapeNonAscii = "";
        try {
            jsonGeneratorImplWithEscapeNonAscii.writeValueAsString(new Object());
            jsonStringWithEscapeNonAscii = jsonGeneratorImplWithEscapeNonAscii.getOutputBuffer().toString();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            jsonGeneratorImplWithEscapeNonAscii.close();
        }
        assertFalse(jsonStringWithEscapeNonAscii.contains("\u007F")); // ASCII DEL character

        String jsonStringWithCustomEscapes = "";
        try {
            jsonGeneratorImplWithCustomEscapes.writeValueAsString(new Object());
            jsonStringWithCustomEscapes = jsonGeneratorImplWithCustomEscapes.getOutputBuffer().toString();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            jsonGeneratorImplWithCustomEscapes.close();
        }
        assertTrue(jsonStringWithCustomEscapes.contains("\\-1"));
        assertTrue(jsonStringWithCustomEscapes.contains("\\-2"));
        assertTrue(jsonStringWithCustomEscapes.contains("\\-3"));

        String jsonStringWithCustomMaxNonEscapedChar = "";
        try {
            jsonGeneratorImplWithCustomMaxNonEscapedChar.writeValueAsString(new String(new char[]{'𠮷'}));
            jsonStringWithCustomMaxNonEscapedChar = jsonGeneratorImplWithCustomMaxNonEscapedChar.getOutputBuffer().toString();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            jsonGeneratorImplWithCustomMaxNonEscapedChar.close();
        }
        assertFalse(jsonStringWithCustomMaxNonEscapedChar.contains("𠮷"));

        String jsonStringWithRootValueSeparator = "";
        try {
            jsonGeneratorImplWithRootValueSeparator.writeValueAsString(new Object());
            jsonStringWithRootValueSeparator = jsonGeneratorImplWithRootValueSeparator.getOutputBuffer().toString();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            jsonGeneratorImplWithRootValueSeparator.close();
        }
        assertTrue(jsonStringWithRootValueSeparator.startsWith("_{"));
    }

    @Test
    public void testJsonGeneratorImpl_defaultConfig() {
        IOContext ioContext = new IOContext();
        int features = JsonFactory.getDefaultInstance().getDefaultSerializationFeatures();
        ObjectCodec codec = null; // not used here
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, features, codec);

        assertEquals(JsonGenerator.Feature.ESCAPE_NON_ASCII.getName(), jsonGeneratorImpl.isEnabled(JsonGenerator.Feature.ESCAPE_NON_ASCII));
        assertFalse(jsonGeneratorImpl.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGeneratorImpl.getHighestEscapedChar());
        assertNull(jsonGeneratorImpl.getCharacterEscapes());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, jsonGeneratorImpl.getRootValueSeparator());

        JsonGeneratorImpl jsonGeneratorImplWithQuoteFieldNames = new JsonGeneratorImpl(ioContext, features, codec).enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertTrue(jsonGeneratorImplWithQuoteFieldNames.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));

        JsonGeneratorImpl jsonGeneratorImplWithEscapeNonAscii = new JsonGeneratorImpl(ioContext, features | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask(), codec);
        assertFalse(jsonGeneratorImplWithEscapeNonAscii.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertEquals(127, jsonGeneratorImplWithEscapeNonAscii.getHighestEscapedChar());

        JsonGeneratorImpl jsonGeneratorImplWithCustomEscapes = new JsonGeneratorImpl(ioContext, features, codec).setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{0};
            }
        });
        assertEquals(new int[]{0}, jsonGeneratorImplWithCustomEscapes.getCharacterEscapes().getEscapeCodesForAscii());

        JsonGeneratorImpl jsonGeneratorImplWithCustomMaxNonEscapedChar = new JsonGeneratorImpl(ioContext, features, codec).setHighestNonEscapedChar(123);
        assertEquals(123, jsonGeneratorImplWithCustomMaxNonEscapedChar.getHighestEscapedChar());

        JsonGeneratorImpl jsonGeneratorImplWithRootValueSeparator = new JsonGeneratorImpl(ioContext, features, codec).setRootValueSeparator(new DefaultPrettyPrinter.DefaultRootValueSeparator("_"));
        assertEquals("_", jsonGeneratorImplWithRootValueSeparator.getRootValueSeparator().getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        assertEquals("{}", jsonGeneratorImpl.writeValueAsString(new Object()));

        JsonGeneratorImpl jsonGeneratorImplWithQuoteFieldNames = new JsonGeneratorImpl(ioContext, features, codec).enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES);
        assertEquals("{\"\":{}}", jsonGeneratorImplWithQuoteFieldNames.writeValueAsString(new Object()));

        JsonGeneratorImpl jsonGeneratorImplWithEscapeNonAscii = new JsonGeneratorImpl(ioContext, features | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask(), codec);
        assertEquals("{\"\\u007F\":{}}", jsonGeneratorImplWithEscapeNonAscii.writeValueAsString(new Object()));

        JsonGeneratorImpl jsonGeneratorImplWithCustomEscapes = new JsonGeneratorImpl(ioContext, features, codec).setCharacterEscapes(new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return new int[]{0, 1, 2};
            }
        });
        assertEquals("{\"\\u0000\":{\"\\u0001\":{\"\\u0002\":{}}}}", jsonGeneratorImplWithCustomEscapes.writeValueAsString(new Object()));

        JsonGeneratorImpl jsonGeneratorImplWithCustomMaxNonEscapedChar = new JsonGeneratorImpl(ioContext, features, codec).setHighestNonEscapedChar(0);
        assertEquals("{\"\":{}}", jsonGeneratorImplWithCustomMaxNonEscapedChar.writeValueAsString(new Object()));

        JsonGeneratorImpl jsonGeneratorImplWithRootValueSeparator = new JsonGeneratorImpl(ioContext, features, codec).setRootValueSeparator(new DefaultPrettyPrinter.DefaultRootValueSeparator("_"));
        assertEquals("_{\"\":{}}", jsonGeneratorImplWithRootValueSeparator.writeValueAsString(new Object()));
    }

    @Test
    public void testJsonGeneratorImpl_customConfig() {
        IOContext ioContext = new IOContext();
        int features = JsonFactory.getDefaultInstance().getDefaultSerializationFeatures() | JsonGenerator.Feature.QUOTE_FIELD_NAMES.getMask();
        ObjectCodec codec = null; // not used here
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(ioContext, features, codec) {
            @Override
            protected int[] _outputEscapes = new int[0];

            @Override
            protected CharacterEscapes _characterEscapes = new CharacterEscapes() {
                @Override
                public int[] getEscapeCodesForAscii() {
                    return new int[]{1, 2, 3};
                }
            };

            @Override
            protected SerializableString _rootValueSeparator = new DefaultPrettyPrinter.DefaultRootValueSeparator("[");
        };

        assertTrue(jsonGeneratorImpl.isEnabled(JsonGenerator.Feature.QUOTE_FIELD_NAMES));
        assertArrayEquals(new int[]{1, 2, 3}, jsonGeneratorImpl.getCharacterEscapes().getEscapeCodesForAscii());
        assertEquals("[", jsonGeneratorImpl.getRootValueSeparator().getValue());

        JsonGeneratorImpl jsonGeneratorImplWithEscapeNonAscii = new JsonGeneratorImpl(ioContext, features | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask(), codec) {
            @Override
            protected int[] _outputEscapes = new int[]{0x7F};
        };
        assertEquals("{\"\\u007F\":{}}", jsonGeneratorImplWithEscapeNonAscii.writeValueAsString(new Object()));

        JsonGeneratorImpl jsonGeneratorImplWithCustomMaxNonEscapedChar = new JsonGeneratorImpl(ioContext, features, codec) {
            @Override
            protected int _maximumNonEscapedChar = 0;
        };
        assertEquals("{}", jsonGeneratorImplWithCustomMaxNonEscapedChar.writeValueAsString(new Object()));

        JsonGeneratorImpl jsonGeneratorImplWithRootValueSeparator = new JsonGeneratorImpl(ioContext, features, codec) {
            @Override
            protected SerializableString _rootValueSeparator = new DefaultPrettyPrinter.DefaultRootValueSeparator("_");
        };
        assertEquals("_{\"\":{}}", jsonGeneratorImplWithRootValueSeparator.writeValueAsString(new Object()));
    }
}