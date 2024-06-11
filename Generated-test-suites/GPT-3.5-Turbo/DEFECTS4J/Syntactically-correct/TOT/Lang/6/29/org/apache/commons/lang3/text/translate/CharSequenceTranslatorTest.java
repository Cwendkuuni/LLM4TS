import org.junit.Test;
import org.junit.Assert;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_CharSequenceIntWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringBuilder input = new StringBuilder("abcdefgh");
        StringWriter writer = new StringWriter();
        try {
            int consumed = translator.translate(input, 0, writer);
            Assert.assertEquals("Consumed should be equal to input length", input.length(), consumed);
            Assert.assertEquals("Output should be equal to input", input.toString(), writer.toString());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslate_CharSequenceIntWriter_Exception() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringBuilder input = new StringBuilder("abcdefgh");
        StringWriter writer = new StringWriter();
        try {
            int consumed = translator.translate(input, 8, writer);
            Assert.assertEquals("Consumed should be zero", 0, consumed);
            Assert.assertEquals("Output should be empty", "", writer.toString());
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testTranslate_CharSequenceWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringBuilder input = new StringBuilder("abcdefgh");
        String expected = ">a<, >b<, >c<, >d<, >e<, >f<, >g<, >h<, ";
        String output = translator.translate(input);
        Assert.assertEquals("Output should be equal to expected", expected, output);
    }

    @Test
    public void testTranslate_CharSequenceWriter_NullInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = null;
        String expected = null;
        String output = translator.translate(input);
        Assert.assertEquals("Output should be null", expected, output);
    }

    @Test
    public void testTranslate_CharSequenceWriter_Exception() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringBuilder input = new StringBuilder("abcgh");
        Writer writer = new Writer() {
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IO exception");
            }
            public void flush() throws IOException {}
            public void close() throws IOException {}
        };
        try {
            translator.translate(input, writer);
            Assert.fail("IOException should be thrown");
        } catch (IOException e) {
            Assert.assertEquals("IOException message should be equal to expected", "Test IO exception", e.getMessage());
        }
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new DummyTranslator();
        CharSequenceTranslator translator2 = new DummyTranslator();
        CharSequenceTranslator translator3 = new DummyTranslator();
        CharSequenceTranslator merged = translator1.with(translator2, translator3);
        Assert.assertTrue("Merged translator should be instance of AggregateTranslator", merged instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String output = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals("Output should be equal to expected", expected, output);
    }

    class DummyTranslator extends CharSequenceTranslator {
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                out.write(">" + input.charAt(index) + "<, ");
                return 1;
            }
            return 0;
        }
    }

}