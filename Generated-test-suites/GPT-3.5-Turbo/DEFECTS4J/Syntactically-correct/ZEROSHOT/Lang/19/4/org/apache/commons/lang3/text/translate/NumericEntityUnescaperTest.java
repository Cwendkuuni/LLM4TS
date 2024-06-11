import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {
  
  @Test
  public void testTranslate() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    // Test translating a numeric entity &#65;
    int result = unescaper.translate("&#65;", 0, writer);
    assertEquals(6, result);
    assertEquals("A", writer.toString());
    
    writer.close();
    writer = new StringWriter();
    
    // Test translating a hex numeric entity &#x41;
    result = unescaper.translate("&#x41;", 0, writer);
    assertEquals(8, result);
    assertEquals("A", writer.toString());
    
    writer.close();
    writer = new StringWriter();
    
    // Test translating a numeric entity without semi-colon &#65
    result = unescaper.translate("&#65", 0, writer);
    assertEquals(5, result);
    assertEquals("A", writer.toString());
    
    writer.close();
    writer = new StringWriter();
    
    // Test translating a hex numeric entity without semi-colon &#x41
    result = unescaper.translate("&#x41", 0, writer);
    assertEquals(7, result);
    assertEquals("A", writer.toString());
  }
  
  @Test
  public void testTranslateWithInvalidEntity() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    // Test translating an invalid entity &#ZZZ;
    int result = unescaper.translate("&#ZZZ;", 0, writer);
    assertEquals(4, result);
    assertEquals("", writer.toString());
    
    writer.close();
  }
}