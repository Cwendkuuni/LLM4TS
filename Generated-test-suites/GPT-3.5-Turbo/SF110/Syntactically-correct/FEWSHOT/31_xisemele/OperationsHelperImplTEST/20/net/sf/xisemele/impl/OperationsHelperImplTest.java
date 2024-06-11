package net.sf.xisemele.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.util.*;

public class OperationsHelperImplTest {
    
    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        Document document = createDocument();
        Node node = document.getDocumentElement();
        List<Node> children = helper.children(node);
        assertEquals(3, children.size());
        assertEquals("child1", children.get(0).getNodeName());
        assertEquals("child2", children.get(1).getNodeName());
        assertEquals("child3", children.get(2).getNodeName());
        
        node = children.get(0);
        children = helper.children(node);
        assertEquals(2, children.size());
        assertEquals("grandchild1", children.get(0).getNodeName());
        assertEquals("grandchild2", children.get(1).getNodeName());
        
        node = children.get(1);
        children = helper.children(node);
        assertEquals(1, children.size());
        assertEquals("greatgrandchild", children.get(0).getNodeName());
        
        node = children.get(0);
        children = helper.children(node);
        assertEquals(0, children.size());
    }
    
    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        Document document = createDocument();
        Node found = helper.find(document, "root");
        assertNotNull(found);
        assertEquals("root", found.getNodeName());
        
        found = helper.find(document, "root/child1");
        assertNotNull(found);
        assertEquals("child1", found.getNodeName());
        
        found = helper.find(document, "root/child2");
        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
        
        found = helper.find(document, "root/child3");
        assertNotNull(found);
        assertEquals("child3", found.getNodeName());
        
        found = helper.find(document, "root/child1/grandchild1");
        assertNotNull(found);
        assertEquals("grandchild1", found.getNodeName());
        
        found = helper.find(document, "root/child1/grandchild2");
        assertNotNull(found);
        assertEquals("grandchild2", found.getNodeName());
        
        found = helper.find(document, "root/child1/grandchild2/greatgrandchild");
        assertNotNull(found);
        assertEquals("greatgrandchild", found.getNodeName());
        
        found = helper.find(document, "root/child3/child4");
        assertNull(found);
        
        found = helper.find(document, "root/child4");
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        Document document = createDocument();
        Node root = document.getDocumentElement();
        List<Node> children = helper.children(root);
        Node node = helper.nodeWithName(children, "child1");
        assertNotNull(node);
        assertEquals("child1", node.getNodeName());
        
        node = helper.nodeWithName(children, "child2");
        assertNotNull(node);
        assertEquals("child2", node.getNodeName());
        
        node = helper.nodeWithName(children, "child3");
        assertNotNull(node);
        assertEquals("child3", node.getNodeName());
        
        children = helper.children(node);
        node = helper.nodeWithName(children, "grandchild1");
        assertNotNull(node);
        assertEquals("grandchild1", node.getNodeName());
        
        node = helper.nodeWithName(children, "grandchild2");
        assertNotNull(node);
        assertEquals("grandchild2", node.getNodeName());
        
        children = helper.children(node);
        node = helper.nodeWithName(children, "greatgrandchild");
        assertNotNull(node);
        assertEquals("greatgrandchild", node.getNodeName());
        
        node = helper.nodeWithName(children, "greatgrandchild2");
        assertNull(node);
        
        node = helper.nodeWithName(children, "greatgrandchild3");
        assertNull(node);
    }
    
    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        
        Document document = createDocument();
        Node root = document.getDocumentElement();
        List<Node> children = helper.children(root);
        List<Node> sublist = helper.sublistWithName(children, "child1");
        assertEquals(1, sublist.size());
        assertEquals("child1", sublist.get(0).getNodeName());
        
        sublist = helper.sublistWithName(children, "child2");
        assertEquals(1, sublist.size());
        assertEquals("child2", sublist.get(0).getNodeName());
        
        sublist = helper.sublistWithName(children, "child3");
        assertEquals(1, sublist.size());
        assertEquals("child3", sublist.get(0).getNodeName());
        
        children = helper.children(children.get(0));
        sublist = helper.sublistWithName(children, "grandchild1");
        assertEquals(1, sublist.size());
        assertEquals("grandchild1", sublist.get(0).getNodeName());
        
        sublist = helper.sublistWithName(children, "grandchild2");
        assertEquals(1, sublist.size());
        assertEquals("grandchild2", sublist.get(0).getNodeName());
        
        children = helper.children(children.get(0));
        sublist = helper.sublistWithName(children, "greatgrandchild");
        assertEquals(1, sublist.size());
        assertEquals("greatgrandchild", sublist.get(0).getNodeName());
        
        sublist = helper.sublistWithName(children, "greatgrandchild2");
        assertEquals(0, sublist.size());
        
        sublist = helper.sublistWithName(children, "greatgrandchild3");
        assertEquals(0, sublist.size());
    }

    private Document createDocument() {
        // create and return custom Document object
    }
}