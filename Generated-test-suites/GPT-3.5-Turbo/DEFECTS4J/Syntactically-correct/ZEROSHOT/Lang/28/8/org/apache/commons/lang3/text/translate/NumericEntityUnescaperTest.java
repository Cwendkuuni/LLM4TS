import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    // Test case where input contains a numeric entity
    int index = 0;
    CharSequence input = "&amp;&#34;&#x3e;";
    int result = unescaper.translate(input, index, writer);
    int expected = 10;
    String expectedOutput = "&\"\u003e";
    assertEquals("Invalid result", expected, result);
    assertEquals("Invalid output", expectedOutput, writer.toString());
    
    // Test case where input does not contain a numeric entity
    index = 0;
    input = "Hello World";
    result = unescaper.translate(input, index, writer);
    expected = 0;
    expectedOutput = "";
    assertEquals("Invalid result", expected, result);
    assertEquals("Invalid output", expectedOutput, writer.toString());
    
    // Test case where input is empty
    index = 0;
    input = "";
    result = unescaper.translate(input, index, writer);
    expected = 0;
    expectedOutput = "";
    assertEquals("Invalid result", expected, result);
    assertEquals("Invalid output", expectedOutput, writer.toString());
  }
}