package org.jsoup.nodes; // Adjust the package name if necessary

import static org.junit.Assert.*;

import org.jsoup.nodes.*;
import org.jsoup.helper.*;
import org.jsoup.select.*;

import org.junit.Test;

public class NodeTest {
    

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertEquals("node1", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertEquals(attributes, node.attributes());
    }

    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        String baseUri = "http://example.com";

        Node node = new Node(baseUri);

        assertEquals("", node.nodeName());
        assertEquals(baseUri, node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeDefaultConstructor() {
        Node node = new Node();

        assertEquals(Collections.EMPTY_LIST, node.childNodes());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertEquals("id", node.attr("id"));
        assertEquals("", node.attr("nonExistent"));
        assertEquals("", node.attr("abs:href"));

        node.attr("class", "testClass");
        assertEquals("testClass", node.attr("class"));
    }

    @Test
    public void testNodeAttributesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");
        attributes.put("class", "testClass");

        Node node = new Node(baseUri, attributes);

        Attributes attributesTest = node.attributes();

        assertEquals("id", attributesTest.get("id"));
        assertEquals("testClass", attributesTest.get("class"));
        assertEquals(2, attributesTest.size());
    }

    @Test
    public void testNodeHasAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistent"));
    }

    @Test
    public void testNodeRemoveAttrMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "node1");

        Node node = new Node(baseUri, attributes);

        node.removeAttr("id");

        assertFalse(node.hasAttr("id"));
    }

    @Test
    public void testNodeAbsUrlMethodWithAbsoluteUrl() {
        String baseUri = "http://example.com";
        Node node = new Elements.TextNode(baseUri + "/path/to/image.jpg");

        assertEquals(baseUri + "/path/to/image.jpg", node.absUrl("src"));
    }

    @Test
    public void testNodeAbsUrlMethodWithRelativeUrl() {
        String baseUri = "http://example.com/index.html";
        Node node = new Elements.TextNode("img src=\"image.jpg\"");

        node.setBaseUri(baseUri);

        assertEquals(baseUri + "/image.jpg", node.absUrl("src"));
    }

    @Test(expected = MalformedURLException.class)
    public void testNodeAbsUrlMethodWithInvalidUrl() {
        String baseUri = "http://example.com";
        Node node = new Elements.TextNode("img src=\"invalid:url\"");

        node.setBaseUri(baseUri);

        node.absUrl("src");
    }

    @Test
    public void testNodeChildNodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "parent";
            }
        };

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "child";
            }
        };

        parentNode.addChildren(childNode);

        Node child = parentNode.childNode(0);

        assertEquals("child", child.nodeName());
    }

    @Test
    public void testNodeChildNodesMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "parent";
            }
        };

        Node childNode1 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "child1";
            }
        };

        Node childNode2 = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "child2";
            }
        };

        parentNode.addChildren(childNode1, childNode2);

        List<Node> children = parentNode.childNodes();

        assertEquals(2, children.size());
        Node child1 = children.get(0);
        Node child2 = children.get(1);

        assertEquals("child1", child1.nodeName());
        assertEquals("child2", child2.nodeName());
    }

    @Test
    public void testNodeParentMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "child";
            }
        };

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "parent";
            }
        };

        parentNode.setParentNode(childNode);

        Node parent = childNode.parent();

        assertEquals("parent", parent.nodeName());
    }

    @Test
    public void testNodeOwnerDocumentMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node node = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "testNode";
            }
        };

        Node document = new Document();
        document.setElements(new Elements());
        document.setBaseUri(baseUri);
        document.setRootElement(node);

        assertEquals(document, node.ownerDocument());
    }

    @Test
    public void testNodeRemoveMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "parent";
            }
        };

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "child";
            }
        };

        parentNode.addChildren(childNode);

        parentNode.remove();

        List<Node> children = parentNode.childNodes();

        assertEquals(0, children.size());
    }

    @Test
    public void testNodeReplaceWithMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "parent";
            }
        };

        Node oldChildNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "oldChild";
            }
        };

        Node newChildNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "newChild";
            }
        };

        parentNode.addChildren(oldChildNode);

        parentNode.replaceWith(newChildNode);

        List<Node> children = parentNode.childNodes();

        assertEquals(1, children.size());
        Node newChild = children.get(0);

        assertEquals("newChild", newChild.nodeName());
    }

    @Test
    public void testNodeSetParentNodeMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node childNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "child";
            }
        };

        Node parentNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "parent";
            }
        };

        childNode.setParentNode(parentNode);

        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testNodeSetSiblingIndexMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        Node firstNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "first";
            }
        };

        Node secondNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "second";
            }
        };

        Node thirdNode = new Node(baseUri, attributes) {
            @Override
            public String nodeName() {
                return "third";
            }
        };

        List<Node> nodes = new ArrayList<>();
        nodes.add(firstNode);
        nodes.add(secondNode);
        nodes.add(thirdNode);

        firstNode.setSiblingIndex(0);
        secondNode.setSiblingIndex(1);
        thirdNode.setSiblingIndex(2);

        assertEquals(0, firstNode.siblingIndex());
        assertEquals(1, secondNode.siblingIndex());
        assertEquals(2, thirdNode.siblingIndex());
    }

    @Test
    public void testNodeOuterHtmlMethod() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "testNode");

        Node node = new Elements.TextNode("Test Node", baseUri, attributes);

        StringBuilder expected = new StringBuilder();
        expected.append("<text node=\"testNode\">Test Node</text>");

        StringBuilder actual = new StringBuilder();
        node.outerHtml(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    
}