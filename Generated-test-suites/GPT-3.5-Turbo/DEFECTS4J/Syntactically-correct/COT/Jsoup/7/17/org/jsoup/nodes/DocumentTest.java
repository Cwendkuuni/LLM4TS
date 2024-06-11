
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("#root", doc.nodeName());
        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertEquals("html", doc.nodeName());
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleNoTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.getElementsByTag("title").remove();
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleNullTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testTitleBlankTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("   ");
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element element = doc.createElement("a");
        Assert.assertNotNull(element);
        Assert.assertEquals("a", element.nodeName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        body.appendText("Hello");
        body.appendText(" World");
        body.appendText("!");
        doc.normalise();
        Assert.assertEquals("Hello World!", body.text());
    }

    @Test
    public void testNormaliseNoHtmlElement() {
        Document doc = new Document("http://example.com");
        doc.normalise();
        Assert.assertNotNull(doc.getHead());
        Assert.assertNotNull(doc.getBody());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        body.appendText("Testing OuterHtml");
        String outerHtml = doc.outerHtml();
        Assert.assertEquals("<body>Testing OuterHtml</body>", outerHtml);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        Element body = doc.body();
        doc.text("Testing Text");
        Assert.assertEquals("Testing Text", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        Assert.assertNotNull(outputSettings);
        Assert.assertEquals(Document.OutputSettings.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Assert.assertNotNull(outputSettings.encoder());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }
}

