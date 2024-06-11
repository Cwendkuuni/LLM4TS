import org.junit.Assert;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate(null);
        Assert.assertNull(result);
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testTranslate_withNonEmptyInput_shouldReturnTranslatedString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("Hello");
        Assert.assertEquals("Translated Hello", result);
    }

    @Test
    public void testTranslate_withUnicodeInput_shouldReturnTranslatedString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("Unicode \u2022 Test");
        Assert.assertEquals("Translated Unicode \u2022 Test", result);
    }

    @Test
    public void testTranslate_withEscapeCharacters_shouldReturnTranslatedString() {
        CharSequenceTranslator translator = new TestTranslator();
        String result = translator.translate("New\nLine");
        Assert.assertEquals("Translated New\\n Line", result);
    }

    @Test
    public void testTranslate_withIOException_shouldThrowRuntimeException() {
        CharSequenceTranslator translator = new TestTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        try {
            translator.translate("Test");
            Assert.fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            Assert.assertEquals(IOException.class, e.getCause().getClass());
        }
    }

    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == '\n') {
                out.write("\\n");
                return 1;
            } else {
                out.write("Translated " + input.charAt(index));
                return 1;
            }
        }
    }

}