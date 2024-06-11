package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {
  
  @Test
  public void testTranslate() {
    // Create a sample translator to use for testing
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write(Character.toUpperCase(input.charAt(index)));
        return 1;
      }
    };
    
    // Test empty input
    String result = translator.translate("");
    assertEquals("", result);
    
    // Test single character input
    result = translator.translate("a");
    assertEquals("A", result);
    
    // Test multiple character input
    result = translator.translate("abc");
    assertEquals("ABC", result);
  }
  
  @Test
  public void testHex() {
    // Test lower case
    String result = CharSequenceTranslator.hex(1234);
    assertEquals("4D2", result);
    
    // Test upper case
    result = CharSequenceTranslator.hex(5678);
    assertEquals("162E", result);
    
    // Test zero padding
    result = CharSequenceTranslator.hex(9);
    assertEquals("9", result);
    
    // Test negative number
    result = CharSequenceTranslator.hex(-123);
    assertEquals("FFFFFF85", result);
  }
}