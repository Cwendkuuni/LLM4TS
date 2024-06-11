import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
  
  @Test
  public void testTranslate_basic() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder("&amp;");
    StringBuilder output = new StringBuilder();
    int translated = unescaper.translate(input, 0, output);
    
    assertEquals(6, translated);
    assertEquals("&", output.toString());
  }

  @Test
  public void testTranslate_basicHex() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder("&#x61;");
    StringBuilder output = new StringBuilder();
    int translated = unescaper.translate(input, 0, output);
    
    assertEquals(7, translated);
    assertEquals("a", output.toString());
  }
  
  @Test
  public void testTranslate_invalidHex() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder("&#x;"); // Invalid hex value
    StringBuilder output = new StringBuilder();
    int translated = unescaper.translate(input, 0, output);
    
    assertEquals(0, translated);
    assertEquals("", output.toString());
  }
  
  @Test
  public void testTranslate_invalidDecimal() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder("&#;"); // Invalid decimal value
    StringBuilder output = new StringBuilder();
    int translated = unescaper.translate(input, 0, output);
    
    assertEquals(0, translated);
    assertEquals("", output.toString());
  }
  
  @Test
  public void testTranslate_noEntity() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringBuilder input = new StringBuilder("abc"); // No entity
    StringBuilder output = new StringBuilder();
    int translated = unescaper.translate(input, 0, output);
    
    assertEquals(0, translated);
    assertEquals("", output.toString());
    assertEquals("abc", input.toString()); // Input should remain unchanged
  }
}