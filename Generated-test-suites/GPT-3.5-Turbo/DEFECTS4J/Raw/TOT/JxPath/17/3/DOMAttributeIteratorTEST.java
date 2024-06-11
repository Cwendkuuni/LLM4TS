
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator domIterator;
    private NodePointer nodePointer;
    private QName qName;
    private List<Attr> attributes;
    private Element element;
    private Document document;

    @Before
    public void setUp() {
        nodePointer = mock(NodePointer.class);
        qName = mock(QName.class);
        attributes = new ArrayList<>();
        element = mock(Element.class);
        document = mock(Document.class);
        when(nodePointer.getNode()).thenReturn(element);
        when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributeNodeNS(any(), any())).thenReturn(null);
        when(element.getAttributeNode(any())).thenReturn(null);
        
    }

    @Test
    public void testConstructorWithNonElementNode() {
        when(element.getNodeType()).thenReturn(Node.TEXT_NODE);
        domIterator = new DOMAttributeIterator(nodePointer, qName);
        assertEquals(0, domIterator.attributes.size());
    }

    @Test
    public void testConstructorWithNameNotEqualToStar() {
        when(qName.getName()).thenReturn("attr");
        when(element.getAttributeNode(any())).thenReturn(mock(Attr.class));
        
        domIterator = new DOMAttributeIterator(nodePointer, qName);
        
        assertEquals(1, domIterator.attributes.size());
    }

    @Test
    public void testConstructorWithNameEqualToStar() {
        when(qName.getName()).thenReturn("*");
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));

        domIterator = new DOMAttributeIterator(nodePointer, qName);

        assertEquals(0, domIterator.attributes.size());
    }

    @Test
    public void testConstructorWithMatchedAttributes() {
        when(qName.getName()).thenReturn("*");
        NamedNodeMap namedNodeMap = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(namedNodeMap);

        Attr attr1 = mock(Attr.class);
        when(attr1.getName()).thenReturn("attr1");
        when(namedNodeMap.item(0)).thenReturn(attr1);
        when(nodePointer.getNamespaceURI(any())).thenReturn(null);

        Attr attr2 = mock(Attr.class);
        when(attr2.getName()).thenReturn("attr2");
        when(namedNodeMap.item(1)).thenReturn(attr2);
        when(nodePointer.getNamespaceURI(any())).thenReturn(null);

        Attr attr3 = mock(Attr.class);
        when(attr3.getName()).thenReturn("attr3");
        when(namedNodeMap.item(2)).thenReturn(attr3);
        when(nodePointer.getNamespaceURI(any())).thenReturn(null);

        domIterator = new DOMAttributeIterator(nodePointer, qName);

        assertEquals(3, domIterator.attributes.size());
    }

    @Test
    public void testTestAttrWithNamespaceNullAndLocalNameStar() {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn(null);
        when(attr.getLocalName()).thenReturn("attr");

        when(qName.getName()).thenReturn("*");
        
        assertFalse(domIterator.testAttr(attr));
    }

    @Test
    public void testEqualStringsWhenBothStringsAreNull() {
        assertTrue(domIterator.equalStrings(null, null));
    }

    @Test
    public void testEqualStringsWhenBothStringsAreSame() {
        String s = "same";
        assertTrue(domIterator.equalStrings(s, s));
    }

    @Test
    public void testEqualStringsWhenOneStringIsNull() {
        assertFalse(domIterator.equalStrings("not null", null));
    }

    @Test
    public void testEqualStringsWhenStringsAreDifferent() {
        assertFalse(domIterator.equalStrings("string1", "string2"));
    }

    @Test
    public void testGetNodePointerWhenPositionZero() {
        domIterator.position = 0;
        assertNull(domIterator.getNodePointer());
    }

    @Test
    public void testGetNodePointerWithPositiveValidPosition() {
        Attr attr = mock(Attr.class);
        domIterator.attributes.add(attr);
        domIterator.position = 1;
        when(nodePointer.getNamespaceResolver()).thenReturn(mock(NamespaceResolver.class));

        NodePointer nodePointerResult = domIterator.getNodePointer();

        assertTrue(nodePointerResult instanceof DOMAttributePointer);
        assertEquals(attr, ((DOMAttributePointer) nodePointerResult).getAttr());
    }

    @Test
    public void testGetNodePointerWithPositiveOutOfBoundsPosition() {
        Attr attr = mock(Attr.class);
        domIterator.attributes.add(attr);
        domIterator.position = 2;
        when(nodePointer.getNamespaceResolver()).thenReturn(mock(NamespaceResolver.class));

        NodePointer nodePointerResult = domIterator.getNodePointer();

        assertTrue(nodePointerResult instanceof DOMAttributePointer);
        assertEquals(attr, ((DOMAttributePointer) nodePointerResult).getAttr());
    }

    @Test
    public void testGetPosition() {
        domIterator.position = 1;
        assertEquals(1, domIterator.getPosition());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        domIterator.position = 0;
        assertTrue(domIterator.setPosition(1));
        assertEquals(1, domIterator.position);
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        domIterator.position = 0;
        assertFalse(domIterator.setPosition(-1));
        assertFalse(domIterator.setPosition(0));
        assertFalse(domIterator.setPosition(4));
    }
}