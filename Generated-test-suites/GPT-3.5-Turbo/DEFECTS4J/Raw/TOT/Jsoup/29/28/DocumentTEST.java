import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        // typical use case
        Document document = Document.createShell("http://example.com");
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body></body>\n" +
                "</html>", document.outerHtml());

        // edge case
        document = Document.createShell("");
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body></body>\n" +
                "</html>", document.outerHtml());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://example.com");
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());

        // edge case
        document = new Document("http://example.com");
        head = document.head();
        Assert.assertNull(head);
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://example.com");
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());

        // edge case
        document = new Document("http://example.com");
        body = document.body();
        Assert.assertNull(body);
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");
        String title = document.title();
        Assert.assertEquals("", title);

        // typical use case
        document.title("Sample Title");
        title = document.title();
        Assert.assertEquals("Sample Title", title);

        // edge case
        document.getElementsByTag("title").remove();
        title = document.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());

        // edge case
        element = document.createElement("");
        Assert.assertNull(element);
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        Document normalisedDoc = document.normalise();
        Assert.assertSame(document, normalisedDoc);

        // edge case
        document.body().prependChild(document.createElement("span").text("Text1"));
        normalisedDoc = document.normalise();
        Assert.assertSame(document, normalisedDoc);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body> Text1\n" +
                " </body>\n" +
                "</html>", normalisedDoc.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        Element body = document.body();

        // typical use case
        document.text("Sample Text");
        Assert.assertEquals("Sample Text", body.text());

        // edge case
        document.text("");
        Assert.assertEquals("", body.text());

        document.text(null);
        Assert.assertEquals("", body.text());
    }

    @Test
    public void testOutputSettings() {
        Document document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        Assert.assertNotNull(outputSettings);

        // typical use case
        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());
        
        // edge case
        outputSettings.indentAmount(0);
        Assert.assertEquals(0, outputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        Document document = Document.createShell("http://example.com");
        Document.QuirksMode quirksMode = document.quirksMode();
        Assert.assertEquals(Document.QuirksMode.noQuirks, quirksMode);
        
        // typical use case
        document.quirksMode(Document.QuirksMode.quirks);
        Assert.assertEquals(Document.QuirksMode.quirks, document.quirksMode());
        
        // edge case
        document.quirksMode(null);
        Assert.assertNull(document.quirksMode());
    }

}