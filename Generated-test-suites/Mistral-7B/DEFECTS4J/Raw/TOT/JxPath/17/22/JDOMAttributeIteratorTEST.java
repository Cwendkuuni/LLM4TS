import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for JDOMAttributeIterator.
 */
public class JDOMAttributeIteratorTest {

    /**
     * Test constructor with a null parent.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullParent() {
        new JDOMAttributeIterator(null, new QName("", ""));
    }

    /**
     * Test constructor with an empty list of attributes.
     */
    @Test
    public void testConstructorEmptyList() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        List<Attribute> attributes = iterator.getAttributes();
        assertTrue(attributes.isEmpty());
    }

    /**
     * Test constructor with an iterator that returns an attribute.
     */
    @Test
    public void testConstructorOneAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:attr1"));
        List<Attribute> attributes = iterator.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attr = attributes.get(0);
        assertEquals("value1", attr.getValue());
    }

    /**
     * Test constructor with an iterator that returns multiple attributes.
     */
    @Test
    public void testConstructorMultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:*"));
        List<Attribute> attributes = iterator.getAttributes();
        assertEquals(2, attributes.size());
        Attribute attr1 = attributes.get(0);
        assertEquals("value1", attr1.getValue());
        Attribute attr2 = attributes.get(1);
        assertEquals("value2", attr2.getValue());
    }

    /**
     * Test getNodePointer with an empty list of attributes.
     */
    @Test
    public void testGetNodePointerEmptyList() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    /**
     * Test getNodePointer with a list of one attribute.
     */
    @Test
    public void testGetNodePointerOneAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attr = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertEquals("value1", attr.getValue());
    }

    /**
     * Test getNodePointer with a list of multiple attributes.
     */
    @Test
    public void testGetNodePointerMultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:*"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attr1 = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertEquals("value1", attr1.getValue());
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attr2 = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertEquals("value2", attr2.getValue());
    }

    /**
     * Test getPosition with an empty list of attributes.
     */
    @Test
    public void testGetPositionEmptyList() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    /**
     * Test getPosition with a list of one attribute.
     */
    @Test
    public void testGetPositionOneAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:attr1"));
        iterator.next();
        int position = iterator.getPosition();
        assertEquals(1, position);
    }

    /**
     * Test getPosition with a list of multiple attributes.
     */
    @Test
    public void testGetPositionMultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:*"));
        iterator.next(); // attr1
        iterator.next(); // attr2
        int position = iterator.getPosition();
        assertEquals(2, position);
    }

    /**
     * Test setPosition with an empty list of attributes.
     */
    @Test
    public void testSetPositionEmptyList() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.setPosition(2));
    }

    /**
     * Test setPosition with a list of one attribute.
     */
    @Test
    public void testSetPositionOneAttribute() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:attr1"));
        assertTrue(iterator.setPosition(1));
        int position = iterator.getPosition();
        assertEquals(1, position);
    }

    /**
     * Test setPosition with a list of multiple attributes.
     */
    @Test
    public void testSetPositionMultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        Namespace ns = Namespace.getNamespace("ns", "http://example.com/");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:*"));
        assertTrue(iterator.setPosition(3));
        int position = iterator.getPosition();
        assertEquals(3, position);
    }
}