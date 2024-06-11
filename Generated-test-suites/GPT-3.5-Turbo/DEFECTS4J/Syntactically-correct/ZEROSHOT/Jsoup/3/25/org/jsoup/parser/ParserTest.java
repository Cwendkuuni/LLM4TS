import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
  
  private Parser parser;
  
  @Before
  public void setup() {
    parser = new Parser("", "", false);
  }
  
  @Test
  public void testParse() {
    String html = "<html><head><title>Test</title></head><body></body></html>";
    String baseUri = "http://example.com";
    Document document = parser.parse(html, baseUri);
    
    Assert.assertNotNull(document);
    Assert.assertEquals("http://example.com", document.baseUri());
    Assert.assertEquals(1, document.children().size());
    Element htmlElement = document.child(0);
    Assert.assertEquals("html", htmlElement.tagName());
    Assert.assertEquals(2, htmlElement.children().size());
    
    Element headElement = htmlElement.child(0);
    Assert.assertEquals("head", headElement.tagName());
    Assert.assertEquals(1, headElement.children().size());
    Element titleElement = headElement.child(0);
    Assert.assertEquals("title", titleElement.tagName());
    Assert.assertEquals("Test", titleElement.text());
    
    Element bodyElement = htmlElement.child(1);
    Assert.assertEquals("body", bodyElement.tagName());
    Assert.assertEquals(0, bodyElement.children().size());
  }
  
  @Test
  public void testParseBodyFragment() {
    String bodyHtml = "<div><p>Test</p></div>";
    String baseUri = "http://example.com";
    Document document = parser.parseBodyFragment(bodyHtml, baseUri);
    
    Assert.assertNotNull(document);
    Assert.assertEquals("http://example.com", document.baseUri());
    Assert.assertEquals(1, document.children().size());
    Element bodyElement = document.child(0);
    Assert.assertEquals("body", bodyElement.tagName());
    Assert.assertEquals(1, bodyElement.children().size());
    
    Element divElement = bodyElement.child(0);
    Assert.assertEquals("div", divElement.tagName());
    Assert.assertEquals(1, divElement.children().size());
    Element pElement = divElement.child(0);
    Assert.assertEquals("p", pElement.tagName());
    Assert.assertEquals("Test", pElement.text());
  }
}