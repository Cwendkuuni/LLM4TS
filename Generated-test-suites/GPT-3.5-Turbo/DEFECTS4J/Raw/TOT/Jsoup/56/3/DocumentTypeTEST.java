import org.junit.*;

public class DocumentTypeTest {

    // Test case for DocumentType constructor
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        
        Assert.assertEquals("html", doctype.attr("name"));
        Assert.assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.attr("publicId"));
        Assert.assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.attr("systemId"));
        Assert.assertEquals("http://www.example.com", doctype.baseUri());
    }

    // Test case for nodeName() method
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        Assert.assertEquals("#doctype", doctype.nodeName());
    }

    // Test case for outerHtmlHead() method with Syntax.html and no publicId and systemId
    @Test
    public void testOuterHtmlHeadHtml5() {
        DocumentType doctype = new DocumentType("html", null, null, "http://www.example.com");

        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, Document.OutputSettings.html());

        Assert.assertEquals("<!doctype", sb.toString());
    }

    // Test case for outerHtmlHead() method with Syntax.html and publicId
    @Test
    public void testOuterHtmlHeadHtmlWithPublicId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", null, "http://www.example.com");

        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, Document.OutputSettings.html());

        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">", sb.toString());
    }

    // Test case for outerHtmlHead() method with Syntax.html and systemId
    @Test
    public void testOuterHtmlHeadHtmlWithSystemId() {
        DocumentType doctype = new DocumentType("html", null, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlHead(sb, 0, Document.OutputSettings.html());

        Assert.assertEquals("<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", sb.toString());
    }

    // Test case for outerHtmlTail() method
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlTail(sb, 0, Document.OutputSettings.html());

        Assert.assertEquals("", sb.toString());
    }

    // Test case for has() method returning true
    @Test
    public void testHasTrue() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        Assert.assertTrue(doctype.has("name"));
    }

    // Test case for has() method returning false
    @Test
    public void testHasFalse() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");

        Assert.assertFalse(doctype.has("class"));
    }
}