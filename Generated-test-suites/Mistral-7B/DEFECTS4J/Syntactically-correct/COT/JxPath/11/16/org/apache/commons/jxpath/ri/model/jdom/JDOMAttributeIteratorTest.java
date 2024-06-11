import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyAttributeList() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        Assert.assertNotNull(iterator);
        Assert.assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        String xmlns = "http://www.w3.org/XML/1998/namespace";
        Element element = new Element("root", "root", new Namespace(xmlns, "xml"));
        element.addAttribute(new Attribute("attr", "value", new Namespace(xmlns, "xml")));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "attr"));
        Assert.assertNotNull(iterator);
        assertEquals(Collections.singletonList(iterator.getAttributes().get(0)), iterator.getAttributes());
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, (Attribute) iterator.getAttributes().get(0)), next);
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        String xmlns = "http://www.w3.org/XML/1998/namespace";
        Element element = new Element("root", "root", new Namespace(xmlns, "xml"));
        element.addAttribute(new Attribute("attr1", "value1", new Namespace(xmlns, "xml")));
        element.addAttribute(new Attribute("attr2", "value2", new Namespace(xmlns, "xml")));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "*"));
        Assert.assertNotNull(iterator);
        List attributes = iterator.getAttributes();
        assertEquals(2, attributes.size());
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, (Attribute) attributes.get(0)), next);
        next = iterator.next();
        assertEquals(new JDOMAttributePointer(parent, (Attribute) attributes.get(1)), next);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullName() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        new JDOMAttribeIterator(parent, null);
    }

    @Test
    public void testJDOMAttributeIterator_EmptyParentElement() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyParentElement_WildcardName() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_NonExistingAttribute() {
        String xmlns = "http://www.w3.org/XML/1998/namespace";
        Element element = new Element("root", "root", new Namespace(xmlns, "xml"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "nonExistingAttr"));
        assertFalse(iterator.hasNext());
    }
}