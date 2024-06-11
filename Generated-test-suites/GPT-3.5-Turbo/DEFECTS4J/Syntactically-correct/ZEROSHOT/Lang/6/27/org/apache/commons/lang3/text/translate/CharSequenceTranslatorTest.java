import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {

  @Test
  public void testTranslateWithWriter() throws IOException {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
      }
    };

    String input = "test";
    StringWriter writer = new StringWriter();
    translator.translate(input, writer);
    String output = writer.toString();

    Assert.assertEquals(input, output);
  }

  @Test
  public void testTranslateWithoutWriter() {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
      }
    };

    String input = "test";
    String output = translator.translate(input);

    Assert.assertEquals(input, output);
  }

  @Test
  public void testTranslateWithNullInputWithoutWriter() {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
      }
    };

    String input = null;
    String output = translator.translate(input);

    Assert.assertNull(output);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslateWithNullWriter() throws IOException {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
      }
    };

    String input = "test";
    translator.translate(input, null);
  }

  @Test
  public void testHex() {
    int codepoint = 65;
    String expected = "41";
    String result = CharSequenceTranslator.hex(codepoint);
    Assert.assertEquals(expected, result);
  }

}