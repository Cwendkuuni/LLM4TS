package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.apache.commons.lang3.text.translate.SafeHtmlTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

  /**
   * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
   */
  @Test
  public void testTranslate() throws Exception {
    // Test with an empty CharSequence.
    CharSequenceTranslator translator = new SafeHtmlTranslator();
    CharSequence input = "";
    Writer out = new StringWriter();
    int consumed = translator.translate(input, 0, out);
    assertEquals("", out.toString());
    assertEquals(0, consumed);

    // Test with a simple CharSequence.
    input = "Hello, World!";
    out = new StringWriter();
    consumed = translator.translate(input, 0, out);
    assertEquals("Hello, World!", out.toString());
    assertEquals(input.length(), consumed);

    // Test with a CharSequence containing surrogate pairs.
    input = "\uD800\uDC00";
    out = new StringWriter();
    consumed = translator.translate(input, 0, out);
    assertEquals("\uD800\uDC00", out.toString());
    assertEquals(input.length() * 2, consumed);

    // Test with a CharSequence containing codepoints outside the BMP.
    input = "\uD840\uDC00";
    out = new StringWriter();
    consumed = translator.translate(input, 0, out);
    assertEquals("\uD840\uDC00", out.toString());
    assertEquals(input.length() * 2, consumed);

    // Test with a CharSequence containing invalid UTF-16 code units.
    input = "\uFFFE";
    out = new StringWriter();
    consumed = translator.translate(input, 0, out);
    assertEquals("", out.toString());
    assertThrows(IllegalArgumentException.class, () -> translator.translate(input, 0, out));
  }

  /**
   * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method.
   */
  @Test
  public void testTranslateString() throws Exception {
    CharSequenceTranslator translator = new SafeHtmlTranslator();

    // Test with an empty CharSequence.
    String input = "";
    String result = translator.translate(input);
    assertEquals("", result);

    // Test with a simple String.
    input = "Hello, World!";
    result = translator.translate(input);
    assertEquals("Hello, World!", result);

    // Test with a String containing surrogate pairs.
    input = "\uD800\uDC00";
    result = translator.translate(input);
    assertEquals("\uD800\uDC00", result);

    // Test with a String containing codepoints outside the BMP.
    input = "\uD840\uDC00";
    result = translator.translate(input);
    assertEquals("\uD840\uDC00", result);

    // Test with a String containing invalid UTF-16 code units.
    input = "\uFFFE";
    assertThrows(IllegalArgumentException.class, () -> translator.translate(input));
  }

  /**
   * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null inputs.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testTranslateNullInputs() {
    CharSequenceTranslator translator = new SafeHtmlTranslator();

    // Test with a null CharSequence.
    translator.translate(null, new StringWriter());

    // Test with a null Writer.
    translator.translate(new String("Hello, World!"), null);
  }

  /**
   * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
   */
  @Test
  public void testWith() throws Exception {
    CharSequenceTranslator translator1 = new SafeHtmlTranslator();
    CharSequenceTranslator translator2 = new ReplacementCharSequenceTranslator("ABC", "XYZ");

    CharSequenceTranslator aggregatedTranslator = translator1.with(translator2);

    String input = "ABCHello, World!ABC";
    String result = aggregatedTranslator.translate(input);
    assertEquals("XYZHello, World!XYZ", result);
  }
}

class ReplacementCharSequenceTranslator extends CharSequenceTranslator {
  private final String from;
  private final String to;

  public ReplacementCharSequenceTranslator(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public int translate(CharSequence input, int index, Writer out) throws IOException {
    if (input == null || index < 0 || index >= input.length()) {
      throw new IndexOutOfBoundsException();
    }

    if (from.charAt(0) == input.charAt(index)) {
      out.write(to.charAt(0));
      return 1;
    }

    return 0;
  }
}

class AggregateTranslator extends CharSequenceTranslator {
  private final CharSequenceTranslator[] translators;

  public AggregateTranslator(CharSequenceTranslator[] translators) {
    this.translators = translators;
  }

  @Override
  public int translate(CharSequence input, int index, Writer out) throws IOException {
    int sumConsumed = 0;

    for (CharSequenceTranslator translator : translators) {
      int consumed = translator.translate(input, index, out);
      if (consumed > 0) {
        sumConsumed += consumed;
        index += consumed;
      } else {
        if (index >= input.length()) {
          break;
        }
        index++;
      }
    }

    return sumConsumed;
  }
}