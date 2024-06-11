package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorWithNamespaceAndSingleAttribute() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1", "http://test.com/ns");

        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("http://test.com/ns", "attr1");

        NodeIterator iterator = new JDOMAttributeIterator(parent, name);

        List expectedAttributes = new ArrayList();
        expectedAttributes.add(root.getAttribute("attr1"));

        assertEquals(expectedAttributes, iterator.asList());
    }

    @Test
    public void testJDOMAttributeIteratorWithNamespaceAndMultipleAttributes() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1", "http://test.com/ns1");
        root.addAttribute("attr2", "value2", "http://test.com/ns1");
        root.addAttribute("attr3", "value3", "http://test.com/ns2");

        NodePointer parent = new JDOMNodePointer(root);
        QName name1 = new QName("http://test.com/ns1", "*");
        NodeIterator iterator1 = new JDOMAttributeIterator(parent, name1);

        List expectedAttributes1 = new ArrayList();
        expectedAttributes1.add(root.getAttribute("attr1"));
        expectedAttributes1.add(root.getAttribute("attr2"));

        assertEquals(expectedAttributes1, iterator1.asList());

        NodeIterator iterator2 = new JDOMAttributeIterator(parent, name1);
        List expectedAttributes2 = new ArrayList();
        expectedAttributes2.add(root.getAttribute("attr2"));

        assertEquals(expectedAttributes2, iterator2.asList());

        NodeIterator iterator3 = new JDOMAttributeIterator(parent, name1);
        List expectedAttributes3 = new ArrayList();
        expectedAttributes3.add(root.getAttribute("attr1"));

        assertEquals(expectedAttributes3, iterator3.asList());

        NodeIterator iterator4 = new JDOMAttributeIterator(parent, new QName("http://test.com/ns2", "attr3"));
        List expectedAttributes4 = new ArrayList();
        expectedAttributes4.add(root.getAttribute("attr3"));

        assertEquals(expectedAttributes4, iterator4.asList());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyList() {
        Element root = new Element("root");

        NodePointer parent = new JDOMNodePointer(root);
        QName name = new QName("", "*");

        NodeIterator iterator = new JDOMAttributeIterator(parent, name);

        List expectedAttributes = Collections.EMPTY_LIST;

        assertEquals(expectedAttributes, iterator.asList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorWithNullParent() {
        QName name = new QName("http://test.com/ns", "attr1");
        NodeIterator iterator = new JDOMAttributeIterator(null, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorWithNullName() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        NodeIterator iterator = new JDOMAttributeIterator(parent, null);
    }
}