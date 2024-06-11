package org.jsoup.safety;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
  private Cleaner cleaner;
  
  @Before
  public void setUp() {
    cleaner = new Cleaner(Whitelist.basic());
  }
  
  @Test
  public void testClean() {
    // Set up the dirty document
    Document dirtyDocument = Jsoup.parseBodyFragment("<p id=\"paragraph\">This is a <b>bold</b> paragraph</p>");
    
    // Clean the document
    Document cleanDocument = cleaner.clean(dirtyDocument);
    
    // Check if only allowed tags are present
    Element body = cleanDocument.body();
    Element paragraph = body.getElementsByTag("p").first();
    Assert.assertNotNull(paragraph);
    Assert.assertEquals("This is a bold paragraph", paragraph.text());
    Element bold = body.getElementsByTag("b").first();
    Assert.assertNull(bold);
  }
  
  @Test
  public void testIsValid() {
    // Set up the dirty document
    Document dirtyDocument = Jsoup.parseBodyFragment("<p id=\"paragraph\">This is a <b>bold</b> paragraph</p>");
    
    // Check if the document is valid
    boolean isValid = cleaner.isValid(dirtyDocument);
    
    Assert.assertFalse(isValid);
  }
  
  @Test
  public void testCopySafeNodes() {
    // Set up the source element
    Element source = new Element("div");
    Element p = new Element("p");
    p.text("This is a paragraph");
    source.appendChild(p);
    
    // Set up the destination element
    Element dest = new Element("div");
    
    // Copy the safe nodes from the source to the destination
    int numDiscarded = cleaner.copySafeNodes(source, dest);
    
    // Check if the safe nodes are copied correctly
    Assert.assertEquals(0, numDiscarded);
    Element destP = dest.getElementsByTag("p").first();
    Assert.assertNotNull(destP);
    Assert.assertEquals("This is a paragraph", destP.text());
  }
  
  @Test
  public void testCreateSafeElement() {
    // Set up the source element
    Element sourceEl = new Element("a");
    sourceEl.attr("href", "http://example.com");
    sourceEl.attr("target", "_blank");
    
    // Create the safe element
    Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
    Element dest = meta.el;
    int numAttribsDiscarded = meta.numAttribsDiscarded;
    
    // Check if the safe element is created correctly
    Assert.assertEquals("a", dest.tagName());
    Assert.assertEquals(1, dest.attributes().size());
    Attribute hrefAttr = dest.attributes().get("href");
    Assert.assertNotNull(hrefAttr);
    Assert.assertEquals("http://example.com", hrefAttr.getValue());
    Assert.assertEquals(1, numAttribsDiscarded);
  }
}