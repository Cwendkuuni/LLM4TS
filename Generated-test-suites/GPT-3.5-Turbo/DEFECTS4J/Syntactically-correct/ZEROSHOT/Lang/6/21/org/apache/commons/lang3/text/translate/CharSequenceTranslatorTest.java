import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

  @Test
  public void testTranslate() {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
      }
    };

    // Test null input
    String nullInput = null;
    assertNull(translator.translate(nullInput));

    // Test empty input
    String emptyInput = "";
    assertEquals("", translator.translate(emptyInput));

    // Test valid input
    String validInput = "test";
    assertEquals("test", translator.translate(validInput));
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
  public void testTranslateWithWriter() throws IOException {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write("test");
        return 0;
      }
    };

    StringWriter writer = new StringWriter();
    String input = "input";
    translator.translate(input, writer);
    assertEquals("test", writer.toString());
  }

  @Test
  public void testHex() {
    int codepoint = 65;
    assertEquals("41", CharSequenceTranslator.hex(codepoint));
  }

  @Test
  public void testWith() {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
      }
    };

    CharSequenceTranslator translator2 = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
      }
    };

    CharSequenceTranslator mergedTranslator = translator.with(translator2);
    assertNotNull(mergedTranslator);
  }

}