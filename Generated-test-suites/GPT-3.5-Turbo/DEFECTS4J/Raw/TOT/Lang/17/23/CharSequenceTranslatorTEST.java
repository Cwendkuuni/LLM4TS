import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("", translator.translate(""));
    }
    
    @Test
    public void testTranslateWithValidInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("ABC", translator.translate("ABC"));
    }
    
    @Test
    public void testTranslateWithEscapeCharacter() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("\\n", translator.translate("\n"));
    }
    
    @Test
    public void testTranslateWithInvalidInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("&#x1234;", translator.translate("&#x1234;"));
    }
    
    private class TestTranslator extends CharSequenceTranslator {
    
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == '\n') {
                out.write("\\n");
                return 1;
            } else if (input.charAt(index) == '#') {
                out.write("&#x" + hex(input.charAt(index)) + ";");
                return 1;
            } else {
                out.write(input.charAt(index));
                return 0;
            }
        }
    }
}