package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    private Document document;
    private Node node;
    private OperationsHelperImpl operationsHelper;

    @Before
    public void setup() {
        document = createMockDocument();
        node = createMockNode(document);
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(node);

        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
    }

    @Test
    public void testFind_whenPathExists() {
        Node found = operationsHelper.find(document, "root/child2");

        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testFind_whenPathDoesNotExist() {
        Node found = operationsHelper.find(document, "root/child3");

        assertNull(found);
    }

    @Test
    public void testNodeWithName_whenNodeExists() {
        List<Node> nodes = operationsHelper.children(node);
        Node found = operationsHelper.nodeWithName(nodes, "child2");

        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testNodeWithName_whenNodeDoesNotExist() {
        List<Node> nodes = operationsHelper.children(node);
        Node found = operationsHelper.nodeWithName(nodes, "child3");

        assertNull(found);
    }

    @Test
    public void testSublistWithName_whenSublistExists() {
        List<Node> nodes = operationsHelper.children(node);
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child2");

        assertEquals(1, sublist.size());
        assertEquals("child2", sublist.get(0).getNodeName());
    }

    @Test
    public void testSublistWithName_whenSublistDoesNotExist() {
        List<Node> nodes = operationsHelper.children(node);
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child3");

        assertEquals(0, sublist.size());
    }

    private Document createMockDocument() {
        Document document = new Document() {
            @Override
            public String getNodeName() {
                return "root";
            }

            @Override
            public NodeList getChildNodes() {
                List<Node> children = new ArrayList<>();
                children.add(createMockNode(this));
                children.add(createMockNode(this));
                return new NodeList() {
                    @Override
                    public Node item(int index) {
                        return children.get(index);
                    }

                    @Override
                    public int getLength() {
                        return children.size();
                    }
                };
            }
        };

        return document;
    }

    private Node createMockNode(final Document document) {
        return new Node() {
            @Override
            public String getNodeName() {
                return "child";
            }

            @Override
            public NodeList getChildNodes() {
                List<Node> children = new ArrayList<>();
                children.add(createMockNode(document));
                children.add(createMockNode(document));
                return new NodeList() {
                    @Override
                    public Node item(int index) {
                        return children.get(index);
                    }

                    @Override
                    public int getLength() {
                        return children.size();
                    }
                };
            }
        };
    }
}