
package net.sf.xisemele.impl;

import org.junit.Test;
import org.w3c.dom.*;
import java.util.*;

import static org.junit.Assert.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilder builder = DocumentBuilder.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");

        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        List<Node> children = helper.children(root);

        assertEquals(3, children.size());
        assertTrue(children.contains(child1));
        assertTrue(children.contains(child2));
        assertTrue(children.contains(child3));
    }

    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilder builder = DocumentBuilder.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");

        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        Node found = helper.find(document, "root/child1");

        assertNotNull(found);
        assertEquals("child1", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilder builder = DocumentBuilder.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");

        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        List<Node> nodes = new ArrayList<>();
        nodes.add(child1);
        nodes.add(child2);
        nodes.add(child3);

        Node found = helper.nodeWithName(nodes, "child2");

        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilder builder = DocumentBuilder.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        Element child3 = document.createElement("child");

        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        List<Node> nodes = Arrays.asList(child1, child2, child3);

        List<Node> sublist = helper.sublistWithName(nodes, "child");

        assertEquals(3, sublist.size());
        assertTrue(sublist.contains(child1));
        assertTrue(sublist.contains(child2));
        assertTrue(sublist.contains(child3));
    }
}
