package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;
    private Document document;
    private Element rootElement;
    private Element childElement1;
    private Element childElement2;
    private Element grandchildElement1;
    private Element grandchildElement2;

    @Before
    public void setup() {
        operationsHelper = new OperationsHelperImpl();
        document = createDummyDocument();
    }

    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(rootElement);
        assertEquals(2, children.size());
        assertTrue(children.contains(childElement1));
        assertTrue(children.contains(childElement2));
    }

    @Test
    public void testChildren_NullNode() {
        List<Node> children = operationsHelper.children(null);
        assertEquals(0, children.size());
    }

    @Test
    public void testFind() {
        Node found = operationsHelper.find(document, "root/child1/grandchild1");
        assertEquals(grandchildElement1, found);
    }

    @Test
    public void testFind_RootNotMatch() {
        Node found = operationsHelper.find(document, "notRoot/child1");
        assertNull(found);
    }

    @Test
    public void testFind_ChildNotMatch() {
        Node found = operationsHelper.find(document, "root/notChild");
        assertNull(found);
    }

    @Test
    public void testFind_NullDocument() {
        Node found = operationsHelper.find(null, "root/child1");
        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(childElement1);
        nodes.add(childElement2);
        nodes.add(grandchildElement1);

        Node node = operationsHelper.nodeWithName(nodes, "child2");
        assertEquals(childElement2, node);
    }

    @Test
    public void testNodeWithName_NoMatch() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(childElement1);
        nodes.add(childElement2);
        nodes.add(grandchildElement1);

        Node node = operationsHelper.nodeWithName(nodes, "notNode");
        assertNull(node);
    }

    @Test
    public void testNodeWithName_NullList() {
        Node node = operationsHelper.nodeWithName(null, "child1");
        assertNull(node);
    }

    @Test
    public void testSublistWithName() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(childElement1);
        nodes.add(childElement2);
        nodes.add(grandchildElement1);

        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child2");
        assertEquals(1, sublist.size());
        assertEquals(childElement2, sublist.get(0));
    }

    @Test
    public void testSublistWithName_NoMatch() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(childElement1);
        nodes.add(childElement2);
        nodes.add(grandchildElement1);

        List<Node> sublist = operationsHelper.sublistWithName(nodes, "notChild");
        assertEquals(0, sublist.size());
    }

    @Test
    public void testSublistWithName_NullList() {
        List<Node> sublist = operationsHelper.sublistWithName(null, "child1");
        assertEquals(0, sublist.size());
    }

    private Document createDummyDocument() {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            rootElement = document.createElement("root");
            childElement1 = document.createElement("child1");
            childElement2 = document.createElement("child2");
            grandchildElement1 = document.createElement("grandchild1");
            grandchildElement2 = document.createElement("grandchild2");

            rootElement.appendChild(childElement1);
            rootElement.appendChild(childElement2);
            childElement1.appendChild(grandchildElement1);
            childElement1.appendChild(grandchildElement2);

            document.appendChild(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
}