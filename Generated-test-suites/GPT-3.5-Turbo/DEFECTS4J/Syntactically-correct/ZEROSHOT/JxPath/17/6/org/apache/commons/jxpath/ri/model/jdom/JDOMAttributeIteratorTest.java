import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "namespaceUri"));
        
        parent = new JDOMNodePointer(element);
        QName name = new QName("attr1");
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Attribute attribute = attributePointer.getAttribute();
        assertNotNull(attribute);
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        assertTrue(result);
        
        int position = iterator.getPosition();
        assertEquals(1, position);
        
        result = iterator.setPosition(3);
        assertFalse(result);
        
        position = iterator.getPosition();
        assertEquals(1, position);
    }

}