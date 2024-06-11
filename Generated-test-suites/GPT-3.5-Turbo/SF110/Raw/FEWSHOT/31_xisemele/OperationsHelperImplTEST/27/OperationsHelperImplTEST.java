package net.sf.xisemele.impl;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl operationsHelper;
    private Document document;
    private Element element1;
    private Element element2;
    private Element element3;
    
    @Before
    public void setup() {
        operationsHelper = new OperationsHelperImpl();
        document = createSampleDocument();
    }
    
    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(document);
        assertEquals(3, children.size());
        assertEquals(element1, children.get(0));
        assertEquals(element2, children.get(1));
        assertEquals(element3, children.get(2));
    }
    
    @Test
    public void testFind() {
        Node found = operationsHelper.find(document, "root/child1/child2");
        assertEquals(element3, found);
        
        found = operationsHelper.find(document, "root/child1/child3");
        assertNull(found);
        
        found = operationsHelper.find(document, "root/child4");
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName() {
        List<Node> nodes = operationsHelper.children(document);
        
        Node node = operationsHelper.nodeWithName(nodes, "child2");
        assertEquals(element3, node);
        
        node = operationsHelper.nodeWithName(nodes, "child4");
        assertNull(node);
    }
    
    @Test
    public void testSublistWithName() {
        List<Node> nodes = operationsHelper.children(document);
        
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
        assertEquals(2, sublist.size());
        assertEquals(element1, sublist.get(0));
        assertEquals(element2, sublist.get(1));
        
        sublist = operationsHelper.sublistWithName(nodes, "child3");
        assertEquals(0, sublist.size());
    }
    
    private Document createSampleDocument() {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            
            Element root = document.createElement("root");
            document.appendChild(root);
            
            element1 = document.createElement("child1");
            root.appendChild(element1);
            
            Element element11 = document.createElement("child2");
            element1.appendChild(element11);
            
            element2 = document.createElement("child1");
            root.appendChild(element2);
            
            Element element21 = document.createElement("child2");
            element2.appendChild(element21);
            
            element3 = document.createElement("child2");
            element2.appendChild(element3);
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        
        return document;
    }
}