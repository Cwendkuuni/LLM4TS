
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
  
  @Test
  public void testTranslate() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter out = new StringWriter();
    
    // Non-hexadecimal entity
    int result = unescaper.translate("&#65;", 0, out);
    assertEquals(6, result);
    assertEquals("A", out.toString());
    
    // Hexadecimal entity
    out.getBuffer().setLength(0);
    result = unescaper.translate("&#x41;", 0, out);
    assertEquals(8, result);
    assertEquals("A", out.toString());
    
    // Non-hexadecimal entity with additional characters
    out.getBuffer().setLength(0);
    result = unescaper.translate("&#65;abc", 0, out);
    assertEquals(6, result);
    assertEquals("A", out.toString());
    
    // Hexadecimal entity with additional characters
    out.getBuffer().setLength(0);
    result = unescaper.translate("&#x41;abc", 0, out);
    assertEquals(8, result);
    assertEquals("A", out.toString());
    
    // Invalid non-hexadecimal entity
    out.getBuffer().setLength(0);
    result = unescaper.translate("&#123z;", 0, out);
    assertEquals(0, result);
    assertEquals("", out.toString());
    
    // Invalid hexadecimal entity
    out.getBuffer().setLength(0);
    result = unescaper.translate("&#xZ1;", 0, out);
    assertEquals(0, result);
    assertEquals("", out.toString());
    
    // No entity
    out.getBuffer().setLength(0);
    result = unescaper.translate("ABC", 0, out);
    assertEquals(0, result);
    assertEquals("", out.toString());
  }
  
}
