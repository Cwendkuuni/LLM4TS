import org.jsoup.nodes.Node;
import org.jsoup.select.NodeTraversor;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NodeTest {

    private Node testNode;

    @Before
    public void setup() {
        testNode = new Node() {
            @Override
            public String nodeName() {
                return "TestNode";
            }

            @Override
            void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("<TestNode>");
            }

            @Override
            void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("</TestNode>");
            }
        };
    }

    @Test
    public void testNodeCreationWithBaseUriAndAttributes() {
        Node testNode = new Node("https://www.example.com", new Attributes());
        assertEquals("https://www.example.com", testNode.baseUri());
        assertTrue(testNode.attributes().isEmpty());
    }

    @Test
    public void testNodeCreationWithBaseUri() {
        Node testNode = new Node("https://www.example.com");
        assertEquals("https://www.example.com", testNode.baseUri());
        assertTrue(testNode.attributes().isEmpty());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node testNode = new Node();
        assertTrue(testNode.childNodes().isEmpty());
        assertNull(testNode.attributes());
    }

    @Test
    public void testNodeEqualsHashCode() {
        Node testNode2 = new Node("https://www.example2.com");
        assertFalse(testNode.equals(testNode2));
        assertNotEquals(testNode.hashCode(), testNode2.hashCode());
    }

    @Test
    public void testNodeClone() {
        Node clonedNode = testNode.clone();
        assertEquals(testNode.baseUri(), clonedNode.baseUri());
        assertEquals(testNode.attributes(), clonedNode.attributes());
        assertEquals(testNode.nodeName(), clonedNode.nodeName());
        assertNotSame(testNode, clonedNode);
    }

    @Test
    public void testNodeParentChildRelationship() {
        Node childNode = new Node("Child");
        testNode.addChildren(childNode);
        assertEquals(1, testNode.childNodes().size());
        assertEquals(childNode, testNode.childNode(0));
        assertEquals(testNode, childNode.parent());
    }

    @Test
    public void testNodeSiblingRelationship() {
        Node siblingNode = new Node("Sibling");
        testNode.addChildren(new Node("Child1"), siblingNode, new Node("Child2"));
        assertEquals(siblingNode, testNode.nextSibling());
        assertNull(testNode.previousSibling());
    }

    @Test
    public void testNodeRemove() {
        Node childNode = new Node("Child");
        testNode.addChildren(childNode);
        testNode.remove();
        assertNull(childNode.parent());
    }

    @Test
    public void testNodeReplaceWith() {
        Node newNode = new Node("NewNode");
        testNode.addChildren(new Node("Child1"), new Node("Child2"));
        testNode.replaceWith(newNode);
        assertNull(testNode.parent());
        assertEquals(newNode, testNode.nextSibling());
    }

    @Test
    public void testNodeAttrMethods() {
        testNode.attr("key1", "value1");
        assertTrue(testNode.hasAttr("key1"));
        assertEquals("value1", testNode.attr("key1"));
        testNode.removeAttr("key1");
        assertFalse(testNode.hasAttr("key1"));
    }

    @Test
    public void testNodeAbsUrl() {
        testNode.setBaseUri("https://www.example.com");
        testNode.attr("href", "/page");
        assertEquals("https://www.example.com/page", testNode.absUrl("href"));
    }

    @Test
    public void testNodeSiblingIndex() {
        testNode.addChildren(new Node("Child1"), new Node("Child2"), new Node("Child3"));
        assertEquals(Integer.valueOf(1), testNode.childNode(1).siblingIndex());
    }

    @Test
    public void testNodeToString() {
        String expectedString = "<TestNode><Child1></Child1><Child2></Child2><Child3></Child3></TestNode>";
        testNode.addChildren(new Node("Child1"), new Node("Child2"), new Node("Child3"));
        assertEquals(expectedString, testNode.toString());
    }

}