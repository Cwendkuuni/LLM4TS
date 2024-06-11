// CharSequenceTranslatorTest.java - Expert 1

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testSimpleTranslator() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String input = "Hello, World!";
        String expectedOutput = "Hello,%20World%21";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator translator = new AggregateTranslator(new SimpleTranslator(), new SimpleTranslator());
        String input = "Hello, World!";
        String expectedOutput = "Hello,%20World%21%20%20Hello,%20World%21";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new SimpleTranslator();
        Writer out = null;
        CharSequence input = "Test";
        try {
            translator.translate(input, out);
        } catch (IOException e) {
            fail("Should not throw IOException, but IllegalArgumentException instead");
        }
        translator.translate(input, out);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCharSequence() {
        CharSequenceTranslator translator = new SimpleTranslator();
        CharSequence input = null;
        try {
            translator.translate(input, new StringWriter(10));
        } catch (IOException e) {
            fail("Should not throw IOException, but NullPointerException instead");
        }
    }

}