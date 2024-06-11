import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator attributeIterator;
    
    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        attributeIterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testGetNodePointer() {
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        Attr attribute = mock(Attr.class);
        NamedNodeMap map = mock(NamedNodeMap.class);
        
        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(name.getName()).thenReturn("*");
        when(node.getAttributes()).thenReturn(map);
        when(map.getLength()).thenReturn(1);
        when(map.item(0)).thenReturn(attribute);
        
        assertEquals(new DOMAttributePointer(parent, attribute), attributeIterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertEquals(true, attributeIterator.setPosition(1));
    }
}