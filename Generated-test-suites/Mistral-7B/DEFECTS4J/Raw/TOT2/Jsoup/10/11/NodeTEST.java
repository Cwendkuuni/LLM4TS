import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class NodeTest {

    @Test
    public void testNodeCreation() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node node1 = new Node(baseUri, attributes);
        Node node2 = new Node(baseUri);
        Node node3 = new Node();

        // Test nodeName method
        Assert.assertNotNull(node1.nodeName());
        Assert.assertEquals("Element name not set correctly.", "testNode", node1.nodeName());
        Assert.assertNull(node2.nodeName());
        Assert.assertNull(node3.nodeName());

        // Test attr method with valid attribute keys
        String attrKey = "class";
        String attrValue = "testClass";
        node1.attr(attrKey, attrValue);
        String result = node1.attr(attrKey);
        Assert.assertEquals("Attribute value not set correctly.", attrValue, result);

        // Test attributes method
        Assert.assertEquals("Number of attributes not set correctly.", 1, node1.attributes().size());
        Assert.assertEquals("Attribute not present in attributes.", attrKey, node1.attributes().keySet().iterator().next());
        Assert.assertEquals("Attribute value not present in attributes.", attrValue, node1.attributes().get(attrKey));

        // Test hasAttr method
        Assert.assertTrue("Node does not have expected attribute.", node1.hasAttr(attrKey));
        Assert.assertFalse("Node has unexpected attribute.", node1.hasAttr("nonExistentAttr"));

        // Test removeAttr method
        node1.removeAttr(attrKey);
        Assert.assertFalse("Node still has removed attribute.", node1.hasAttr(attrKey));

        // Test baseUri method
        Assert.assertEquals("Base URI not set correctly.", baseUri, node1.baseUri());

        // Test absUrl method with valid attribute key
        attrKey = "href";
        String relUrl = "/testPage.html";
        node1.attr(attrKey, relUrl);
        String absUrl = node1.absUrl(attrKey);
        String expectedAbsUrl = "http://example.com/testPage.html";
        Assert.assertEquals("Absolute URL not generated correctly.", expectedAbsUrl, absUrl);

        // Test absUrl method with invalid attribute key
        Assert.assertEquals("Absolute URL generated with invalid key.", "", node1.absUrl("nonExistentAttr"));
    }

    @Test
    public void testNodeChildRelationships() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes);
        Node childNode1 = new Node(baseUri, attributes);
        Node childNode2 = new Node(baseUri, attributes);

        parentNode.addChildren(childNode1, childNode2);

        // Test childNode method
        Node child = parentNode.childNode(0);
        Assert.assertEquals("Child node not correctly retrieved.", childNode1, child);

        // Test childNodes method
        List<Node> children = parentNode.childNodes();
        Assert.assertEquals("Number of children not set correctly.", 2, children.size());
        Assert.assertEquals("Child node not present in children list.", childNode1, children.get(0));
        Assert.assertEquals("Child node not present in children list.", childNode2, children.get(1));

        // Test childNodesAsArray method
        Node[] childrenArray = parentNode.childNodesAsArray();
        Assert.assertArrayEquals("Number of children not set correctly in array.", new Node[]{childNode1, childNode2}, childrenArray);

        // Test parent method
        Node child = childNode1;
        Assert.assertEquals("Parent node not correctly retrieved.", parentNode, child.parent());

        // Test ownerDocument method
        Document document = new Document();
        child.setParentNode(document);
        Assert.assertNull("Owner document not correctly retrieved.", parentNode.ownerDocument());
        Assert.assertEquals("Owner document not correctly retrieved.", document, child.ownerDocument());
    }

    @Test
    public void testNodeRemoval() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes);
        Node childNode = new Node(baseUri, attributes);
        parentNode.addChildren(childNode);

        // Test remove method
        parentNode.remove();
        Assert.assertNull("Node not correctly removed.", parentNode);

        // Test replaceWith method
        Node replacementNode = new Node(baseUri, attributes);
        parentNode.replaceWith(replacementNode);
        Assert.assertEquals("Replaced node not correctly replaced.", replacementNode, parentNode);

        // Test setParentNode method
        childNode.setParentNode(parentNode);
        Assert.assertEquals("Child node not correctly set as parent node's child.", childNode, parentNode.childNode(0));

        // Test replaceChild method
        Node newChildNode = new Node(baseUri, attributes);
        parentNode.replaceChild(childNode, newChildNode);
        Assert.assertEquals("Replaced child node not correctly replaced.", newChildNode, parentNode.childNode(0));

        // Test removeChild method
        parentNode.removeChild(childNode);
        Assert.assertFalse("Child node not correctly removed.", parentNode.hasChildNode(childNode));
    }
}