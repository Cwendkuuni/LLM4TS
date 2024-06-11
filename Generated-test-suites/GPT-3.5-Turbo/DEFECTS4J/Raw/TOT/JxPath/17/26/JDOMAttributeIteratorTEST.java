
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDOMAttributeIteratorTest {
    
    private JDOMAttributeIterator jdomAttributeIterator;
    private NodePointer parent;
    
    private Element elementMock;
    private Attribute attributeMock;
    
    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        elementMock = mock(Element.class);
        attributeMock = mock(Attribute.class);
        
        when(parent.getNode()).thenReturn(elementMock);
    }
    
    @Test
    public void testConstructor_ElementIsNull_AttributesShouldBeEmptyList() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        assertTrue(jdomAttributeIterator.attributes.isEmpty());
    }
    
    @Test
    public void testConstructor_QNameIsXml_AttributesShouldContainXmlNamespaceAttribute() {
        Attribute xmlNamespaceAttr = new Attribute("xmlns", "xml", Namespace.XML_NAMESPACE);
        when(elementMock.getAttribute("xml", Namespace.XML_NAMESPACE)).thenReturn(xmlNamespaceAttr);
        
        QName xmlQName = new QName("xmlns", "xml");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, xmlQName);
        
        assertTrue(jdomAttributeIterator.attributes.contains(xmlNamespaceAttr));
    }
    
    @Test
    public void testConstructor_QNameWithoutPrefix_ElementHasMatchingAttribute_AttributesContainOnlyMatchingAttribute() {
        when(elementMock.getAttribute("attr", Namespace.NO_NAMESPACE)).thenReturn(attributeMock);
        
        QName attrQName = new QName("attr");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.contains(attributeMock));
        assertEquals(1, jdomAttributeIterator.attributes.size());
    }
    
    @Test
    public void testConstructor_QNameWithoutPrefix_ElementDoesNotHaveMatchingAttribute_AttributesShouldBeEmptyList() {
        when(elementMock.getAttribute("attr", Namespace.NO_NAMESPACE)).thenReturn(null);
        
        QName attrQName = new QName("attr");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.isEmpty());
    }
    
    @Test
    public void testConstructor_QNameWithPrefix_ElementHasNamespace_ResolverReturnsMatchingURI_AttributesContainOnlyMatchingAttribute() {
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        when(parent.getNamespaceResolver().getNamespaceURI("prefix")).thenReturn("uri");
        when(elementMock.getAttribute("attr", namespace)).thenReturn(attributeMock);
        
        QName attrQName = new QName("prefix:attr");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.contains(attributeMock));
        assertEquals(1, jdomAttributeIterator.attributes.size());
    }
    
    @Test
    public void testConstructor_QNameWithPrefix_ElementHasNamespace_ResolverDoesNotReturnMatchingURI_AttributesShouldBeEmptyList() {
        when(parent.getNamespaceResolver().getNamespaceURI("prefix")).thenReturn(null);
        
        QName attrQName = new QName("prefix:attr");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.isEmpty());
    }
    
    @Test
    public void testConstructor_QNameWithPrefix_ElementDoesNotHaveMatchingAttribute_AttributesShouldBeEmptyList() {
        QName attrQName = new QName("prefix:attr");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.isEmpty());
    }
    
    @Test
    public void testConstructor_QNameWithPrefixIsXmlNamespace_AttributesShouldContainXmlNamespaceAttribute() {
        Attribute xmlNamespaceAttr = new Attribute("xmlns", "xml", Namespace.XML_NAMESPACE);
        when(elementMock.getAttribute("xmlns", Namespace.XML_NAMESPACE)).thenReturn(xmlNamespaceAttr);
        
        QName attrQName = new QName("xmlns:xml");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.contains(xmlNamespaceAttr));
    }
    
    @Test
    public void testConstructor_QNameWithWildcard_ElementContainsMatchingAttributes_AttributesShouldContainMatchingAttributes() {
        Attribute attr1 = new Attribute("attr1");
        Attribute attr2 = new Attribute("attr2");
        List<Attribute> allAttributes = Arrays.asList(attr1, attr2);
        when(elementMock.getAttributes()).thenReturn(allAttributes);
        
        QName attrQName = new QName("*");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.contains(attr1));
        assertTrue(jdomAttributeIterator.attributes.contains(attr2));
        assertEquals(2, jdomAttributeIterator.attributes.size());
    }
    
    @Test
    public void testConstructor_QNameWithWildcard_ElementDoesNotContainMatchingAttributes_AttributesShouldBeEmptyList() {
        List<Attribute> allAttributes = Arrays.asList();
        when(elementMock.getAttributes()).thenReturn(allAttributes);
        
        QName attrQName = new QName("*");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, attrQName);
        
        assertTrue(jdomAttributeIterator.attributes.isEmpty());
    }
    
    @Test
    public void testGetNodePointer_PositionZero_ReturnsNull() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        assertNull(jdomAttributeIterator.getNodePointer());
    }
    
    @Test
    public void testGetNodePointer_ValidPosition_ReturnsNodePointer() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        jdomAttributeIterator.attributes.add(attributeMock);
        jdomAttributeIterator.attributes.add(attributeMock);
        jdomAttributeIterator.attributes.add(attributeMock);
        
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(attributeMock, ((JDOMAttributePointer) nodePointer).getAttribute());
    }
    
    @Test
    public void testGetPosition_AttributeIteratorHasNotBeenMoved_ReturnsZero() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        assertEquals(0, jdomAttributeIterator.getPosition());
    }
    
    @Test
    public void testGetPosition_PositionIsSet_ReturnsCorrectPosition() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        jdomAttributeIterator.position = 5;
        
        assertEquals(5, jdomAttributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_PositionIsZero_ReturnsFalse() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        assertFalse(jdomAttributeIterator.setPosition(0));
    }
    
    @Test
    public void testSetPosition_PositionIsGreaterThanSizeOfAttributes_ReturnsFalse() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        jdomAttributeIterator.attributes.add(attributeMock);
        jdomAttributeIterator.attributes.add(attributeMock);
        
        assertFalse(jdomAttributeIterator.setPosition(3));
    }
    
    @Test
    public void testSetPosition_ValidPosition_ReturnsTrue() {
        jdomAttributeIterator = new JDOMAttributeIterator(parent, null);
        jdomAttributeIterator.attributes.add(attributeMock);
        jdomAttributeIterator.attributes.add(attributeMock);
        jdomAttributeIterator.attributes.add(attributeMock);
        
        assertTrue(jdomAttributeIterator.setPosition(2));
    }
}
