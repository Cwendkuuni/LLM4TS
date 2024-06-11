package org.znerd.xmlenc;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

public class XMLEncoderTest{
  @Test
  public void testGetEncoder(){
    XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
    
    assertNotNull(encoder);
    assertEquals("UTF-8", encoder.getEncoding());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetEncoder_InvalidEncoding(){
    XMLEncoder.getEncoder(null);
  }
  
  @Test(expected = UnsupportedEncodingException.class)
  public void testGetEncoder_UnsupportedEncoding(){
    XMLEncoder.getEncoder("invalidEncoding");
  }
  
  @Test
  public void testConstructor(){
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    assertNotNull(encoder);
    assertEquals("UTF-8", encoder.getEncoding());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_InvalidEncoding(){
    new XMLEncoder(null);
  }
  
  @Test(expected = UnsupportedEncodingException.class)
  public void testConstructor_UnsupportedEncoding(){
    new XMLEncoder("invalidEncoding");
  }
  
  @Test
  public void testDeclaration() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.declaration(out);
    String result = out.toString();
    
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", result);
  }
  
  @Test
  public void testTextEscapeAmpersands() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, "Test &", true);
    String result = out.toString();
    
    assertEquals("Test &amp;", result);
  }
  
  @Test
  public void testTextNoEscapeAmpersands() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, "Test &", false);
    String result = out.toString();
    
    assertEquals("Test &", result);
  }
  
  @Test
  public void testTextCharArrayEscapeAmpersands() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, "Test &".toCharArray(), 0, 6, true);
    String result = out.toString();
    
    assertEquals("Test &amp;", result);
  }
  
  @Test
  public void testTextCharArrayNoEscapeAmpersands() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, "Test &".toCharArray(), 0, 6, false);
    String result = out.toString();
    
    assertEquals("Test &", result);
  }
  
  @Test(expected = InvalidXMLException.class)
  public void testTextCharArrayInvalidCharacter() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, "\u007F", false);
  }
  
  @Test
  public void testTextCharacter() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, 'a');
    String result = out.toString();
    
    assertEquals("a", result);
  }
  
  @Test
  public void testTextCharacterEscapeAmpersands() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, '&', true);
    String result = out.toString();
    
    assertEquals("&amp;", result);
  }
  
  @Test(expected = InvalidXMLException.class)
  public void testTextCharacterInvalidCharacter() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.text(out, '\u007F');
  }
  
  @Test
  public void testWhitespaceString() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.whitespace(out, "  ");
    String result = out.toString();
    
    assertEquals("  ", result);
  }
  
  @Test
  public void testWhitespaceCharArray() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.whitespace(out, "  ".toCharArray(), 0, 2);
    String result = out.toString();
    
    assertEquals("  ", result);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testWhitespaceCharArray_InvalidRange() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.whitespace(out, "  ".toCharArray(), 0, 3);
  }
  
  @Test
  public void testAttributeEscapeAmpersands() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.attribute(out, "name", "value &", '\"', true);
    String result = out.toString();
    
    assertEquals(" name=\"value &amp;\"", result);
  }
  
  @Test
  public void testAttributeNoEscapeAmpersands() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.attribute(out, "name", "value &", '\"', false);
    String result = out.toString();
    
    assertEquals(" name=\"value &\"", result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAttribute_InvalidQuotationMark() throws Exception{
    Writer out = new StringWriter();
    XMLEncoder encoder = new XMLEncoder("UTF-8");
    
    encoder.attribute(out, "name", "value", '\'', false);
  }
}