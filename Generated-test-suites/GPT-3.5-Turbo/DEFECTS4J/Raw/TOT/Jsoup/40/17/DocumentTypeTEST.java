import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;
import org.junit.Assert;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor_PositiveCase() {
        String name = "HTML";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        String baseUri = "https://www.example.com";
        
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        Assert.assertEquals(name, documentType.attr("name"));
        Assert.assertEquals(publicId, documentType.attr("publicId"));
        Assert.assertEquals(systemId, documentType.attr("systemId"));
        Assert.assertEquals(baseUri, documentType.baseUri());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyName() {
        String name = "";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        String baseUri = "https://www.example.com";
        
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
    }
    
    @Test
    public void testConstructor_NullPublicId() {
        String name = "HTML";
        String publicId = null;
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        String baseUri = "https://www.example.com";
        
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        Assert.assertNull(documentType.attr("publicId"));
    }
    
    @Test
    public void testConstructor_NullSystemId() {
        String name = "HTML";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = null;
        String baseUri = "https://www.example.com";
        
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        Assert.assertNull(documentType.attr("systemId"));
    }
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01 Transitional//EN",
                "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        
        Assert.assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead_PositiveCase() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01 Transitional//EN",
                "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, depth, out);
        
        String expectedOutput = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
        Assert.assertEquals(expectedOutput, accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_NoPublicId() {
        DocumentType documentType = new DocumentType("HTML",
                null, "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, depth, out);
        
        String expectedOutput = "<!DOCTYPE HTML \"http://www.w3.org/TR/html4/loose.dtd\">";
        Assert.assertEquals(expectedOutput, accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_NoSystemId() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01 Transitional//EN",
                null, "https://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, depth, out);
        
        String expectedOutput = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
        Assert.assertEquals(expectedOutput, accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01 Transitional//EN",
                "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlTail(accum, depth, out);
        
        Assert.assertEquals("", accum.toString());
    }

}