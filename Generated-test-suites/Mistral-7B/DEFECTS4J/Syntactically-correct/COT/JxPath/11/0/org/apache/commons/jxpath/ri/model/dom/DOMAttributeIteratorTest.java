import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testConstructor_ParentNotNull_NameNotNull() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructor_ParentNull() {
        QName name = new QName("test");
        new DOMAttributeIterator(null, name);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructor_NameNull() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        new DOMAttributeIterator(parent, null);
    }

    @org.junit.Test
    public void testConstructor_ParentNotNull_NameWithNamespace() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("xmlns:test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructor_ParentNotNull_NameWithNullPrefix() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName(null, "test");
        new DOMAttributeIterator(parent, name);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testConstructor_ParentNotNull_NameWithEmptyPrefix() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("", "test");
        new DOMAttributeIterator(parent, name);
    }

    @org.junit.Test
    public void testConstructor_ParentNotNull_NameWildcard() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @org.junit.Test
    public void testConstructor_ParentNotNull_NameWithLocalName() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @org.junit.Test
    public void testConstructor_ParentNotNull_ElementWithAttributes() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }

    @org.junit.Test
    public void testConstructor_ParentNotNull_ElementWithNoAttributes() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testConstructor_ParentNotNull_ElementWithAttributes_TestAttr() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        Attr attr = (Attr) iterator.getNodePointer().getNode();
        String attrValue = attr.getValue();
        String expected = "expectedValue";
        attr.setValue(expected);
        assertEquals(expected, attr.getValue());
    }

    @org.junit.Test
    public void testHasNext() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        assertTrue(iterator.hasNext());
    }

    @org.junit.Test
    public void testHasNext_EndOfIterator() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNext_EndOfIterator() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testNext_ValidAttribute() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.next();
        Attr attribute = (Attr) attributePointer.getNode();
        assertEquals("expectedValue", attribute.getValue());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testNext_MoveToInvalidPosition() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(-1);
        iterator.next();
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testNext_MoveToPositionGreaterThanSize() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.setPosition(3);
        iterator.next();
    }

    @org.junit.Test
    public void testSetPosition_ValidPosition() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(1);
        assertTrue(iterator.hasNext());
        assertNotNull(iterator.next());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetPosition_InvalidPosition() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(-1);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetPosition_PositionGreaterThanSize() {
        NodePointer parent = new DOMPointNode(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.setPosition(3);
    }
}