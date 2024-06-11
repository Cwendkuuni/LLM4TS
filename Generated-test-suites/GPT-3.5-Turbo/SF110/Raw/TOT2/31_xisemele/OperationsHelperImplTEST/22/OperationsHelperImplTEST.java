package net.sf.xisemele.impl;

import org.w3c.dom.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    private Document createTestDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.newDocument();
    }

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        root.appendChild(child1);
        document.appendChild(root);

        List<Node> children = helper.children(root);

        assertEquals(1, children.size());
        assertEquals(child1, children.get(0));
    }

    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        root.appendChild(child1);
        document.appendChild(root);

        Node found = helper.find(document, "root/child1");

        assertEquals(child1, found);
    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);
        document.appendChild(root);

        List<Node> nodes = Arrays.asList(child1, child2);

        Node result = helper.nodeWithName(nodes, "child1");

        assertEquals(child1, result);
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child1");
        root.appendChild(child1);
        root.appendChild(child2);
        document.appendChild(root);

        List<Node> nodes = Arrays.asList(child1, child2);

        List<Node> sublist = helper.sublistWithName(nodes, "child1");

        assertEquals(2, sublist.size());
        assertEquals(Arrays.asList(child1, child2), sublist);
    }
}