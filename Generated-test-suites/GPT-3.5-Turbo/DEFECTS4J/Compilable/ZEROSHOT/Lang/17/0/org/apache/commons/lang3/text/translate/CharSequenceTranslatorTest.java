package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {

  @Test
  public void testTranslate_intCharSequenceWriter() throws IOException {
    CharSequenceTranslator translator = new TestTranslator();
    StringWriter writer = new StringWriter();
    String input = "test";
    int result = translator.translate(input, 0, writer);
    assertEquals(4, result);
    assertEquals("translated", writer.toString());
  }

  @Test
  public void testTranslate_CharSequence() {
    CharSequenceTranslator translator = new TestTranslator();
    String input = "test";
    String result = translator.translate(input);
    assertEquals("translated", result);
  }

  @Test
  public void testTranslate_CharSequenceWriter() throws IOException {
    CharSequenceTranslator translator = new TestTranslator();
    StringWriter writer = new StringWriter();
    String input = "test";
    translator.translate(input, writer);
    assertEquals("translated", writer.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTranslate_NullWriter() throws IOException {
    CharSequenceTranslator translator = new TestTranslator();
    String input = "test";
    translator.translate(input, null);
  }

  @Test
  public void testTranslate_NullInput() {
    CharSequenceTranslator translator = new TestTranslator();
    String input = null;
    String result = translator.translate(input);
    assertNull(result);
  }

  @Test
  public void testTranslate_EmptyInput() {
    CharSequenceTranslator translator = new TestTranslator();
    String input = "";
    String result = translator.translate(input);
    assertEquals("", result);
  }

  @Test
  public void testTranslate_WithTranslators() {
    TestTranslator translator1 = new TestTranslator();
    TestTranslator translator2 = new TestTranslator();
    CharSequenceTranslator result = translator1.with(translator2);
    assertNotNull(result);
    assertTrue(result instanceof AggregateTranslator);
  }

  @Test
  public void testHex() {
    int codepoint = 101;
    String result = CharSequenceTranslator.hex(codepoint);
    assertEquals("65", result);
  }

  private class TestTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      out.write("translated");
      return input.length();
    }
  }
}