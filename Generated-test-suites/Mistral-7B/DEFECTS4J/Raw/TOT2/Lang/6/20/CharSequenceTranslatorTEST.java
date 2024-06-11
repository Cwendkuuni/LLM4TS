import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AbstractCharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new AbstractCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String source = "Hello World!";
        String expected = "Hellow OrdL!"; // uppercase 'O' and lowercase 'L' in 'World'
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(new StringSource(source), 0, writer);
        String result = writer.toString();

        assertEquals(expected, result);
        assertEquals(source.length(), consumed);
    }
}