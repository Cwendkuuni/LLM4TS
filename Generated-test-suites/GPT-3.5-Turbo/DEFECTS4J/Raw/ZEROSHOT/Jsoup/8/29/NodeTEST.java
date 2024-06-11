import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class NodeTest {
    private Node testNode;

    @Before
    public void setUp() {
        testNode = new TestNode("http://www.example.com");
    }

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        Attributes attributes = new Attributes();
        Node node = new TestNode("http://www.example.com", attributes);
        assertThat(node.parentNode, is(nullValue()));
        assertThat(node.childNodes.size(), is(0));
        assertThat(node.attributes, is(sameInstance(attributes)));
        assertThat(node.baseUri, is("http://www.example.com"));
        assertThat(node.siblingIndex, is(0));
    }

    @Test
    public void testNodeConstructorWithBaseUri() {
        Node node = new TestNode("http://www.example.com");
        assertThat(node.parentNode, is(nullValue()));
        assertThat(node.childNodes.size(), is(0));
        assertThat(node.attributes.size(), is(0));
        assertThat(node.baseUri, is("http://www.example.com"));
        assertThat(node.siblingIndex, is(0));
    }

    @Test
    public void testNodeConstructorWithoutArguments() {
        Node node = new TestNode();
        assertThat(node.parentNode, is(nullValue()));
        assertThat(node.childNodes.size(), is(0));
        assertThat(node.attributes, is(nullValue()));
        assertThat(node.baseUri, is(nullValue()));
        assertThat(node.siblingIndex, is(0));
    }

    @Test
    public void testNodeAttr() {
        testNode.attributes.put("key", "value");
        String attrValue = testNode.attr("key");
        assertThat(attrValue, is("value"));
    }

    @Test
    public void testNodeAttrWithAbsolutePath() {
        testNode.attributes.put("abs:href", "http://www.example.com/page.html");
        String absUrl = testNode.attr("abs:href");
        assertThat(absUrl, is("http://www.example.com/page.html"));
    }

    @Test
    public void testNodeAttributes() {
        Attributes attributes = testNode.attributes();
        assertThat(attributes, is(sameInstance(testNode.attributes)));
    }

    @Test
    public void testNodeAttrWhenAttributeNotPresent() {
        String attrValue = testNode.attr("key");
        assertThat(attrValue, is(emptyString()));
    }

    @Test
    public void testNodeHasAttr() {
        testNode.attributes.put("key", "value");
        boolean hasAttr = testNode.hasAttr("key");
        assertThat(hasAttr, is(true));
    }

    @Test
    public void testNodeHasAttrWhenAttributeNotPresent() {
        boolean hasAttr = testNode.hasAttr("key");
        assertThat(hasAttr, is(false));
    }

    @Test
    public void testNodeRemoveAttr() {
        testNode.attributes.put("key", "value");
        testNode.removeAttr("key");
        boolean hasAttr = testNode.hasAttr("key");
        assertThat(hasAttr, is(false));
    }

    @Test
    public void testNodeBaseUri() {
        String baseUri = testNode.baseUri();
        assertThat(baseUri, is("http://www.example.com"));
    }

    @Test
    public void testNodeSetBaseUri() {
        testNode.setBaseUri("http://example.com");
        String baseUri = testNode.baseUri();
        assertThat(baseUri, is("http://example.com"));
    }

    @Test
    public void testNodeChildNode() {
        Node childNode = new TestNode();
        testNode.childNodes.add(childNode);
        Node retrievedNode = testNode.childNode(0);
        assertThat(retrievedNode, is(sameInstance(childNode)));
    }

    @Test
    public void testNodeChildNodes() {
        Node childNode1 = new TestNode();
        Node childNode2 = new TestNode();
        testNode.childNodes.add(childNode1);
        testNode.childNodes.add(childNode2);
        List<Node> childNodes = testNode.childNodes();
        assertThat(childNodes.size(), is(2));
        assertThat(childNodes.get(0), is(sameInstance(childNode1)));
        assertThat(childNodes.get(1), is(sameInstance(childNode2)));
    }

    @Test
    public void testNodeParent() {
        Node parentNode = new TestNode();
        testNode.parentNode = parentNode;
        Node retrievedParentNode = testNode.parent();
        assertThat(retrievedParentNode, is(sameInstance(parentNode)));
    }

    private static class TestNode extends Node {
        public TestNode(String baseUri, Attributes attributes) {
            super(baseUri, attributes);
        }

        public TestNode(String baseUri) {
            super(baseUri);
        }

        public TestNode() {
            super();
        }

        @Override
        public String nodeName() {
            return null;
        }

        @Override
        void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {

        }

        @Override
        void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {

        }
    }
}