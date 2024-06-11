package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate_WithValidInput_ReturnsTranslatedXmlEntity() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#65;", 0, writer);
    
    assertEquals(5, result);
    assertEquals("A", writer.toString());
  }

  @Test
  public void testTranslate_WithHexadecimalInput_ReturnsTranslatedXmlEntity() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#x43;", 0, writer);
    
    assertEquals(6, result);
    assertEquals("C", writer.toString());
  }

  @Test
  public void testTranslate_WithInvalidInteger_ReturnsZero() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#99999999999999999999999;", 0, writer);
    
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }

  @Test
  public void testTranslate_WithInvalidHexadecimal_ReturnsZero() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#xZZ;", 0, writer);
    
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }

  @Test
  public void testTranslate_WithNoSemicolon_ReturnsZero() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#65", 0, writer);
    
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }

  @Test
  public void testTranslate_WithInvalidHexadecimalWithNoSemicolon_ReturnsZero() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#x43", 0, writer);
    
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }

  @Test
  public void testTranslate_WithNoCharactersAfterAmpersand_ReturnsZero() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#;", 0, writer);
    
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }

  @Test
  public void testTranslate_WithNoDigitsAfterAmpersand_ReturnsZero() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    StringWriter writer = new StringWriter();
    
    int result = unescaper.translate("&#x;", 0, writer);
    
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
  
}