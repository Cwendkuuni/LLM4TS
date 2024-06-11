package net.sf.xisemele.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    
    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element root = document.createElement("root");
        document.appendChild(root);
        
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
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        
        Node found = helper.find(document, "root/child1");
        
        assertNotNull(found);
        assertEquals(child1, found);
        
        found = helper.find(document, "root/child3");
        
        assertNotNull(found);
        assertEquals(child3, found);
        
        found = helper.find(document, "root/child4");
        
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        
        root.appendChild(child1);
        root.appendChild(child2);
        
        List<Node> children = helper.children(root);
        
        Node node = helper.nodeWithName(children, "child");
        
        assertNotNull(node);
        assertTrue(node == child1 || node == child2);
    }
    
    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        Element child3 = document.createElement("child3");
        
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        
        List<Node> children = helper.children(root);
        
        List<Node> sublist = helper.sublistWithName(children, "child");
        
        assertEquals(2, sublist.size());
        assertTrue(sublist.contains(child1));
        assertTrue(sublist.contains(child2));
    }
}