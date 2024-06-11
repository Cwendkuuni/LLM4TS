import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("");
    }

    @Test
    public void testDocumentConstructor() {
        Assert.assertNotNull(document);
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("");
        Assert.assertNotNull(shell);
        Assert.assertEquals(1, shell.getElementsByTag("html").size());
        Assert.assertEquals(1, shell.getElementsByTag("head").size());
        Assert.assertEquals(1, shell.getElementsByTag("body").size());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        Assert.assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        Assert.assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        Assert.assertEquals(1, document.getElementsByTag("html").size());
        Assert.assertEquals(1, document.getElementsByTag("head").size());
        Assert.assertEquals(1, document.getElementsByTag("body").size());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        Assert.assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Test Text");
        String text = document.text();
        Assert.assertEquals("Test Text", text);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        Assert.assertEquals("#document", nodeName);
    }

}