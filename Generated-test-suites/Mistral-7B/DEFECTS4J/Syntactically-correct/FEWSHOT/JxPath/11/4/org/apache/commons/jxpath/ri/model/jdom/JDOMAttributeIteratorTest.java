package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.getPosition() == 0);
    }

    @Test
    public void testSingleAttributeIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        Attribute expectedAttribute = root.getAttribute("attr1");
        NodePointer actualAttributePointer = iterator.getNodePointer();

        assertEquals(expectedAttribute.getValue(), actualAttributePointer.getNode().getValue());
        assertEquals(parent, actualAttributePointer.getParentPointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSingleAttributeIteratorWithNullParent() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("attr1", "http://www.example.com"));
        Attribute expectedAttribute = root.getAttribute("attr1");
        NodePointer actualAttributePointer = iterator.getNodePointer();

        assertEquals(expectedAttribute.getValue(), actualAttributePointer.getNode().getValue());
        assertEquals(new JDOMPointer(root), actualAttributePointer.getParentPointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        List<NodePointer> expectedAttributePointers = new ArrayList<>();
        expectedAttributePointers.add(new JDOMAttributePointer(parent, root.getAttribute("attr1")));
        expectedAttributePointers.add(new JDOMAttributePointer(parent, root.getAttribute("attr2")));

        for (int i = 0; i < iterator.getPosition(); i++) {
            NodePointer actualAttributePointer = iterator.getNodePointer();
            Attribute actualAttribute = (Attribute) actualAttributePointer.getNode();
            NodePointer expectedAttributePointer = new JDOMAttributePointer(parent, actualAttribute);

            assertEquals(expectedAttributePointers.get(iterator.getPosition() - 1), expectedAttributePointer);
            iterator.next();
        }

        NodePointer lastAttributePointer = iterator.getNodePointer();
        assertEquals(lastAttributePointer, null);
        assertEquals(expectedAttributePointers.size(), iterator.getPosition());
    }

    @Test
    public void testMultipleAttributesIteratorWithPrefix() {
        Element root = new Element("root");
        root.addNamespaceDeclaration("ns", "http://www.example.com");
        root.addAttribute("ns:attr1", "value1");
        root.addAttribute("ns:attr2", "value2");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns:attr1"));

        List<NodePointer> expectedAttributePointers = new ArrayList<>();
        expectedAttributePointers.add(new JDOMAttributePointer(parent, root.getAttribute("ns:attr1")));
        expectedAttributePointers.add(new JDOMAttributePointer(parent, root.getAttribute("ns:attr2")));

        for (int i = 0; i < iterator.getPosition(); i++) {
            NodePointer actualAttributePointer = iterator.getNodePointer();
            Attribute actualAttribute = (Attribute) actualAttributePointer.getNode();
            NodePointer expectedAttributePointer = new JDOMAttributePointer(parent, actualAttribute);

            assertEquals(expectedAttributePointers.get(iterator.getPosition() - 1), expectedAttributePointer);
            iterator.next();
        }

        NodePointer lastAttributePointer = iterator.getNodePointer();
        assertEquals(lastAttributePointer, null);
        assertEquals(expectedAttributePointers.size(), iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next(); // Should throw IndexOutOfBoundsException
    }

    @Test
    public void testSetPositionToFirstAttribute() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        iterator.setPosition(1);
        NodePointer actualAttributePointer = iterator.getNodePointer();

        NodePointer expectedAttributePointer = new JDOMAttributePointer(parent, root.getAttribute("attr1"));
        assertEquals(expectedAttributePointer, actualAttributePointer);
    }

    @Test
    public void testSetPositionToLastAttribute() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        iterator.setPosition(iterator.getPosition() - 1);
        NodePointer actualAttributePointer = iterator.getNodePointer();

        NodePointer expectedAttributePointer = new JDOMAttributePointer(parent, root.getAttribute("attr2"));
        assertEquals(expectedAttributePointer, actualAttributePointer);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionToNullIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        iterator = null;
        iterator.setPosition(0);
    }

    @Test
    public void testSetPositionToOutOfBoundsIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        iterator.setPosition(iterator.getPosition() + 1);
        iterator.setPosition(iterator.getPosition() - 1);
    }
}