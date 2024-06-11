import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        String expectedHtmlHead = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" http://www.w3.org/TR/html4/strict.dtd>";
        Assert.assertEquals(expectedHtmlHead, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoPublicId() {
        documentType = new DocumentType("doctype", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        String expectedHtmlHead = "<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd>";
        Assert.assertEquals(expectedHtmlHead, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoSystemId() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.0//EN", "", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        String expectedHtmlHead = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">";
        Assert.assertEquals(expectedHtmlHead, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoPublicIdAndSystemId() {
        documentType = new DocumentType("doctype", "", "", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, 0, out);

        String expectedHtmlHead = "<!DOCTYPE html>";
        Assert.assertEquals(expectedHtmlHead, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlTail(accum, 0, out);

        String expectedHtmlTail = "";
        Assert.assertEquals(expectedHtmlTail, accum.toString());
    }
}