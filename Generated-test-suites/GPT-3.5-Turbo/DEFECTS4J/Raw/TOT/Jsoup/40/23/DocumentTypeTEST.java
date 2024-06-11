
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.junit.*;

public class DocumentTypeTest {
    
    DocumentType documentType;
    
    @Before
    public void setup() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
    }
    
    @Test
    public void testNodeName() {
        Assert.assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        DocumentType documentTypeWithNameOnly = new DocumentType("html", "", "", "");
        documentTypeWithNameOnly.outerHtmlHead(sb, 0, new Node.OutputSettings());
        Assert.assertEquals("<!DOCTYPE html>", sb.toString());
        
        DocumentType documentTypeWithPublicId = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "");
        sb.setLength(0);
        documentTypeWithPublicId.outerHtmlHead(sb, 0, new Node.OutputSettings());
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">", sb.toString());
        
        DocumentType documentTypeWithSystemId = new DocumentType("html", "", "http://www.example.com", "");
        sb.setLength(0);
        documentTypeWithSystemId.outerHtmlHead(sb, 0, new Node.OutputSettings());
        Assert.assertEquals("<!DOCTYPE html \"http://www.example.com\">", sb.toString());
        
        // Test edge case of empty attribute values
        DocumentType documentTypeWithEmptyAttributes = new DocumentType("", "", "", "");
        sb.setLength(0);
        documentTypeWithEmptyAttributes.outerHtmlHead(sb, 0, new Node.OutputSettings());
        Assert.assertEquals("<!DOCTYPE>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlTail(sb, 0, new Node.OutputSettings());
        Assert.assertEquals("", sb.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        DocumentType documentTypeWithEmptyName = new DocumentType("", "publicId", "systemId", "baseUri");
    }
}
