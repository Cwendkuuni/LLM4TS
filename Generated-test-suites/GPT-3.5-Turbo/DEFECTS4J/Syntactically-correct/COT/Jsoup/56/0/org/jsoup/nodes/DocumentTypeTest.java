
import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType1 = new DocumentType("DOCTYPE", null, null, null);
        StringBuilder accum1 = new StringBuilder();
        docType1.outerHtmlHead(accum1, 0, new Document.OutputSettings(Document.OutputSettings.Syntax.html));
        Assert.assertEquals("<!doctype", accum1.toString());

        DocumentType docType2 = new DocumentType("NAME", "publicId", "systemId", null);
        StringBuilder accum2 = new StringBuilder();
        docType2.outerHtmlHead(accum2, 0, new Document.OutputSettings(Document.OutputSettings.Syntax.html));
        Assert.assertEquals("<!DOCTYPE NAME PUBLIC \"publicId\"", accum2.toString());

        DocumentType docType3 = new DocumentType("NAME", null, "systemId", null);
        StringBuilder accum3 = new StringBuilder();
        docType3.outerHtmlHead(accum3, 0, new Document.OutputSettings(Document.OutputSettings.Syntax.html));
        Assert.assertEquals("<!DOCTYPE NAME \"systemId\"", accum3.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        Assert.assertEquals("", accum.toString());
    }

}
