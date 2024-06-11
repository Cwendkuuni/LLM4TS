package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JDOMAttributeIterator.class)
public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private Element testElement;
    private Attribute testAttribute1;
    private Attribute testAttribute2;
    private Attribute testAttribute3;

    @Before
    public void setUp() {
        testElement = new Element("test");
        testElement.setAttribute("attr1", "value1");
        testElement.setAttribute("attr2", "value2");
        testElement.setAttribute("attr3", "value3");

        Namespace xmlNamespace = Namespace.getNamespace("xml");
        testElement.setNamespace("xml", xmlNamespace);

        parentNode = new NodePointer(testElement);
    }

    @Test
    public void testJDOMAttributeIteratorWithNoAttributes() {
        QName qname = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithOneAttribute() {
        QName attrQName = new QName("xml", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertEquals(attrQName, nodePointer.getQName());
        assertEquals("value1", nodePointer.getValue().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithMultipleAttributes() {
        QName attrQName = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);

        NodePointer nodePointer1 = iterator.next();
        assertNotNull(nodePointer1);
        assertEquals(attrQName, nodePointer1.getQName());
        assertEquals("attr1", nodePointer1.getValue().getName());
        assertEquals("value1", nodePointer1.getValue().getValue());

        NodePointer nodePointer2 = iterator.next();
        assertNotNull(nodePointer2);
        assertEquals(attrQName, nodePointer2.getQName());
        assertEquals("attr2", nodePointer2.getValue().getName());
        assertEquals("value2", nodePointer2.getValue().getValue());

        NodePointer nodePointer3 = iterator.next();
        assertNotNull(nodePointer3);
        assertEquals(attrQName, nodePointer3.getQName());
        assertEquals("attr3", nodePointer3.getValue().getName());
        assertEquals("value3", nodePointer3.getValue().getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullParentNode() {
        QName qname = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, qname);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullQName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorWithParentNodeThatIsNotElement() {
        QName qname = new QName("", "");
        new JDOMAttributeIterator(new NodePointer(new Attribute("test", "testValue")), qname);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorWithNullAttributesList() {
        NodePointer parent = new NodePointer(testElement);
        new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testGetNodePointer() {
        QName attrQName = new QName("xml", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(attrQName, nodePointer.getQName());
    }

    @Test
    public void testGetPosition() {
        QName attrQName = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        assertEquals(0, iterator.getPosition());
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        QName attrQName = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        iterator.next();
        iterator.next();
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(5));
    }

    @Test
    public void testIteratorHasNext() {
        QName attrQName = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorReset() {
        QName attrQName = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.previous();
        iterator.previous();
        iterator.previous();
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIteratorNextWithEmptyList() {
        QName qname = new QName("");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, qname);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNextWithSingleAttribute() {
        QName attrQName = new QName("xml", "attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        NodePointer nodePointer = iterator.next();
        assertNotNull(nodePointer);
        assertEquals(attrQName, nodePointer.getQName());
        assertEquals("attr1", nodePointer.getValue().getName());
        assertEquals("value1", nodePointer.getValue().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNextWithMultipleAttributes() {
        QName attrQName = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);

        NodePointer nodePointer1 = iterator.next();
        assertNotNull(nodePointer1);
        assertEquals(attrQName, nodePointer1.getQName());
        assertEquals("attr1", nodePointer1.getValue().getName());
        assertEquals("value1", nodePointer1.getValue().getValue());

        NodePointer nodePointer2 = iterator.next();
        assertNotNull(nodePointer2);
        assertEquals(attrQName, nodePointer2.getQName());
        assertEquals("attr2", nodePointer2.getValue().getName());
        assertEquals("value2", nodePointer2.getValue().getValue());

        NodePointer nodePointer3 = iterator.next();
        assertNotNull(nodePointer3);
        assertEquals(attrQName, nodePointer3.getQName());
        assertEquals("attr3", nodePointer3.getValue().getName());
        assertEquals("value3", nodePointer3.getValue().getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNextWithNullIterator() {
        JDOMAttributeIterator iterator = null;
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNextWithEmptyIterator() {
        NodePointer emptyParent = new NodePointer(new Element("test"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(emptyParent, new QName("", ""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNextWithIteratorThatHasBeenReset() {
        QName attrQName = new QName("xml", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, attrQName);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.previous();
        iterator.previous();
        iterator.previous();
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.hasNext());
    }
}