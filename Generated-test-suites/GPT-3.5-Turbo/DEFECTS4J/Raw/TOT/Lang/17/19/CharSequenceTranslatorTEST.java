import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

  @Test
  public void testTranslateWithNullInput() {
    CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
    String result = translator.translate(null);
    assertNull(result);
  }

  @Test
  public void testTranslateWithEmptyInput() {
    CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
    String result = translator.translate("");
    assertEquals("", result);
  }

  @Test
  public void testTranslateWithNormalInput() {
    CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
    String result = translator.translate("This is a test");
    assertEquals("This is a test", result);
  }

  @Test
  public void testTranslateWithEscaping() {
    CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
    String result = translator.translate("<html>");
    assertEquals("&lt;html&gt;", result);
  }

  @Test
  public void testTranslateWithUnescaping() {
    CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
    String result = translator.translate("&lt;html&gt;");
    assertEquals("<html>", result);
  }

  @Test
  public void testTranslateWithSurrogatePair() {
    CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
    String result = translator.translate("\uD83D\uDE00"); // Grinning face emoji
    assertEquals("\uD83D\uDE00", result);
  }

  @Test
  public void testTranslateWithIOException() {
    CharSequenceTranslator translator = new ConcreteCharSequenceTranslator();
    String result = translator.translate(new FailingCharSequence());
    assertNull(result);
  }

}

class ConcreteCharSequenceTranslator extends CharSequenceTranslator {
  @Override
  public int translate(CharSequence input, int index, Writer out) throws IOException {
    if (input.charAt(index) == '<') {
      out.write("&lt;");
      return 1;
    } else if (input.charAt(index) == '>') {
      out.write("&gt;");
      return 1;
    } else {
      out.write(input.charAt(index));
      return 1;
    }
  }
}

class FailingCharSequence implements CharSequence {
  @Override
  public int length() {
    return 0;
  }
  @Override
  public char charAt(int index) {
    throw new IOException();
  }
  @Override
  public CharSequence subSequence(int start, int end) {
    return null;
  }
}