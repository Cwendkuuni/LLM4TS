import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class XmlDeclarationTest {

    private static XmlDeclaration xmlDeclaration;
    private static String name = "xml";
    private static String baseUri = "http://example.com";
    private static boolean isProcessingInstruction = true;

    @BeforeClass
    public static void setUp() {
        xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        Assert.assertEquals(name, xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        Assert.assertEquals(name, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(accum, depth, out);
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", accum.toString());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(accum, depth, out);
            Assert.assertEquals("", accum.toString());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testToString() {
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }
}