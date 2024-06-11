
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer createNodePointer(Object node) {
        return new NodePointer() {
            @Override
            public Object getImmediateNode() {
                return node;
            }
            @Override
            public Object getValue() {
                return null;
            }
            @Override
            public QName getName() {
                return null;
            }
            @Override
            public Object getBaseValue() {
                return null;
            }
            @Override
            public boolean isCollection() {
                return false;
            }
            @Override
            public int getLength() {
                return 0;
            }
            @Override
            public Object getAttribute(QName name) {
                return null;
            }
            @Override
            public void setAttribute(QName name, Object value) {
            }
            @Override
            public NodeIterator childIterator(QName name, QName[] select) {
                return null;
            }
            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
            @Override
            public NodeIterator namespaceIterator() {
                return null;
            }
            @Override
            public NodePointer getValuePointer() {
                return null;
            }
            @Override
            public String asPath() {
                return null;
            }
            @Override
            public boolean testNode(NodePointer pointer) {
                return false;
            }
            @Override
            public NodePointer namespacePointer(String prefix) {
                return null;
            }
            @Override
            public NodePointer parent() {
                return null;
            }
            @Override
            public NodePointer parent(int level) {
                return null;
            }
            @Override
            public NodePointer root() {
                return null;
            }
            @Override
            public boolean isLeaf() {
                return false;
            }
            @Override
            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }
            @Override
            public Object clone() {
                return null;
            }
            @Override
            public String toString() {
                return null;
            }
        };
    }

    private List<Attribute> createAttributeList() {
        List<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute("attr1", "value1"));
        attributeList.add(new Attribute("attr2", "value2"));
        attributeList.add(new Attribute("attr3", "value3"));
        return attributeList;
    }

    @Test
    public void testSingleNodePointer() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("attr1", attributePointer.getAttribute().getName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testMultipleNodePointers() {
        Element element1 = new Element("test");
        element1.setAttribute("attr1", "value1");
        element1.setAttribute("attr2", "value2");

        Element element2 = new Element("test");
        element2.setAttribute("attr1", "value1");
        element2.setAttribute("attr3", "value3");
        
        NodePointer nodePointer1 = createNodePointer(element1);
        NodePointer nodePointer2 = createNodePointer(element2);
        
        JDOMAttributeIterator attributeIterator1 = new JDOMAttributeIterator(nodePointer1, new QName("attr1"));
        JDOMAttributeIterator attributeIterator2 = new JDOMAttributeIterator(nodePointer2, new QName("attr1"));
        
        assertTrue(attributeIterator1.setPosition(1));
        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) attributeIterator1.getNodePointer();
        assertNotNull(attributePointer1);
        assertEquals("attr1", attributePointer1.getAttribute().getName());
        assertEquals("value1", attributePointer1.getAttribute().getValue());
        
        assertTrue(attributeIterator2.setPosition(1));
        JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) attributeIterator2.getNodePointer();
        assertNotNull(attributePointer2);
        assertEquals("attr1", attributePointer2.getAttribute().getName());
        assertEquals("value1", attributePointer2.getAttribute().getValue());
    }

    @Test
    public void testNonExistentNodePointer() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("nonExistentAttr"));
        
        assertFalse(attributeIterator.setPosition(1));
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testWildcardNodePointer() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("attr1", attributePointer.getAttribute().getName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testNoNamespaceAttr() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("attr1", attributePointer.getAttribute().getName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testXMLNamespaceAttr() {
        Element element = new Element("test", Namespace.XML_NAMESPACE);
        element.setAttribute("xml:attr1", "value1", Namespace.XML_NAMESPACE);
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("xml:attr1"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("xml:attr1", attributePointer.getAttribute().getQualifiedName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testNamespaceAttr() {
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://example.com/ns1"));
        element.setAttribute("ns1:attr1", "value1", Namespace.getNamespace("ns1", "http://example.com/ns1"));
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("ns1:attr1"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("ns1:attr1", attributePointer.getAttribute().getQualifiedName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testNoNamespaceWildcardAttr() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("attr1", attributePointer.getAttribute().getName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testXMLNamespaceWildcardAttr() {
        Element element = new Element("test", Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.XML_NAMESPACE);
        element.setAttribute("xml:attr1", "value1", Namespace.XML_NAMESPACE);
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("xml:attr1", attributePointer.getAttribute().getQualifiedName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testNamespaceWildcardAttr() {
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://example.com/ns1"));
        element.setAttribute("ns1:attr1", "value1", Namespace.getNamespace("ns1", "http://example.com/ns1"));
        element.setAttribute("attr2", "value2");
        
        NodePointer nodePointer = createNodePointer(element);
        
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        assertTrue(attributeIterator.setPosition(1));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) attributeIterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("ns1:attr1", attributePointer.getAttribute().getQualifiedName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }
}

