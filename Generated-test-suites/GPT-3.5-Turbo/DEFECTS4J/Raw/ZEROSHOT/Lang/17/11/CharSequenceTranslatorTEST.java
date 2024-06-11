import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Test
    public void testTranslate() throws IOException {
        translator = new TestCharSequenceTranslator();
        
        String input = "Hello world!";
        Writer out = new StringWriter();
        translator.translate(input, out);
        String result = out.toString();
        
        Assert.assertEquals("Translated text should match expected result", "Hola mundo!", result);
    }
    
    @Test
    public void testTranslateWithNullInput() {
        translator = new TestCharSequenceTranslator();
        
        String result = translator.translate(null);
        
        Assert.assertNull("Translated text should be null", result);
    }
    
    @Test
    public void testTranslateWithIOException() {
        translator = new TestCharSequenceTranslator();
        
        String input = "Hello world!";
        Writer out = new TestWriter();
        
        try {
            translator.translate(input, out);
            Assert.fail("IOException should have been thrown");
        } catch (IOException e) {
            Assert.assertTrue("IOException should be thrown", true);
        }
    }
    
    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        translator = new TestCharSequenceTranslator();
        
        String input = "";
        Writer out = new StringWriter();
        translator.translate(input, out);
        String result = out.toString();
        
        Assert.assertEquals("Translated text should match expected result", "", result);
    }
    
    @Test
    public void testTranslateWithSingleCodepoint() throws IOException {
        translator = new TestCharSequenceTranslator();
        
        String input = "A";
        Writer out = new StringWriter();
        translator.translate(input, out);
        String result = out.toString();
        
        Assert.assertEquals("Translated text should match expected result", "B", result);
    }
    
    @Test
    public void testTranslateWithMultipleCodepoints() throws IOException {
        translator = new TestCharSequenceTranslator();
        
        String input = "AB";
        Writer out = new StringWriter();
        translator.translate(input, out);
        String result = out.toString();
        
        Assert.assertEquals("Translated text should match expected result", "CD", result);
    }
    
    @Test
    public void testWith() {
        translator = new TestCharSequenceTranslator();
        CharSequenceTranslator[] translators = {new TestCharSequenceTranslator2()};
        
        CharSequenceTranslator mergedTranslator = translator.with(translators);
        
        Assert.assertTrue("Merged translator should be an instance of AggregateTranslator", mergedTranslator instanceof AggregateTranslator);
        Assert.assertTrue("Merged translator should contain the original translator", ((AggregateTranslator) mergedTranslator).getTranslators().contains(translator));
        Assert.assertTrue("Merged translator should contain the additional translator", ((AggregateTranslator) mergedTranslator).getTranslators().contains(translators[0]));
    }
    
    private static class TestCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'H') {
                out.write('H');
                return 1;
            } else if (input.charAt(index) == 'e') {
                out.write('e');
                return 1;
            } else if (input.charAt(index) == 'l') {
                out.write('l');
                return 1;
            } else if (input.charAt(index) == 'o') {
                out.write('o');
                return 1;
            } else if (input.charAt(index) == ' ') {
                out.write(' ');
                return 1;
            } else if (input.charAt(index) == 'w') {
                out.write('w');
                return 1;
            } else if (input.charAt(index) == 'r') {
                out.write('r');
                return 1;
            } else if (input.charAt(index) == 'd') {
                out.write('d');
                return 1;
            } else if (input.charAt(index) == '!') {
                out.write('!');
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    private static class TestCharSequenceTranslator2 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'A') {
                out.write('A');
                return 1;
            } else if (input.charAt(index) == 'B') {
                out.write('B');
                return 1;
            } else if (input.charAt(index) == 'C') {
                out.write('C');
                return 1;
            } else if (input.charAt(index) == 'D') {
                out.write('D');
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    private static class TestWriter extends Writer {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            throw new IOException("Test IOException");
        }

        @Override
        public void flush() throws IOException {
            throw new IOException("Test IOException");
        }

        @Override
        public void close() throws IOException {
            throw new IOException("Test IOException");
        }
    }
}