import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
    
    private DOMAttributeIterator attributeIterator;
    private NodePointer parent;
    private QName name;
    private Node node;
    private Element element;
    private NodeList nodeList;
    private NamedNodeMap namedNodeMap;
    private Attr attr1;
    private Attr attr2;
    
    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        node = mock(Node.class);
        element = mock(Element.class);
        nodeList = mock(NodeList.class);
        namedNodeMap = mock(NamedNodeMap.class);
        attr1 = mock(Attr.class);
        attr2 = mock(Attr.class);
        
        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(name.getName()).thenReturn("testName");
        when(element.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getLength()).thenReturn(2);
        when(namedNodeMap.item(0)).thenReturn(attr1);
        when(namedNodeMap.item(1)).thenReturn(attr2);
        
        attributeIterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(attributeIterator);
        verify(parent, times(1)).getNode();
        verify(node, times(1)).getNodeType();
        verify(name, times(1)).getName();
    }
    
    @Test
    public void testConstructor_nonElementNode() {
        when(node.getNodeType()).thenReturn(Node.TEXT_NODE);
        
        attributeIterator = new DOMAttributeIterator(parent, name);
        
        assertNotNull(attributeIterator);
        verify(parent, times(1)).getNode();
        verify(node, times(1)).getNodeType();
        verify(name, times(0)).getName();
        verify(element, times(0)).getAttributes();
        verify(namedNodeMap, times(0)).getLength();
    }
    
    @Test
    public void testConstructor_nonMatchingAttribute() {
        when(attr1.getLocalName()).thenReturn("testAttr1");
        when(attr1.getPrefix()).thenReturn("testPrefix1");
        when(attr2.getLocalName()).thenReturn("testAttr2");
        when(attr2.getPrefix()).thenReturn("testPrefix2");
        
        attributeIterator = new DOMAttributeIterator(parent, name);
        
        assertNotNull(attributeIterator);
        verify(parent, times(1)).getNode();
        verify(node, times(1)).getNodeType();
        verify(name, times(1)).getName();
        verify(element, times(1)).getAttributes();
        verify(namedNodeMap, times(1)).getLength();
        verify(namedNodeMap, times(1)).item(0);
        verify(namedNodeMap, times(1)).item(1);
    }
    
    @Test
    public void testConstructor_matchingAttribute() {
        when(name.getName()).thenReturn("attr1");
        when(attr1.getLocalName()).thenReturn("attr1");
        
        attributeIterator = new DOMAttributeIterator(parent, name);
        
        assertNotNull(attributeIterator);
        verify(parent, times(1)).getNode();
        verify(node, times(1)).getNodeType();
        verify(name, times(1)).getName();
        verify(element, times(1)).getAttributes();
        verify(namedNodeMap, times(1)).getLength();
        verify(namedNodeMap, times(1)).item(0);
        verify(namedNodeMap, times(0)).item(1);
    }
    
    @Test
    public void testGetNodePointer() {
        DOMAttributePointer expectedPointer = mock(DOMAttributePointer.class);
        when(parent.getNamespaceResolver()).thenReturn(mock(NamespaceResolver.class));
        when(parent.getNamespaceURI(anyString())).thenReturn("testNamespace");
        when(attr1.getLocalName()).thenReturn("attr1");
        when(attr1.getPrefix()).thenReturn("attrPrefix");
        when(attributeIterator.setPosition(1)).thenReturn(true);
        when(attributeIterator.getNodePointer()).thenReturn(expectedPointer);
        
        NodePointer actualPointer = attributeIterator.getNodePointer();
        
        assertEquals(expectedPointer, actualPointer);
        verify(parent, times(2)).getNamespaceURI(anyString());
        verify(attr1, times(2)).getLocalName();
        verify(attr1, times(1)).getPrefix();
        verify(attributeIterator, times(1)).setPosition(1);
    }
    
    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();
        
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition_validPosition() {
        boolean result = attributeIterator.setPosition(1);
        
        assertTrue(result);
        assertEquals(1, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_invalidPosition() {
        boolean result = attributeIterator.setPosition(3);
        
        assertFalse(result);
        assertEquals(3, attributeIterator.getPosition());
    }
    
}