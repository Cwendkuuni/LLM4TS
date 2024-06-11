package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;
    private Node node;
    private Element element;
    private NodeList nodeList;
    private NamedNodeMap namedNodeMap;
    private Attr attr;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        node = mock(Node.class);
        element = mock(Element.class);
        nodeList = mock(NodeList.class);
        namedNodeMap = mock(NamedNodeMap.class);
        attr = mock(Attr.class);

        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(name.getName()).thenReturn("testName");
        when(element.getAttributeNodeNS(null, "testName")).thenReturn(attr);
        when(namedNodeMap.getLength()).thenReturn(1);
        when(namedNodeMap.item(0)).thenReturn(attr);
        when(attr.getLocalName()).thenReturn("testName");
        when(attr.getPrefix()).thenReturn(null);

        iterator = new DOMAttributeIterator(parent, name);
        iterator.attributes = new ArrayList<>();
        iterator.attributes.add(attr);
    }

    @Test
    public void testSetPositionWithinRange() {
        boolean result = iterator.setPosition(1);

        assertEquals(1, iterator.getPosition());
        assertEquals(true, result);
    }

    @Test
    public void testSetPositionOutOfRange() {
        boolean result = iterator.setPosition(2);

        assertEquals(0, iterator.getPosition());
        assertEquals(false, result);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();

        assertEquals(new DOMAttributePointer(parent, attr), nodePointer);
    }
  
    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
  
        assertEquals(0, position);
    }
}