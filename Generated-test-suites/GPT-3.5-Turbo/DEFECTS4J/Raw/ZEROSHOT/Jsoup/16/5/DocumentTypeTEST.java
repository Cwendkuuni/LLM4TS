import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        String nodeName = documentType.nodeName();
        Assert.assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
        String actual = accum.toString();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        
        String expected = "<!DOCTYPE html http://www.w3.org/TR/html4/loose.dtd\">";
        String actual = accum.toString();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, outputSettings);
        
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd\">";
        String actual = accum.toString();
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, outputSettings);
        
        String expected = "";
        String actual = accum.toString();
        
        Assert.assertEquals(expected, actual);
    }
}