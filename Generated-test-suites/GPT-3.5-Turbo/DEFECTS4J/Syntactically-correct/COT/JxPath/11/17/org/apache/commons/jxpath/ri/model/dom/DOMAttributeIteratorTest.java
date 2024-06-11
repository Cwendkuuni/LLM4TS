
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    // Test fixture
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        name = new QName("test", "attr");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        Node node = Mockito.mock(Node.class);
        Element element = Mockito.mock(Element.class);
        NamedNodeMap attributes = Mockito.mock(NamedNodeMap.class);
        Attr attr1 = Mockito.mock(Attr.class);
        attr1.setPrefix("test");
        attr1.setLocalName("attr");
        Attr attr2 = Mockito.mock(Attr.class);
        attr2.setPrefix("test");
        attr2.setLocalName("attr2");
        Attr attr3 = Mockito.mock(Attr.class);
        attr3.setPrefix("prefix");
        attr3.setLocalName("attr");

        Mockito.when(parent.getNode()).thenReturn(node);
        Mockito.when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        Mockito.when((Element) node).thenReturn(element);
        Mockito.when(element.getAttributes()).thenReturn(attributes);
        Mockito.when(attributes.getLength()).thenReturn(3);
        Mockito.when(attributes.item(0)).thenReturn(attr1);
        Mockito.when(attributes.item(1)).thenReturn(attr2);
        Mockito.when(attributes.item(2)).thenReturn(attr3);

        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals("test:attr", nodePointer1.getPropertyName());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals("test:attr2", nodePointer2.getPropertyName());
        NodePointer nodePointer3 = iterator.getNodePointer();
        assertNull(nodePointer3);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(4));
    }
}
