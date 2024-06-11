package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl helper = new OperationsHelperImpl();
    
    @Test
    public void testChildren() {
        Document document = createDocument();
        NodeList nodeList = document.getChildNodes();
        
        List<Node> result = helper.children(nodeList.item(1));
        
        assertEquals(3, result.size());
        assertEquals("apple", result.get(0).getNodeName());
        assertEquals("banana", result.get(1).getNodeName());
        assertEquals("cherry", result.get(2).getNodeName());
    }
    
    @Test
    public void testFind() {
        Document document = createDocument();
        
        Node result1 = helper.find(document, "fruits");
        assertEquals("fruits", result1.getNodeName());
        
        Node result2 = helper.find(document, "fruits/banana");
        assertEquals("banana", result2.getNodeName());
        
        Node result3 = helper.find(document, "fruits/banana/color");
        assertEquals("color", result3.getNodeName());
        
        Node result4 = helper.find(document, "fruits/cherry");
        assertEquals("cherry", result4.getNodeName());
        
        Node result5 = helper.find(document, "fruits/cherry/hardness");
        assertEquals("hardness", result5.getNodeName());
        
        Node result6 = helper.find(document, "fruits/apple");
        assertNull(result6);
    }
    
    @Test
    public void testNodeWithName() {
        Document document = createDocument();
        NodeList nodeList = document.getChildNodes();
        
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }
        
        Node result1 = helper.nodeWithName(nodes, "fruits");
        assertEquals("fruits", result1.getNodeName());
        
        Node result2 = helper.nodeWithName(nodes, "banana");
        assertEquals("banana", result2.getNodeName());
        
        Node result3 = helper.nodeWithName(nodes, "color");
        assertNull(result3);
    }
    
    @Test
    public void testSublistWithName() {
        Document document = createDocument();
        NodeList nodeList = document.getChildNodes();
        
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            nodes.add(nodeList.item(i));
        }
        
        List<Node> result1 = helper.sublistWithName(nodes, "fruits");
        assertEquals(1, result1.size());
        assertEquals("fruits", result1.get(0).getNodeName());
        
        List<Node> result2 = helper.sublistWithName(nodes, "banana");
        assertEquals(1, result2.size());
        assertEquals("banana", result2.get(0).getNodeName());
        
        List<Node> result3 = helper.sublistWithName(nodes, "color");
        assertTrue(result3.isEmpty());
    }
    
    private Document createDocument() {
        Document document = null; // Create document object
        return document;
    }
}