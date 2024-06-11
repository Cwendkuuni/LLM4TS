package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.Test;

public class CharSequenceTranslatorTest {

  @Test
  public void testTranslateSingleCharacter() throws IOException {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write(input.charAt(index));
        return 1;
      }
    };

    String result = translator.translate("hello");
    assertEquals("hello", result);
  }

  @Test
  public void testTranslateNullInput() {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException("This should not be called");
      }
    };

    String result = translator.translate(null);
    assertEquals(null, result);
  }

  @Test
  public void testTranslateEmptyInput() throws IOException {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException("This should not be called");
      }
    };

    String result = translator.translate("");
    assertEquals("", result);
  }

  @Test
  public void testTranslateSurrogatePair() throws IOException {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index < input.length() - 1) {
          char[] c = Character.toChars(Character.codePointAt(input, index));
          out.write(c);
          return 2;
        } else {
          out.write(input.charAt(index));
          return 1;
        }
      }
    };

    String result = translator.translate("hello\uD83D\uDE00");
    assertEquals("hello\uD83D\uDE00", result);
  }

  @Test
  public void testWith() throws IOException {
    CharSequenceTranslator translator1 = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write(input.charAt(index));
        return 1;
      }
    };

    CharSequenceTranslator translator2 = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write(Character.toUpperCase(input.charAt(index)));
        return 1;
      }
    };

    CharSequenceTranslator mergedTranslator = translator1.with(translator2);
    String result = mergedTranslator.translate("hello");
    assertEquals("HELLO", result);
  }
}
