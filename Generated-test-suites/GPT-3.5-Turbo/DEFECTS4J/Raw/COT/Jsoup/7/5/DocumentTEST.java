
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals(Tag.valueOf("#root"), doc.tag());
        assertEquals("http://example.com", doc.baseUri());
        
        Element html = doc.child(0);
        assertNotNull(html);
        assertEquals(Tag.valueOf("html"), html.tag());
        
        Element head = html.child(0);
        assertNotNull(head);
        assertEquals(Tag.valueOf("head"), head.tag());
        
        Element body = html.child(1);
        assertNotNull(body);
        assertEquals(Tag.valueOf("body"), body.tag());
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        assertNull(head);

        Element html = doc.appendElement("html");
        Element newHead = doc.head();
        assertNotNull(newHead);
        assertEquals(Tag.valueOf("head"), newHead.tag());
        
        head = doc.head();
        assertNotNull(head);
        assertEquals(newHead, head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        assertNull(body);

        Element html = doc.appendElement("html");
        Element newBody = doc.body();
        assertNotNull(newBody);
        assertEquals(Tag.valueOf("body"), newBody.tag());
        
        body = doc.body();
        assertNotNull(body);
        assertEquals(newBody, body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");
        String title = doc.title();
        assertEquals("", title);

        Element titleEl = doc.appendElement("title");
        titleEl.text("Test Title");
        
        title = doc.title();
        assertEquals("Test Title", title);
        
        doc.title("New Title");
        assertEquals("New Title", titleEl.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");

        Element htmlEl = doc.appendElement("html");
        Element head = htmlEl.appendElement("head");
        Element body = htmlEl.appendElement("body");
        
        Element textNode1 = new TextNode("Text 1", "");
        Element textNode2 = new TextNode("Text 2", "");
        Element textNode3 = new TextNode("Text 3", "");
        
        htmlEl.appendChild(textNode1);
        head.appendChild(textNode2);
        body.appendChild(textNode3);
        
        doc.normalise();
        
        assertEquals(0, htmlEl.childNodeSize());
        assertEquals(1, head.childNodeSize());
        assertEquals(3, body.childNodeSize());
        assertEquals(textNode1, body.childNode(0));
        assertEquals(textNode3, body.childNode(1));
        assertEquals(textNode2, body.childNode(2));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element htmlEl = doc.appendElement("html");
        Element head = htmlEl.appendElement("head");
        Element body = htmlEl.appendElement("body");
        
        String html = doc.outerHtml();
        assertEquals("", html);
        
        head.appendElement("title").text("Test Title");
        body.appendElement("div").text("Test Content");
        
        html = doc.outerHtml();
        assertEquals("<html>\n<head><title>Test Title</title></head>\n<body><div>Test Content</div></body></html>", html);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        
        doc.text("Test Text");
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");
        String nodeName = doc.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertEquals(true, outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        
        outputSettings.prettyPrint(false);
        assertEquals(false, outputSettings.prettyPrint());
        
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}

