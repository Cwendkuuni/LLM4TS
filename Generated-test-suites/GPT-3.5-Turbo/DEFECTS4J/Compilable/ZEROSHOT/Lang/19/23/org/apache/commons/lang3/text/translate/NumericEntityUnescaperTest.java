package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {
  
  private NumericEntityUnescaper unescaper;
  
  @Before
  public void setUp() {
    unescaper = new NumericEntityUnescaper();
  }
  
  @Test
  public void testTranslate() throws IOException {
    String input = "&#65;";
    Writer writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    assertEquals(5, input.length());
    assertEquals(2, result);
    assertEquals("A", writer.toString());
  }
  
  @Test
  public void testTranslateWithHexadecimal() throws IOException {
    String input = "&#x41;";
    Writer writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    assertEquals(7, input.length());
    assertEquals(2, result);
    assertEquals("A", writer.toString());
  }
  
  @Test
  public void testTranslateWithoutSemicolon() throws IOException {
    String input = "&#65";
    Writer writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    assertEquals(4, input.length());
    assertEquals(2, result);
    assertEquals("A", writer.toString());
  }
  
  @Test
  public void testTranslateWithInvalidInput() throws IOException {
    String input = "&#invalid;";
    Writer writer = new StringWriter();
    int result = unescaper.translate(input, 0, writer);
    assertEquals(10, input.length());
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
}