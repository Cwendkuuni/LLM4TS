package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeTest {

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://example.com";
        String attributeKey = "class";
        String attributeValue = "newClass";
        String childNodeValue = "Child Node";
        String expectedOuterHtml = "<node class=\"newClass\">" + childNodeValue + "</node>";

        Node node = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "node";
            }

            @Override
            public String outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append("<node");
                attr("class", "oldClass");
                indent(accum, depth, out);
                accum.append(">");
                indent(accum, depth + 1, out);
            }

            @Override
            public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
                indent(accum, depth, out);
                accum.append("</node>");
            }
        };

        // Test basic methods
        assertEquals(baseUri, node.baseUri());
        assertEquals("oldClass", node.attr(attributeKey));
        assertTrue(node.childNodes().isEmpty());

        // Test setting attributes
        node.attr(attributeKey, attributeValue);
        assertEquals("newClass", node.attr(attributeKey));

        // Test adding children
        Node childNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "#text";
            }

            @Override
            public String outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
                // Empty for text node
            }

            @Override
            public void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {
                accum.append(childNodeValue);
            }
        };
        node.addChildren(childNode);
        assertEquals(1, node.childNodes().size());
        assertEquals(childNodeValue, node.childNodes().get(0).outerHtml());

        // Test setting baseUri
        node.setBaseUri("http://example.org");
        assertEquals("http://example.org", node.baseUri());
        assertEquals("http://example.org" + childNodeValue, node.childNodes().get(0).outerHtml());

        // Test hasAttr
        assertTrue(node.hasAttr(attributeKey));

        // Test removeAttr
        node.removeAttr(attributeKey);
        assertFalse(node.hasAttr(attributeKey));

        // Test absUrl
        assertEquals("http://example.com" + childNodeValue, node.absUrl(attributeKey));

        // Test getChildNode and getChildNodes
        Node childNode2 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "node2";
            }
        };
        node.addChildren(childNode2);
        Node childNode3 = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "node3";
            }
        };
        node.addChildren(1, childNode3);
        assertEquals(childNode3, node.childNodes().get(1));
        assertEquals(3, node.childNodes().size());

        // Test getParent, ownerDocument, and remove
        Node parentNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "parent";
            }
        };
        parentNode.addChildren(node);
        assertEquals(node, parentNode.childNodes().get(0));
        assertEquals(parentNode, node.parent());
        assertEquals(parentNode, node.ownerDocument());
        node.remove();
        assertFalse(parentNode.childNodes().contains(node));

        // Test replaceWith and setParentNode
        Node replacementNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "replacement";
            }
        };
        parentNode.replaceChild(node, replacementNode);
        assertEquals(replacementNode, parentNode.childNodes().get(0));
        node.setParentNode(parentNode);
        assertEquals(parentNode, node.parent());

        // Test equals and hashCode
        Node identicalNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "node";
            }
        };
        identicalNode.addChildren(childNode);
        Node notIdenticalNode = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "notNode";
            }
        };
        notIdenticalNode.addChildren(childNode);
        assertEquals(identicalNode, identicalNode);
        assertNotEquals(identicalNode, notIdenticalNode);
    }

    @Test
    public void testNodeWithMalformedURL() {
        String baseUri = "http://example.com";
        String attributeKey = "href";
        String attributeValue = "malformed://example.org";
        Node node = new Node(baseUri, new Attributes()) {
            @Override
            public String nodeName() {
                return "a";
            }

            @Override
            public String absUrl(String attributeKey) {
                return super.absUrl(attributeKey);
            }
        };
        node.attr(attributeKey, attributeValue);
        try {
            node.absUrl(attributeKey);
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // Expected
        }
    }

    @Test
    public void testNodeWithEmptyListOfChildren() {
        Node node = new Node();
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeWithNullAttributes() {
        Node node = new Node();
        assertNull(node.attributes());
    }

    @Test
    public void testNodeWithNullBaseUri() {
        Node node = new Node();
        assertNull(node.baseUri());
    }

    @Test
    public void testNodeWithNullParent() {
        Node node = new Node();
        assertNull(node.parent());
    }

    @Test
    public void testNodeWithNullOwnerDocument() {
        Node node = new Node();
        assertNull(node.ownerDocument());
    }

    @Test
    public void testNodeWithNullAttributesAndEmptyListOfChildren() {
        Node node = new Node();
        assertNull(node.attributes());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeWithNullParentAndEmptyListOfChildren() {
        Node node = new Node();
        assertNull(node.parent());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test
    public void testNodeWithNullParentAndNullAttributesAndEmptyListOfChildren() {
        Node node = new Node();
        assertNull(node.parent());
        assertNull(node.attributes());
        assertEquals(Collections.EMPTY_LIST, node.childNodes());
    }

    @Test(expected = NullPointerException.class)
    public void testNodeWithNullParentAndEmptyListOfChildrenAndNullAttributesThrowsNullPointerException() {
        Node node = new Node();
        assertNull(node.parent());
        assertNull(node.attributes());
        node.setParentNode(null);
    }

    @Test
    public void testNodeWithNullParentAndEmptyListOfChildrenAndNullAttributesDoesNotThrowNullPointerException() {
        Node node = new Node();
        assertNull(node.parent());
        assertNull(node.attributes());
        node.setParentNode(null);
    }

    @Test
    public void testNodeWithEmptyListOfChildrenAndNullAttributes() {
        Node node = new Node();
        assertNull(node.attributes());
        List<Node> childNodes = new ArrayList<>();
        node.addChildren(childNodes.toArray(new Node[0]));
        assertEquals(childNodes, node.childNodes());
    }

    @Test
    public void testNodeWithEmptyListOfChildrenAndNullAttributesDoesNotThrowNullPointerException() {
        Node node = new Node();
        List<Node> childNodes = new ArrayList<>();
        node.addChildren(childNodes.toArray(new Node[0]));
        assertEquals(childNodes, node.childNodes());
    }

    @Test
    public void testNodeWithEmptyListOfChildrenAndAttributes() {
        Node node = new Node();
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        node.attributes = attributes;
        List<Node> childNodes = new ArrayList<>();
        node.addChildren(childNodes.toArray(new Node[0]));
        assertEquals(childNodes, node.childNodes());
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testNodeWithEmptyListOfChildrenAndAttributesDoesNotThrowNullPointerException() {
        Node node = new Node();
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        node.attributes = attributes;
        List<Node> childNodes = new ArrayList<>();
        node.addChildren(childNodes.toArray(new Node[0]));
        assertEquals(childNodes, node.childNodes());
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testNodeWithListOfChildrenAndAttributes() {
        Node node = new Node();
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        node.attributes = attributes;

        List<Node> childNodes1 = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        node.addChildren(childNodes1.toArray(new Node[0]));

        List<Node> expectedChildNodes = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        assertEquals(expectedChildNodes, node.childNodes());
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testNodeWithListOfChildrenAndAttributesDoesNotThrowNullPointerException() {
        Node node = new Node();
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        node.attributes = attributes;

        List<Node> childNodes1 = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        node.addChildren(childNodes1.toArray(new Node[0]));

        List<Node> expectedChildNodes = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        assertEquals(expectedChildNodes, node.childNodes());
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testNodeWithNullAttributesAndListOfChildren() {
        Node node = new Node();
        List<Node> childNodes = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        node.addChildren(childNodes.toArray(new Node[0]));
        assertEquals(childNodes, node.childNodes());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeWithNullAttributesAndListOfChildrenDoesNotThrowNullPointerException() {
        Node node = new Node();
        List<Node> childNodes = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        node.addChildren(childNodes.toArray(new Node[0]));
        assertEquals(childNodes, node.childNodes());
        assertNull(node.attributes());
    }

    @Test
    public void testNodeWithAttributesAndListOfChildren() {
        Node node = new Node();
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        node.attributes = attributes;

        List<Node> childNodes1 = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        node.addChildren(childNodes1.toArray(new Node[0]));

        List<Node> expectedChildNodes = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        assertEquals(expectedChildNodes, node.childNodes());
        assertEquals("testValue", node.attr("testKey"));
    }

    @Test
    public void testNodeWithAttributesAndListOfChildrenDoesNotThrowNullPointerException() {
        Node node = new Node();
        Attributes attributes = new Attributes();
        attributes.put("testKey", "testValue");
        node.attributes = attributes;

        List<Node> childNodes1 = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        node.addChildren(childNodes1.toArray(new Node[0]));

        List<Node> expectedChildNodes = Arrays.asList(
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child1";
                    }
                },
                new Node() {
                    @Override
                    public String nodeName() {
                        return "child2";
                    }
                }
        );
        assertEquals(expectedChildNodes, node.childNodes());
        assertEquals("testValue", node.attr("testKey"));
    }
}