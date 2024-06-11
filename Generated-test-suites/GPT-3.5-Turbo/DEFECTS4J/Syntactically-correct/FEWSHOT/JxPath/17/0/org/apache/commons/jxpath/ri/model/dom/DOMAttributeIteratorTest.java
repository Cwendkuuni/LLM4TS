import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;
    private Attr attr;

    @Before
    public void setup() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        NamedNodeMap map = mock(NamedNodeMap.class);
        attr = mock(Attr.class);

        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element.getAttributes()).thenReturn(map);
        when(map.getLength()).thenReturn(1);
        when(map.item(0)).thenReturn(attr);
        when(name.getName()).thenReturn("testName");
        when(name.getPrefix()).thenReturn("testPrefix");
        when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespaceURI");
        when(attr.getName()).thenReturn("testAttrName");
        when(attr.getPrefix()).thenReturn("testAttrPrefix");
        when(attr.getLocalName()).thenReturn("testAttrLocalName");
        when(parent.getNamespaceURI("testAttrPrefix")).thenReturn("testAttrNamespaceURI");
        when(iterator.testAttr(attr)).thenReturn(true);
        when(iterator.testAttr(attr)).thenReturn(true);

        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertEquals(1, iterator.attributes.size());
        assertEquals(attr, iterator.attributes.get(0));
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }

    @Test
    public void testGetNodePointer() {
        iterator.setPosition(1);
        DOMAttributePointer pointer = (DOMAttributePointer) iterator.getNodePointer();

        assertNotNull(pointer);
        assertEquals(parent, pointer.getParent());
        assertEquals(attr, pointer.getAttribute());
    }

}