import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NodeTest {

    @Test
    public void testNodeConstructorWithBaseUriAndAttributes() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "elementId");
                    put("class", "testClass");
                }});
            }
        };

        assertEquals("div", node.nodeName());
        assertEquals("http://example.com", node.baseUri());
        assertEquals("elementId", node.attr("id"));
        assertEquals("testClass", node.attr("class"));
        assertTrue(node.hasAttr("id"));
        assertTrue(node.hasAttr("class"));
    }

    @Test
    public void testNodeConstructorWithBaseUriOnly() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
            }
        };

        assertEquals("div", node.nodeName());
        assertEquals("http://example.com", node.baseUri());
        assertNull(node.attributes());
    }

    @Test
    public void testDefaultNodeConstructor() {
        Node node = new Node();

        assertEquals("", node.nodeName());
        assertNull(node.baseUri());
        assertNull(node.attributes());
        assertTrue(node.childNodes().isEmpty());
    }

    @Test
    public void testGetNodeName() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "elementId");
                    put("class", "testClass");
                }});
            }
        };

        assertEquals("div", node.nodeName());
    }

    @Test
    public void testGetAttribute() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "elementId");
                }});
            }
        };

        assertEquals("elementId", node.attr("id"));
        assertEquals("", node.attr("nonExistentAttr"));
        assertNull(node.attr(null));
    }

    @Test
    public void testGetAttributes() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "elementId");
                    put("class", "testClass");
                }});
            }
        };

        Attributes attributes = node.attributes();
        assertEquals("elementId", attributes.get("id"));
        assertEquals("testClass", attributes.get("class"));
        assertTrue(attributes.hasKey("id"));
        assertTrue(attributes.hasKey("class"));
    }

    @Test
    public void testSetAttribute() {
        Node node = new Element("div");

        node.attr("id", "elementId");
        assertEquals("elementId", node.attr("id"));

        node.attr("class", "testClass");
        assertEquals("testClass", node.attr("class"));

        TestNode parentNode = new TestNode();
        node.setParentNode(parentNode);
        assertSame(parentNode, node.parent());
    }

    @Test
    public void testHasAttribute() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "elementId");
                }});
            }
        };

        assertTrue(node.hasAttr("id"));
        assertFalse(node.hasAttr("nonExistentAttr"));
    }

    @Test
    public void testRemoveAttribute() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "elementId");
                    put("class", "testClass");
                }});
            }
        };

        node.removeAttr("id");
        assertFalse(node.hasAttr("id"));

        node.removeAttr("class");
        assertFalse(node.hasAttr("class"));
    }

    @Test
    public void testGetBaseUri() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
            }
        };

        assertEquals("http://example.com", node.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testSetBaseUriWithNullBaseUri() {
        Node node = new Element("div") {
            {
                setBaseUri(null);
            }
        };
    }

    @Test
    public void testAbsUrl() {
        Node node = new Element("a") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("href", "/test.html");
                }});
            }
        };

        assertEquals("http://example.com/test.html", node.absUrl("href"));
        assertEquals("", node.absUrl("nonExistentAttr"));
        assertNull(node.absUrl(null));
    }

    @Test(expected = NullPointerException.class)
    public void testAbsUrlWithNullAttributeKey() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
            }
        };

        node.absUrl(null);
    }

    @Test
    public void testGetChildNode() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        Node childNode = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId");
                }});
                parentNode.appendChild(this);
            }
        };

        Node retrievedChildNode = parentNode.childNode(0);
        assertSame(childNode, retrievedChildNode);
    }

    @Test
    public void testGetChildren() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        Node childNode1 = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId1");
                }});
                parentNode.appendChild(this);
            }
        };

        Node childNode2 = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId2");
                }});
                parentNode.appendChild(this);
            }
        };

        List<Node> children = parentNode.childNodes();
        assertEquals(2, children.size());
        assertTrue(children.contains(childNode1));
        assertTrue(children.contains(childNode2));
    }

    @Test
    public void testGetChildrenAsArray() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        Node childNode1 = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId1");
                }});
                parentNode.appendChild(this);
            }
        };

        Node childNode2 = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId2");
                }});
                parentNode.appendChild(this);
            }
        };

        Node[] childrenArray = parentNode.childNodesAsArray();
        assertEquals(2, childrenArray.length);
        assertEquals(childNode1, childrenArray[0]);
        assertEquals(childNode2, childrenArray[1]);
    }

    @Test
    public void testGetParentNode() {
        Node childNode = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId");
                }});

                Node parentNode = new Element("div") {
                    {
                        setBaseUri("http://example.com");
                        setAttributes(new Attributes() {{
                            put("id", "parentId");
                        }});
                        childNode.setParentNode(this);
                    }
                };

                assertEquals(parentNode, childNode.parent());
            }
        };
    }

    @Test
    public void testGetOwnerDocument() {
        Node node = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "elementId");
                }});
            }
        };

        Document ownerDocument = node.ownerDocument();
        assertEquals(node.getClass(), ownerDocument.getClass());
    }

    @Test
    public void testRemove() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        Node nodeToRemove = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "nodeToRemove");
                }});
                parentNode.appendChild(this);
            }
        };

        parentNode.removeChild(nodeToRemove);
        assertFalse(parentNode.childNodes().contains(nodeToRemove));
    }

    @Test
    public void testReplaceWith() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        Node nodeToReplace = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "nodeToReplace");
                }});
                parentNode.appendChild(this);
            }
        };

        Node newNode = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "newNode");
                }});
            }
        };

        parentNode.replaceChild(nodeToReplace, newNode);
        assertFalse(parentNode.childNodes().contains(nodeToReplace));
        assertTrue(parentNode.childNodes().contains(newNode));
    }

    @Test
    public void testSetParentNode() {
        Node childNode = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId");
                }});
            }
        };

        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        childNode.setParentNode(parentNode);
        assertEquals(parentNode, childNode.parent());
    }

    @Test
    public void testReplaceChild() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        Node oldChildNode = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "oldChildId");
                }});
                parentNode.appendChild(this);
            }
        };

        Node newChildNode = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "newChildId");
                }});
            }
        };

        parentNode.replaceChild(oldChildNode, newChildNode);
        assertEquals(newChildNode, parentNode.childNode(oldChildNode.siblingIndex()));
    }

    @Test
    public void testRemoveChild() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "parentId");
                }});
            }
        };

        Node childNode = new Element("p") {
            {
                setBaseUri("http://example.com");
                setAttributes(new Attributes() {{
                    put("id", "childId");
                }});
                parentNode.appendChild(this);
            }
        };

        parentNode.removeChild(childNode);
        assertFalse(parentNode.childNodes().contains(childNode));
    }

    @Test
    public void testAddChildren() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
            }
        };

        Element childNode1 = new Element("p") {
            {
                setBaseUri("http://example.com");
            }
        };

        Element childNode2 = new Element("p") {
            {
                setBaseUri("http://example.com");
            }
        };

        parentNode.addChildren(childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertTrue(parentNode.childNodes().contains(childNode1));
        assertTrue(parentNode.childNodes().contains(childNode2));
    }

    @Test
    public void testAddChildrenWithIndex() {
        Node parentNode = new Element("div") {
            {
                setBaseUri("http://example.com");
            }
        };

        Element childNode1 = new Element("p") {
            {
                setBaseUri("http://example.com");
            }
        };

        Element childNode2 = new Element("p") {
            {
                setBaseUri("http://example.com");
            }
        };

        parentNode.addChildren(1, childNode1, childNode2);
        assertEquals(2, parentNode.childNodes().size());
        assertEquals(childNode1, parentNode.childNodes().get(1));
        assertEquals(childNode2, parentNode.childNodes().get(2));
    }

    private static class TestNode extends Node {
        @Override
        public String nodeName() {
            return "testNode";
        }
    }
}