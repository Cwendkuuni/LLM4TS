import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals("<!DOCTYPE doctype PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            documentType.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
        Assert.assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        Assert.assertTrue(documentType.has("name"));
        Assert.assertTrue(documentType.has("publicId"));
        Assert.assertTrue(documentType.has("systemId"));
    }
}