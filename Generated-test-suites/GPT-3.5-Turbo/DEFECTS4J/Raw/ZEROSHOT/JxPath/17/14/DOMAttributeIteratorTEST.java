import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @Test
    public void testTestAttrNodeNull() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.testAttr(null);
        assertFalse(result);
    }

    @Test
    public void testTestAttrNodeWithNamespace() {
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn("testPrefix");
        Mockito.when(DOMNodePointer.getLocalName(attr)).thenReturn("testLocalName");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(name.getPrefix()).thenReturn("testPrefix");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.testAttr(attr);

        assertTrue(result);
    }

    @Test
    public void testTestAttrNodeWithoutNamespace() {
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn(null);
        Mockito.when(DOMNodePointer.getLocalName(attr)).thenReturn("testLocalName");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(name.getPrefix()).thenReturn(null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.testAttr(attr);

        assertTrue(result);
    }

    @Test
    public void testTestAttrNodeWithInvalidNamespacePrefix() {
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn("xmlns");
        Mockito.when(DOMNodePointer.getLocalName(attr)).thenReturn("testLocalName");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(name.getPrefix()).thenReturn("testPrefix");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.testAttr(attr);

        assertFalse(result);
    }

    @Test
    public void testTestAttrNodeWithInvalidLocalName() {
        Mockito.when(DOMNodePointer.getPrefix(attr)).thenReturn(null);
        Mockito.when(DOMNodePointer.getLocalName(attr)).thenReturn("testLocalName");
        Mockito.when(name.getName()).thenReturn("invalid");
        Mockito.when(name.getPrefix()).thenReturn(null);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.testAttr(attr);

        assertFalse(result);
    }

    @Test
    public void testEqualStringsEqual() {
        boolean result = DOMAttributeIterator.equalStrings("test", "test");
        assertTrue(result);
    }

    @Test
    public void testEqualStringsNotEqual() {
        boolean result = DOMAttributeIterator.equalStrings("test1", "test2");
        assertFalse(result);
    }

    @Test
    public void testEqualStringsFirstNull() {
        boolean result = DOMAttributeIterator.equalStrings(null, "test");
        assertFalse(result);
    }

    @Test
    public void testEqualStringsSecondNull() {
        boolean result = DOMAttributeIterator.equalStrings("test", null);
        assertFalse(result);
    }

    @Test
    public void testGetAttributeWithNamespace() {
        Mockito.when(name.getPrefix()).thenReturn("testPrefix");
        Mockito.when(parent.getNamespaceResolver().getNamespaceURI("testPrefix")).thenReturn("testNS");
        Mockito.when(element.getAttributeNodeNS("testNS", name.getName())).thenReturn(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr result = iterator.getAttribute(element, name);

        assertEquals(attr, result);
    }

    @Test
    public void testGetAttributeWithoutNamespace() {
        Mockito.when(name.getPrefix()).thenReturn(null);
        Mockito.when(element.getAttributeNode(name.getName())).thenReturn(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr result = iterator.getAttribute(element, name);

        assertEquals(attr, result);
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.attributes = new ArrayList<>();
        iterator.attributes.add(attr);

        Mockito.when(parent.getNode()).thenReturn(node);

        DOMAttributePointer pointer = new DOMAttributePointer(parent, attr);
        NodePointer result = iterator.getNodePointer();

        assertEquals(pointer, result);
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.position = 5;

        int result = iterator.getPosition();

        assertEquals(5, result);
    }

    @Test
    public void testSetPositionValid() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.setPosition(1);
        iterator.position = 1;
        assertTrue(result);
    }

    @Test
    public void testSetPositionInvalid() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result = iterator.setPosition(10);
        assertFalse(result);
    }

}