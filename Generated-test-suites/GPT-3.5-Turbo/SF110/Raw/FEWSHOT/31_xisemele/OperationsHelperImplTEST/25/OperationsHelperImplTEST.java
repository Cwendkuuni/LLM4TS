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

import java.util.List;

public class OperationsHelperImplTest {
    private OperationsHelperImpl operationsHelper;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
    }

    @Test
    public void testChildren() {
        Document document = createDocument();
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        document.appendChild(rootElement);

        List<Node> children = operationsHelper.children(document.getDocumentElement());

        assertEquals(2, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
    }

    @Test
    public void testFind() {
        Document document = createDocument();
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element grandchild = document.createElement("grandchild");
        child1.appendChild(grandchild);
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        document.appendChild(rootElement);

        Node found = operationsHelper.find(document, "root/child1/grandchild");

        assertEquals(grandchild, found);
    }

    @Test
    public void testFind_NotFound() {
        Document document = createDocument();
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element grandchild = document.createElement("grandchild");
        child1.appendChild(grandchild);
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        document.appendChild(rootElement);

        Node found = operationsHelper.find(document, "root/child1/grandchild2");

        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        document.appendChild(rootElement);
        List<Node> children = operationsHelper.children(document.getDocumentElement());

        Node nodeWithName = operationsHelper.nodeWithName(children, "child2");

        assertEquals(child2, nodeWithName);
    }

    @Test
    public void testNodeWithName_NotFound() {
        Document document = createDocument();
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        document.appendChild(rootElement);
        List<Node> children = operationsHelper.children(document.getDocumentElement());

        Node nodeWithName = operationsHelper.nodeWithName(children, "child3");

        assertNull(nodeWithName);
    }

    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        Element rootElement = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child2");
        rootElement.appendChild(child1);
        rootElement.appendChild(child2);
        rootElement.appendChild(child3);
        document.appendChild(rootElement);
        List<Node> children = operationsHelper.children(document.getDocumentElement());

        List<Node> sublistWithName = operationsHelper.sublistWithName(children, "child2");

        assertEquals(2, sublistWithName.size());
        assertTrue(sublistWithName.contains(child2));
        assertTrue(sublistWithName.contains(child3));
    }

    private Document createDocument() {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }
}