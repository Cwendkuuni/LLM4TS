import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    
    private NodePointer parent;
    private QName name;
    private Document document;
    
    @Before
    public void setUp() {
        document = createTestDocument();
        Node rootNode = document.getFirstChild();
        parent = new DOMNodePointer(rootNode);
        name = new QName("attribute");
    }
    
    @Test
    public void testDOMAttributeIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);
    }
    
    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        int position = iterator.getPosition();
        
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.setPosition(1);
        
        assertTrue(result);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition_OutOfBounds() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.setPosition(5);
        
        assertFalse(result);
        assertEquals(5, iterator.getPosition());
    }
    
    private Document createTestDocument() {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);
        Attr attr1 = document.createAttribute("attribute");
        attr1.setValue("value1");
        Attr attr2 = document.createAttribute("attribute");
        attr2.setValue("value2");
        Attr attr3 = document.createAttribute("attribute3");
        attr3.setValue("value3");
        rootElement.setAttributeNode(attr1);
        rootElement.setAttributeNode(attr2);
        rootElement.setAttributeNode(attr3);
        return document;
    }
}