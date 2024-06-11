package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {
  
  @Test
  public void testTranslate() throws IOException {
    LookupTranslator translator = new LookupTranslator(new CharSequence[]{"ab", "ABC"}, new CharSequence[]{"cd", "DEF"});
    
    StringWriter writer = new StringWriter();
    
    int result = translator.translate("abcdef", 0, writer);
    assertEquals(2, result);
    assertEquals("ABC", writer.toString());
    writer.getBuffer().setLength(0);
    
    result = translator.translate("abcdef", 2, writer);
    assertEquals(2, result);
    assertEquals("DEF", writer.toString());
    writer.getBuffer().setLength(0);
    
    result = translator.translate("abcdef", 4, writer);
    assertEquals(0, result);
    assertEquals("", writer.toString());
    writer.getBuffer().setLength(0);
    
    result = translator.translate("ghijkl", 0, writer);
    assertEquals(0, result);
    assertEquals("", writer.toString());
    writer.getBuffer().setLength(0);
  }
  
  @Test
  public void testConstructorWithNullLookup() {
    LookupTranslator translator = new LookupTranslator(null);
    assertNotNull(translator);
  }
  
  @Test
  public void testConstructorWithEmptyLookup() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
    assertNotNull(translator);
  }
  
  @Test
  public void testConstructorWithValidLookup() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "A"});
    assertNotNull(translator);
  }
}