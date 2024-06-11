import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {
  
  private CharSequenceTranslator translator;
  
  @Before
  public void setUp() {
    translator = new MockTranslator();
  }
  
  // Test translate(CharSequence input, int index, Writer out) method
  @Test
  public void testTranslate_withValidInputIndexAndWriter_returnsCodepointsConsumed() throws IOException {
    StringBuilder input = new StringBuilder("Hello World");
    StringWriter writer = new StringWriter();
    int index = 0;
    int result = translator.translate(input, index, writer);
    assertEquals(5, result);
    assertEquals("Hello", writer.toString());
  }
  
  @Test
  public void testTranslate_withInvalidIndexAndWriter_returnsZeroCodepointsConsumed() throws IOException {
    StringBuilder input = new StringBuilder("Hello World");
    StringWriter writer = new StringWriter();
    int index = 10;
    int result = translator.translate(input, index, writer);
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
  
  // Test translate(CharSequence input) method
  @Test
  public void testTranslate_withValidInput_returnsTranslatedString() {
    String input = "Hello World";
    String result = translator.translate(input);
    assertEquals("Hello World", result);
  }
  
  @Test
  public void testTranslate_withNullInput_returnsNull() {
    String result = translator.translate(null);
    assertNull(result);
  }
  
  // Test translate(CharSequence input, Writer out) method
  @Test
  public void testTranslate_withValidInputAndWriter_translatesInput() throws IOException {
    StringBuilder input = new StringBuilder("Hello World");
    StringWriter writer = new StringWriter();
    translator.translate(input, writer);
    assertEquals("Hello World", writer.toString());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTranslate_withNullWriter_throwsIllegalArgumentException() throws IOException {
    StringBuilder input = new StringBuilder("Hello World");
    translator.translate(input, null);
  }
  
  @Test
  public void testTranslate_withNullInputAndNonNullWriter_doesNothing() throws IOException {
    StringWriter writer = new StringWriter();
    translator.translate(null, writer);
    assertEquals("", writer.toString());
  }
  
  // Test with(...) method
  @Test
  public void testWith_withMultipleTranslators_returnsNewMergedTranslator() {
    CharSequenceTranslator translator1 = new MockTranslator();
    CharSequenceTranslator translator2 = new MockTranslator();
    CharSequenceTranslator mergedTranslator = translator1.with(translator2);
    assertNotNull(mergedTranslator);
    assertTrue(mergedTranslator instanceof AggregateTranslator);
  }
  
  // Test hex(int codepoint) method
  @Test
  public void testHex_withValidCodepoint_returnsUpperCaseHex() {
    int codepoint = 65;
    String result = CharSequenceTranslator.hex(codepoint);
    assertEquals("41", result);
  }
  
  @Test
  public void testHex_withNegativeCodepoint_returnsUpperCaseHex() {
    int codepoint = -65;
    String result = CharSequenceTranslator.hex(codepoint);
    assertEquals("FFFFFFBF", result);
  }
  
  // MockTranslator class for testing
  private class MockTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      StringBuilder translated = new StringBuilder();
      int consumed = 0;
      while (index < input.length()) {
        char c = input.charAt(index);
        if (Character.isLetter(c)) {
          translated.append(Character.toUpperCase(c));
          consumed++;
        } else {
          translated.append(c);
          break;
        }
        index++;
      }
      out.write(translated.toString());
      return consumed;
    }
  }
}