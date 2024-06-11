import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        DocumentType documentType = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!DOCTYPE html >", accum.toString());
    }
}