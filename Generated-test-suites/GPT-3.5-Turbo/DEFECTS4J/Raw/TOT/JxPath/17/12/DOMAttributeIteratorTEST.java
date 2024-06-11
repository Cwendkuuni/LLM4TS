import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    private DOMNodePointer parentNode;
    private QName attributeName;
    private DOMAttributeIterator attributeIterator;
    private Element elementMock;
    private NamedNodeMap namedNodeMapMock;

    @Before
    public void setup() {
        elementMock = mock(Element.class);
        namedNodeMapMock = mock(NamedNodeMap.class);
        parentNode = mock(DOMNodePointer.class);
        attributeName = mock(QName.class);
        attributeIterator = new DOMAttributeIterator(parentNode, attributeName);
    }
    
    @Test
    public void testConstructor_elementNode_addsExpectedAttributes() {
        Node nodeMock = mock(Node.class);
        when(parentNode.getNode()).thenReturn(nodeMock);
        when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(namedNodeMapMock);
        when(namedNodeMapMock.getLength()).thenReturn(2);
        Attr attrMock1 = mock(Attr.class);
        Attr attrMock2 = mock(Attr.class);
        when(namedNodeMapMock.item(0)).thenReturn(attrMock1);
        when(namedNodeMapMock.item(1)).thenReturn(attrMock2);
        when(attributeName.getName()).thenReturn("*");
        
        attributeIterator = new DOMAttributeIterator(parentNode, attributeName);
        
        assertEquals(2, attributeIterator.attributes.size());
        assertEquals(attrMock1, attributeIterator.attributes.get(0));
        assertEquals(attrMock2, attributeIterator.attributes.get(1));
    }
    
    @Test
    public void testConstructor_elementNode_addsSpecificAttribute() {
        Node nodeMock = mock(Node.class);
        when(parentNode.getNode()).thenReturn(nodeMock);
        when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributeNode("test")).thenReturn(mock(Attr.class));
        when(attributeName.getName()).thenReturn("test");
        
        attributeIterator = new DOMAttributeIterator(parentNode, attributeName);
        
        assertEquals(1, attributeIterator.attributes.size());
        assertEquals(elementMock.getAttributeNode("test"), attributeIterator.attributes.get(0));
    }
    
    @Test
    public void testConstructor_elementNode_withNullAttr_returnsNullAttributes() {
        Node nodeMock = mock(Node.class);
        when(parentNode.getNode()).thenReturn(nodeMock);
        when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributeNode("test")).thenReturn(null);
        when(attributeName.getName()).thenReturn("test");
        
        attributeIterator = new DOMAttributeIterator(parentNode, attributeName);
        
        assertEquals(0, attributeIterator.attributes.size());
    }
    
    @Test
    public void testConstructor_nonElementNode_doesNotAddAttributes() {
        Node nodeMock = mock(Node.class);
        when(parentNode.getNode()).thenReturn(nodeMock);
        when(nodeMock.getNodeType()).thenReturn(Node.TEXT_NODE);
        
        attributeIterator = new DOMAttributeIterator(parentNode, attributeName);
        
        assertEquals(0, attributeIterator.attributes.size());
    }
    
    @Test
    public void testTestAttr_attrIsXmlns_returnsFalse() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn("xmlns");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testTestAttr_attrIsXmlnsAndLocalNameIsNot_returnsTrue() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn(null);
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("xmlns");
        when(attributeName.getName()).thenReturn("test");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testTestAttr_localNameDoesNotEqualTestLocalName_returnsFalse() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn(null);
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("local");
        when(attributeName.getName()).thenReturn("test");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testTestAttr_localNameEqualsTestLocalName_returnsTrue() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn(null);
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("test");
        when(attributeName.getName()).thenReturn("test");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testTestAttr_testPrefixIsNull_returnsTrue() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn("testPrefix");
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("test");
        when(attributeName.getName()).thenReturn("test");
        when(attributeName.getPrefix()).thenReturn(null);
        when(parentNode.getNamespaceURI(null)).thenReturn("testPrefixNamespace");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testTestAttr_testPrefixIsNullAndNodePrefixIsNull_returnsFalse() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn(null);
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("test");
        when(attributeName.getName()).thenReturn("test");
        when(attributeName.getPrefix()).thenReturn(null);
        when(parentNode.getNamespaceURI(null)).thenReturn("testPrefixNamespace");
        when(parentNode.getNamespaceURI(null)).thenReturn(null);
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testTestAttr_nodePrefixEqualsTestPrefix_returnsTrue() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn("testPrefix");
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("test");
        when(attributeName.getName()).thenReturn("test");
        when(attributeName.getPrefix()).thenReturn("testPrefix");
        when(parentNode.getNamespaceURI("testPrefix")).thenReturn("testPrefixNamespace");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testTestAttr_testNsAndNodeNsMatch_returnsTrue() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn("testPrefix");
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("test");
        when(attributeName.getName()).thenReturn("test");
        when(attributeName.getPrefix()).thenReturn("testPrefix");
        when(parentNode.getNamespaceURI("testPrefix")).thenReturn("testNS");
        when(parentNode.getNamespaceURI("testPrefix")).thenReturn("testNS");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testTestAttr_testNsAndNodeNsDoNotMatch_returnsFalse() {
        Attr attrMock = mock(Attr.class);
        when(DOMNodePointer.getPrefix(attrMock)).thenReturn("testPrefix");
        when(DOMNodePointer.getLocalName(attrMock)).thenReturn("test");
        when(attributeName.getName()).thenReturn("test");
        when(attributeName.getPrefix()).thenReturn("testPrefix");
        when(parentNode.getNamespaceURI("testPrefix")).thenReturn("testNS1");
        when(parentNode.getNamespaceURI("testPrefix")).thenReturn("testNS2");
        
        boolean result = attributeIterator.testAttr(attrMock);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualStrings_sameStrings_returnsTrue() {
        String s1 = "testString";
        String s2 = "testString";
        
        boolean result = attributeIterator.equalStrings(s1, s2);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testEqualStrings_differentStrings_returnsFalse() {
        String s1 = "testString1";
        String s2 = "testString2";
        
        boolean result = attributeIterator.equalStrings(s1, s2);
        
        assertEquals(false, result);
    }

    @Test
    public void testAttributeEnumeration_iteratorNotNull_returnsExpectedPointer() {
        NodeList nodeListMock = mock(NodeList.class);
        Node nodeMock = mock(Node.class);
        when(parentNode.getNode()).thenReturn(nodeMock);
        when(nodeMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(namedNodeMapMock);
        when(namedNodeMapMock.getLength()).thenReturn(2);
        Attr attrMock1 = mock(Attr.class);
        Attr attrMock2 = mock(Attr.class);
        when(namedNodeMapMock.item(0)).thenReturn(attrMock1);
        when(namedNodeMapMock.item(1)).thenReturn(attrMock2);
        when(attributeName.getName()).thenReturn("*");
        attributeIterator = new DOMAttributeIterator(parentNode, attributeName);
        
        NodePointer pointer = attributeIterator.getNodePointer();
        
        assertEquals(new DOMAttributePointer(parentNode, attrMock1), pointer);
    }
    
    @Test
    public void testGetNodePointer_positionZero_returnsNull() {
        attributeIterator.setPosition(0);
        
        NodePointer pointer = attributeIterator.getNodePointer();
        
        assertEquals(null, pointer);
    }
    
    @Test
    public void testGetNodePointer_positionOne_returnsFirstAttrPointer() {
        Attr attrMock = mock(Attr.class);
        attributeIterator.attributes.add(attrMock);
        
        NodePointer pointer = attributeIterator.getNodePointer();
        
        assertEquals(new DOMAttributePointer(parentNode, attrMock), pointer);
    }
    
    @Test
    public void testGetPosition_returnsPositionValue() {
        attributeIterator.setPosition(3);
        
        int position = attributeIterator.getPosition();
        
        assertEquals(3, position);
    }
    
    @Test
    public void testSetPosition_validPosition_returnsTrue() {
        boolean result = attributeIterator.setPosition(2);
        
        assertEquals(true, result);
        assertEquals(2, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_invalidPositionLessThanOne_returnsFalse() {
        boolean result = attributeIterator.setPosition(0);
        
        assertEquals(false, result);
        assertEquals(0, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_invalidPositionGreaterThanSize_returnsFalse() {
        attributeIterator.attributes.add(mock(Attr.class));
        
        boolean result = attributeIterator.setPosition(2);
        
        assertEquals(false, result);
        assertEquals(0, attributeIterator.getPosition());
    }

}