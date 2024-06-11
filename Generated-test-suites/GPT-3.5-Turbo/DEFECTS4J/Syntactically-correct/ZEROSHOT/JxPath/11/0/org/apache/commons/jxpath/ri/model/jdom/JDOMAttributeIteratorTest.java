import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import java.util.List;

public class JDOMAttributeIteratorTest {

    private Element createTestElement() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        Namespace ns = Namespace.getNamespace("test", "http://www.test.com");
        element.setAttribute("attr3", "value3", ns);
        return element;
    }

    @Test
    public void testGetNodePointer() {
        Element testElement = createTestElement();
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(new JXPathContextReferenceImpl().getNodePointer(testElement), new QName("*"));
        assertEquals("attr1", ((Attribute) attributeIterator.getNodePointer().getNode()).getName());
    }

    @Test
    public void testGetPosition() {
        Element testElement = createTestElement();
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(new JXPathContextReferenceImpl().getNodePointer(testElement), new QName("*"));
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element testElement = createTestElement();
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(new JXPathContextReferenceImpl().getNodePointer(testElement), new QName("*"));
        assertTrue(attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());
    }

}