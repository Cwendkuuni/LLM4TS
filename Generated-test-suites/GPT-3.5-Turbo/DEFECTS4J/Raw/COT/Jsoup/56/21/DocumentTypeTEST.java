
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        String nodeName = documentType.nodeName();
        Assert.assertEquals("#doctype", nodeName);
    }
    
    @Test
    public void testOuterHtmlHead() throws Exception {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = new OutputSettings();
        out.syntax(OutputSettings.Syntax.html);
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!doctype", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        boolean hasName = documentType.has("name");
        boolean hasPublicId = documentType.has("publicId");
        boolean hasSystemId = documentType.has("systemId");
        boolean hasInvalidAttribute = documentType.has("invalidAttribute");
        
        Assert.assertTrue(hasName);
        Assert.assertTrue(hasPublicId);
        Assert.assertTrue(hasSystemId);
        Assert.assertFalse(hasInvalidAttribute);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testConstructorWithBlankArguments() {
        DocumentType documentType = new DocumentType("", "", "", "");
        String nodeName = documentType.nodeName();
        boolean hasName = documentType.has("name");
        boolean hasPublicId = documentType.has("publicId");
        boolean hasSystemId = documentType.has("systemId");
        
        Assert.assertEquals("#doctype", nodeName);
        Assert.assertFalse(hasName);
        Assert.assertFalse(hasPublicId);
        Assert.assertFalse(hasSystemId);
    }
    
    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() throws Exception {
        DocumentType documentType = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = new OutputSettings();
        out.syntax(OutputSettings.Syntax.html);
        documentType.outerHtmlHead(accum, depth, out);
        Assert.assertEquals("<!DOCTYPE name  \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        OutputSettings out = new OutputSettings();
        documentType.outerHtmlTail(accum, depth, out);
        Assert.assertEquals("", accum.toString());
    }
}
