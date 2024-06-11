import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;

public class DOMAttributeIteratorTest {

    @Mock
    private NodePointer parent;

    @Mock
    private QName name;

    @Mock
    private Node node;

    @Mock
    private Element element;

    @Mock
    private Attr attr;

    @Mock
    private NamedNodeMap map;

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        node = mock(Node.class);
        element = mock(Element.class);
        attr = mock(Attr.class);
        map = mock(NamedNodeMap.class);
        iterator = new DOMAttributeIterator(parent, name);

        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(name.getName()).thenReturn("*");
        when(element.getAttributeNodeNS(null, "*")).thenReturn(attr);
        when(node.getAttributes()).thenReturn(map);
        when(map.getLength()).thenReturn(1);
        when(map.item(0)).thenReturn(attr);
        when(parent.getNamespaceURI(null)).thenReturn(null);
        when(DOMNodePointer.getPrefix(attr)).thenReturn(null);
        when(DOMNodePointer.getLocalName(attr)).thenReturn("attr");
        when(name.getPrefix()).thenReturn(null);
        when(parent.getNamespaceResolver().getNamespaceURI(null)).thenReturn(null);
        when(element.getAttributeNode("attr")).thenReturn(attr);
    }

    @Test
    public void testConstructor() {
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(DOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        assertEquals(true, result);
    }
}