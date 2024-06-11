import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertEquals("#document", doc.nodeName());
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());

        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());

        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());

        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testTitleNullInput() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test
    public void testTitleSetAndGet() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
    }

    @Test
    public void testTitleSetAndGetEmpty() {
        Document doc = new Document("http://example.com");
        doc.title("");
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        TextNode t1 = doc.createTextNode("Test text");
        p.appendChild(t1);
        doc.appendChild(p);
        doc.normalise();
        List<Node> nodes = doc.body().childNodes();
        Assert.assertEquals(1, nodes.size());
        Element p2 = (Element) nodes.get(0);
        List<Node> children = p2.childNodes();
        Assert.assertEquals(1, children.size());
        TextNode t2 = (TextNode) children.get(0);
        Assert.assertEquals("Test text", t2.text());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("http://example.com");
        Element head1 = doc.createElement("head");
        Element head2 = doc.createElement("head");
        doc.appendChild(head1);
        doc.appendChild(head2);
        doc.normalise();
        Element html = doc.getElementById("root");
        List<Element> heads = html.getElementsByTagName("head");
        Assert.assertEquals(1, heads.size());
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());

        OutputSettings newSettings = new OutputSettings();
        newSettings.escapeMode(Entities.EscapeMode.extended);
        Assert.assertEquals(Entities.EscapeMode.extended, newSettings.escapeMode());

        newSettings.charset("UTF-16");
        Assert.assertEquals("UTF-16", newSettings.charset().name());

        newSettings.prettyPrint(false);
        Assert.assertFalse(newSettings.prettyPrint());

        newSettings.indentAmount(2);
        Assert.assertEquals(2, newSettings.indentAmount());

        doc.outputSettings(newSettings);
        Assert.assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
        Assert.assertEquals("UTF-16", doc.outputSettings().charset().name());
        Assert.assertFalse(doc.outputSettings().prettyPrint());
        Assert.assertEquals(2, doc.outputSettings().indentAmount());
    }
}