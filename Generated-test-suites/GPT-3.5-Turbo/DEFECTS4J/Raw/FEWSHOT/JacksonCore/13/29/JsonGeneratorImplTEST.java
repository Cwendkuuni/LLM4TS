
package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {
    
    @Mock
    IOContext ctxt;
    
    @Mock
    ObjectCodec codec;
    
    JsonGeneratorImpl generator;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        generator = new JsonGeneratorImpl(ctxt, 0, codec) {
            @Override
            public void close() throws IOException {
            }
        };
    }
    
    // Test cases for setCharacterEscapes() method
    
    @Test
    public void testSetCharacterEscapes_shouldSetCharacterEscapes() {
        CharacterEscapes esc = new CharacterEscapes() {
            @Override
            public int[] getEscapeCodesForAscii() {
                return null;
            }

            @Override
            public SerializableString getEscapeSequence(int charToEscape) {
                return null;
            }
        };
        generator.setCharacterEscapes(esc);

        assertEquals(esc, generator.getCharacterEscapes());
    }
    
    @Test
    public void testSetCharacterEscapes_withNull_shouldSetStandardEscapes() {
        generator.setCharacterEscapes(null);
        
        assertTrue(generator.getCharacterEscapes() instanceof CharacterEscapes);

        int[] expectedOutputEscapes = CharTypes.get7BitOutputEscapes();
        int[] actualOutputEscapes = generator._outputEscapes;

        assertEquals(expectedOutputEscapes.length, actualOutputEscapes.length);
        for (int i = 0; i < expectedOutputEscapes.length; i++) {
            assertEquals(expectedOutputEscapes[i], actualOutputEscapes[i]);
        }
    }
    
    // Test cases for setRootValueSeparator() method
    
    @Test
    public void testSetRootValueSeparator_shouldSetRootValueSeparator() {
        SerializableString sep = new SerializableString() {
            @Override
            public String getValue() {
                return "";
            }
            @Override
            public int charLength() {
                return 0;
            }
            @Override
            public char[] asQuotedChars() {
                return null;
            }
            @Override
            public byte[] asUnquotedUTF8() {
                return null;
            }
            @Override
            public char[] asUnquotedChars() {
                return null;
            }
            @Override
            public byte[] asQuotedUTF8() {
                return null;
            }
            @Override
            public boolean hasQuotes() {
                return false;
            }
        };
        generator.setRootValueSeparator(sep);

        assertEquals(sep, generator._rootValueSeparator);
    }
    
    // Test cases for version() method
    
    @Test
    public void testVersion_shouldReturnVersion() {
        assertTrue(generator.version() instanceof Version);
    }
}
