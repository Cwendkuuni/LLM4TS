package net.sf.xisemele.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl helper;
    private Document document;
    private Element root;
    private Element child1;
    private Element child2;
    
    @Before
    public void setup() {
        helper = new OperationsHelperImpl();
        document = DocumentBuilder.newDocument();
        root = document.createElement("root");
        document.appendChild(root);
        child1 = document.createElement("child1");
        root.appendChild(child1);
        child2 = document.createElement("child2");
        root.appendChild(child2);
    }
    
    @Test
    public void testChildren() {
        List<Node> expectedChildren = new ArrayList<>();
        expectedChildren.add(child1);
        expectedChildren.add(child2);
        
        List<Node> children = helper.children(root);
        
        Assert.assertEquals(expectedChildren, children);
        
        // Testing an empty node
        List<Node> emptyChildren = helper.children(child1);
        
        Assert.assertTrue(emptyChildren.isEmpty());
    }
    
    @Test
    public void testFind() {
        Node foundNode = helper.find(document, "root");
        
        Assert.assertEquals(root, foundNode);
        
        foundNode = helper.find(document, "root/child1");
        
        Assert.assertEquals(child1, foundNode);
        
        foundNode = helper.find(document, "root/child2");
        
        Assert.assertEquals(child2, foundNode);
        
        // Testing invalid path
        foundNode = helper.find(document, "invalidPath");
        
        Assert.assertNull(foundNode);
    }
    
    @Test
    public void testNodeWithName() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(child1);
        nodes.add(child2);
        
        Node node = helper.nodeWithName(nodes, "child1");
        
        Assert.assertEquals(child1, node);
        
        node = helper.nodeWithName(nodes, "child2");
        
        Assert.assertEquals(child2, node);
        
        // Testing not found node
        node = helper.nodeWithName(nodes, "invalidNode");
        
        Assert.assertNull(node);
    }
    
    @Test
    public void testSublistWithName() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(child1);
        nodes.add(child2);
        nodes.add(root);
        
        List<Node> sublist = helper.sublistWithName(nodes, "child1");
        
        Assert.assertEquals(1, sublist.size());
        Assert.assertEquals(child1, sublist.get(0));
        
        sublist = helper.sublistWithName(nodes, "root");
        
        Assert.assertEquals(1, sublist.size());
        Assert.assertEquals(root, sublist.get(0));
        
        // Testing when no nodes match the name
        sublist = helper.sublistWithName(nodes, "invalidName");
        
        Assert.assertTrue(sublist.isEmpty());
    }
}