import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello World";
        String expected = "Hello World";
        assert translator.translate(input).equals(expected);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        assert translator.translate(null) == null;
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello World";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assert writer.toString().equals(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello World";
        translator.translate(input, null);
    }

    @Test
    public void testTranslateWithIndex() throws IOException {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello World";
        StringWriter writer = new StringWriter();
        int index = 0;
        int consumed = translator.translate(input, index, writer);
        assert consumed == input.length();
        assert writer.toString().equals(input);
    }

    @Test
    public void testTranslateWithIndexAndSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello \uD83D\uDC4B";
        StringWriter writer = new StringWriter();
        int index = 0;
        int consumed = translator.translate(input, index, writer);
        assert consumed == 7;
        assert writer.toString().equals(input);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new DummyTranslator();
        CharSequenceTranslator translator2 = new DummyTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assert mergedTranslator instanceof AggregateTranslator;
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        assert CharSequenceTranslator.hex(codepoint).equals(expected);
    }
}