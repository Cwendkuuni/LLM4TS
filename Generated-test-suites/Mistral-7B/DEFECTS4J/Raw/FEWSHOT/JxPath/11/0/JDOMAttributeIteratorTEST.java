package org.apache.commons.cli.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    @Test
    public void testJDOMAttributeIterator() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1", "http://test.com/ns");
        Attribute attr2 = new Attribute("attr2", "value2");
        element.addContent(attr1);
        element.addContent(attr2);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://test.com/ns", "attr1"));
        assertNotNull(iterator);

        List expectedAttributes = new ArrayList();
        expectedAttributes.add(attr1);
        assertEquals(expectedAttributes, iterator.attributes);
    }

    @Test
    public void testJDOMAttributeIteratorWithWildcard() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1", "http://test.com/ns");
        Attribute attr2 = new Attribute("attr2", "value2");
        Attribute attr3 = new Attribute("attr3", "value3", "http://test.com/ns");
        element.addContent(attr1);
        element.addContent(attr2);
        element.addContent(attr3);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNotNull(iterator);

        List expectedAttributes = new ArrayList();
        expectedAttributes.add(attr1);
        expectedAttributes.add(attr2);
        expectedAttributes.add(attr3);
        assertEquals(expectedAttributes, iterator.attributes);
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyList() {
        Element element = new Element("root");

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNotNull(iterator);
        assertTrue(iterator.attributes.isEmpty());
    }

    @Test
    public void testJDOMAttributeIteratorNext() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1", "http://test.com/ns");
        Attribute attr2 = new Attribute("attr2", "value2");
        Attribute attr3 = new Attribute("attr3", "value3", "http://test.com/ns");
        element.addContent(attr1);
        element.addContent(attr2);
        element.addContent(attr3);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://test.com/ns", "attr1"));
        assertNotNull(iterator);

        List expectedAttributes = new ArrayList();
        expectedAttributes.add(attr1);
        assertEquals(expectedAttributes, iterator.attributes);

        iterator.next();
        assertEquals(attr2, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());

        iterator.next();
        assertEquals(attr3, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());

        iterator.next();
        assertNull(iterator.getNodePointer().getNode());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIteratorPrevious() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1", "http://test.com/ns");
        Attribute attr2 = new Attribute("attr2", "value2");
        Attribute attr3 = new Attribute("attr3", "value3", "http://test.com/ns");
        element.addContent(attr1);
        element.addContent(attr2);
        element.addContent(attr3);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://test.com/ns", "attr1"));
        assertNotNull(iterator);

        iterator.next();
        iterator.next();
        iterator.next();

        iterator.previous();
        assertEquals(attr3, iterator.getNodePointer().getNode());
        assertEquals(2, iterator.getPosition());

        iterator.previous();
        assertEquals(attr2, iterator.getNodePointer().getNode());
        assertEquals(1, iterator.getPosition());

        iterator.previous();
        assertEquals(attr1, iterator.getNodePointer().getNode());
        assertEquals(0, iterator.getPosition());
    }
}