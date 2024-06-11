package org.apache.commons.cli.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorWithEmptyElement() {
        NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(new Element("")), new QName("", ""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithSingleAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value", new Namespace("prefix", ""));
        element.addContent(attribute);

        NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("prefix:name"));
        assertEquals(attribute, iterator.next().getNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1", new Namespace("prefix1", ""));
        Attribute attribute2 = new Attribute("name2", "value2", new Namespace("prefix2", ""));
        Attribute attribute3 = new Attribute("name3", "value3", new Namespace("prefix3", ""));
        element.addContent(attribute1);
        element.addContent(attribute2);
        element.addContent(attribute3);

        NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("", ""));

        List<NodePointer> expected = new ArrayList<>();
        expected.add(new JDOMAttributePointer(new JDOMPointer(element), attribute1));
        expected.add(new JDOMAttributePointer(new JDOMPointer(element), attribute2));
        expected.add(new JDOMAttributePointer(new JDOMPointer(element), attribute3));

        for (int i = 0; i < 3; i++) {
            NodePointer nodePointer = iterator.next();
            NodePointer expectedNodePointer = expected.get(i);
            assertEquals(expectedNodePointer.getNode(), nodePointer.getNode());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyNamespace() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value");
        element.addContent(attribute);

        NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("", "name"));
        assertEquals(attribute, iterator.next().getNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithWildcardNamespace() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1", new Namespace("prefix1", ""));
        Attribute attribute2 = new Attribute("name2", "value2", new Namespace("prefix2", ""));
        Attribute attribute3 = new Attribute("name3", "value3", new Namespace("prefix3", ""));
        element.addContent(attribute1);
        element.addContent(attribute2);
        element.addContent(attribute3);

        NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("", "*"));

        List<NodePointer> expected = new ArrayList<>();
        expected.add(new JDOMAttributePointer(new JDOMPointer(element), attribute1));
        expected.add(new JDOMAttributePointer(new JDOMPointer(element), attribute2));
        expected.add(new JDOMAttributePointer(new JDOMPointer(element), attribute3));

        for (int i = 0; i < 3; i++) {
            NodePointer nodePointer = iterator.next();
            NodePointer expectedNodePointer = expected.get(i);
            assertEquals(expectedNodePointer.getNode(), nodePointer.getNode());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullElement() {
        NodeIterator iterator = new JDOMAttributeIterator(null, new QName("", ""));
        assertNull(iterator.next().getNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorWithNullQName() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value", new Namespace("prefix", ""));
        element.addContent(attribute);

        NodeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), null);
        assertNull(iterator.next().getNode());
        assertFalse(iterator.hasNext());
    }
}