import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        assertEquals("abc", translator.translate("abc"));
        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
    }

    private class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }
}