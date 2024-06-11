
import org.jsoup.nodes.DocumentType;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        DocumentType doctype = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, null);
        Assert.assertEquals("<!DOCTYPE html >", accum.toString());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        Assert.assertEquals("name", doctype.attr("name"));
        Assert.assertEquals("publicId", doctype.attr("publicId"));
        Assert.assertEquals("systemId", doctype.attr("systemId"));
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, null);
        Assert.assertEquals("", accum.toString());
    }

}

