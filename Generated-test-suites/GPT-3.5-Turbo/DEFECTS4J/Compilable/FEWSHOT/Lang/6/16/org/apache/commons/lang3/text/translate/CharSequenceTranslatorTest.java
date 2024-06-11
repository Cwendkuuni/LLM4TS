
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        // Given
        CharSequenceTranslator translator = new TestTranslator();
        String input = "This is a test.";
        String expectedOutput = "Th!s !s ! t1st.";
        
        // When
        String actualOutput = translator.translate(input);
        
        // Then
        assertEquals(expectedOutput, actualOutput);
    }
    
    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'i') {
                out.write('!');
                return 1;
            }
            if (input.charAt(index) == 'e') {
                out.write('1');
                return 1;
            }
            out.write(input.charAt(index));
            return 0;
        }
    }
}
