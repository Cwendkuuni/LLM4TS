import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withNullInput_returnsNull() {
        CharSequenceTranslator translator = new TestTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_withEmptyInput_returnsEmptyString() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_translatesInputCorrectly() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "abc123";
        String expected = "ABC123";
        assertEquals(expected, translator.translate(input));
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (int i = index; i < input.length(); i++) {
                if (Character.isUpperCase(input.charAt(i))) {
                    out.write(Character.toLowerCase(input.charAt(i)));
                    consumed++;
                } else if (Character.isLowerCase(input.charAt(i))) {
                    out.write(Character.toUpperCase(input.charAt(i)));
                    consumed++;
                }
            }
            return consumed;
        }
    }
}