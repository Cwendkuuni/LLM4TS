import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NodeTest {

    @Test
    public void testBasicNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "myElement");
        Node node = new Element("div", baseUri, attributes);

        // Basic checks
        assertNull(node.parentNode());
        assertNotNull(node.attributes());
        assertEquals("div", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertFalse(node.hasAttr("nonExistentAttr"));
        assertTrue(node.hasAttr("id"));
        assertEquals("myElement", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttr"));

        // Test getters and setters
        assertEquals("div", node.nodeName());
        node.nodeName("p");
        assertEquals("p", node.nodeName());

        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://example.org");
        assertEquals("http://example.org", node.baseUri());

        assertNotNull(node.attributes());
        node.attributes().put("class", "newClass");
        assertEquals("newClass", node.attr("class"));

        assertFalse(node.hasAttr("nonExistentAttr"));
        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "myElement");

        Node parentNode = new Element("div", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes);
        Node childNode2 = new Element("span", baseUri, attributes);

        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);

        // Basic checks
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNode(0));
        assertEquals(childNode2, parentNode.childNode(1));

        // Test getters and setters
        assertEquals(2, parentNode.childNodes().size());
        parentNode.removeChild(childNode1);
        assertEquals(1, parentNode.childNodes().size());
        assertEquals(childNode2, parentNode.childNode(0));

        parentNode.appendChild(childNode1);
        parentNode.addChildren(new Element("i"), new Element("em"));
        assertEquals(4, parentNode.childNodes().size());

        List<Node> children = new ArrayList<>();
        children.add(childNode1);
        children.add(childNode2);
        parentNode.setChildren(children);
        assertEquals(children, parentNode.childNodes());
    }

    @Test
    public void testUrlHandling() {
        String baseUri = "http://example.com";
        Attributes attributes = new AttributesImpl();

        Node node = new Element("a", baseUri, attributes);
        node.attr("href", "about.html");
        String absUrl = node.absUrl("href");
        assertEquals("http://example.com/about.html", absUrl);

        node.setBaseUri("http://example.org");
        absUrl = node.absUrl("href");
        assertEquals("http://example.org/about.html", absUrl);

        // Test with relative URL
        node.setBaseUri("http://example.com/index.html");
        node.attr("href", "images/logo.png");
        absUrl = node.absUrl("href");
        assertEquals("http://example.com/images/logo.png", absUrl);

        // Test with absolute URL
        node.setBaseUri("http://example.com");
        node.attr("href", "http://example.net/index.html");
        absUrl = node.absUrl("href");
        assertEquals("http://example.net/index.html", absUrl);

        try {
            node.setBaseUri("http://example.com");
            node.attr("href", "http://example.com/test?id=1&id=2");
            absUrl = node.absUrl("href");
            new URL(absUrl);
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // Expected
        }
    }
}