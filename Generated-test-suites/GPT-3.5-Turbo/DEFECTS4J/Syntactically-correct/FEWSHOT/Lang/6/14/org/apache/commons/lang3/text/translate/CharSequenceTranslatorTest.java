package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
  
  //Example JUnit test case from UtilTest class
  
  @Test
    public void testTranslate() {
        assertEquals("foo", Util.translate("foo"));
        assertEquals("foo bar", Util.translate("foo bar"));
        assertEquals("foo and bar", Util.translate("foo and bar"));
        assertNull(Util.translate(null));
    }
  
  @Test
    public void testTranslateWithWriter() {
        StringWriter writer = new StringWriter();
        try {
            Util.translate("foo", writer);
            assertEquals("foo", writer.toString());
            writer.getBuffer().setLength(0);
            Util.translate("foo bar", writer);
            assertEquals("foo bar", writer.toString());
            writer.getBuffer().setLength(0);
            Util.translate("foo and bar", writer);
            assertEquals("foo and bar", writer.toString());
            writer.getBuffer().setLength(0);
            Util.translate(null, writer);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            // ignore
        }
    }
  
  @Test
    public void testTranslateWithIndexAndWriter() {
        StringWriter writer = new StringWriter();
        try {
            CharSequenceTranslator translator = new ExampleTranslator();
            assertEquals(3, translator.translate("foo", 0, writer));
            assertEquals("foo", writer.toString());
            writer.getBuffer().setLength(0);
            assertEquals(1, translator.translate("foo bar", 0, writer));
            assertEquals("foo bar", writer.toString());
            writer.getBuffer().setLength(0);
            assertEquals(3, translator.translate("foo and bar", 0, writer));
            assertEquals("foo and bar", writer.toString());
        } catch (IOException e) {
            // ignore
        }
    }
  
  private class ExampleTranslator extends CharSequenceTranslator {
    
    @Override
    public int translate(CharSequence input, int index, Writer out) {
      if (input.charAt(index) == 'a') {
        try {
          out.write("b");
          return 1;
        } catch (IOException e) {
          // ignore
        }
      }
      return 0;
    }
  }
  
  @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("4D", CharSequenceTranslator.hex(77));
        assertEquals("5A", CharSequenceTranslator.hex(90));
    }
}