import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        String nodeName = docType.nodeName();
        Assert.assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType1 = new DocumentType("name", null, null, null);
        DocumentType docType2 = new DocumentType("name", "publicId", null, null);
        DocumentType docType3 = new DocumentType("name", null, "systemId", null);
        DocumentType docType4 = new DocumentType("name", "publicId", "systemId", null);
        DocumentType docType5 = new DocumentType("name", null, null, "baseUri");
        DocumentType docType6 = new DocumentType("name", "publicId", null, "baseUri");
        DocumentType docType7 = new DocumentType("name", null, "systemId", "baseUri");
        DocumentType docType8 = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        StringBuilder accum1 = new StringBuilder();
        docType1.outerHtmlHead(accum1, 0, new Document.OutputSettings(Document.Syntax.html));
        Assert.assertEquals("<!doctype>", accum1.toString());
        
        StringBuilder accum2 = new StringBuilder();
        docType1.outerHtmlHead(accum2, 0, new Document.OutputSettings(Document.Syntax.xml));
        Assert.assertEquals("<!DOCTYPE>", accum2.toString());
        
        StringBuilder accum3 = new StringBuilder();
        docType1.outerHtmlHead(accum3, 0, new Document.OutputSettings(Document.Syntax.html));
        Assert.assertEquals("<!doctype>", accum3.toString());
        
        StringBuilder accum4 = new StringBuilder();
        docType2.outerHtmlHead(accum4, 0, new Document.OutputSettings(Document.Syntax.xml));
        Assert.assertEquals("<!DOCTYPE name PUBLIC \"publicId\">", accum4.toString());
        
        StringBuilder accum5 = new StringBuilder();
        docType3.outerHtmlHead(accum5, 0, new Document.OutputSettings(Document.Syntax.html));
        Assert.assertEquals("<!DOCTYPE name \"systemId\">", accum5.toString());
        
        StringBuilder accum6 = new StringBuilder();
        docType4.outerHtmlHead(accum6, 0, new Document.OutputSettings(Document.Syntax.xml));
        Assert.assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum6.toString());
        
        StringBuilder accum7 = new StringBuilder();
        docType5.outerHtmlHead(accum7, 0, new Document.OutputSettings(Document.Syntax.html));
        Assert.assertEquals("<!doctype>", accum7.toString());
        
        StringBuilder accum8 = new StringBuilder();
        docType6.outerHtmlHead(accum8, 0, new Document.OutputSettings(Document.Syntax.xml));
        Assert.assertEquals("<!DOCTYPE name PUBLIC \"publicId\">", accum8.toString());
        
        StringBuilder accum9 = new StringBuilder();
        docType7.outerHtmlHead(accum9, 0, new Document.OutputSettings(Document.Syntax.html));
        Assert.assertEquals("<!DOCTYPE name \"systemId\">", accum9.toString());
        
        StringBuilder accum10 = new StringBuilder();
        docType8.outerHtmlHead(accum10, 0, new Document.OutputSettings(Document.Syntax.xml));
        Assert.assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum10.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("name", null, null, null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        Assert.assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        boolean hasBlank = docType.has("");
        boolean hasName = docType.has("name");
        boolean hasPublicId = docType.has("publicId");
        boolean hasSystemId = docType.has("systemId");
        Assert.assertFalse(hasBlank);
        Assert.assertTrue(hasName);
        Assert.assertTrue(hasPublicId);
        Assert.assertTrue(hasSystemId);
    }
}