import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    // Test input with numeric entity
    StringBuilder input = new StringBuilder("Testing &#x61;");
    StringBuilder output = new StringBuilder();
    int index = unescaper.translate(input, 8, output);
    assertEquals("Translation index incorrect", 12, index);
    assertEquals("Output incorrect", "Testing a", output.toString());

    // Test input with numeric entity and optional semicolon
    input = new StringBuilder("Testing &#x61;;");
    output = new StringBuilder();
    index = unescaper.translate(input, 8, output);
    assertEquals("Translation index incorrect", 13, index);
    assertEquals("Output incorrect", "Testing a;", output.toString());

    // Test input with decimal numeric entity
    input = new StringBuilder("Testing &#97;");
    output = new StringBuilder();
    index = unescaper.translate(input, 8, output);
    assertEquals("Translation index incorrect", 12, index);
    assertEquals("Output incorrect", "Testing a", output.toString());
    
    // Test input with decimal numeric entity and optional semicolon
    input = new StringBuilder("Testing &#97;;");
    output = new StringBuilder();
    index = unescaper.translate(input, 8, output);
    assertEquals("Translation index incorrect", 13, index);
    assertEquals("Output incorrect", "Testing a;", output.toString());
    
    // Test input with invalid numeric entity
    input = new StringBuilder("Testing &#1234;");
    output = new StringBuilder();
    index = unescaper.translate(input, 8, output);
    assertEquals("Translation index incorrect", 12, index);
    assertEquals("Output incorrect", "Testing &#1234;", output.toString());
  }
}