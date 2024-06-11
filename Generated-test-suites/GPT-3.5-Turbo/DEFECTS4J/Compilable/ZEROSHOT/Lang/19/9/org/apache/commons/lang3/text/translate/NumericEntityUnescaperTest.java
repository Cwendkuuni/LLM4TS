package org.apache.commons.lang3.text.translate;
import org.apache.commons.lang3.text.translate.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
  
  @Test
  public void testTranslate() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    // Test translating "&#65;"
    CharSequence input1 = "&#65;";
    StringWriter out1 = new StringWriter();
    int result1 = unescaper.translate(input1, 0, out1);
    assertEquals(6, result1);
    assertEquals("A", out1.toString());

    // Test translating "&#x41;"
    CharSequence input2 = "&#x41;";
    StringWriter out2 = new StringWriter();
    int result2 = unescaper.translate(input2, 0, out2);
    assertEquals(7, result2);
    assertEquals("A", out2.toString());

    // Test translating "&#x00A9;"
    CharSequence input3 = "&#x00A9;";
    StringWriter out3 = new StringWriter();
    int result3 = unescaper.translate(input3, 0, out3);
    assertEquals(10, result3);
    assertEquals("©", out3.toString());

    // Test translating "&#169;"
    CharSequence input4 = "&#169;";
    StringWriter out4 = new StringWriter();
    int result4 = unescaper.translate(input4, 0, out4);
    assertEquals(7, result4);
    assertEquals("©", out4.toString());

    // Test translating "&#8364;"
    CharSequence input5 = "&#8364;";
    StringWriter out5 = new StringWriter();
    int result5 = unescaper.translate(input5, 0, out5);
    assertEquals(8, result5);
    assertEquals("€", out5.toString());
  }
}