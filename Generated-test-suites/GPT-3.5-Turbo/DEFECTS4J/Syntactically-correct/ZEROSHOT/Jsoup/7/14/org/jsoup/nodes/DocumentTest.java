import org.junit.Before;
import org.junit.Test;
import org.junit.AssertJUnit;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        AssertJUnit.assertEquals("<html><head></head><body></body></html>", shell.outerHtml());
    }

    @Test
    public void testHead() {
        AssertJUnit.assertNull(document.head());
    }

    @Test
    public void testBody() {
        AssertJUnit.assertNull(document.body());
    }

    @Test
    public void testTitle() {
        AssertJUnit.assertEquals("", document.title());
        document.title("Test Document");
        AssertJUnit.assertEquals("Test Document", document.title());
    }

    @Test
    public void testNormalise() {
        Document normalised = document.normalise();
        AssertJUnit.assertNotNull(normalised.head());
        AssertJUnit.assertNotNull(normalised.body());
        AssertJUnit.assertTrue(normalised.toString().contains("<html>"));
        AssertJUnit.assertTrue(normalised.toString().contains("<head>"));
        AssertJUnit.assertTrue(normalised.toString().contains("<body>"));
    }

    @Test
    public void testText() {
        AssertJUnit.assertEquals("", document.text());
        document.text("Test Text");
        AssertJUnit.assertEquals("Test Text", document.text());
    }

    @Test
    public void testNodeName() {
        AssertJUnit.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        AssertJUnit.assertEquals(Document.OutputSettings.class, document.outputSettings().getClass());
    }
}