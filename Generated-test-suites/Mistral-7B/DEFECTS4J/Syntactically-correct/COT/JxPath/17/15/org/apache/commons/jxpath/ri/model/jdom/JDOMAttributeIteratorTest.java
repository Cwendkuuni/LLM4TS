import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JDOMAttributeIteratorTest.java
 */
public class JDOMAttributeIteratorTest {

    private NodePointer nodePointer;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element root = new Element("root");
        String xmlNs = "http://www.w3.org/XML/1998/namespace";
        root.addNamespaceDeclaration(xmlNs);
        root.addAttribute("attr1", "value1", xmlNs);
        root.addAttribute("attr2", "value2");
        root.addAttribute("xml:id", "rootId");

        nodePointer = new NodePointer(root);
        iterator = new JDOMAttributeIterator(nodePointer, new QName("*", xmlNs));
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Assert.assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals("attr2", ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute().getName());
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition_EndPosition() {
        for (int i = 0; i < iterator.size(); i++) {
            iterator.next();
        }
        Assert.assertEquals(3, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(4));
        Assert.assertEquals(3, iterator.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullParent() {
        new JDOMAttributeIterator(null, new QName("*", xmlNs));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullName() {
        new JDOMAttributeIterator(nodePointer, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyNamespaceUri() {
        new JDOMAttributeIterator(nodePointer, new QName("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyAttributeName() {
        new JDOMAttributeIterator(nodePointer, new QName("", ""));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(3, iterator.size());
    }

    @Test
    public void testHasNext() {
        for (int i = 0; i < iterator.size(); i++) {
            Assert.assertTrue(iterator.hasNext());
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        JDOMAttributePointer next = (JDOMAttributePointer) iterator.next();
        Assert.assertEquals("attr1", next.getAttribute().getName());

        next = (JDOMAttributePointer) iterator.next();
        Assert.assertEquals("attr2", next.getAttribute().getName());

        next = (JDOMAttributePointer) iterator.next();
        Assert.assertEquals("xml:id", next.getAttribute().getName());

        Assert.assertFalse(iterator.hasNext());
    }

}