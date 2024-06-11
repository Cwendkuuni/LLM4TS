import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator jdomAttributeIterator;

    @Before
    public void setUp() {
        NodePointer parent = createMockNodePointer();
        QName name = new QName("attribute");
        jdomAttributeIterator = new JDOMAttributeIterator(parent, name);
    }

    private NodePointer createMockNodePointer() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attributeName", "attributeValue"));
        return new NodePointer(element);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(Attribute.class, nodePointer.getNode().getClass());
        Attribute attribute = (Attribute) nodePointer.getNode();
        assertEquals("attributeName", attribute.getName());
        assertEquals("attributeValue", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        int position = jdomAttributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean setPositionResult = jdomAttributeIterator.setPosition(1);
        assertTrue(setPositionResult);
        assertEquals(1, jdomAttributeIterator.getPosition());

        setPositionResult = jdomAttributeIterator.setPosition(0);
        assertFalse(setPositionResult);

        setPositionResult = jdomAttributeIterator.setPosition(2);
        assertFalse(setPositionResult);
    }

    @Test
    public void testConstructor_AttributeExists() {
        NodePointer parent = createMockNodePointer();
        QName name = new QName("attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_AttributeDoesNotExist() {
        NodePointer parent = createMockNodePointer();
        QName name = new QName("nonExistentAttribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_InvalidNamespacePrefix() {
        NodePointer parent = createMockNodePointer();
        QName name = new QName("xml:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_NamespacePrefixIsNull() {
        NodePointer parent = createMockNodePointer();
        QName name = new QName(":attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_NamespacePrefixNotAvailable() {
        NodePointer parent = createMockNodePointer();
        QName name = new QName("someprefix:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_AllAttributesReturned() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attribute1", "value1"));
        element.setAttribute(new Attribute("attribute2", "value2"));
        NodePointer parent = new NodePointer(element);

        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNotNull(iterator.getNodePointer());
        assertEquals(2, iterator.attributes.size());
    }

    @Test
    public void testConstructor_EmptyAttributes() {
        Element element = new Element("test");
        NodePointer parent = new NodePointer(element);

        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNull(iterator.getNodePointer());
        assertEquals(Collections.emptyList(), iterator.attributes);
    }

}