package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.hasItem;

public class NodeTest {
    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);

        Assert.assertThat(node.baseUri(), is(equalTo(baseUri)));
        Assert.assertThat(node.attributes(), is(notNullValue()));
        Assert.assertThat(node.attributes().hasKey("id"), is(true));
        Assert.assertThat(node.attributes().get("id"), is(equalTo("node1")));
    }

    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri);

        Assert.assertThat(node.baseUri(), is(equalTo(baseUri)));
        Assert.assertThat(node.attributes(), is(nullValue()));
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        Assert.assertThat(node.baseUri(), is(nullValue()));
        Assert.assertThat(node.attributes(), is(nullValue()));
        Assert.assertThat(node.childNodes(), is(Collections.EMPTY_LIST));
    }

    @Test
    public void testNodeGetNodeName() {
        Node node = new Node() {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Assert.assertThat(node.nodeName(), is(equalTo("testNode")));
    }

    @Test
    public void testNodeGetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "testClass");
        Node node = new Node(baseUri, attributes);

        Assert.assertThat(node.attr("id"), is(equalTo("node1")));
        Assert.assertThat(node.attr("class"), is(equalTo("testClass")));
        Assert.assertThat(node.attr("nonexistent"), is(equalTo("")));
        Assert.assertThat(node.attr("abs:href"), is(instanceOf(URL.class)));
    }

    @Test
    public void testNodeGetAllAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "testClass");
        Node node = new Node(baseUri, attributes);

        Assert.assertThat(node.attributes(), is(notNullValue()));
        Assert.assertThat(node.attributes().size(), is(equalTo(2)));
        Assert.assertThat(node.attributes().hasKey("id"), is(true));
        Assert.assertThat(node.attributes().hasKey("class"), is(true));
    }

    @Test
    public void testNodeSetAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("id", "node1");
        node.attr("class", "testClass");

        Assert.assertThat(node.attributes().size(), is(equalTo(2)));
        Assert.assertThat(node.attributes().hasKey("id"), is(true));
        Assert.assertThat(node.attributes().hasKey("class"), is(true));
        Assert.assertThat(node.attributes().get("id"), is(equalTo("node1")));
        Assert.assertThat(node.attributes().get("class"), is(equalTo("testClass")));
    }

    @Test
    public void testNodeHasAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);

        Assert.assertThat(node.hasAttr("id"), is(true));
        Assert.assertThat(node.hasAttr("class"), is(false));
    }

    @Test
    public void testNodeRemoveAttribute() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");
        Assert.assertThat(node.attributes().size(), is(equalTo(0)));
        Assert.assertThat(node.attributes().hasKey("id"), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testNodeSetAttributeWithNullAttributeKey() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNodeSetAttributeWithNullAttributeValue() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("key", null);
    }

    @Test
    public void testNodeGetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());

        Assert.assertThat(node.baseUri(), is(equalTo(baseUri)));
    }

    @Test
    public void testNodeSetBaseUri() {
        String baseUri = "http://example.com";
        Node node = new Node();

        node.setBaseUri(baseUri);
        Assert.assertThat(node.baseUri(), is(equalTo(baseUri)));
    }

    @Test(expected = NullPointerException.class)
    public void testNodeSetBaseUriWithNullBaseUri() {
        Node node = new Node();

        node.setBaseUri(null);
    }

    @Test
    public void testNodeGetAbsoluteUrlFromRelativeUrl() {
        String baseUri = "http://example.com";
        Node node = new Node(baseUri, new Attributes());

        Assert.assertThat(node.absUrl("href"), is(instanceOf(URL.class)));
        Assert.assertThat(node.absUrl("href").toString(), startsWith(baseUri));
    }

    @Test
    public void testNodeGetChildNodeByIndex() {
        Node parentNode = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parentNode.addChildren(child1, child2);

        Node child = parentNode.childNode(0);
        Assert.assertThat(child, is(equalTo(child1)));
    }

    @Test
    public void testNodeGetChildren() {
        Node parentNode = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parentNode.addChildren(child1, child2);

        List<Node> children = parentNode.childNodes();
        Assert.assertThat(children, hasItem(child1));
        Assert.assertThat(children, hasItem(child2));
    }

    @Test
    public void testNodeGetChildrenAsArray() {
        Node parentNode = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parentNode.addChildren(child1, child2);

        Node[] children = parentNode.childNodesAsArray();
        Assert.assertThat(children[0], is(equalTo(child1)));
        Assert.assertThat(children[1], is(equalTo(child2)));
    }

    @Test
    public void testNodeGetParentNode() {
        Node parentNode = new Node();
        Node child = new Node();
        child.setParentNode(parentNode);

        Assert.assertThat(child.parent(), is(equalTo(parentNode)));
    }

    @Test
    public void testNodeGetOwnerDocument() {
        Node child = new Node();
        Document document = new Document();
        child.setParentNode(document);

        Assert.assertThat(child.ownerDocument(), is(equalTo(document)));
    }

    @Test
    public void testNodeRemove() {
        Node parentNode = new Node();
        Node child = new Node();
        parentNode.addChildren(child);

        parentNode.remove(); // does nothing since parentNode is abstract
        Assert.assertThat(parentNode.childNodes().size(), is(equalTo(0)));
    }

    @Test
    public void testNodeReplaceWith() {
        Node parentNode = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parentNode.addChildren(child1);

        parentNode.replaceChild(child1, child2);
        Assert.assertThat(parentNode.childNodes().get(0), is(equalTo(child2)));
    }

    @Test
    public void testNodeSetParentNode() {
        Node child = new Node();
        Node parentNode = new Node();

        child.setParentNode(parentNode);
        Assert.assertThat(child.parentNode(), is(equalTo(parentNode)));
    }

    @Test
    public void testNodeReplaceChild() {
        Node parentNode = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parentNode.addChildren(child1);

        parentNode.replaceChild(child1, child2);
        Assert.assertThat(parentNode.childNodes().get(0), is(equalTo(child2)));
        Assert.assertThat(child1.parentNode(), is(nullValue()));
    }

    @Test
    public void testNodeRemoveChild() {
        Node parentNode = new Node();
        Node child1 = new Node();
        Node child2 = new Node();
        parentNode.addChildren(child1, child2);

        parentNode.removeChild(child1);
        Assert.assertThat(parentNode.childNodes().size(), is(equalTo(1)));
        Assert.assertThat(child1.parentNode(), is(nullValue()));
    }
}