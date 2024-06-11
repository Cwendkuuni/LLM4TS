import org.junit.Test;
import org.junit.Assert;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Assert.assertEquals("html", doctype.attr("name"));
        Assert.assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
        Assert.assertEquals("http://www.example.com", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Assert.assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithSyntaxHtml() {
        DocumentType doctype = new DocumentType("html", null, null, "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutSyntaxHtml() {
        DocumentType doctype = new DocumentType("html", null, null, "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", null, null, "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        try {
            doctype.outerHtmlTail(accum, 0, new Document.OutputSettings());
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("", accum.toString());
    }

    @Test
    public void testHasAttributeWithEmptyValue() {
        DocumentType doctype = new DocumentType("html", "", null, "http://www.example.com");
        Assert.assertTrue(doctype.has("name"));
    }

    @Test
    public void testHasAttributeWithNonEmptyValue() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        Assert.assertTrue(doctype.has("publicId"));
    }

    @Test
    public void testHasAttributeWithBlankValue() {
        DocumentType doctype = new DocumentType("html", "   ", null, "http://www.example.com");
        Assert.assertFalse(doctype.has("publicId"));
    }
  
    
    // Complete test cases go here
  
    
}