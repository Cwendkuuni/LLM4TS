import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        Assert.assertEquals("#doctype", docType.nodeName());
        Assert.assertEquals("html", docType.attr("name"));
        Assert.assertEquals("publicId", docType.attr("publicId"));
        Assert.assertEquals("systemId", docType.attr("systemId"));
        Assert.assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html");
        Assert.assertEquals("#doctype", docType.nodeName());
        Assert.assertEquals("html", docType.attr("name"));
        Assert.assertNull(docType.attr("publicId"));
        Assert.assertNull(docType.attr("systemId"));
        Assert.assertNull(docType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html SYSTEM systemId>\n";
        Assert.assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \n";
        Assert.assertEquals(expected, accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        docType.outerHtmlHead(null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullOutputSettings() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
    }

    @Test
    public void testToString() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        String actual = docType.toString();
        Assert.assertEquals(expected, actual);
    }
}