
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        Assert.assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        Assert.assertNotNull(html);
        Element head = doc.getElementsByTag("head").first();
        Assert.assertNotNull(head);
        Element body = doc.getElementsByTag("body").first();
        Assert.assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("https://www.example.com");
        Element head = doc.head();
        Assert.assertNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("https://www.example.com");
        Element body = doc.body();
        Assert.assertNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("https://www.example.com");
        String title = doc.title();
        Assert.assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testcreateElement() {
        Document doc = new Document("https://www.example.com");
        Element element = doc.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("https://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        Element body = doc.body();
        body.appendElement("p").text("This is a test paragraph.");
        body.appendElement("p").text("This is another test paragraph.");
        doc.normalise();
        Assert.assertEquals(2, body.childrenSize());
        Assert.assertEquals("This is a test paragraph.", body.child(0).text());
        Assert.assertEquals("This is another test paragraph.", body.child(1).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("https://www.example.com");
        String html = doc.outerHtml();
        Assert.assertEquals("", html);
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("https://www.example.com");
        doc.text("This is some text.");
        String text = doc.text();
        Assert.assertEquals("This is some text.", text);
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("https://www.example.com");
        Document.OutputSettings settings = doc.outputSettings();
        Assert.assertNotNull(settings);

        Assert.assertEquals("base", settings.escapeMode().name());

        settings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        Assert.assertEquals("extended", settings.escapeMode().name());

        Assert.assertEquals("UTF-8", settings.charset().name());

        settings.charset("ISO-8859-1");
        Assert.assertEquals("ISO-8859-1", settings.charset().name());

        Assert.assertTrue(settings.prettyPrint());

        settings.prettyPrint(false);
        Assert.assertFalse(settings.prettyPrint());

        Assert.assertEquals(1, settings.indentAmount());

        settings.indentAmount(2);
        Assert.assertEquals(2, settings.indentAmount());
    }

}
