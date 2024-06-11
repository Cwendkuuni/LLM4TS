import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
  
  @Test
  public void testConstructor() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");
    
    Assert.assertEquals("html", docType.attr("name"));
    Assert.assertEquals("-//W3C//DTD HTML 4.0//EN", docType.attr("publicId"));
    Assert.assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
    Assert.assertEquals("http://example.com/", docType.baseUri());
  }
  
  @Test
  public void testNodeName() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");
    
    Assert.assertEquals("#doctype", docType.nodeName());
  }
  
  @Test
  public void testOuterHtmlHead() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    
    docType.outerHtmlHead(accum, 0, outputSettings);
    
    Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
  }
  
  @Test
  public void testOuterHtmlTail() {
    DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com/");
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    
    docType.outerHtmlTail(accum, 0, outputSettings);
    
    Assert.assertEquals("", accum.toString());
  }
  
}