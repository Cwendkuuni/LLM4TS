package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * Test class for Node class.
 */
public class NodeTest {

    /**
     * Test Node constructor.
     */
    @Test
    public void testNodeConstructor() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        Node node1 = new Node(baseUri, attributes);
        assertEquals("testValue", node1.attr("testKey"));
        assertEquals(baseUri, node1.baseUri());

        Node node2 = new Node(baseUri);
        assertEquals(baseUri, node2.baseUri());
        assertNull(node2.attributes());

        Node node3 = new Node();
        assertNull(node3.baseUri());
        assertNull(node3.attributes());
    }

    /**
     * Test abstract methods.
     */
    @Test(expected = AssertionError.class)
    public void testAbstractMethods() {
        Node node = new Node();
        node.nodeName();
    }

    /**
     * Test getAttribute method.
     */
    @Test
    public void testGetAttribute() {
        String baseUri = "http://test.com";
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");

        Node node = new Element("test", baseUri, attributes);
        assertEquals("testValue", node.attr("testKey"));
        assertEquals("", node.attr("nonExistentAttr"));

        Node nodeWithBaseAbsUrl = new Element("test", baseUri, attributes);
        nodeWithBaseAbsUrl.absUrl("testKey");
        assertEquals("http://test.com/test", nodeWithBaseAbsUrl.absUrl("test"));
    }

    /**
     * Test getAttributes method.
     */
    @Test
    public void testGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("testKey1", "testValue1");
        attributes.put("testKey2", "testValue2");

        Node node = new Element("test", "", attributes);
        List<String> attributesList = new ArrayList<>();
        node.attributes().forEach(attributesList::add);
        assertEquals(2, attributesList.size());
        assertEquals("testValue1", attributesList.get(0));
        assertEquals("testValue2", attributesList.get(1));
        assertTrue(attributesList.containsAll(Collections.singletonList("testKey1")));
        assertTrue(attributesList.containsAll(Collections.singletonList("testKey2")));
    }

    /**
     * Test setAttribute method.
     */
    @Test
    public void testSetAttribute() {
        Node node = new Element("test", "", new Attributes());
        node.attr("testKey", "testValue");
        assertEquals("testValue", node.attr("testKey"));

        node.attr("testKey", "newTestValue");
        assertEquals("newTestValue", node.attr("testKey"));
    }

    /**
     * Test hasAttribute method.
     */
    @Test
    public void testHasAttribute() {
        Node node = new Element("test", "", new Attributes().put("testKey", "testValue"));
        assertTrue(node.hasAttr("testKey"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    public void testRemoveAttribute() {
        Node node = new Element("test", "", new Attributes().put("testKey", "testValue"));
        node.removeAttr("testKey");
        assertFalse(node.hasAttr("testKey"));
    }

    /**
     * Test baseUri method.
     */
    @Test
    public void testBaseUri() {
        String baseUri = "http://test.com";
        Node node = new Element("test", baseUri, new Attributes());
        assertEquals(baseUri, node.baseUri());
    }

    /**
     * Test setBaseUri method.
     */
    @Test
    public void testSetBaseUri() {
        Node node = new Element("test", "", new Attributes());
        node.setBaseUri("http://test.com");
        assertEquals("http://test.com", node.baseUri());
    }

    /**
     * Test childNode method.
     */
    @Test
    public void testChildNode() {
        Node parent = new Element("parent", "", new Attributes());
        Node child = new Element("child", "", new Attributes());
        parent.addChild(child);
        Node childRetrieved = parent.childNode(0);
        assertEquals(child, childRetrieved);
    }

    /**
     * Test childNodes method.
     */
    @Test
    public void testChildNodes() {
        Node parent = new Element("parent", "", new Attributes());
        Node child1 = new Element("child1", "", new Attributes());
        Node child2 = new Element("child2", "", new Attributes());
        parent.addChildren(child1, child2);
        List<Node> childNodes = parent.childNodes();
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.contains(child1));
        assertTrue(childNodes.contains(child2));
    }

    /**
     * Test childNodesAsArray method.
     */
    @Test
    public void testChildNodesAsArray() {
        Node parent = new Element("parent", "", new Attributes());
        Node child1 = new Element("child1", "", new Attributes());
        Node child2 = new Element("child2", "", new Attributes());
        parent.addChildren(child1, child2);
        Node[] childNodesArray = parent.childNodesAsArray();
        assertEquals(2, childNodesArray.length);
        assertTrue(Arrays.equals(child1, childNodesArray[0]));
        assertTrue(Arrays.equals(child2, childNodesArray[1]));
    }

    /**
     * Test parent method.
     */
    @Test
    public void testParent() {
        Node parent = new Element("parent", "", new Attributes());
        Node child = new Element("child", "", new Attributes());
        parent.addChild(child);
        assertEquals(parent, child.parent());
        assertNull(new Element("root", "", new Attributes()).parent());
    }

    /**
     * Test ownerDocument method.
     */
    @Test
    public void testOwnerDocument() {
        Node node = new Element("test", "", new Attributes());
        Document document = new Document();
        document.appendChild(node);
        assertEquals(document, node.ownerDocument());
    }

    /**
     * Test remove method.
     */
    @Test
    public void testRemove() {
        Node parent = new Element("parent", "", new Attributes());
        Node child = new Element("child", "", new Attributes());
        parent.addChild(child);
        parent.removeChild(child);
        assertFalse(parent.hasChildNodes());
    }

    /**
     * Test replaceWith method.
     */
    @Test
    public void testReplaceWith() {
        Node parent = new Element("parent", "", new Attributes());
        Node child1 = new Element("child1", "", new Attributes());
        Node child2 = new Element("child2", "", new Attributes());
        parent.addChild(child1);
        parent.replaceChild(child1, child2);
        assertEquals(child2, parent.childNode(0));
        assertTrue(child1.parentNode == null);
    }

    /**
     * Test setParentNode method.
     */
    @Test
    public void testSetParentNode() {
        Node child = new Element("child", "", new Attributes());
        Node parent1 = new Element("parent1", "", new Attributes());
        Node parent2 = new Element("parent2", "", new Attributes());
        child.setParentNode(parent1);
        parent1.addChild(child);
        child.setParentNode(parent2);
        assertFalse(parent1.hasChildNodes());
        assertTrue(parent2.hasChildNodes());
    }

    /**
     * Test replaceChild method.
     */
    @Test
    public void testReplaceChild() {
        Node parent = new Element("parent", "", new Attributes());
        Node child1 = new Element("child1", "", new Attributes());
        Node child2 = new Element("child2", "", new Attributes());
        parent.addChild(child1);
        parent.replaceChild(child1, child2);
        assertEquals(child2, parent.childNode(0));
        assertTrue(child1.parentNode == null);
    }

    /**
     * Test removeChild method.
     */
    @Test
    public void testRemoveChild() {
        Node parent = new Element("parent", "", new Attributes());
        Node child1 = new Element("child1", "", new Attributes());
        Node child2 = new Element("child2", "", new Attributes());
        parent.addChild(child1);
        parent.addChild(child2);
        parent.removeChild(child1);
        assertFalse(parent.hasChildNodesWithName(child1.nodeName()));
        assertTrue(parent.hasChildNodesWithName(child2.nodeName()));
    }

    /**
     * Test addChildren method.
     */
    @Test
    public void testAddChildren() {
        Node parent = new Element("parent", "", new Attributes());
        Node child1 = new Element("child1", "", new Attributes());
        Node child2 = new Element("child2", "", new Attributes());
        parent.addChildren(child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertTrue(parent.hasChildNodesWithName(child1.nodeName()));
        assertTrue(parent.hasChildNodesWithName(child2.nodeName()));
    }

    /**
     * Test addChildren method with index.
     */
    @Test
    public void testAddChildrenWithIndex() {
        Node parent = new Element("parent", "", new Attributes());
        Node child1 = new Element("child1", "", new Attributes());
        Node child2 = new Element("child2", "", new Attributes());
        parent.addChildren(1, child1, child2);
        assertEquals(2, parent.childNodes().size());
        assertTrue(parent.hasChildNodesWithName(child1.nodeName()));
        assertTrue(parent.hasChildNodesWithName(child2.nodeName()));
        assertEquals(child1, parent.childNode(1));
        assertEquals(child2, parent.childNode(2));
    }

    /**
     * Test outerHtml method.
     */
    @Test
    public void testOuterHtml() {
        Node node = new Element("test", "", new Attributes().put("testKey", "testValue"));
        StringBuilder accum = new StringBuilder();
        node.outerHtml(accum);
        String expected = "<test testkey=\"testValue\">\n";
        assertEquals(expected, accum.toString());

        // Test with indent
        Document document = new Document();
        document.setOutputSettings(new Document.OutputSettings().indentAmount(2));
        Element element = new Element("test", "", new Attributes().put("testKey", "testValue"));
        document.appendChild(element);
        StringBuilder accumIndent = new StringBuilder();
        element.outerHtml(accumIndent, 1, document.outputSettings());
        String expectedIndent = "\n<test testkey=\"testValue\">\n";
        assertEquals(expectedIndent, accumIndent.toString());
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        Node original = new Element("test", "", new Attributes().put("testKey", "testValue"));
        Node clone = original.clone();
        assertEquals(original.baseUri(), clone.baseUri());
        assertEquals(original.attributes(), clone.attributes());
        assertEquals(original.childNodes().size(), clone.childNodes().size());
        for (Node child : original.childNodes()) {
            Node childClone = clone.childNode(clone.childNodes().indexOf(child));
            assertEquals(child, childClone);
        }
    }
}