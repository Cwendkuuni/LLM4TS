import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Node node = mock(Node.class);
        when(parent.getNode()).thenReturn(node);
        Element element = mock(Element.class);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node instanceof Element).thenReturn(true);
        when(element.getAttributeNode(anyString())).thenReturn(null);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(parent.getNamespaceURI(anyString())).thenReturn(null);
        when(parent.getNamespaceResolver()).thenReturn(mock(NamespaceResolver.class));
        iterator.attributes.add(mock(Attr.class));
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
    }

}