import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new SampleTranslator();
        String input = "Hello World";
        String expectedOutput = "H2ll4 W4rld";
        assertEquals(expectedOutput, translator.translate(input));
    }
    
    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new SampleTranslator();
        String input = null;
        String expectedOutput = null;
        assertEquals(expectedOutput, translator.translate(input));
    }
    
    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new SampleTranslator();
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, translator.translate(input));
    }
    
    private static class SampleTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int count = 0;
            for (int i = index; i < input.length(); i++) {
                char c = input.charAt(i);
                switch (c) {
                    case 'e':
                        out.write('2');
                        count++;
                        break;
                    case 'o':
                        out.write('4');
                        count++;
                        break;
                    default:
                        out.write(c);
                        break;
                }
            }
            return count;
        }
    }

}