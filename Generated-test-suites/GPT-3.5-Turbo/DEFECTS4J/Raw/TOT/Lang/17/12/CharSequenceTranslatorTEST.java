import org.junit.Assert;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Typical use case
        String result = translator.translate("Hello World");
        Assert.assertEquals("Hello World", result);

        // Edge case - empty input
        result = translator.translate("");
        Assert.assertEquals("", result);

        // Error scenario - null input
        result = translator.translate(null);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testTranslateWithIndexAndWriter() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Typical use case
        StringWriter writer = new StringWriter();
        try {
            translator.translate("Hello World", 0, writer);
            String result = writer.toString();
            Assert.assertEquals("Hello World", result);
        } catch (Exception e) {
            Assert.fail("Unexpected exception thrown");
        }

        // Edge case - empty input
        writer = new StringWriter();
        try {
            translator.translate("", 0, writer);
            String result = writer.toString();
            Assert.assertEquals("", result);
        } catch (Exception e) {
            Assert.fail("Unexpected exception thrown");
        }

        // Error scenario - null input
        writer = new StringWriter();
        try {
            translator.translate(null, 0, writer);
            Assert.fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected IOException to be thrown
        }
    }

    private class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                throw new IOException("Input is null");
            }
            
            out.write(input.toString());
            return input.length();
        }
    }
}