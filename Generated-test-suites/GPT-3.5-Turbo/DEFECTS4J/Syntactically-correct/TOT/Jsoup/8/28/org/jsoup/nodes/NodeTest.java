import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeTest {

    private Node testNode;

    @Before
    public void setup() {
        testNode = new TestNode("http://www.example.com", new Attributes());
    }

    @Test
    public void testNodeName() {
        assertEquals("TestNode", testNode.nodeName());
    }

    @Test
    public void testAttr() {
        testNode.attr("testKey", "testValue");
        assertEquals("testValue", testNode.attr("testKey"));
    }

    @Test
    public void testHasAttr() {
        assertFalse(testNode.hasAttr("testKey"));
        testNode.attr("testKey", "testValue");
        assertTrue(testNode.hasAttr("testKey"));
    }

    @Test
    public void testRemoveAttr() {
        testNode.attr("testKey", "testValue");
        assertTrue(testNode.hasAttr("testKey"));
        testNode.removeAttr("testKey");
        assertFalse(testNode.hasAttr("testKey"));
    }

    @Test
    public void testBaseUri() {
        assertEquals("http://www.example.com", testNode.baseUri());
        testNode.setBaseUri("http://www.newurl.com");
        assertEquals("http://www.newurl.com", testNode.baseUri());
    }

    @Test
    public void testAbsUrl() {
        testNode.attr("href", "/test");
        assertEquals("http://www.example.com/test", testNode.absUrl("href"));
    }

    @Test
    public void testChildNode() {
        Node child = new TestNode("http://www.example.com", new Attributes());
        testNode.addChildren(child);
        assertEquals(child, testNode.childNode(0));
    }

    @Test
    public void testParent() {
        assertNull(testNode.parent());
        Node parent = new TestNode("http://www.example.com", new Attributes());
        parent.addChildren(testNode);
        assertEquals(parent, testNode.parent());
    }

    @Test
    public void testSiblingNodes() {
        Node sibling1 = new TestNode("http://www.example.com", new Attributes());
        Node sibling2 = new TestNode("http://www.example.com", new Attributes());
        Node parent = new TestNode("http://www.example.com", new Attributes());
        parent.addChildren(sibling1, testNode, sibling2);
        assertEquals(3, testNode.siblingNodes().size());
        assertEquals(sibling1, testNode.siblingNodes().get(0));
        assertEquals(sibling2, testNode.siblingNodes().get(2));
    }

    private static class TestNode extends Node {
        public TestNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        @Override
        public String nodeName() {
            return "TestNode";
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

        }

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

        }
    }
}