package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {
  
  private XmlDeclaration declaration;

  @Before
  public void setUp() {
    declaration = new XmlDeclaration("name", "baseUri", true);
  }
  
  @Test
  public void testNodeName() {
    assertEquals("#declaration", declaration.nodeName());
  }
  
  @Test
  public void testGetName() {
    assertEquals("name", declaration.name());
  }
  
  @Test
  public void testGetWholeDeclaration() {
    assertEquals("name", declaration.getWholeDeclaration());
  }
  
  @Test
  public void testOuterHtmlHead() {
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();
    
    try {
      declaration.outerHtmlHead(accum, 0, out);
    } catch (IOException e) {
      fail("IOException was thrown");
    }
    
    assertEquals("<!name>", accum.toString());
  }
  
  @Test
  public void testOuterHtmlTail() {
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();
    
    try {
      declaration.outerHtmlTail(accum, 0, out);
    } catch (IOException e) {
      fail("IOException was thrown");
    }
    
    assertEquals("", accum.toString());
  }
  
  @Test
  public void testToString() {
    assertEquals("<!name>", declaration.toString());
  }
}