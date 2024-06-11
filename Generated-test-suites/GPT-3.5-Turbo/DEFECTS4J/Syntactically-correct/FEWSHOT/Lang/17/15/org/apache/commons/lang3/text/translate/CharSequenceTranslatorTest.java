package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWhenInputIsNull() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        // Act
        String result = translator.translate(null);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testTranslateWhenInputIsEmpty() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        // Act
        String result = translator.translate("");
        
        // Assert
        assertEquals("", result);
    }
    
    @Test
    public void testTranslateWhenInputContainsNoTranslatableCharacters() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        
        // Act
        String result = translator.translate("abcdefghi");
        
        // Assert
        assertEquals("abcdefghi", result);
    }
    
    @Test
    public void testTranslateWhenInputContainsTranslatableCharacters() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("123");
                return 1;
            }
        };
        
        // Act
        String result = translator.translate("abcdefghi");
        
        // Assert
        assertEquals("1bcdefghi", result);
    }
    
    @Test
    public void testTranslateWhenInputContainsTranslatableCharactersAndNonTranslatableCharacters() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'd') {
                    out.write("4");
                    return 1;
                } else if (input.charAt(index) == 'g') {
                    out.write("7");
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        
        // Act
        String result = translator.translate("abcdefghi");
        
        // Assert
        assertEquals("abc4ef7hi", result);
    }
    
    private abstract class ConcreteCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }
}