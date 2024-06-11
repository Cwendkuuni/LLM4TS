import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {
    private DocumentType documentType;

    @Test
    public void testConstructor() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        Assert.assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntaxNoPublicIdNoSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        documentType = new DocumentType("html", null, null, "http://www.example.com");
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("<!doctype", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntaxWithPublicId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntaxWithSystemId() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        documentType = new DocumentType("html", null, "http://www.example.com", "http://www.example.com");
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE html \"http://www.example.com\">", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadXmlSyntax() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder sb = new StringBuilder();
        try {
            documentType.outerHtmlTail(sb, 0, outputSettings);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testHasWithAttribute() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        boolean hasAttribute = documentType.has("name");
        Assert.assertTrue(hasAttribute);
    }

    @Test
    public void testHasWithoutAttribute() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        boolean hasAttribute = documentType.has("version");
        Assert.assertFalse(hasAttribute);
    }
}