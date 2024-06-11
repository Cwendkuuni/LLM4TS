
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    private Element createMockElement() {
        Element element = new Element("test");
        Namespace namespace = Namespace.getNamespace("testNamespace");
        element.setAttribute("attribute1", "value1", namespace);
        element.setAttribute("attribute2", "value2");
        return element;
    }

    private Element createMockElementNoAttributes() {
        return new Element("test");
    }

    private Element createMockElementNoNamespace() {
        Element element = new Element("test");
        element.setAttribute("attribute1", "value1");
        element.setAttribute("attribute2", "value2");
        return element;
    }

    @Test
    public void testConstructorWithNamespace() {
        Element element = createMockElement();
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attribute1", "testNamespace"));
        assertEquals(element.getAttribute("attribute1", Namespace.getNamespace("testNamespace")) , ((JDOMAttributePointer)iterator.getNodePointer()).getAttribute());
    }

    @Test
    public void testConstructorWithNoNamespace() {
        Element element = createMockElementNoNamespace();
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attribute1"));
        assertEquals(element.getAttribute("attribute1") , ((JDOMAttributePointer)iterator.getNodePointer()).getAttribute());
    }

    @Test
    public void testConstructorWithAsterisk() {
        Element element = createMockElement();
        Namespace namespace = Namespace.getNamespace("testNamespace");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("*", "testNamespace"));
        assertTrue(((JDOMAttributePointer)iterator.getNodePointer()).getAttribute().getNamespace().equals(namespace));
    }

    @Test
    public void testConstructorWithNoAttributes() {
        Element element = createMockElementNoAttributes();
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attribute1"));
        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testGetPosition() {
        Element element = createMockElement();
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attribute1"));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = createMockElement();
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attribute1"));
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
    }
}
