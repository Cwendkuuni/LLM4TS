import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testTitle() {
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        Assert.assertSame(document, normalizedDocument);
    }

    @Test
    public void testOuterHtml() {
        Assert.assertEquals("", document.outerHtml());
    }

    @Test
    public void testText() {
        document.text("Test Text");
        Assert.assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        Assert.assertNotSame(document, clone);
        Assert.assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings settings = new Document.OutputSettings();
        document.outputSettings(settings);
        Assert.assertSame(settings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = Document.QuirksMode.quirks;
        document.quirksMode(quirksMode);
        Assert.assertSame(quirksMode, document.quirksMode());
    }

}