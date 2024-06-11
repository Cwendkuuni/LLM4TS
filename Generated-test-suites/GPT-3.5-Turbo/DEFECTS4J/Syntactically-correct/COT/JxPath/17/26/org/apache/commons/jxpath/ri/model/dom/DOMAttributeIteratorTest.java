
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor_NullParentNode() {
        NodePointer parent = null;
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_NullName() {
        NodePointer parent = createMockNodePointer();
        QName name = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_NonElementNode() {
        NodePointer parent = createMockNodePointer();
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_AllAttributes() {
        NodePointer parent = createMockElementNodePointer();
        QName name = new QName("*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_MissingAttribute() {
        NodePointer parent = createMockElementNodePointer();
        QName name = new QName("nonexistent");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_PrefixedAttribute() {
        NodePointer parent = createMockPrefixedAttributeNodePointer();
        QName name = new QName("prefix:attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_DefaultNamespaceAttribute() {
        NodePointer parent = createMockDefaultNamespaceAttributeNodePointer();
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
    }

    private NodePointer createMockNodePointer() {
        NodePointer pointer = new DOMNodePointer(null, null);
        return pointer;
    }

    private NodePointer createMockElementNodePointer() {
        Node node = createMockElement();
        NodePointer pointer = new DOMNodePointer(null, node);
        return pointer;
    }
    
    private Node createMockElement() {
        Element element = createMockElement();
        return element;
    }

    private Node createMockElement() {
        Element element = mock(Element.class);
        Attr attr = mock(Attr.class);
        when(element.getAttributeNode(anyString())).thenReturn(attr);
        return element;
    }

    private NodePointer createMockPrefixedAttributeNodePointer() {
        Node node = createMockPrefixedAttributeNode();
        NodePointer pointer = new DOMNodePointer(null, node);
        return pointer;
    }
    
    private Node createMockPrefixedAttributeNode() {
        Element element = createMockElement();
        Attr attr = createMockPrefixedAttribute();
        when(element.getAttributeNode(anyString())).thenReturn(attr);
        return element;
    }

    private Attr createMockPrefixedAttribute() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn("prefix");
        when(attr.getLocalName()).thenReturn("attribute");
        return attr;
    }

    private NodePointer createMockDefaultNamespaceAttributeNodePointer() {
        Node node = createMockDefaultNamespaceAttributeNode();
        NodePointer pointer = new DOMNodePointer(null, node);
        return pointer;
    }
    
    private Node createMockDefaultNamespaceAttributeNode() {
        Element element = createMockElement();
        Attr attr = createMockDefaultNamespaceAttribute();
        when(element.getAttributeNode(anyString())).thenReturn(attr);
        return element;
    }

    private Attr createMockDefaultNamespaceAttribute() {
        Attr attr = mock(Attr.class);
        when(attr.getLocalName()).thenReturn("attribute");
        return attr;
    }
}
