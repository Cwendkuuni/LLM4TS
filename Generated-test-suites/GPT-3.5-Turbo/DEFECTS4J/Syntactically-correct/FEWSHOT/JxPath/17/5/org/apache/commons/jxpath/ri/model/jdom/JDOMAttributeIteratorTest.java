package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
    
public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private JDOMAttributeIterator iterator;
    private Element element;
    private List<Attribute> attributes;
    
    @Before
    public void setUp() throws Exception {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        iterator = new JDOMAttributeIterator(parent, name);
        element = mock(Element.class);
        attributes = new ArrayList<Attribute>();
        when(parent.getNode()).thenReturn(element);
        when(parent.getNamespaceResolver()).thenReturn(mock(JDOMNamespaceResolver.class));
        when(name.getPrefix()).thenReturn("prefix");
        when(name.getName()).thenReturn("name");
        when(element.getAttribute("name", Namespace.getNamespace("prefix", "uri"))).thenReturn(null);
        when(element.getAttributes()).thenReturn(attributes);
    }
    
    @Test
    public void testConstructor_nullNamespace_returnsEmptyList() {
        when(name.getPrefix()).thenReturn(null);
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertNull(iterator.attributes);
    }
    
    @Test
    public void testConstructor_nsXml_returnsXmlNamespace() {
        when(name.getPrefix()).thenReturn("xml");
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(iterator.attributes, Collections.EMPTY_LIST);
    }
    
    @Test
    public void testConstructor_nsNonNullNamespace_returnsNonNullNamespace() {
        when(element.getAttribute("name", Namespace.getNamespace("prefix", "uri"))).thenReturn(mock(Attribute.class));
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(1, iterator.attributes.size());
    }
    
    @Test
    public void testConstructor_lnameNotWildcard_returnsAttributeWithName() {
        when(element.getAttribute("name", Namespace.getNamespace("prefix", "uri"))).thenReturn(mock(Attribute.class));
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(1, iterator.attributes.size());
    }
    
    @Test
    public void testConstructor_lnameWildcard_returnsAttributesWithNamespace() {
        attributes.add(mock(Attribute.class));
        when(element.getAttributes()).thenReturn(attributes);
        iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(1, iterator.attributes.size());
    }
    
    @Test
    public void testGetNodePointer_position0_returnsNull() {
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetNodePointer_position1_returnsNodePointer() {
        attributes.add(mock(Attribute.class));
        when(parent.getNode()).thenReturn(element);
        iterator.setPosition(1);
        
        assertEquals(JDOMAttributePointer.class, iterator.getNodePointer().getClass());
    }
    
    @Test
    public void testGetPosition_returnsPosition() {
        iterator.setPosition(2);
        
        assertEquals(2, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition_0_returnsFalse() {
        assertFalse(iterator.setPosition(0));
    }
    
    @Test
    public void testSetPosition_1_returnsTrue() {
        assertTrue(iterator.setPosition(1));
    }
    
    @Test
    public void testSetPosition_greaterThanSize_returnsFalse() {
        assertFalse(iterator.setPosition(2));
    }
}