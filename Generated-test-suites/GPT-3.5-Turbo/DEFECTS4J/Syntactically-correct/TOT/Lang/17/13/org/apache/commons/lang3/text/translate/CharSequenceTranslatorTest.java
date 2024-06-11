import org.junit.Test;

public class CharSequenceTranslatorTest {

  @Test
  public void testTranslate() {
    // Typical use case
    CharSequenceTranslator translator = new MockTranslator();
    String input = "Hello, World!";
    String expectedOutput = "Hello, World!";
    String actualOutput = translator.translate(input);
    assertEquals(expectedOutput, actualOutput);

    // Edge case: input is null
    input = null;
    expectedOutput = null;
    actualOutput = translator.translate(input);
    assertEquals(expectedOutput, actualOutput);

    // Error scenario: Writer produces IOException
    translator = new FailingTranslator();
    input = "Hello, World!";
    assertThrows(IOException.class, () -> {
      translator.translate(input);
    });
  }

  @Test
  public void testTranslateWithIndexAndWriter() throws IOException {
    // Typical use case
    CharSequenceTranslator translator = new MockTranslator();
    String input = "Hello, World!";
    Writer writer = new StringWriter();
    int index = 0;
    int expectedConsumed = input.length();
    int actualConsumed = translator.translate(input, index, writer);
    assertEquals(expectedConsumed, actualConsumed);
    assertEquals(input, writer.toString());

    // Edge case: input is empty
    input = "";
    writer = new StringWriter();
    index = 0;
    expectedConsumed = 0;
    actualConsumed = translator.translate(input, index, writer);
    assertEquals(expectedConsumed, actualConsumed);
    assertEquals(input, writer.toString());

    // Error scenario: Writer produces IOException
    translator = new FailingTranslator();
    input = "Hello, World!";
    writer = new StringWriter();
    index = 0;
    assertThrows(IOException.class, () -> {
      translator.translate(input, index, writer);
    });
  }

  @Test
  public void testHex() {
    // Typical use case
    int codepoint = 65;
    String expectedHex = "41";
    String actualHex = CharSequenceTranslator.hex(codepoint);
    assertEquals(expectedHex, actualHex);

    // Edge case: codepoint is 0
    codepoint = 0;
    expectedHex = "0";
    actualHex = CharSequenceTranslator.hex(codepoint);
    assertEquals(expectedHex, actualHex);
  }

  /**
   * A mock translator for testing purposes.
   */
  private static class MockTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      out.write(input.toString());
      return input.length();
    }
  }

  /**
   * A translator that always throws IOException.
   */
  private static class FailingTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      throw new IOException();
    }
  }
}