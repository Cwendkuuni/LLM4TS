import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withNullInput_returnsNull() {
        CharSequenceTranslator translator = new TestCharSequenceTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }
    
    @Test
    public void testTranslate_withEmptyInput_returnsEmptyString() {
        CharSequenceTranslator translator = new TestCharSequenceTranslator();
        String result = translator.translate("");
        assertEquals("", result);
    }
    
    @Test
    public void testTranslate_withNonEmptyInput_translatesSuccessfully() {
        CharSequenceTranslator translator = new TestCharSequenceTranslator();
        String input = "Test Input";
        String result = translator.translate(input);
        assertEquals("Translated Test Input", result);
    }
    
    @Test
    public void testTranslate_withNonEmptyInputAndNullWriter_throwsIllegalArgumentException() {
        CharSequenceTranslator translator = new TestCharSequenceTranslator();
        String input = "Test Input";
        Writer writer = null;
        try {
            translator.translate(input, writer);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Exception was thrown as expected
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testTranslate_withNonEmptyInputAndWriter_translatesSuccessfully() throws IOException {
        CharSequenceTranslator translator = new TestCharSequenceTranslator();
        String input = "Test Input";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String result = writer.toString();
        assertEquals("Translated Test Input", result);
    }
    
    @Test
    public void testWith_withMultipleTranslators_mergesSuccessfully() {
        CharSequenceTranslator translator1 = new TestCharSequenceTranslator1();
        CharSequenceTranslator translator2 = new TestCharSequenceTranslator2();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        String input = "Test Input";
        String result = mergedTranslator.translate(input);
        assertEquals("Merged Translated Test Input", result);
    }
    
    private class TestCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            out.write("Translated ");
            out.write(input.toString());
            return 0;
        }
    }
    
    private class TestCharSequenceTranslator1 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            out.write("Translated ");
            out.write(input.toString());
            return 0;
        }
    }
    
    private class TestCharSequenceTranslator2 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            out.write("Merged Translated ");
            out.write(input.toString());
            return 0;
        }
    }
}