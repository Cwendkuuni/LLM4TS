package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NodeTest {

    @Test
    public void testNodeConstructor() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertNotNull(node.attributes());
        Assert.assertEquals(Arrays.asList(), node.childNodes());
        Assert.assertNull(node.nodeName());
    }

    @Test
    public void testNodeConstructorWithAttributes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes().put("class", "example");
        Node node = new Node(baseUri, attributes);

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals("example", node.attr("class"));
        Assert.assertNotNull(node.attributes());
        Assert.assertEquals(Arrays.asList(), node.childNodes());
        Assert.assertNull(node.nodeName());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        Assert.assertEquals(Collections.EMPTY_LIST, node.childNodes());
        Assert.assertNull(node.attributes());
        Assert.assertNull(node.baseUri());
        Assert.assertNull(node.nodeName());
    }

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes().put("class", "example");
        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Assert.assertEquals(baseUri, node.baseUri());
        Assert.assertEquals("example", node.attr("class"));
        Assert.assertEquals(Collections.singletonList(node), node.childNodes());
        Assert.assertEquals("div", node.nodeName());
    }

    @Test
    public void testGetAttribute() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes().put("class", "example").put("id", "example-id");
        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Assert.assertEquals("example", node.attr("class"));
        Assert.assertEquals("example-id", node.attr("id"));
        Assert.assertEquals("", node.attr("nonexistent"));
        Assert.assertEquals("", node.attr("abs:href"));
    }

    @Test
    public void testGetAllAttributes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes().put("class", "example").put("id", "example-id");
        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        List<String> attributesList = new ArrayList<>(node.attributes().keySet());
        Assert.assertEquals(Arrays.asList("class", "id"), attributesList);
    }

    @Test
    public void testSetAttribute() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node node = new Node(baseUri, attributes);

        node.attr("class", "example");
        Assert.assertEquals("example", node.attr("class"));

        node.attr("id", "example-id");
        Assert.assertEquals("example-id", node.attr("id"));

        node.attr("nonexistent", "nonexistent-value");
        Assert.assertEquals("nonexistent-value", node.attr("nonexistent"));

        node.attr("abs:href", "http://www.another-example.com");
        Assert.assertEquals("http://www.another-example.com", node.absUrl("href"));
    }

    @Test
    public void testHasAttribute() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes().put("class", "example").put("id", "example-id");
        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Assert.assertTrue(node.hasAttr("class"));
        Assert.assertTrue(node.hasAttr("id"));
        Assert.assertFalse(node.hasAttr("nonexistent"));
    }

    @Test
    public void testRemoveAttribute() {
        String baseUri = "http://www.example.com";        Attributes attributes = new Attributes().put("class", "example").put("id", "example-id");
        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Assert.assertEquals("example", node.attr("class"));
        Assert.assertEquals("example-id", node.attr("id"));

        node.removeAttr("class");
        Assert.assertEquals("", node.attr("class"));

        node.removeAttr("id");
        Assert.assertEquals("", node.attr("id"));
    }

    @Test
    public void testGetBaseUri() {
        String baseUri = "http://www.example.com";
        Node node = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Assert.assertEquals(baseUri, node.baseUri());
    }

    @Test
    public void testSetBaseUri() {
        String baseUri = "http://www.example.com";
        String newBaseUri = "http://www.another-example.com";
        Node node = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Assert.assertEquals(baseUri, node.baseUri());

        node.setBaseUri(newBaseUri);
        Assert.assertEquals(newBaseUri, node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUriConstruction() {
        new Node(null, new Attributes());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributesConstruction() {
        new Node("http://www.example.com", null);
    }

    @Test
    public void testChildNodes() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node child1 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Node child2 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        parentNode.addChildren(child1, child2);

        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(child1, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));

        List<Node> childNodesArray = parentNode.childNodesAsArray();
        Assert.assertArrayEquals(new Node[]{child1, child2}, childNodesArray);

        List<Node> emptyChildNodes = parentNode.childNodes();
        Assert.assertEquals(0, emptyChildNodes.size());
    }

    @Test
    public void testGetParentNode() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        parentNode.setParentNode(null);
        Assert.assertNull(parentNode.parent());

        parentNode.setParentNode(childNode);
        Assert.assertEquals(childNode, parentNode.parent());
    }

    @Test
    public void testReplaceChild() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node child1 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Node child2 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        parentNode.addChildren(child1, child2);

        Node newChild = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "ul";
            }
        };

        parentNode.replaceChild(child1, newChild);

        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals(newChild, childNodes.get(0));
        Assert.assertEquals(child2, childNodes.get(1));
    }

    @Test
    public void testRemoveChild() {
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "div";
            }
        };

        Node child1 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        Node child2 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "p";
            }
        };

        parentNode.addChildren(child1, child2);

        parentNode.removeChild(child1);

        List<Node> childNodes = parentNode.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(child2, childNodes.get(0));

        List<Node> emptyChildNodes = parentNode.childNodes();
        Assert.assertEquals(0, emptyChildNodes.size());
    }
}