import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator jdomAttributeIterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setup() {
        parent = new NodePointer();
        name = new QName("TestNamespace", "TestName");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor_noAttributes() {
        NodePointer parent = new NodePointer();
        QName name = new QName("TestNamespace", "TestName");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);
        assertFalse(jdomAttributeIterator.setPosition(1));
    }

    @Test
    public void testConstructor_attributesWithPrefix() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute", "testValue", Namespace.getNamespace("TestNamespace"));
        parent.setValue(element);

        QName name = new QName("TestNamespace", "testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertTrue(Arrays.asList(jdomAttributeIterator.getNodePointer().getAttribute().getValue()).contains("testValue"));

        assertFalse(jdomAttributeIterator.setPosition(2));
        assertNull(jdomAttributeIterator.getNodePointer());
    }

    @Test
    public void testConstructor_attributesWithoutPrefix() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute", "testValue", Namespace.NO_NAMESPACE);
        parent.setValue(element);

        QName name = new QName("", "testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertTrue(Arrays.asList(jdomAttributeIterator.getNodePointer().getAttribute().getValue()).contains("testValue"));

        assertFalse(jdomAttributeIterator.setPosition(2));
        assertNull(jdomAttributeIterator.getNodePointer());
    }

    @Test
    public void testConstructor_allAttributes() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute1", "testValue1", Namespace.NO_NAMESPACE);
        element.setAttribute("testAttribute2", "testValue2", Namespace.NO_NAMESPACE);
        parent.setValue(element);

        QName name = new QName("", "*");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertTrue(Arrays.asList(jdomAttributeIterator.getNodePointer().getAttribute().getValue()).contains("testValue1"));

        assertTrue(jdomAttributeIterator.setPosition(2));
        assertTrue(Arrays.asList(jdomAttributeIterator.getNodePointer().getAttribute().getValue()).contains("testValue2"));

        assertFalse(jdomAttributeIterator.setPosition(3));
        assertNull(jdomAttributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition_noAttributes() {
        assertEquals(0, jdomAttributeIterator.getPosition());
        assertFalse(jdomAttributeIterator.setPosition(1));
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testGetPosition_attributesWithPrefix() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute", "testValue", Namespace.getNamespace("TestNamespace"));
        parent.setValue(element);

        QName name = new QName("TestNamespace", "testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.getPosition());

        assertFalse(jdomAttributeIterator.setPosition(2));
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testGetPosition_attributesWithoutPrefix() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute", "testValue", Namespace.NO_NAMESPACE);
        parent.setValue(element);

        QName name = new QName("", "testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.getPosition());

        assertFalse(jdomAttributeIterator.setPosition(2));
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testGetPosition_allAttributes() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute1", "testValue1", Namespace.NO_NAMESPACE);
        element.setAttribute("testAttribute2", "testValue2", Namespace.NO_NAMESPACE);
        parent.setValue(element);

        QName name = new QName("", "*");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.getPosition());

        assertTrue(jdomAttributeIterator.setPosition(2));
        assertEquals(2, jdomAttributeIterator.getPosition());

        assertFalse(jdomAttributeIterator.setPosition(3));
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_noAttributes() {
        assertFalse(jdomAttributeIterator.setPosition(1));
    }

    @Test
    public void testSetPosition_attributesWithPrefix() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute", "testValue", Namespace.getNamespace("TestNamespace"));
        parent.setValue(element);

        QName name = new QName("TestNamespace", "testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertFalse(jdomAttributeIterator.setPosition(0));
        assertFalse(jdomAttributeIterator.setPosition(3));
        assertFalse(jdomAttributeIterator.setPosition(-1));
        assertEquals(0, jdomAttributeIterator.getPosition());

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.getPosition());

        assertFalse(jdomAttributeIterator.setPosition(2));
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_attributesWithoutPrefix() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute", "testValue", Namespace.NO_NAMESPACE);
        parent.setValue(element);

        QName name = new QName("", "testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertFalse(jdomAttributeIterator.setPosition(0));
        assertFalse(jdomAttributeIterator.setPosition(3));
        assertFalse(jdomAttributeIterator.setPosition(-1));
        assertEquals(0, jdomAttributeIterator.getPosition());

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.getPosition());

        assertFalse(jdomAttributeIterator.setPosition(2));
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_allAttributes() {
        Element element = new Element("TestElement");
        element.setAttribute("testAttribute1", "testValue1", Namespace.NO_NAMESPACE);
        element.setAttribute("testAttribute2", "testValue2", Namespace.NO_NAMESPACE);
        parent.setValue(element);

        QName name = new QName("", "*");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, name);

        assertFalse(jdomAttributeIterator.setPosition(0));
        assertFalse(jdomAttributeIterator.setPosition(4));
        assertFalse(jdomAttributeIterator.setPosition(-1));
        assertEquals(0, jdomAttributeIterator.getPosition());

        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.getPosition());

        assertTrue(jdomAttributeIterator.setPosition(2));
        assertEquals(2, jdomAttributeIterator.getPosition());

        assertFalse(jdomAttributeIterator.setPosition(3));
        assertEquals(0, jdomAttributeIterator.getPosition());
    }
}