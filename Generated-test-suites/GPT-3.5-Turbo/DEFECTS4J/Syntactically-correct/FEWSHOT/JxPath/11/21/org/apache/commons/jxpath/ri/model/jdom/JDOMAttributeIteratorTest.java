
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    @Mock
    private NodePointer parent;
    
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor_withElementAndPrefix() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attr", "value", Namespace.NO_NAMESPACE));
        element.setAttribute(new Attribute("xml:lang", "en", Namespace.XML_NAMESPACE));
        element.setAttribute(new Attribute("attr2", "value2", Namespace.getNamespace("prefix", "namespace")));
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "prefix"));
        List<Attribute> attributes = Collections.singletonList(new Attribute("attr", "value", Namespace.NO_NAMESPACE));
        assertEquals(attributes, attributeIterator.attributes);
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr2", "prefix"));
        List<Attribute> attributes2 = Collections.singletonList(new Attribute("attr2", "value2", Namespace.getNamespace("prefix", "namespace")));
        assertEquals(attributes2, attributeIterator.attributes);
    }
    
    @Test
    public void testConstructor_withElementAndNamespace() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attr", "value", Namespace.getNamespace("prefix", "namespace")));
        element.setAttribute(new Attribute("attr2", "value2", Namespace.getNamespace("prefix", "namespace")));
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "prefix", "namespace"));
        List<Attribute> attributes = Collections.singletonList(new Attribute("attr", "value", Namespace.getNamespace("prefix", "namespace")));
        assertEquals(attributes, attributeIterator.attributes);
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName(null, "attr2", "namespace"));
        List<Attribute> attributes2 = Collections.singletonList(new Attribute("attr2", "value2", Namespace.getNamespace("prefix", "namespace")));
        assertEquals(attributes2, attributeIterator.attributes);
    }
    
    @Test
    public void testConstructor_withWildcardName() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attr", "value", Namespace.NO_NAMESPACE));
        element.setAttribute(new Attribute("attr2", "value2", Namespace.getNamespace("prefix", "namespace")));
        
        attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
        List<Attribute> attributes = element.getAttributes();
        assertEquals(attributes, attributeIterator.attributes);
    }
    
    @Test
    public void testGetPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "namespace"));
        
        attributeIterator.position = 1;
        assertEquals(1, attributeIterator.getPosition());
        
        attributeIterator.position = 5;
        assertEquals(5, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition_withValidPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "namespace"));
        
        boolean result = attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.position);
        assertEquals(true, result);
    }
    
    @Test
    public void testSetPosition_withInvalidPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "namespace"));
        
        boolean result = attributeIterator.setPosition(0);
        assertEquals(0, attributeIterator.position);
        assertEquals(false, result);
        
        result = attributeIterator.setPosition(6);
        assertEquals(6, attributeIterator.position);
        assertEquals(false, result);
    }
    
    @Test
    public void testSetPosition_withNullAttributes() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "namespace"));
        attributeIterator.attributes = null;
        
        boolean result = attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.position);
        assertEquals(false, result);
    }
    
    @Test
    public void testGetNodePointer_withNonZeroPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "namespace"));
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("namespace"));
        attributeIterator.attributes = Collections.singletonList(attribute);
        attributeIterator.position = 1;
        
        JDOMAttributePointer expectedPointer = new JDOMAttributePointer(parent, attribute);
        assertEquals(expectedPointer, attributeIterator.getNodePointer());
    }
    
    @Test
    public void testGetNodePointer_withZeroPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "namespace"));
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("namespace"));
        attributeIterator.attributes = Collections.singletonList(attribute);
        attributeIterator.position = 0;
        
        JDOMAttributePointer expectedPointer = new JDOMAttributePointer(parent, attribute);
        assertEquals(expectedPointer, attributeIterator.getNodePointer());
    }
    
    @Test
    public void testGetNodePointer_withNullAttributes() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr", "namespace"));
        attributeIterator.attributes = null;
        
        assertNull(attributeIterator.getNodePointer());
    }
}
