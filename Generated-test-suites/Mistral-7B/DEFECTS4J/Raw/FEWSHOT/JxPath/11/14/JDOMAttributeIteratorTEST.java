package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyElement() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        Namespace ns = Namespace.getNamespace("test");
        Element root = new Element("root");
        root.addAttribute("attr1", "value1", ns);

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("test:attr1"));
        Attribute expectedAttribute = root.getAttribute("attr1", ns);

        assertEquals(expectedAttribute.getValue(), iterator.getNodePointer().getValue());
        assertEquals(expectedAttribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testJDOMAttributeIterator_SingleNamespace() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr1"));
        Attribute expectedAttribute = root.getAttribute("attr1");

        assertEquals(expectedAttribute.getValue(), iterator.getNodePointer().getValue());
        assertEquals(expectedAttribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        Namespace ns = Namespace.getNamespace("test");
        Element root = new Element("root");
        root.addAttribute("attr1", "value1", ns);
        root.addAttribute("attr2", "value2", ns);

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("test:*"));

        List<Attribute> expectedAttributes = root.getAttributes();
        for (int i = 0; i < iterator.getPosition(); i++) {
            Attribute attribute = iterator.getNodePointer().getNode();
            assertEquals(expectedAttributes.get(i).getValue(), attribute.getValue());
            assertEquals(expectedAttributes.get(i), iterator.getNodePointer().getNode());
            iterator.next();
        }
    }

    @Test
    public void testJDOMAttributeIterator_InvalidQName() {
        Element root = new Element("root");

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_InvalidElement() {
        Element root = new Element("root");

        NodePointer parent = new JDOMNodePointer(null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr1"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_NextPosition() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr1"));
        iterator.next();

        assertTrue(iterator.getNodePointer().getValue().equals("value1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testJDOMAttributeIterator_OutOfBounds() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr1"));
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testJDOMAttributeIterator_SetPosition() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*:attr1"));
        iterator.next();

        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());

        iterator.next();
        assertEquals("value2", iterator.getNodePointer().getValue());
    }
}