package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "x";
        String localName = "attribute";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, prefix);
        root.addAttribute("xmlns:" + prefix, xmlns);
        root.addAttribute(new QName(xmlns, localName), "value");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, localName));

        NodePointer attributePointer = iterator.next();
        assertNotNull(attributePointer);
        assertEquals(new QName(xmlns, localName), attributePointer.getNodeName());
        assertEquals("value", attributePointer.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "x";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, prefix);
        root.addAttribute(new QName(xmlns, "attr1"), "value1");
        root.addAttribute(new QName(xmlns, "attr2"), "value2");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));

        NodePointer attribute1Pointer = iterator.next();
        assertEquals(new QName(xmlns, "attr1"), attribute1Pointer.getNodeName());
        assertEquals("value1", attribute1Pointer.getNodeValue());

        NodePointer attribute2Pointer = iterator.next();
        assertEquals(new QName(xmlns, "attr2"), attribute2Pointer.getNodeName());
        assertEquals("value2", attribute2Pointer.getNodeValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithAttributeNSNone() {
        String prefix = "x";
        String localName = "attribute";

        Element root = new Element("root");
        root.addAttribute(new QName("", localName), "value");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", localName));

        NodePointer attributePointer = iterator.next();
        assertNotNull(attributePointer);
        assertEquals(new QName("", localName), attributePointer.getNodeName());
        assertEquals("value", attributePointer.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithAttributeNSDefined() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "x";
        String localName = "attribute";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, prefix);
        root.addAttribute(new QName(xmlns, localName), "value");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, localName));

        NodePointer attributePointer = iterator.next();
        assertNotNull(attributePointer);
        assertEquals(new QName(xmlns, localName), attributePointer.getNodeName());
        assertEquals("value", attributePointer.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSetPosition() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "x";
        String localName = "attribute";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, prefix);
        root.addAttribute(new QName(xmlns, "attr1"), "value1");
        root.addAttribute(new QName(xmlns, "attr2"), "value2");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));

        iterator.next();

        assertTrue(iterator.setPosition(1));
        assertEquals(new QName(xmlns, "attr2"), iterator.next().getNodeName());
        assertFalse(iterator.hasNext());

        assertFalse(iterator.setPosition(3));
    }
}