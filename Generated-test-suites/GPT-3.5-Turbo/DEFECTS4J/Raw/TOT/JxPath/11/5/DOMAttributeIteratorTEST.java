import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.*;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    private Node node;

    @Before
    public void setUp() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            Element root = document.createElement("root");
            root.setAttribute("attr1", "value1");
            root.setAttribute("attr2", "value2");
            root.setAttribute("xmlns:ex", "http://example.com");
            root.setAttribute("ex:attr3", "value3");
            document.appendChild(root);
            node = document.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("*"));
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("*"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof DOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("*"));
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionValid() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("*"));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("*"));
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionBoundary() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("*"));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetNodePointerNullAttr() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("nonExistentAttr"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testSetPositionInvalidNoNodePointers() {
        Node nonElementNode = node.getAttributes().getNamedItem("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, nonElementNode), new QName("*"));
        assertFalse(iterator.setPosition(1));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalidLargePosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null, node), new QName("*"));
        assertFalse(iterator.setPosition(4));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalidNegativePosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalidZeroPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionOutOfRangePosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionBeyondIteration() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(3));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testSetPositionIteration() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionNegative() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(-10));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionZero() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalidType() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(new Object()));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalidBoundary() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(
                null, node), new QName("*"));
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }
}