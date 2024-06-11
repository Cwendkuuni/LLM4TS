package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

  // Test for translate(CharSequence input, int index, Writer out)
  @Test
  public void testTranslateWithIndexAndWriter() throws IOException {
    CharSequenceTranslator translator = new MockCharSequenceTranslator();
    StringWriter writer = new StringWriter();
    CharSequence input = "Hello";
    int index = 0;
    
    int count = translator.translate(input, index, writer);
    
    assertEquals(5, count);
    assertEquals("Hello", writer.toString());
  }
  
  // Test for translate(CharSequence input)
  @Test
  public void testTranslateWithoutWriter() {
    CharSequenceTranslator translator = new MockCharSequenceTranslator();
    CharSequence input = "Hello";
    
    String output = translator.translate(input);
    
    assertEquals("Hello", output);
  }
  
  // Test for translate(CharSequence input, Writer out)
  @Test
  public void testTranslateWithWriter() throws IOException {
    CharSequenceTranslator translator = new MockCharSequenceTranslator();
    StringWriter writer = new StringWriter();
    CharSequence input = "Hello";
    
    translator.translate(input, writer);
    
    assertEquals("Hello", writer.toString());
  }
  
  // Test for IllegalArgumentException in translate(CharSequence input, Writer out)
  @Test(expected = IllegalArgumentException.class)
  public void testTranslateWithNullWriter() throws IOException {
    CharSequenceTranslator translator = new MockCharSequenceTranslator();
    CharSequence input = "Hello";
    
    translator.translate(input, null);
  }
  
  // Test for NullPointerException in translate(CharSequence input)
  @Test(expected = NullPointerException.class)
  public void testTranslateWithNullInput() {
    CharSequenceTranslator translator = new MockCharSequenceTranslator();
    
    translator.translate(null);
  }
  
  // Test for RuntimeException in translate(CharSequence input)
  @Test(expected = RuntimeException.class)
  public void testTranslateWithIOExceptionInWriter() {
    CharSequenceTranslator translator = new MockCharSequenceTranslatorWithIOException();
    CharSequence input = "Hello";
    
    translator.translate(input);
  }
  
  // Test for with(CharSequenceTranslator... translators)
  @Test
  public void testWith() {
    CharSequenceTranslator translator1 = new MockCharSequenceTranslator();
    CharSequenceTranslator translator2 = new MockCharSequenceTranslator();
    
    CharSequenceTranslator mergedTranslator = translator1.with(translator2);
    
    assertNotNull(mergedTranslator);
  }
  
  // Test for hex(int codepoint)
  @Test
  public void testHex() {
    int codepoint = 65;
    
    String hexString = CharSequenceTranslator.hex(codepoint);
    
    assertEquals("41", hexString);
  }

  private static final class MockCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
      }
      if (input == null) {
        return 0;
      }
      out.write(input.toString());
      return input.length();
    }
  }
  
  private static final class MockCharSequenceTranslatorWithIOException extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      throw new IOException();
    }
  }
}