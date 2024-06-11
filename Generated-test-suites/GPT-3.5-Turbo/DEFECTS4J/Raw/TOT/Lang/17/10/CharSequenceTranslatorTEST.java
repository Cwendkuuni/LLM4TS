import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        String result = translator.translate(null);
        Assert.assertNull(result);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        String result = translator.translate("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testTranslateWithBasicTranslation() {
        CharSequenceTranslator translator = new DummyTranslator();
        String result = translator.translate("abc");
        Assert.assertEquals("ABC", result);
    }

    @Test
    public void testTranslateWithMultipleTranslators() {
        CharSequenceTranslator translator1 = new DummyTranslator();
        CharSequenceTranslator translator2 = new DummyTranslator2();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        String result = mergedTranslator.translate("abc");
        Assert.assertEquals("ABC123", result);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("abc", writer);
        String result = writer.toString();
        Assert.assertEquals("ABC", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new DummyTranslator();
        translator.translate("abc", null);
    }
    
    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new DummyTranslator();
        String result = translator.translate(new IOExceptionThrowingCharSequence());
        Assert.assertNull(result);
    }
    
    @Test
    public void testHex() {
        String result = CharSequenceTranslator.hex(97);
        Assert.assertEquals("61", result);
    }

    private static class DummyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (Character.isLowerCase(input.charAt(index))) {
                out.write(Character.toUpperCase(input.charAt(index)));
                return 1;
            }
            return 0;
        }
    }

    private static class DummyTranslator2 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (Character.isDigit(input.charAt(index))) {
                out.write(input.charAt(index));
                return 1;
            }
            return 0;
        }
    }
    
    private static class IOExceptionThrowingCharSequence implements CharSequence {
        @Override
        public int length() {
            return 3;
        }
        
        @Override
        public char charAt(int index) {
            throw new RuntimeException();
        }
        
        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }
    }

}