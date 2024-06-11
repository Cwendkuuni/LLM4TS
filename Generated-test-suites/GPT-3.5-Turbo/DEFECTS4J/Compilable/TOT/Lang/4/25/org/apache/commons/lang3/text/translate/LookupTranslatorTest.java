package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalUseCase() throws IOException {
        // Arrange
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { 
            { "a", "1" }, { "b", "2" }, { "c", "3" } 
        });
        StringWriter writer = new StringWriter();

        // Act 
        int result = translator.translate("abc", 0, writer);

        // Assert
        assertEquals("123", writer.toString());
        assertEquals(3, result);
    }

    @Test
    public void testTranslate_shortInput() throws IOException {
        // Arrange
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { 
            { "a", "1" }, { "b", "2" }, { "c", "3" } 
        });
        StringWriter writer = new StringWriter();

        // Act 
        int result = translator.translate("b", 0, writer);

        // Assert
        assertEquals("2", writer.toString());
        assertEquals(1, result);
    }

    @Test
    public void testTranslate_longInput() throws IOException {
        // Arrange
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { 
            { "aa", "11" }, { "bb", "22" }, { "cc", "33" } 
        });
        StringWriter writer = new StringWriter();

        // Act 
        int result = translator.translate("cc", 0, writer);

        // Assert
        assertEquals("33", writer.toString());
        assertEquals(2, result);
    }

    @Test
    public void testTranslate_edgeCase() throws IOException {
        // Arrange
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { 
            { "", "empty" }, { "a", "1" } 
        });
        StringWriter writer = new StringWriter();

        // Act 
        int result = translator.translate("", 0, writer);

        // Assert
        assertEquals("empty", writer.toString());
        assertEquals(0, result);
    }


    @Test
    public void testTranslate_noMatch() throws IOException {
        // Arrange
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { 
            { "a", "1" }, { "b", "2" }, { "c", "3" } 
        });
        StringWriter writer = new StringWriter();

        // Act 
        int result = translator.translate("d", 0, writer);

        // Assert
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    // Add more test cases for other methods if required

}