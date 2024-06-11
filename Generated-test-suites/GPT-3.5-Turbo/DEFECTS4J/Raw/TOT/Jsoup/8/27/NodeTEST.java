
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    private Element element;
    private TextNode textNode;
    private Comment comment;

    @Before
    public void setUp() {
        element = new Element("div");
        textNode = new TextNode("This is a text node");
        comment = new Comment("This is a comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("div", element.nodeName());
        assertEquals("#text", textNode.nodeName());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testAttributes() {
        assertFalse(element.hasAttr("class"));

        element.attr("class", "container");
        assertTrue(element.hasAttr("class"));
        assertEquals("container", element.attr("class"));

        element.removeAttr("class");
        assertFalse(element.hasAttr("class"));
        assertEquals("", element.attr("class"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("", element.baseUri());

        element.setBaseUri("http://example.com");
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testAbsUrl() {
        String attributeKey = "href";

        element.attr(attributeKey, "/page.html");
        element.setBaseUri("http://example.com");
        assertEquals("http://example.com/page.html", element.absUrl(attributeKey));

        element.attr(attributeKey, "http://example2.com");
        element.setBaseUri("http://example.com");
        assertEquals("http://example2.com", element.absUrl(attributeKey));
    }

    @Test
    public void testChildNodes() {
        assertEquals(0, element.childNodes().size());

        element.appendChild(textNode);
        element.appendChild(comment);
        assertEquals(2, element.childNodes().size());
        assertEquals(textNode, element.childNode(0));
        assertEquals(comment, element.childNode(1));
    }

    @Test
    public void testParent() {
        assertNull(element.parent());

        element.appendChild(textNode);
        assertEquals(element, textNode.parent());
    }

    @Test
    public void testOwnerDocument() {
        assertNull(element.ownerDocument());

        Document document = new Document("");
        document.appendChild(element);
        assertEquals(document, element.ownerDocument());
        assertEquals(document, textNode.ownerDocument());
        assertEquals(document, comment.ownerDocument());
    }

    @Test
    public void testRemove() {
        Document document = new Document("");
        document.appendChild(element);
        element.remove();
        assertNull(element.parent());
        assertEquals(0, document.childNodes().size());
    }

    @Test
    public void testReplaceWith() {
        Document document = new Document("");
        document.appendChild(element);

        Element newElement = new Element("div");
        element.replaceWith(newElement);
        assertNull(element.parent());
        assertEquals(1, document.childNodes().size());
        assertEquals(newElement, document.childNode(0));
    }
}

