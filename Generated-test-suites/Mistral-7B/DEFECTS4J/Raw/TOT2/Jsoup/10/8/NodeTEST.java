import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.AttributesImpl;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "elementId");
        Node node = new Element("div", baseUri, attributes);

        Assert.assertEquals("div", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertNull(node.parentNode());
        Assert.assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testNodeWithChildren() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "elementId");

        Node parentNode = new Element("body", baseUri, attributes);
        Node childNode1 = new Element("p", baseUri, attributes).text("Child 1");
        Node childNode2 = new Element("p", baseUri, attributes).text("Child 2");

        parentNode.appendChild(childNode1);
        parentNode.appendChild(childNode2);

        Assert.assertEquals(2, parentNode.childNodes().size());
        Assert.assertEquals("p", childNode1.nodeName());
        Assert.assertEquals("Child 1", childNode1.outerHtml());
        Assert.assertEquals("p", childNode2.nodeName());
        Assert.assertEquals("Child 2", childNode2.outerHtml());
        Assert.assertEquals(parentNode, childNode1.parentNode());
        Assert.assertEquals(parentNode, childNode2.parentNode());
    }

    @Test
    public void testNodeWithAttributes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "elementId");
        attributes.put("class", "className");

        Node node = new Element("div", baseUri, attributes);

        Assert.assertEquals("div", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertNotNull(node.attr("id"));
        Assert.assertNotNull(node.attr("class"));
        Assert.assertEquals("elementId", node.attr("id"));
        Assert.assertEquals("className", node.attr("class"));
    }

    @Test
    public void testNodeWithEmptyAttributes() {
        String baseUri = "http://www.example.com";
        Node node = new Element("div", baseUri);

        Assert.assertEquals("div", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNull(node.attributes());
    }

    @Test
    public void testNodeWithNullAttributes() {
        String baseUri = "http://www.example.com";
        Node node = new Element("div", baseUri, null);

        Assert.assertEquals("div", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNull(node.attributes());
    }

    @Test
    public void testNodeWithTextChild() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("id", "elementId");

        Node parentNode = new Element("body", baseUri, attributes);
        Node textNode = new TextNode("Text content");

        parentNode.appendChild(textNode);

        Assert.assertEquals(1, parentNode.childNodes().size());
        Assert.assertEquals("Text content", textNode.outerHtml());
        Assert.assertEquals(parentNode, textNode.parentNode());
    }

    @Test
    public void testNodeWithMalformedURL() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("href", "mailto:example@");

        Node node = new Element("a", baseUri, attributes);

        Assert.assertEquals("a", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        Assert.assertNull(node.absUrl("href"));
    }

    @Test(expected = MalformedURLException.class)
    public void testNodeWithInvalidURL() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new AttributesImpl();
        attributes.put("href", "javascrip:alert('Hello')");

        Node node = new Element("a", baseUri, attributes);

        Assert.assertEquals("a", node.nodeName());
        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals(attributes, node.attributes());
        node.absUrl("href");
    }
}