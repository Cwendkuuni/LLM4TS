package org.apache.commons.jxpath.ri.model.jdom;

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
    public void testJDOMAttributeIterator_NoAttribute() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "attr";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, prefix);
        root.addAttribute(new QName(xmlns, localName), "value");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, localName));

        Attribute attribute = iterator.getNodePointer().getNode();
        assertEquals("value", attribute.getValue());

        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";

        Element root = new Element("root");
        root.addNamespaceDeclaration(xmlns, prefix);
        root.addAttribute(new QName(xmlns, "attr1"), "value1");
        root.addAttribute(new QName(xmlns, "attr2"), "value2");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));

        for (int i = 0; i < 2; i++) {
            Attribute attribute = iterator.getNodePointer().getNode();
            if (i == 0) {
                assertEquals("attr1", attribute.getName().getLocalPart());
                assertEquals("value1", attribute.getValue());
            } else {
                assertEquals("attr2", attribute.getName().getLocalPart());
                assertEquals("value2", attribute.getValue());
            }
            iterator.next();
        }

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyNamespace() {
        String prefix = "xml";
        String localName = "attr";

        Element root = new Element("root");
        root.addAttribute(new QName("", localName), "value");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", localName));

        Attribute attribute = iterator.getNodePointer().getNode();
        assertEquals("value", attribute.getValue());

        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_NoMatchingAttribute() {
        String prefix = "xml";
        String localName = "attr";

        Element root = new Element("root");
        root.addNamespaceDeclaration(new Namespace(prefix, "http://www.w3.org/2000/xmlns/"));
        root.addAttribute(new QName(prefix, "attr1"), "value1");

        NodePointer parent = new JDOMPointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));

        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullParent() {
        new JDOMAttributeIterator(null, new QName("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullNamespaceResolver() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullQName() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }
}