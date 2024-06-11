
package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationsHelperImplTest {
    
    @Test
    public void testChildren() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        assertEquals(2, nodeList.getLength());
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                children.add(node);
            }
        }
        assertEquals(2, children.size());
    }
    
    @Test
    public void testFind() {
        Document document = createDocument();
        Node found = new OperationsHelperImpl().find(document, "root/child1");
        assertEquals("child1Value", found.getTextContent());
        
        found = new OperationsHelperImpl().find(document, "root/child2");
        assertEquals("child2Value", found.getTextContent());
        
        found = new OperationsHelperImpl().find(document, "root");
        assertEquals("rootValue", found.getTextContent());
        
        found = new OperationsHelperImpl().find(document, "nonexistent");
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                nodes.add(node);
            }
        }

        Node nodeWithName = new OperationsHelperImpl().nodeWithName(nodes, "child1");
        assertEquals("child1Value", nodeWithName.getTextContent());
        
        nodeWithName = new OperationsHelperImpl().nodeWithName(nodes, "child2");
        assertEquals("child2Value", nodeWithName.getTextContent());
    }
    
    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                nodes.add(node);
            }
        }
        
        List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, "child1");
        assertEquals(1, sublist.size());
        assertEquals("child1Value", sublist.get(0).getTextContent());
        
        sublist = new OperationsHelperImpl().sublistWithName(nodes, "child2");
        assertEquals(1, sublist.size());
        assertEquals("child2Value", sublist.get(0).getTextContent());
    }

    private Document createDocument() {
        Document document = new org.apache.xerces.dom.DocumentImpl();
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Element child1 = document.createElement("child1");
        child1.setTextContent("child1Value");
        Element child2 = document.createElement("child2");
        child2.setTextContent("child2Value");
        
        root.appendChild(child1);
        root.appendChild(child2);
        
        return document;
    }
}
